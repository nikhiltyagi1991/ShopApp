package nikhiltyagi.shopapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nikhil Tyagi on 06-11-2015.
 */
public class NetworkController {

    private String url;

    public NetworkController(String url){
        this.url = url;
    }

    public JSONObject getJSONObjectFromUrl(HashMap<String,String> params) throws Exception{
        String result = getContentFromWeb(params);
        return new JSONObject(result);
    }

    public JSONArray getJSONArrayFromUrl(HashMap<String,String> params) throws Exception{
        String result = getContentFromWeb(params);
        return new JSONArray(result);
    }

    private String getContentFromWeb(HashMap<String,String> params) throws Exception{
        URL connectURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
        bw.write(getPostDataString(params));
        bw.flush();bw.close();os.close();

        conn.connect();

        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        in.close();
        return sb.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
