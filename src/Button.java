import SimpleOpenNI.SimpleOpenNI;
import com.sun.accessibility.internal.resources.accessibility;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by nate on 1/18/14.
 */
public class Button {
    private int _x;
    private int _y;
    private int _width;
    private int _height;
    private boolean _on = false;
    private HashSet<ActionListener> _listeners;

    private PVector _entryVector;
    private boolean _active = false;
    private boolean _triggered = false;

    private static float deltaZThreshold = 0.03f;

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

    public void drawInContext(PApplet applet) {
        if (_on) {
            applet.fill(255, 0, 0);
        } else {
            applet.fill(0,255,0);
        }
        applet.rect(_x, _y, _width, _height);
    }

    public boolean containsPoint(PVector vector){
        return _x + _width > vector.x && vector.x > _x &&
                _y + _height > vector.y && vector.y > _y;
    }

    public void addListener(ActionListener listener) {
        _listeners.add(listener);
    }

    public void removeListener(ActionListener listener) {
        _listeners.remove(listener);
    }

    private void handInFrame(PVector position) {

        if (!_triggered) {
            System.out.println(_entryVector.z - position.z);
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
