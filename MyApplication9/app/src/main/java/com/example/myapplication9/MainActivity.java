package com.example.myapplication9;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    private Interpreter tflite;
    private static final String MODEL_PATH = "ckpt.tflite";
    private boolean NC =false;
    private boolean CS =false;
    private boolean jus =false;
    private int hangle;
    private  String filename;
    private String saveresult;
    private static final String TAG = "MainActivity";
    private static final int NUM_CLASSES = 47; // 클래스의 총 수에 맞게 수정
    private EditText ImageName;
    private String finalresunt;
    private String yes="ㅇ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button); // 버튼 id가 'button'인 경우
        Button button2 = findViewById(R.id.button2);
        ImageName=findViewById(R.id.sample_EditText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 클릭할 때 실행할 사용자 정의 함수를 여기에 작성합니다.
                asdf();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 클릭할 때 실행할 사용자 정의 함수를 여기에 작성합니다.
                Intent intent=new Intent(getApplicationContext(),btActivity.class);
                startActivity(intent);
            }
        });
    }

    private void asdf(){
        try {
            tflite = new Interpreter(loadModelFile(MainActivity.this,MODEL_PATH));
        } catch (IOException e) {
            Log.e(TAG, "오류 엿먹어");
            e.printStackTrace();
        }
        filename= String.valueOf(ImageName.getText());
        //이미지 추론을 위한 테스트 이미지 로드
        Bitmap bitmap = loadImageFromAssets(filename+".png");

        // 이미지 분류 수행
        finalresunt = imageInference(bitmap);
        BrailleTranslator();
        Log.d(TAG, "Inference result: " + finalresunt);

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

    // 이미지를 모델에 맞게 전처리하는 메서드
    private ByteBuffer preprocessImage(Bitmap bitmap) {
        int modelInputSize = 28; // 모델의 입력 크기에 따라 조정합니다.

        // 이미지를 모델의 입력 크기에 맞게 리사이징합니다.
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, modelInputSize, modelInputSize, true);

        // ByteBuffer를 생성하고 order를 설정합니다.
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(modelInputSize * modelInputSize * 3 * 4); // 이미지 크기 * 채널 수 * 4
        byteBuffer.order(ByteOrder.nativeOrder());

        // 이미지를 BGR로 변환합니다.
        bitmap = Bitmap.createBitmap(resizedBitmap.getWidth(), resizedBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        int[] intValues = new int[modelInputSize * modelInputSize];
        resizedBitmap.getPixels(intValues, 0, resizedBitmap.getWidth(), 0, 0, resizedBitmap.getWidth(), resizedBitmap.getHeight());
        for (int i = 0; i < intValues.length; ++i) {
            int temp = intValues[i];
            intValues[i] = (temp >> 16) | ((temp >> 8) & 0xFF00) | (temp & 0xFF);
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
                    finalresunt=" ";
                    break;

            }
        }
        if(finalresunt.contains("(초성)")){
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
        }


    }

}
