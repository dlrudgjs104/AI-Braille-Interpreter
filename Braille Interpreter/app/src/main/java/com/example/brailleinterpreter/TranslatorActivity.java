package com.example.brailleinterpreter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslatorActivity extends Activity {

    private EditText translationText;
    private Button translationButton;
    private TextView resultText;
    private TextView JSONText;
    private String result;

    private String BrailleMatrix_Value = "010001";  //단일 값에 대한 예시

    // 백 그라운드에서 파파고 API와 연결하여 번역 결과를 가져옵니다.
    class BackgroundTask extends AsyncTask<Integer, Integer, Integer> {
        protected void onPreExecute() {
        }

        @Override
        protected Integer doInBackground(Integer... arg0) {
            StringBuilder output = new StringBuilder();
            String clientId = "hvKe1AFrwCp9UAawlMna"; // 애플리케이션 클라이언트 아이디 값";
            String clientSecret = "hGh9TittOJ"; // 애플리케이션 클라이언트 시크릿 값";
            try {
                // 번역문을 UTF-8으로 인코딩합니다.
                String text = URLEncoder.encode(translationText.getText().toString(), "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";

                // 파파고 API와 연결을 수행합니다.
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                // 번역할 문장을 파라미터로 전송합니다.
                String postParams = "source=ko&target=en&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();

                // 번역 결과를 받아옵니다.
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    output.append(inputLine);
                }
                br.close();
            } catch(Exception ex) {
                Log.e("SampleHTTP", "Exception in processing response.", ex);
                ex.printStackTrace();
            }
            result = output.toString();
            return null;
        }

        protected void onPostExecute(Integer a) {
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            if(element.getAsJsonObject().get("errorMessage") != null) {
                Log.e("번역 오류", "번역 오류가 발생했습니다. " +
                        "[오류 코드: " + element.getAsJsonObject().get("errorCode").getAsString() + "]");
            } else if(element.getAsJsonObject().get("message") != null) {
                // 번역 결과 출력
                resultText.setText(element.getAsJsonObject().get("message").getAsJsonObject().get("result")
                        .getAsJsonObject().get("translatedText").getAsString());
            }

        }

    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translator_page);
        Log.v("test","정상");
        translationText = (EditText) findViewById(R.id.translationText);
        translationButton = (Button) findViewById(R.id.translationButton);
        resultText = (TextView) findViewById(R.id.resultText);
        JSONText=findViewById(R.id.JSONText);
        getJson();
        translationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundTask().execute();
            }
        });



    }
    public void getJson(){
        Log.d("getJSON","함수실행");
        try {
            InputStream inputStream = getAssets().open("json/AbbreviationBrailleData.json");
            Log.d("getJSON","try 문실행");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;
            StringBuffer buffer = new StringBuffer();
            while (true){
                line = reader.readLine();
                buffer.append(line+"\n");
                Log.d("Json", "line : " + line);

                if(line == null){
                    break;
                }
            }
            String jsdata=buffer.toString();
            JSONObject jsonObject= new JSONObject(jsdata);
            JSONArray jsonArray= jsonObject.getJSONArray("AbbreviationBrailleData");
            String s="";
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jo= jsonArray.getJSONObject(i);

                String LetterName= jo.getString("LetterName");
                String BrailleMatrix= jo.getString("BrailleMatrix");
                String AssistanceName= jo.getString("AssistanceName");
                String RawId= jo.getString("RawId");
                Log.d("jo","정상");
                if (BrailleMatrix.equals(BrailleMatrix_Value)){
                    Log.d("jo2","정상2");
                    s+= LetterName+"    "+BrailleMatrix+"   "+AssistanceName+"  "+RawId+"\n";
                    break;
                }

            }
            JSONText.setText(s);
            reader.close();

        }catch (Exception e){

        }
    }


}
