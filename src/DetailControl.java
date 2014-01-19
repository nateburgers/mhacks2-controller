import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.Set;

/**
 * Created by nate on 1/18/14.
 */
public class DetailControl implements IControl {

    private final Utils.Rect _bounds;

    private final String _cityName;
    private final String _title;
    private final ForecastDay _forecast;

    public DetailControl (String cityName,
                          String title,
                          ForecastDay forecast,
                          Utils.Rect bounds) {
        _cityName = cityName;
        _title = title;
        _forecast = forecast;
        _bounds = bounds;
    }

    public void update(SimpleOpenNI context, Set<Integer>userIds) {

    }

    public void drawInContext(PApplet applet) {
        applet.fill(126,5,121,127);
        applet.rect(_bounds.x,_bounds.y,_bounds.width,_bounds.height);

        PImage icon = Utils.getImage(_forecast.getIcon());
        if (icon == null) {
            icon = applet.loadImage(_forecast.getIcon());
            Utils.cacheImage(_forecast.getIcon(), icon);
        }

        PImage mutableIcon = new PImage(icon.width*2,icon.height*2);
        mutableIcon.copy(icon,0,0,icon.width,icon.height,0,0,icon.width*2,icon.height*2);
        float iconCenterY = _bounds.width / 2;
        float iconCenterX = _bounds.width / 2;
        float iconX = iconCenterX - mutableIcon.width / 2;
        float iconY = iconCenterY - mutableIcon.height / 2;
        applet.image(mutableIcon, iconX, iconY);

        applet.fill(255);
        applet.textSize(icon.height);

    }
}
