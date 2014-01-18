import java.util.HashMap;

/**
 * Created by lee on 1/18/14.
 */
public class ControllerTest {

    public static void main(String[] args) throws Exception{






        Controller c = new Controller();

        SimpleWundergroundResponse sr = c.getCityWeather( "FL/Miami");
        System.out.println( "Floring: "  + sr + "    " + sr.getTemp() + "   " + sr.getIcon());

        WundergroundWebcamResponse r = c.getCityLatestImage("CA/San_Fransisco");

        System.out.println( r.getImageUrls() );




        for( ForecastDay d : c.get10Day( "CA/San_Fransisco" ).getForcastDays() ){

            System.out.println( d.getHigh()  +  "   " + d.getLow() + "  " + d.getIcon() );

        }



    }
}