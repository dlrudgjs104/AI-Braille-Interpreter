package de.kai_morich.simple_bluetooth_le_terminal;
import static android.speech.tts.TextToSpeech.ERROR;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

import java.util.Locale;

public class SettingActivity extends Activity {
    SharedPreferences pref;          // 프리퍼런스
    SharedPreferences.Editor editor; // 에디터
    float ts;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page);
        pref = getSharedPreferences("ttsspeed", Activity.MODE_PRIVATE);
        editor = pref.edit();
        ts = pref.getFloat("ts",0);
        TTSTASK ttsTask = new TTSTASK(getApplicationContext());
        ttsTask.execute();
        Spinner spinner1 = (Spinner) findViewById (R.id.CU_SP);
        Spinner spinner2 = (Spinner) findViewById (R.id.language_SP);
        Spinner spinner3 = (Spinner) findViewById (R.id.SR_SP);

        ArrayAdapter<CharSequence> adapter0=ArrayAdapter.createFromResource(this
                , R.array. speachrate_unit_array , android.R.layout. simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter0);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view , int pos , long id) {
                if(pos==0){ttsTask.setSpeed(1.0f);
                    editor.putFloat("ts", 1.0f);
                    editor.apply();} // 저장}
                else if (pos==1) {ttsTask.setSpeed(0.5f);
                        editor.putFloat("ts", 0.5f);
                        editor.apply();}
                else if (pos==2) {ttsTask.setSpeed(2.0f);
                        editor.putFloat("ts", 2.0f);
                        editor.apply();}
                else if (pos==3) {ttsTask.setSpeed(3.0f);
                        editor.putFloat("ts", 3.0f);
                        editor.apply();}
                ttsTask.setSpeak(parent.getItemAtPosition(pos).toString());

            }
            public void onNothingSelected(AdapterView<?> arg0){}
        });

        ArrayAdapter<CharSequence> adapter1=ArrayAdapter.createFromResource(this
                , R.array. character_unit_array , android.R.layout. simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view , int pos , long id) {
                ttsTask.setSpeak(parent.getItemAtPosition(pos).toString());

            }
            public void onNothingSelected(AdapterView<?> arg0){}
        });
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this
                , R.array.language_array , android.R.layout. simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent, View view , int pos , long id) {
                ttsTask.setSpeak(parent.getItemAtPosition(pos).toString());
            }
            public void onNothingSelected(AdapterView<?> arg0){}
        });



    }

}