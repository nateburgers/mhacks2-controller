/**
 * Created by nate on 1/18/14.
 */
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Controller{
    private final Gson gson;
    String APIKEY = "310edd12b9023998";
    private final String USER_AGENT = "Mozilla/5.0";


    public Controller(){
       gson = new GsonBuilder().setPrettyPrinting().create();

    }


    /**
     * SF, NY, Ann Arbor, Miami, Chicago
     * @return
     * @throws Exception
     */
    public HashMap<String, SimpleWundergroundResponse> get_big_city_weather() throws Exception{

        String[] cities = { "CA/San_Fransisco", "MI/Ann_Arbor", "FL/Miami", "IL/Chicago" };

        HashMap<String, SimpleWundergroundResponse> map = new HashMap<String, SimpleWundergroundResponse>();

        for( String cityCode : cities ){

            String url = "http://api.wunderground.com/api/310edd12b9023998/conditions/q/" + cityCode + ".json";

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);

            // add request header
            request.addHeader("User-Agent", USER_AGENT);

            HttpResponse response = client.execute(request);



            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            String totalLine = "";
            String line = "";
            while((line = rd.readLine()) != null){
                totalLine += line;

            }



            SimpleWundergroundResponse wres = gson.fromJson( totalLine, SimpleWundergroundResponse.class );
            map.put( cityCode, wres );

        }

        return map;



    }



    public String get10Day() throws Exception{

        String url = "http://api.wunderground.com/api/310edd12b9023998/forecast10day/q/CA/San_Francisco.json";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);



        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String totalLine = "";
        String line = "";
        while((line = rd.readLine()) != null){
            totalLine += line;

        }
        return totalLine;

    }
    public void printTestData() throws Exception{

        String url = "http://api.wunderground.com/api/310edd12b9023998/animatedradar/animatedsatellite/q/NY/New_York.gif?num=8&delay=25&interval=30";
//        String url = "http://api.wunderground.com/api/310edd12b9023998/radar/image.gif?centerlat=38&centerlon=-96.4&radius=100&width=280&height=280&newmaps=1";
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        request.addHeader("User-Agent", USER_AGENT);

        HttpResponse response = client.execute(request);



        BufferedInputStream rd = new BufferedInputStream(
                (response.getEntity().getContent()));

        int line = 0;
        FileOutputStream out = new FileOutputStream("img.gif");

        byte[] buff_array;
        while ((line = rd.read()) != -1) {
            out.write((byte) line);
        }

        out.close();

    }
}