import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by lee on 1/19/14.
 */
public class JSONApi {

    private final String USER_AGENT = "Mozilla/5.0";

    public String getJson( String url ) throws Exception{

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);



        BufferedReader rd = new BufferedReader( new InputStreamReader(response.getEntity().getContent()));
        String json = "";
        String line = "";
        while((line = rd.readLine()) != null){
            json += line;

        }

        return json;
    }
}
