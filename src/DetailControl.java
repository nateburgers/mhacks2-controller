import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;

import java.util.Set;

/**
 * Created by nate on 1/18/14.
 */
public class DetailControl implements IControl {

    private final String _cityName;
    private final String _date;

    public DetailControl (String cityName, String date) {
        _cityName = cityName;
        _date = date;
    }

    public void updateData() {

    }

    public void update(SimpleOpenNI context, Set<Integer>userIds) {

    }

    public void drawInContext(PApplet applet) {

    }

}
