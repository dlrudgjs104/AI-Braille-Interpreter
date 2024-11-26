package com.example.myapplication13;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치
    private TextView textViewReceive; // 수신 된 데이터를 표시하기 위한 텍스트 뷰
    private EditText editTextSend; // 송신 할 데이터를 작성하기 위한 에딧 텍스트
    private Button buttonSend; // 송신하기 위한 버튼
    private ImageView imageView; // 수신된 이미지 출력할 이미지 뷰
    public ProgressDialog asyncDialog; // 블루투스 연결중 시각 처리 프로세스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 각 컨테이너들의 id를 매인 xml과 맞춰준다.
        textViewReceive = (TextView) findViewById(R.id.textView_receive);
        editTextSend = (EditText) findViewById(R.id.editText_send);
        buttonSend = (Button) findViewById(R.id.button_send);
        imageView = findViewById(R.id.imageView);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData(editTextSend.getText().toString());
            }
        });

        // 블루투스 활성화하기
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정

        if (bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
            // 여기에 처리 할 코드를 작성하세요.
        } else { // 디바이스가 블루투스를 지원 할 때
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 1000);
            }
            if (bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
                selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
            } else { // 블루투스가 비 활성화 상태 (기기에 블루투스가 꺼져있음)

                // 블루투스를 활성화 하기 위한 다이얼로그 출력
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) { // '사용'을 눌렀을 때
                    selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
                } else if (resultCode == RESULT_CANCELED) { // '취소'를 눌렀을 때
                    Toast.makeText(this, "블루투스 활성화를 취소했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void selectBluetoothDevice() {
        try{
            // 이미 페어링 되어있는 블루투스 기기를 찾습니다.
            devices = bluetoothAdapter.getBondedDevices();

            // 페어링 된 디바이스의 크기를 저장
            float pariedDeviceCount = devices.size();

            // 페어링 되어있는 장치가 없는 경우
            if (pariedDeviceCount == 0) {
                // 페어링을 하기위한 함수 호출
            }
            // 페어링 되어있는 장치가 있는 경우
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 디바이스를 선택하기 위한 다이얼로그 생성
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");

                        // 페어링 된 각각의 디바이스의 이름과 주소를 저장
                        List<String> list = new ArrayList<>();

                        // 모든 디바이스의 이름을 리스트에 추가
                        for (BluetoothDevice bluetoothDevice : devices) {
                            list.add(bluetoothDevice.getName());
                        }
                        list.add("취소");

                        // List를 CharSequence 배열로 변경
                        final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
                        list.toArray(new CharSequence[list.size()]);

                        // 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너
                        builder.setItems(charSequences, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 선택된 항목이 "취소"인 경우
                                if (charSequences[which].equals("취소")) {
                                    dialog.dismiss(); // 다이얼로그를 종료합니다.
                                } else {
                                    // 해당 디바이스와 연결하는 함수 호출
                                    connectDevice(charSequences[which].toString());
                                }
                            }

                        });

                        // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정
                        builder.setCancelable(false);

                        // 다이얼로그 생성
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

            }
        }
        catch(SecurityException e) {
            Toast.makeText(getApplicationContext(), "디바이스 탐색 오류발생", Toast.LENGTH_SHORT).show();
        }

    }

    public void connectDevice(String deviceName) {
        //Progress Dialog
        asyncDialog = new ProgressDialog(MainActivity.this);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("블루투스 연결중..");
        asyncDialog.show();

        // 블루투스 연결 과정 쓰레드로 실행
        Thread Connect = new Thread(new Runnable() {
            public void run() {

                // 페어링 된 디바이스들을 모두 탐색
                for(BluetoothDevice tempDevice :devices)
                {
                    // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
                    try {
                        if (deviceName.equals(tempDevice.getName())) {
                            bluetoothDevice = tempDevice;
                            break;
                        }
                    }
                    catch (SecurityException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "디바이스 탐색 오류발생", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }

                // UUID 생성
                UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

                // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
                try {
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                    bluetoothSocket.connect();

                    // 데이터 송,수신 스트림을 얻어옵니다.
                    outputStream = bluetoothSocket.getOutputStream();
                    inputStream = bluetoothSocket.getInputStream();

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // 연결에 성공했을 때 Toast 메시지를 띄웁니다.
                            Toast.makeText(getApplicationContext(), deviceName + "에 연결되었습니다.", Toast.LENGTH_SHORT).show();
                            asyncDialog.dismiss();
                        }
                    });

                    // 데이터 수신 함수 호출
                    receiveData();

                }
                catch(IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // 연결 실패시 Toast 메시지를 띄우고 기기를 다시 선택할 수 있는 다이얼로그를 띄웁니다.
                            Toast.makeText(getApplicationContext(), deviceName + " 연결을 실패했습니다.", Toast.LENGTH_SHORT).show();
                            asyncDialog.dismiss();
                        }
                    });
                    selectBluetoothDevice();
                }
                catch(SecurityException e)
                {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "소켓 생성 오류 발생", Toast.LENGTH_SHORT).show();
                            asyncDialog.dismiss();
                        }
                    });

                }


        }
        });
        Connect.start();
    }

    public void receiveData() {
        final Handler handler = new Handler(Looper.getMainLooper());

        // 데이터를 수신하기 위한 버퍼를 생성
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        // 데이터를 수신하기 위한 쓰레드 생성
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(Thread.currentThread().isInterrupted()) {
                    try {
                        // 데이터를 수신했는지 확인합니다.
                        int byteAvailable = inputStream.available();

                        // 데이터가 수신 된 경우
                        if(byteAvailable > 0) {
                            // 입력 스트림에서 바이트 단위로 읽어 옵니다.
                            byte[] bytes = new byte[byteAvailable];
                            inputStream.read(bytes);

                            // 이미지를 받은 경우
                            if (isImage(bytes)) {
                                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // ImageView에 이미지 설정
                                        imageView.setImageBitmap(bitmap);
                                    }
                                });
                            }
                            else {  //이미지가 아닌 경우
                                // 입력 스트림 바이트를 한 바이트씩 읽어 옵니다.
                                for (int i = 0; i < byteAvailable; i++) {
                                    byte tempByte = bytes[i];

                                    // 개행문자를 기준으로 받음(한줄)
                                    if (tempByte == '\n') {
                                        // readBuffer 배열을 encodedBytes로 복사
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);

                                        // 인코딩 된 바이트 배열을 문자열로 변환
                                        final String text = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                // 텍스트 뷰에 출력
                                                textViewReceive.append(text + "\n");
                                            }
                                        });
                                    } // 개행 문자가 아닐 경우
                                    else {
                                        readBuffer[readBufferPosition++] = tempByte;
                                    }

                                }
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        // 1초마다 받아옴
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }

        });

        workerThread.start();

    }

    private boolean isImage(byte[] bytes) {
        // 간단한 방법은 이미지 파일 시그니처를 확인하는 것입니다.
        // 여기서는 간단히 JPEG 파일 시그니처를 확인하는 것으로 예를 들었습니다.
        if (bytes.length > 2 && ((bytes[0] & 0xFF) == 0xFF) && ((bytes[1] & 0xFF) == 0xD8)) {
            // JPEG 시그니처 확인
            return true;
        }

        // PNG 시그니처 확인
        if (bytes.length > 3 &&
                bytes[0] == (byte) 0x89 &&
                bytes[1] == (byte) 0x50 &&
                bytes[2] == (byte) 0x4E &&
                bytes[3] == (byte) 0x47) {
            return true;
        }
        return false;
    }

    void sendData(String text) {
        // 문자열에 개행문자("\n")를 추가해줍니다.
        text += "\n";

        try{
            // 데이터 송신
            outputStream.write(text.getBytes());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}