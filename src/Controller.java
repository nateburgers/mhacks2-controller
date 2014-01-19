/**
 * Created by nate on 1/18/14.
 */
import java.io.*;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Controller extends JSONApi {
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

            String json = getJson( url );



            SimpleWundergroundResponse wres = gson.fromJson( json, SimpleWundergroundResponse.class );
            map.put( cityCode, wres );

        }

        return map;



    }



    public SimpleWundergroundResponse getCityWeather( String locString ) throws Exception{

        String url = "http://api.wunderground.com/api/310edd12b9023998/conditions/q/" + locString + ".json";
        String json = getJson( url );

        SimpleWundergroundResponse res = gson.fromJson( json, SimpleWundergroundResponse.class );
        return res;

    }



    public Wunderground10DayResponse get10Day( String locString ) throws Exception{


        String url = "http://api.wunderground.com/api/310edd12b9023998/forecast10day/q/" + locString + ".json";

        String json = getJson( url );

        return gson.fromJson( json, Wunderground10DayResponse.class );


    }




    public WundergroundWebcamResponse getCityLatestImage( String locString ) throws Exception{


        String earl = "http://api.wunderground.com/api/310edd12b9023998/webcams/q/" + locString + ".json";
        System.out.println( earl );
        String json = getJson( earl );

        WundergroundWebcamResponse richard = gson.fromJson( json,  WundergroundWebcamResponse.class );

        return richard;



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