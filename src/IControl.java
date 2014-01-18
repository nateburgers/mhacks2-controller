import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import sun.print.SunMinMaxPage;
import java.util.*;

/**
 * Created by nate on 1/18/14.
 */
public interface IControl {

    public void update(SimpleOpenNI context, Set<Integer> userIds);
    public void drawInContext(PApplet applet);

}
