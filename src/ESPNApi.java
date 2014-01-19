import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;

/**
 * Created by lee on 1/19/14.
 */
public class ESPNApi extends JSONApi{

    private String secret ="rBKUTYzx6PdR3DXdSwTYNWTt";
    private String key = "vex4her6gbwcdkjjdxf57kae";



    public String[] getTitles() throws Exception {

        Gson gson = new GsonBuilder().create();

        String url = "http://api.espn.com/v1/sports/news/headlines/top?apikey=vex4her6gbwcdkjjdxf57kae";
        String json = getJson(url);

        System.out.println( json );


        ESPNResponse o = gson.fromJson(json, ESPNResponse.class);
        String[] titles = new String[ o.headlines.length ];
        for( int i = 0; i < o.headlines.length; i++ ){
            titles[i] = o.headlines[ i ].headline;
        }

        return titles;
    }



    public static void main( String[] args ) throws Exception {

        ESPNApi e = new ESPNApi();
        String[] titles = e.getTitles();
        for(String s : titles ){
            System.out.println( s );
        }

    }


}



class ESPNResponse{


    Headline[] headlines;

    class Headline{
        String headline;
    }



}

