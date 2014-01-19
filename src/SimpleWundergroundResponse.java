
/**
 * Created by lee on 1/18/14.
 */
public class SimpleWundergroundResponse {

    public Current_observation current_observation;

    class Current_observation {
        public double temp_f;
        public String icon;

        public Current_observation(){

        }
    }

    public double getTemp(){
        return current_observation.temp_f;
    }

    public String getIcon(){
        return current_observation.icon;
    }


    public String toString(){
        return String.format("%s (%f f)", this.current_observation.icon, this.current_observation.temp_f );
    }

}

