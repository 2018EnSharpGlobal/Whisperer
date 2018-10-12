package com.ensharp.kimyejin.voicerecognitiontest.InformationExtractor;

import android.os.AsyncTask;
import android.util.Log;

import com.ensharp.kimyejin.voicerecognitiontest.Constant;
import com.ensharp.kimyejin.voicerecognitiontest.Navigation;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONTask extends AsyncTask<String, String, String> {

    private Analyzer analyzer;

    private String column;
    private String value;
    private int purpose;
    private String word;

    JSONObject jsonObject;
    HttpURLConnection con;
    BufferedReader reader;

    public JSONTask(String column, String value, int purpose) {
        this.column = column;
        this.value = value;
        this.purpose = purpose;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            initialize();

            jsonObject.accumulate(column, value);
            try{
                URL url = new URL(strings[0]);
                //연결을 함
                con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");//POST방식으로 보냄
                con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                con.setRequestProperty("Accept", "text/html");//서버에 response 데[이터를 html로 받음
                con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                con.connect();

                //서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                //버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();//버퍼를 받아줌

                //서버로 부터 데이터를 받음
                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                finalServer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initialize() {
        jsonObject = new JSONObject();
        con = null;
        reader = null;
    }

    private void finalServer() {
        if(con != null){
            con.disconnect();
        }
        try {
            if(reader != null){
                reader.close();//버퍼를 닫아줌
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        switch (purpose) {
            case Constant.DESTINATION:
                analyzer.setDestination(result.split(",")[0]);
                break;
            case Constant.DEPARTURE:
                analyzer.setDeparture(result);
                break;
            case Constant.INTEND:
                Log.e("aaa", "aaa");
                analyzer.setIntend(result);
                break;
            case Constant.END:
                analyzer.printWords();
                break;
            case Constant.NOUN:
                discernNoun(result);
                break;
            case Constant.VERB:
                analyzer.setIntend(result);
                break;
            case Constant.TAG:
                analyzer.setDestinationDetail(result);
                break;
        }

        if (analyzer.getIsEnd())
        {
            analyzer.printWords();
            analyzer.setInformation();
            analyzer.runInformationCollector();
        }
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void discernNoun(String result) {
        if (result.split(",")[0].equals("null"))
            analyzer.anaylizeVerb(value);
        else
            analyzer.setDestination(result);
    }
}
