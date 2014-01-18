import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 1/18/14.
 */
public class Wunderground10DayResponse {


    Forecast forecast;



    class Forecast{


        SimpleForecast simpleforecast;

        public Forecast(){



        }



        class SimpleForecast{

            ForecastDay[] forecastday;





        }





    }



    public List<ForecastDay> getForcastDays(){
        List<ForecastDay> lst = new ArrayList<ForecastDay>();

        for( ForecastDay day : this.forecast.simpleforecast.forecastday ){
            lst.add( day );
        }
        return lst;

    }


}


class ForecastDay{
    private String icon;
    private int period;
    private HighLow high;
    private HighLow low;

    public int getHigh() {
        return high.fahrenheit;
    }

    public int getLow(){
        return low.fahrenheit;
    }

    public String getIcon() {
        return icon;
    }


    class HighLow{
        int celsius;
        int fahrenheit;
    }
}

