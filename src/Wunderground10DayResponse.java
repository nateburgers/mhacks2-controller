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

            class ForecastDay{
                String icon;
                int period;
                HighLow high;
                HighLow low;
            }


            class HighLow{
                int celsius;
                int fahrenheit;
            }

        }





    }


    public String toString(){
        String s ="";
        for(Forecast.SimpleForecast.ForecastDay d : this.forecast.simpleforecast.forecastday ){
            s += d.icon + "  (" + d.high.fahrenheit + "F)\n";
        }
        return s;
    }

}
