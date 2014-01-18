import java.util.HashMap;

/**
 * Created by lee on 1/18/14.
 */
public class ControllerTest {

    public static void main(String[] args) throws Exception{

        System.out.println( "Hello world");

        Controller c = new Controller();
        WundergroundWebcamResponse r = c.getCityLatestImage("CA/San_Fransisco");

        System.out.println( r.getImageUrls() );




        for( ForecastDay d : c.get10Day( "CA/San_Fransisco" ).getForcastDays() ){

            System.out.println( d.getHigh()  +  "   " + d.getLow() + "  " + d.getIcon() );

        }



    }
}