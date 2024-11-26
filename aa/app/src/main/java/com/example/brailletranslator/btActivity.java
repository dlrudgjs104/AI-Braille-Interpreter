package com.example.brailletranslator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.Utils;
import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.opencv.core.*;
import org.opencv.imgproc.*;


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

    int i=0;
    private Interpreter tflite;
    private static final String MODEL_PATH = "ckpt.tflite";
    private boolean NC =false;
    private boolean CS =false;
    private boolean jus =false;
    private int hangle;
    private String saveresult;
    private static final String TAG = "btActivity";
    private static final int NUM_CLASSES = 47; // 클래스의 총 수에 맞게 수정

    private String finalresunt;
    private String yes="ㅇ";
    private Bitmap recivebitmap;
    private TTSTASK ttsTask;
    private ImageView imageView;
    long delay = 0;
    SharedPreferences pref;
    private float ttss;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btactivity);
        BTButton = findViewById(R.id.button3);
        TestView = findViewById(R.id.textView);

        imageView = findViewById(R.id.image_view);

        // 블루투스 지원 여부 확인
        ttsTask = new TTSTASK(getApplicationContext());
        ttsTask.execute();
        pref = getSharedPreferences("ttsspeed", MODE_PRIVATE);
        ttss= pref.getFloat("ts", 0);
        ttsTask.setSpeed(ttss);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) { //장치가 블루투스를 지원하지 않는 경우.
            Toast.makeText(getApplicationContext(), "Bluetooth 지원을 하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
            ttsTask.setSpeak("Bluetooth 지원을 하지 않는 기기입니다.");
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
                    ttsTask.setSpeak("먼저 Bluetooth 설정에 들어가 페어링을 진행해 주세요.");
                }

            }

        }
        handler.post(runnable);

        BTButton.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.S)
            @Override
            public void onClick(View view) {
                if (System.currentTimeMillis() > delay) {
                    ttsTask.setSpeak("번역버튼");
                    //한번 클릭했을 때
                    delay = System.currentTimeMillis() + 200;
                    return;
                }
                if(System.currentTimeMillis() <= delay){
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
                        Log.d(TAG, "인풋스트림 닫기");
                        mInputStream.close();
                        Log.d(TAG, "소캣 닫기");
                        bSocket.close();
                        onBT = false;
                        BTButton.setText("connect");
                    } catch (Exception ignored) {
                    }

                }
            }
            }
        });

    }
    private void checkConnectionStatus() {
        if (mRemoteDevice != null) {
            @SuppressLint("MissingPermission")
            int state = mRemoteDevice.getBondState();
            if (state == BluetoothDevice.BOND_NONE) {
                // 블루투스 기기와 연결이 끊겼습니다.
                // 모바일 기기에서도 연결을 끊습니다.
                try {
                    Log.d(TAG, "인풋스트림 닫기");
                    mInputStream.close();
                    Log.d(TAG, "소캣 닫기");
                    bSocket.close();
                    onBT = false;
                    BTButton.setText("connect");
                } catch (Exception ignored) {
                }
            }
        }
    }

    // 일정 시간마다 연결 상태를 확인합니다.
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            checkConnectionStatus();
            handler.postDelayed(this, 100);
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 액티비티가 종료될 때 Bluetooth 자원 닫기
        try {
            if (mInputStream != null) {
                mInputStream.close();
            }
            if (mOutputStream != null) {
                mOutputStream.close();
            }
            if (bSocket != null) {
                bSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

           // AlertDialog.Builder builder = new AlertDialog.Builder(this);
           // builder.setTitle("블루투스 장치 선택");


            // 페어링 된 블루투스 장치의 이름 목록 작성
           // List<String> listItems = new ArrayList<>();
            for (BluetoothDevice device : mDevices) {

                if(device.getName().equals("ESP32-CAM")){
                    connectToSelectedDevice(device.getName());
                }
            }
           /* listItems.add("취소");    // 취소 항목 추가

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
            alert.show();*/
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

                    // 데이터 수신을 위한 스트림 열기

                    mInputStream = bSocket.getInputStream();



                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), selectedDeviceName + " 연결 완료", Toast.LENGTH_LONG).show();
                            ttsTask.setSpeak("연결되었습니다");
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

        readBuffer = new byte[1024 * 10]; // 충분한 크기로 조정

        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    if (mInputStream != null) {
                        try {
                            SystemClock.sleep(500);
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
                                recivebitmap = BitmapFactory.decodeByteArray(readBuffer, 0, readBufferPosition);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (recivebitmap != null) {
                                            Log.d("handler", "Image received and decoded");
                                            readBufferPosition=0;

                                            imageView.setImageBitmap(recivebitmap);

                                            asdf();

                                        } else {
                                            Log.d("handler", "Image decoding failed");
                                        }
                                    }
                                });

                            }
                            if(byteAvailable==0)readBufferPosition=0;
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("handler", "뭐가문제야 베이비");
                        }
                    }else{readBufferPosition=0;}

                }

            }


        });
        workerThread.start();

    }

    private void asdf(){
        try {
            tflite = new Interpreter(loadModelFile(btActivity.this,MODEL_PATH));
        } catch (IOException e) {
            Log.e(TAG, "오류 엿먹어");
            e.printStackTrace();
        }
        //이미지 추론을 위한 테스트 이미지 로드

        // AdaptiveThreshold();
        BitmapCrop();

        //이미지 추론을 위한 테스트 이미지 로드
        //Bitmap bitmap = loadImageFromAssets(".png");
        if(recivebitmap!=null)
            {// 이미지 분류 수행
            finalresunt = imageInference(recivebitmap);
            BrailleTranslator();
            Log.d(TAG, "Inference result: " + finalresunt);}

    }

    // 모델 파일 로드
    private MappedByteBuffer loadModelFile(Activity activity, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // 이미지 파일 로드
    private Bitmap loadImageFromAssets(String fileName) {
        try {
            return BitmapFactory.decodeStream(getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;    // 오류 발생 시 null 반환
    }

    void AdaptiveThreshold() {
            // bitmap을 읽습니다.
            Bitmap bitmap = recivebitmap;

            // bitmap을 Mat 객체로 변환합니다.
            Mat img = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC3);
            Utils.bitmapToMat(bitmap, img);

            // Mat 객체를 적응적 이진화 알고리즘을 지원하는 객체로 변환합니다.
            Mat gray = new Mat();
            Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);

            // 적응적 이진화 알고리즘을 설정합니다.
            int blockSize = 31;
            int constant = 7;
            int type = Imgproc.THRESH_BINARY;

            // 적응적 이진화를 수행합니다.
            Mat binary = new Mat();
            Imgproc.adaptiveThreshold(gray, binary, 255, type, Imgproc.ADAPTIVE_THRESH_MEAN_C, blockSize, constant);

            // 이진화된 bitmap을 생성합니다.
            Bitmap binaryBitmap = Bitmap.createBitmap(binary.cols(), binary.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(binary, binaryBitmap);

            // 이진화된 bitmap을 출력합니다
            imageView.setImageBitmap(binaryBitmap);

    }

    void BitmapCrop(){
        // Bitmap bitmap = loadImageFromAssets("test_crop.PNG");
        Bitmap bitmap = recivebitmap;

        // OpenCV Mat 객체로 변환
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);

        // 이미지 가로 중앙 기준으로 50픽셀씩 자르기
        int startX = 75; // 왼쪽에서 50픽셀 지점
        int width2 = mat.width() - 150; // 전체 가로 길이에서 100픽셀 제거

        Mat croppedMat = new Mat(mat, new Rect(startX, 0, width2, mat.height())); // 해당 영역 잘라내기

        // 잘라낸 영역 다시 비트맵으로 변환
        bitmap = Bitmap.createBitmap(croppedMat.width(), croppedMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(croppedMat, bitmap);

        recivebitmap = bitmap;
       // imageView.setImageBitmap(bitmap);

    }


    // 이미지를 모델에 맞게 전처리하는 메서드
    private ByteBuffer preprocessImage(Bitmap bitmap) {
        int modelInputSize = 28; // 모델의 입력 크기에 따라 조정합니다.


        
        // 이미지를 모델의 입력 크기에 맞게 리사이징합니다.
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, modelInputSize, modelInputSize, true);

        imageView.setImageBitmap(resizedBitmap);

        // ByteBuffer를 생성하고 order를 설정합니다.
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(modelInputSize * modelInputSize * 3 * 4); // 이미지 크기 * 채널 수 * 4
        byteBuffer.order(ByteOrder.nativeOrder());

        // 이미지를 BGR로 변환합니다.
        bitmap = Bitmap.createBitmap(resizedBitmap.getWidth(), resizedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        int[] intValues = new int[modelInputSize * modelInputSize];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
        for (int i = 0; i < intValues.length; ++i) {
            int temp = intValues[i];
            intValues[i] = (temp)|((temp>>24) & 0xFF) | ((temp >> 8) & 0xFF) | ((temp>>16) & 0xFF);
            //intValues[i] = (intValues[i] / 255);
        }
        resizedBitmap.setPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());

        // 이미지의 각 픽셀 값을 가져와 ByteBuffer에 추가합니다.
        intValues = new int[modelInputSize * modelInputSize];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());

        int pixel = 0;
                for (int i = 0; i < modelInputSize; ++i) {
                    for (int j = 0; j < modelInputSize; ++j) {
                        final int val = intValues[pixel++];
                        byteBuffer.putFloat(((val >> 16) & 0xFF) - 128.0f); // R
                        byteBuffer.putFloat(((val >> 8) & 0xFF) - 128.0f); // G
                        byteBuffer.putFloat((val & 0xFF) - 128.0f); // B
            }
        }

        return byteBuffer;
    }


    // 이미지 추론 수행
    private String imageInference(Bitmap bitmap) {
        // 입력 이미지를 모델에 맞게 전처리해야 합니다.
        // TensorFlow Lite 모델에 입력으로 제공할 수 있는 형식으로 변환해야 합니다.
        ByteBuffer byteBuffer = preprocessImage(bitmap);

        // TensorFlow Lite 모델에 입력을 전달하고 추론 결과를 받습니다.
        float[][] result = new float[1][NUM_CLASSES]; // numClasses에는 클래스 수가 들어가야 합니다.
        tflite.run(byteBuffer, result);

        // 추론 결과를 해석하여 가장 높은 확률을 가진 클래스를 찾습니다.
        String inferenceResult = interpretResults(result);

        return inferenceResult;
    }

    // 추론 결과를 해석하는 메서드
    private String interpretResults(float[][] result) {
        // 추론 결과를 해석하여 가장 높은 확률을 가진 클래스를 찾습니다.
        // 각 클래스에 대한 레이블이 있어야 합니다.

        // 가장 높은 확률을 가진 클래스의 인덱스를 찾습니다.
        int maxIndex = 0;
        float maxProb = result[0][0];
        for (int i = 1; i < result[0].length; i++) {
            if (result[0][i] > maxProb) {
                maxProb = result[0][i];
                maxIndex = i;
            }
        }

        // 인덱스에 해당하는 클래스 레이블을 찾아서 반환합니다.
        String[] labels = new String[]{"7", "ㄱ(종성)", "ㄱ(초성)", "ㄴ(종성)", "ㄴ(초성)", "ㄷ(종성)", "ㄷ(초성)", "ㄹ(종성)", "ㄹ(자음)", "ㅁ(종성)", "ㅁ(초성)", "ㅂ(종성)",
                "ㅂ(초성)", "ㅅ(종성)", "ㅆ(종성)", "ㅇ(종성)", "ㅇ(초성)", "ㅈ(종성)", "ㅈ(초성)", "ㅊ(종성)", "ㅊ(초성)", "ㅋ(종성)", "ㅋ(초성)", "ㅌ(종성)", "ㅌ(초성)",
                "ㅍ(종성)", "ㅍ(초성)", "ㅎ(종성)", "ㅎ(초성)", "ㅏ(모음)", "ㅐ(모음)", "ㅑ(모음)", "ㅓ(모음)", "ㅔ(모음)", "ㅕ(모음)", "ㅖ(모음)", "ㅗ(모음)",
                "ㅘ(모음)", "ㅚ(모음)", "ㅛ(모음)", "ㅜ(모음)", "ㅝ(모음)", "ㅠ(모음)", "ㅡ(모음)", "ㅢ(모음)", "ㅣ(모음)", "수표"}; // 클래스 레이블들을 지정합니다.
        String inferenceResult = labels[maxIndex]; // 추론 결과를 레이블로 변환합니다.

        return inferenceResult;
    }
    public void BrailleTranslator(){

        if (finalresunt.contains("수표")){
            if(CS || jus){
                CS=false;
                jus=false;
                saveresult+=String.valueOf((char)hangle);
            }
            NC=true;
        }
        if(NC){
            switch (finalresunt){
                case "ㄱ(초성)":
                    finalresunt="1";
                    saveresult+=finalresunt;
                    break;
                case "ㅂ(종성)":
                    finalresunt="2";
                    saveresult+=finalresunt;
                    break;
                case "ㄴ(초성)":
                    finalresunt="3";
                    saveresult+=finalresunt;
                    break;
                case "ㅍ(초성)":
                    finalresunt="4";
                    saveresult+=finalresunt;
                    break;
                case "ㅁ(초성)":
                    finalresunt="5";
                    saveresult+=finalresunt;
                    break;
                case "ㅋ(초성)":
                    finalresunt="6";
                    saveresult+=finalresunt;
                    break;
                case "7":
                    finalresunt="7";
                    saveresult+=finalresunt;
                    break;
                case "ㅌ(초성)":
                    finalresunt="8";
                    saveresult+=finalresunt;
                    break;
                case "ㄷ(초성)":
                    finalresunt="9";
                    saveresult+=finalresunt;
                    break;
                case "ㅎ(초성)":
                    finalresunt="0";
                    saveresult+=finalresunt;
                    break;
                case "ㅇ(초성)":
                    NC=false;
                    finalresunt="수표끝";
                    break;

            }
        }
        /*if(finalresunt.contains("(초성)")){
            if(CS||jus){

                CS=false;
                jus=false;
                saveresult+=String.valueOf((char)hangle);
            }
            Log.d(TAG,"초성");
            finalresunt=finalresunt.replace("(초성)","");
            Log.d(TAG,finalresunt);
            hangle= finalresunt.codePointAt(0)*21;
            CS=true;
        }
        if(finalresunt.contains("모음")){
            if(CS|| jus){
                CS=false;
                jus=false;
                saveresult+=String.valueOf((char)hangle);
            }
            if(!CS){
                hangle+=yes.codePointAt(0)*21;
            }
            finalresunt=finalresunt.replace("(모음)","");
            hangle+=finalresunt.codePointAt(0)*28;
            jus=true;
        }
        if(finalresunt.contains("종성")){
            finalresunt.replace("(종성)","");
            hangle+=finalresunt.codePointAt(0);
            CS=false;
            jus=false;
            saveresult+=String.valueOf((char)hangle);
        }*/
        ttsTask.setSpeak(finalresunt);

    }
}

