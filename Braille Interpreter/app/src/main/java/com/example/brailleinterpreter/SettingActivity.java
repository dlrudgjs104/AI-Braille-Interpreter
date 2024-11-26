package com.example.brailleinterpreter;
import static android.speech.tts.TextToSpeech.ERROR;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Locale;

public class SettingActivity extends Activity {
    private TextToSpeech tts2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);
        TTS2();
        Spinner spinner1 = (Spinner) findViewById (R.id.CU_SP);
        Spinner spinner2 = (Spinner) findViewById (R.id.language_SP);
        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this
                , R.array. character_unit_array , android.R.layout. simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view , int pos , long id) {
                tts2.speak(parent.getItemAtPosition(pos).toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
            public void onNothingSelected(AdapterView<?> arg0){}
        });
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this
                , R.array.language_array , android.R.layout. simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view , int pos , long id) {
                tts2.speak(parent.getItemAtPosition(pos).toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
            public void onNothingSelected(AdapterView<?> arg0){}
        });



    }
    public void TTS2(){
        tts2 = new  TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts2.setLanguage(Locale.KOREAN);
                }
            }
        });
    }
}