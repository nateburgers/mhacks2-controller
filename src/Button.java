import SimpleOpenNI.SimpleOpenNI;
import com.sun.accessibility.internal.resources.accessibility;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by nate on 1/18/14.
 */
public class Button implements IControl {
    private int _x;
    private int _y;
    private int _width;
    private int _height;
    private boolean _on = false;
    private Set<ActionListener> _listeners;

    private PVector _entryVector;
    private boolean _active = false;
    private boolean _triggered = false;
    private String _imageName;

    private Object _callbackData;

    private static float deltaZThreshold = 0.05f;

    public Button(Utils.Rect rect) {
        this(rect.x, rect.y, rect.width, rect.height);
    }

    public Button(int x, int y, int width, int height) {
        _x = x;
        _y = y;
        _width = width;
        _height = height;
        _listeners = new HashSet<ActionListener>();
    }

    public void update(SimpleOpenNI context, Set<Integer> userIds) {
        for (int userId : userIds) {
            if (context.isTrackingSkeleton(userId)) {
                PVector leftHand = Utils.normalizedLeftHand(context, userId);
                PVector rightHand = Utils.normalizedRightHand(context, userId);
                if (containsPoint(leftHand) && !_active) {
                    _entryVector = _entryVector == null ? leftHand : _entryVector;
                    handInFrame(leftHand);
                } else if (containsPoint(rightHand) && !_active) {
                    _entryVector = _entryVector == null ? rightHand : _entryVector;
                    handInFrame(rightHand);
                } else {
                    _entryVector = null;
                }
            }
        }
    }

    public void setImage(String imageName) {
        _imageName = imageName;
    }

    public void drawInContext(PApplet applet) {
        PImage image = Utils.getImage(_imageName);
        if (image == null && !(_imageName == null)) {
            ClassLoader loader = Main.class.getClassLoader();
            image = applet.loadImage(loader.getResource(_imageName).toString());
            image.resize(_width,_height);
            Utils.cacheImage(_imageName, image);
        }

        if (_imageName == null) {
            if (_on) {
                applet.fill(255, 0, 0);
            } else {
                applet.fill(0,255,0);
            }
            applet.rect(_x, _y, _width, _height);
        } else {
            if (_on) {
                applet.tint(0, 153, 204);
                applet.image(image, _x, _y);
                applet.noTint();
            } else {
                applet.image(image, _x, _y);
            }
        }
    }

    public boolean containsPoint(PVector vector){
        return _x + _width > vector.x && vector.x > _x &&
                _y + _height > vector.y && vector.y > _y;
    }

    public void setCallbackData(Object data) {
        _callbackData = data;
    }

    public void setOn(boolean active) {
        _triggered = active;
        _on = active;
    }

    public boolean isOn() {
        return _on;
    }

    public void addListener(ActionListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(ActionListener listener) {
        _listeners.remove(listener);
    }

    private void handInFrame(PVector position) {

        if (!_triggered) {
            if(_entryVector.z - position.z > deltaZThreshold) {
                _triggered = true;
                _on = !_on;
                for (ActionListener listener : _listeners) {
                    listener.actionPerformed(new ActionEvent(this, 1, "stfu"));
                }
            }
        }
        if (_triggered && _entryVector.z - position.z < deltaZThreshold) {
            _triggered = false;
        }
    }
}
