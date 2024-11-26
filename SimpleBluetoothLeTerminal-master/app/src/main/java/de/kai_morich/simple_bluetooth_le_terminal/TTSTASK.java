
package de.kai_morich.simple_bluetooth_le_terminal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

class TTSTASK extends AsyncTask<Void, Void, Void> {
    private Context context;
    private float speed = 1.0f;
    private TextToSpeech tts;
    private String textToSpeak;

    TTSTASK(Context context) {
        this.context = context;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        Log.d("TTSTASK","속도변경"+this.speed);
    }

    public void setSpeak(String text) {
        textToSpeak = text;
        Log.d("TTSTASK","tts문장"+textToSpeak);
        if (tts != null) {
            speakText();
        }
    }

    private void speakText() {
        if (textToSpeak != null) {
            tts.setSpeechRate(speed);
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {

        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                    speakText(); // 초기화 후 텍스트를 바로 읽도록 호출
                }
                Log.d("TTSTASK","TTSTASK실행");
            }
        });
        return null;
    }
}



