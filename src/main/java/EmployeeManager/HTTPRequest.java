package EmployeeManager;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest {

    // HTTP GET请求
    public JSONObject sendGET(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        con.disconnect();

        JSONObject jsonObject = new JSONObject(response.toString());
        return jsonObject;
    }
    
    // HTTP POST请求
    public JSONObject sendPost(String url,JSONObject data) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpsURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setDoInput(true);

        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        writer.write(data.toString());
        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        con.disconnect();

        JSONObject jsonObject = new JSONObject(response.toString());
        return jsonObject;
    }

}
