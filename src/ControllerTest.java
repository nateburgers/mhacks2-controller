import java.util.HashMap;

/**
 * Created by lee on 1/18/14.
 */
public class ControllerTest {

    public static void main(String[] args) throws Exception{

        System.out.println( "Hello world");

        Controller c = new Controller();
        Wunderground10DayResponse data = c.get10Day( "San_Fransisco", "CA");
        System.out.println( data );



    }
}