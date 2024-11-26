package de.kai_morich.simple_bluetooth_le_terminal;

import static android.speech.tts.TextToSpeech.ERROR;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends Activity {
        TextView textView;
        Button button;
        Button button2;
        Intent intent;
        SpeechRecognizer mRecognizer;
        final int PERMISSION = 1;

        //TTS 변수선언 시작
        public static Context mContext;
        private TTSTASK ttsTask;
        //TTS 변수선언 끝
        SharedPreferences pref;
        private float ttss;
        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_main);
                pref = getSharedPreferences("ttsspeed", MODE_PRIVATE);

                TextView tv;
                //STT관련 코드 시작


                if (Build.VERSION.SDK_INT >= 23) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                                Manifest.permission.RECORD_AUDIO}, PERMISSION);
                }
                textView = findViewById(R.id.textView2);
                button = findViewById(R.id.button3);
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName()); // 여분의 키
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); // 언어 설정
                button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                mRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this); // 새 SpeechRecognizer 를 만드는 팩토리 메서드
                                mRecognizer.setRecognitionListener(listener); // 리스너 설정
                                mRecognizer.startListening(intent); // 듣기 시작
                        }
                });
                //STT관련 코드 끝

                //TTS함수 선언 시작
                ttsTask = new TTSTASK(getApplicationContext());
                ttsTask.execute();

                //TTS관련 코드 끝
        }
        @Override
        public void onStart() {
                super.onStart();
                ttss= pref.getFloat("ts", 0);
                ttsTask.setSpeed(ttss);
        }
        //STT 관련 코드 시작
        private RecognitionListener listener = new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                        // 말하기 시작할 준비가되면 호출
                        Toast.makeText(getApplicationContext(), "음성인식 시작", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBeginningOfSpeech() {
                        // 말하기 시작했을 때 호출
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                        // 입력받는 소리의 크기를 알려줌
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                        // 말을 시작하고 인식이 된 단어를 buffer에 담음
                }

                @Override
                public void onEndOfSpeech() {
                        // 말하기를 중지하면 호출
                }

                @Override
                public void onError(int error) {
                        // 네트워크 또는 인식 오류가 발생했을 때 호출
                        String message;

                        switch (error) {
                                case SpeechRecognizer.ERROR_AUDIO:
                                        message = "오디오 에러";
                                        break;
                                case SpeechRecognizer.ERROR_CLIENT:
                                        message = "클라이언트 에러";
                                        break;
                                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                                        message = "퍼미션 없음";
                                        break;
                                case SpeechRecognizer.ERROR_NETWORK:
                                        message = "네트워크 에러";
                                        break;
                                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                                        message = "네트웍 타임아웃";
                                        break;
                                case SpeechRecognizer.ERROR_NO_MATCH:
                                        message = "찾을 수 없음";
                                        break;
                                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                                        message = "RECOGNIZER 가 바쁨";
                                        break;
                                case SpeechRecognizer.ERROR_SERVER:
                                        message = "서버가 이상함";
                                        break;
                                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                                        message = "말하는 시간초과";
                                        break;
                                default:
                                        message = "알 수 없는 오류임";
                                        break;
                        }

                        Toast.makeText(getApplicationContext(), "에러 발생 : " + message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResults(Bundle results) {

                        Spinner spinner3 = (Spinner) findViewById (R.id.SR_SP);
                        // 인식 결과가 준비되면 호출
                        // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
                        ArrayList<String> matches =
                                results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                        for (int i = 0; i < matches.size(); i++) {
                                textView.setText(matches.get(i));
                        }
                        String sId = textView.getText().toString();
                        if(sId.contains("블루")){
                                Intent intent = new Intent(getApplicationContext(),BluetoothActivity.class);
                                startActivity(intent);
                        } else if (sId.contains("설정")) {
                                Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
                                startActivity(intent);
                        } else if (sId.contains("기능")) {
                                ttsTask.setSpeak("기능음성안내를 해드리겠습니다. 화면의 상단부터 차례대로 음성인식, 설정, 블루투스, 기능음성안내가 차례대로" +
                                        "4분할 되어있습니다. 찾아서 누르는게 불편하시다면 최상단 버튼을 누르고 원하는 기능을 말씀하시면 페이지가 이동됩니다.");
                        }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                        // 부분 인식 결과를 사용할 수 있을 때 호출
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                        // 향후 이벤트를 추가하기 위해 예약
                }
        };
        //STT 관련 코드 끝

        //블루투스 페이지 이동
        public void myListener1(View target) {
                ttsTask.setSpeak("블루투스");
                Intent intent = new Intent(getApplicationContext(),
                        BluetoothActivity.class);
                startActivity(intent);
        }

        //설정 페이지 이동
        public void myListener4(View target) {
                ttsTask.setSpeak("설정");
                /*tts.speak("설정페이지 입니다. 상단에서부터 음성속도조절, 글자단위설정, 언어설정이 있습니다.", TextToSpeech.QUEUE_FLUSH, null);*/
                Intent intent = new Intent(getApplicationContext(),
                        SettingActivity.class);
                startActivity(intent);
        }

        //음성인식 TTS
        public void myListener5(View target) {
                ttsTask.setSpeak("기능음성안내를 해드리겠습니다. 화면의 상단부터 차례대로 음성인식, 설정, 블루투스, 기능음성안내가 차례대로" +
                        "4분할 되어있습니다. 찾아서 누르는게 불편하시다면 최상단 버튼을 누르고 원하는 기능을 말씀하시면 페이지가 이동됩니다.");
        }



        //TTS관련 코드 시작


        @Override
        protected void onDestroy() {
                super.onDestroy();
                // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.

        }
        //TTS관련 코드 끝

}