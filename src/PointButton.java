import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.channels.Pipe;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by nate on 1/18/14.
 */
public class PointButton implements IControl {

    final static private Utils.Rect _bounds = new Utils.Rect(0,0,640,480);

    private float latitude;
    private float longitude;

    private static float distanceThreshold = 50.f;
    private static boolean _on = false;

    private PVector _position;
    private PVector _movedPosition;
    private float _diameter = 20.f;
    private static final float _minDiameter = 20.f;
    private static final float _maxDiameter = 120.f;

    private boolean _triggered = false;
    private PVector _entryVector;
    private float deltaZThreshold = 0.10f;

    private Set<ActionListener> _listeners;
    private ActionListener _hoverListener;

    private String _cityName;
    private int _temperature;
    private String _imageURL;

    public PointButton(int x, int y, String cityName, int temperature){
        _position = new PVector(x,y);
        _movedPosition = new PVector(_position.x, _position.y);
        _listeners = new HashSet<ActionListener>();
        _cityName = cityName;
        _temperature = temperature;
    }

    public void update(SimpleOpenNI context, Set<Integer> userIds) {
        for(int userId : userIds) {
            PVector leftHand = Utils.normalizedLeftHand(context, userId);
            PVector rightHand = Utils.normalizedRightHand(context, userId);

            if (_bounds.contains(leftHand) && _bounds.contains(rightHand)) {
                float leftHandDistance = Utils.distance(_position, leftHand);
                float rightHandDistance = Utils.distance(_position, rightHand);
                if (leftHandDistance < rightHandDistance) {
                    updatePositionWithHand(leftHand, leftHandDistance);
                } else {
                    //updatePositionWithHand(rightHand, rightHandDistance);
                }
            }
        }
    }

    public String getCityName() {
        return _cityName;
    }

    public void setImage(String url) {
        _imageURL = url;
    }

    public void setTemperature(int temp) {
        _temperature = temp;
    }

    public void addListener(ActionListener listener) {
        _listeners.add(listener);
    }

    public void setOnHoverListener(ActionListener listener) {
        _hoverListener = listener;
    }

    public void updatePositionWithHand(PVector hand, float distance) {
        if (distance <= distanceThreshold) {
            _diameter = _minDiameter + (_maxDiameter - _minDiameter) * (1 - distance / distanceThreshold);

            float dy = hand.y - _position.y;
            float dx = hand.x - _position.x;
            float normalY = dy / distance;
            float normalX = dx / distance;

            _movedPosition.y = _position.y + normalY * (float)Math.sqrt(distance);
            _movedPosition.x = _position.x + normalX * (float)Math.sqrt(distance);

            if (distance <= _diameter) {
                _entryVector = _entryVector == null ? hand : _entryVector;
                if (_entryVector.z - hand.z > deltaZThreshold) {
                    _entryVector = null;
                    for (ActionListener listener : _listeners) {
                        listener.actionPerformed(new ActionEvent(this, 1, "lol"));
                    }
                } else {
                    _hoverListener.actionPerformed(new ActionEvent(this, 1, "lol"));
                }
            }
        } else {
            _diameter = _minDiameter;
            _movedPosition.x = _position.x;
            _movedPosition.y = _position.y;
        }
    }


    public void drawInContext(PApplet applet) {
        applet.strokeWeight(2.0f);
        applet.stroke(0);
        applet.line(_position.x, _position.y, _movedPosition.x, _movedPosition.y);
        PImage image = Utils.getImage(_imageURL);
        if (image == null) {
            applet.fill(0);
            applet.ellipse(_movedPosition.x, _movedPosition.y, _diameter, _diameter);
        } else {
            applet.image(image,_movedPosition.x-image.width/2,_movedPosition.y-image.height/2);
        }
        //applet.text(_cityName, _position.x - _diameter /2 - 10, _position.y - _diameter/2 - 5);
        float temperatureTextHeight = _diameter / 1.4f;
        applet.textSize(temperatureTextHeight);
        applet.fill(255);
        String temperatureString = ((Integer)_temperature).toString();
        float temperatureTextWidth = applet.textWidth(temperatureString);
        applet.text(temperatureString, _position.x-temperatureTextWidth/2, _position.y+temperatureTextHeight/2);
    }
}
