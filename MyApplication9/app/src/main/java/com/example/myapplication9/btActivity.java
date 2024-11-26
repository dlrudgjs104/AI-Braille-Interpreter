package com.example.myapplication9;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class btActivity extends AppCompatActivity {
    public BluetoothAdapter mBluetoothAdapter;
    public Set<BluetoothDevice> mDevices;
    private BluetoothSocket bSocket;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private BluetoothDevice mRemoteDevice;
    public boolean onBT = false;
    public byte[] sendByte = new byte[4];
    public ProgressDialog asyncDialog;
    private static final int REQUEST_ENABLE_BT = 1;
    private Button BTButton;
    private TextView TestView;
    private Thread workerThread = null; //문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; //수신된 문자열 저장 버퍼
    private int readBufferPosition=0; //버퍼  내 문자 저장 위치
    String[] array = {"0"}; //수신된 문자열을 쪼개서 저장할 배열
    int i=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btactivity);
        BTButton = findViewById(R.id.button3);
        TestView = findViewById(R.id.textView);
        // 블루투스 지원 여부 확인

        BTButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!onBT) { //Connect
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) { //장치가 블루투스를 지원하지 않는 경우.
                        Toast.makeText(getApplicationContext(), "Bluetooth 지원을 하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
                    } else { // 장치가 블루투스를 지원하는 경우.
                        if (ActivityCompat.checkSelfPermission(btActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(btActivity.this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 1000);
                        }
                        if (!mBluetoothAdapter.isEnabled()) {
                            // 블루투스를 지원하지만 비활성 상태인 경우
                            // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요청
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        } else {
                            // 블루투스를 지원하며 활성 상태인 경우
                            // 페어링된 기기 목록을 보여주고 연결할 장치를 선택.


                            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                            if (pairedDevices.size() > 0) {
                                // 페어링 된 장치가 있는 경우.
                                selectDevice();
                                receiveData();

                            } else {
                                // 페어링 된 장치가 없는 경우.
                                Toast.makeText(getApplicationContext(), "먼저 Bluetooth 설정에 들어가 페어링을 진행해 주세요.", Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                } else { //DisConnect

                    try {

                        mInputStream.close();
                        mOutputStream.close();
                        bSocket.close();
                        onBT = false;
                        BTButton.setText("connect");
                    } catch (Exception ignored) {
                    }

                }
            }
        });

    }


    public void selectDevice() {

        try {
            // 권한을 필요로 하는 작업을 수행합니다.
            mDevices = mBluetoothAdapter.getBondedDevices();
            final int mPairedDeviceCount = mDevices.size();

            if (mPairedDeviceCount == 0) {
                //  페어링 된 장치가 없는 경우
                Toast.makeText(getApplicationContext(), "장치를 페어링 해주세요!", Toast.LENGTH_SHORT).show();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("블루투스 장치 선택");


            // 페어링 된 블루투스 장치의 이름 목록 작성
            List<String> listItems = new ArrayList<>();
            for (BluetoothDevice device : mDevices) {
                listItems.add(device.getName());
            }
            listItems.add("취소");    // 취소 항목 추가

            final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    if (item == mPairedDeviceCount) {
                        // 연결할 장치를 선택하지 않고 '취소'를 누른 경우
                        //finish();
                    } else {
                        // 연결할 장치를 선택한 경우
                        // 선택한 장치와 연결을 시도함
                        connectToSelectedDevice(items[item].toString());
                    }
                }
            });


            builder.setCancelable(false);    // 뒤로 가기 버튼 사용 금지
            AlertDialog alert = builder.create();
            alert.show();
        } catch (SecurityException e) {
            // 권한이 부여되지 않은 경우 처리합니다.
        }


    }


    public void connectToSelectedDevice(final String selectedDeviceName) {
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);

        //Progress Dialog
        asyncDialog = new ProgressDialog(btActivity.this);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("블루투스 연결중..");
        asyncDialog.show();
        asyncDialog.setCancelable(false);
        try {
            // 권한을 필요로 하는 작업을 수행합니다.
        } catch (SecurityException e) {
            // 권한이 부여되지 않은 경우 처리합니다.
        }

        Thread BTConnect = new Thread(new Runnable() {
            @SuppressLint("MissingPermission")
            public void run() {

                try {
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //HC-06 UUID
                    // 소켓 생성

                    bSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);


                    // RFCOMM 채널을 통한 연결
                    bSocket.connect();

                    // 데이터 송수신을 위한 스트림 열기
                    mOutputStream = bSocket.getOutputStream();
                    mInputStream = bSocket.getInputStream();


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), selectedDeviceName + " 연결 완료", Toast.LENGTH_LONG).show();
                            BTButton.setText("disconnect");
                            asyncDialog.dismiss();
                        }
                    });

                    onBT = true;


                } catch (Exception e) {
                    // 블루투스 연결 중 오류 발생
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            asyncDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "블루투스 연결 오류", Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }
        });
        BTConnect.start();


    }

    public BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;

        // mDevices 변수가 null인지 확인합니다.
        if (mDevices == null) {
            // NullPointerException을 처리하거나 해당 코드를 실행하지 않습니다.
        }

        // mDevices 변수가 비어 있는지 확인합니다.
        if (mDevices.isEmpty()) {
            // NoSuchElementException을 처리하거나 해당 코드를 실행하지 않습니다.
        }

        // name 변수가 null인지 확인합니다.
        if (name == null) {
            // NullPointerException을 처리하거나 해당 코드를 실행하지 않습니다.
        }

        // BluetoothAdapter.getBondedDevices() 메서드를 호출하기 전에 권한을 확인합니다.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 허가되지 않은 경우 사용자에게 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 123);
        } else {
            // 권한이 허가된 경우 BluetoothAdapter.getBondedDevices() 메서드를 호출합니다.
            mDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        }

        // device.getName() 메서드가 null을 반환하는지 확인합니다.
        for (BluetoothDevice device : mDevices) {
            String deviceName = device.getName();
            if (deviceName == null) {
                // NullPointerException을 처리하거나 해당 코드를 실행하지 않습니다.
            }

            if (name.equals(deviceName)) {
                selectedDevice = device;
                break;
            }
        }

        return selectedDevice;
    }
    public void receiveData() {
        final Handler handler = new Handler();

        readBuffer = new byte[1024 * 1024]; // 충분한 크기로 조정

        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    if (mInputStream != null) {
                        try {
                            int byteAvailable = mInputStream.available();

                            if (byteAvailable > 0) {
                                byte[] bytes = new byte[byteAvailable];
                                mInputStream.read(bytes);
                                for(i=0; i<byteAvailable;i++){
                                    readBuffer[readBufferPosition]=bytes[i];
                                    readBufferPosition++;
                                }
                                //int bytesRead =mInputStream.read(bytes);
                                //InputStream inputStream =new ByteArrayInputStream(bytes);
                                Log.d("handler(바이트 값 출력)", Arrays.toString(bytes));
                                Log.d("handle(바이트 길이 출력)", ""+ bytes.length);
                                Log.d("handler(리드버퍼바이트 값 출력)", Arrays.toString(readBuffer));
                                Log.d("handle(리드버퍼바이트 길이 출력)", ""+ readBufferPosition);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(readBuffer, 0, readBufferPosition);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (bitmap != null) {
                                            Log.d("handler", "Image received and decoded");
                                            readBufferPosition=0;
                                            ImageView imageView = (ImageView) findViewById(R.id.image_view);
                                            imageView.setImageBitmap(bitmap);

                                        } else {
                                            Log.d("handler", "Image decoding failed");
                                        }
                                    }
                                });

                            }if(byteAvailable==0)readBufferPosition=0;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("handler", "뭐가문제야 베이비");
                        }
                    }
                }


            }


        });
        workerThread.start();
    }


    Thread BTSend  = new Thread(new Runnable() {
        public void run() {
            try {
                mOutputStream.write(sendByte);    // 프로토콜 전송
            } catch (Exception e) {
                // 문자열 전송 도중 오류가 발생한 경우.
            }
        }
    });
    public void sendbtData(int btLightPercent) throws IOException {
        //sendBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] bytes = new byte[4];
        bytes[0] = (byte) 0xa5;
        bytes[1] = (byte) 0x5a;
        bytes[2] = 1; //command
        bytes[3] = (byte) btLightPercent;
        sendByte = bytes;
        BTSend.run();
    }
}

