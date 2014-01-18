import java.util.HashMap;

/**
 * Created by lee on 1/18/14.
 */
public class ControllerTest {

    public static void main(String[] args) throws Exception{

        System.out.println( "Hello world");

        Controller c = new Controller();


        HashMap<String, SimpleWundergroundResponse> bcw = c.get_big_city_weather();
        for( String k : bcw.keySet() ){
            System.out.println( k );
            System.out.println( bcw.get(k) );
        }

    }
}