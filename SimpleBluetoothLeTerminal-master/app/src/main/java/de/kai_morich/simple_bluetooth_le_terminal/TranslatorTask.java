package de.kai_morich.simple_bluetooth_le_terminal;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class TranslatorTask extends AsyncTask<Integer, Integer, Integer> {
    private String sendtext;
    private String result_text;

    public void TranslatorText(String text) {
        this.sendtext = text;

    }

    private void result(String text) {

        this.sendtext = null;
        this.result_text=text;
    }


    protected void onPreExecute() {
    }

    @Override
    protected Integer doInBackground(Integer... arg0) {
        StringBuilder output = new StringBuilder();
        String clientId = "hvKe1AFrwCp9UAawlMna"; // 애플리케이션 클라이언트 아이디 값";
        String clientSecret = "hGh9TittOJ"; // 애플리케이션 클라이언트 시크릿 값";
        if(sendtext!=null){
            try {
                // 번역문을 UTF-8으로 인코딩합니다.
                String text = URLEncoder.encode(sendtext, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";

                // 파파고 API와 연결을 수행합니다.
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    output.append(inputLine);
                }
                br.close();
            } catch (Exception ex) {
                Log.e("SampleHTTP", "Exception in processing response.", ex);
                ex.printStackTrace();
            }
            result(output.toString());
        }
        return null;
    }
    protected void onPostExecute(Integer a) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result_text);
        if(element.getAsJsonObject().get("errorMessage") != null) {
            Log.e("번역 오류", "번역 오류가 발생했습니다. " +
                    "[오류 코드: " + element.getAsJsonObject().get("errorCode").getAsString() + "]");
        } else if(element.getAsJsonObject().get("message") != null) {
            // 번역 결과 출력

            //출력에 따라 방법이 바뀔 예정

        }
    }
}
