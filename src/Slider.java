import SimpleOpenNI.SimpleOpenNI;
import processing.core.PApplet;
import processing.core.PVector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by nate on 1/18/14.
 */
public class Slider implements IControl {

    public enum Orientation {
        SliderOrientationHorizontal,
        SliderOrientationVertical,
    };

    private Utils.Rect _bounds;
    private Utils.Rect _slider;
    private Orientation _orientation;

    private PVector _entryVector;

    private static float deltaZThreshold = 0.03f;

    static Slider VerticalSlider(Utils.Rect bounds) {
        Utils.Rect slider = new Utils.Rect(bounds.x, bounds.y, bounds.width, 60);
        return new Slider(bounds, slider, Orientation.SliderOrientationVertical);
    }

    static Slider HorizontalSlider(Utils.Rect bounds) {
        Utils.Rect slider = new Utils.Rect(bounds.x, bounds.y, 60, bounds.height);
        return new Slider(bounds, slider, Orientation.SliderOrientationHorizontal);
    }

    private Slider(Utils.Rect bounds, Utils.Rect slider, Orientation orientation) {
        _bounds = bounds;
        _slider = slider;
        _orientation = orientation;
    }

    public void setValue(float value) {
        if (_orientation == Orientation.SliderOrientationVertical) {
            _slider = _slider.centered(new PVector(
                    _bounds.x + _bounds.width / 2,
                    _bounds.y + _bounds.height * value
            ));
        } else {
            _slider = _slider.centered(new PVector(
                    _bounds.x + _bounds.width * value,
                    _bounds.y + _bounds.height / 2
            ));
        }
    }

    public float getValue() {
        PVector center = _slider.center();
        if (_orientation == Orientation.SliderOrientationVertical) {
            return (center.y - _bounds.y) / _bounds.height;
        } else {
            return (_bounds.x - center.x) / _bounds.width;
        }
    }

    public void update(SimpleOpenNI context, Set<Integer> userIds) {
        for(int userId : userIds) {
            if (context.isTrackingSkeleton(userId)) {
                PVector leftHand = Utils.normalizedLeftHand(context, userId);
                PVector rightHand = Utils.normalizedRightHand(context, userId);
                if (_bounds.contains(leftHand)) {
                    _entryVector = _entryVector == null ? leftHand : _entryVector;
                    handInFrame(leftHand);
                } else if (_bounds.contains(rightHand)) {
                    _entryVector = _entryVector == null ? rightHand : _entryVector;
                    handInFrame(rightHand);
                }
            }
        }
    }

    public void handInFrame(PVector position) {
        System.out.println(position);
        if(_entryVector.z - position.z > deltaZThreshold) {
            if (_orientation == Orientation.SliderOrientationVertical) {
                _slider.y = (int)position.y - _slider.height / 2;
            } else {
                _slider.x = (int)position.x - _slider.width / 2;
            }
        }
    }

    public void drawInContext(PApplet applet) {
        applet.stroke(0, 0, 255);
        if (_orientation == Orientation.SliderOrientationVertical) {
            int originCenterX = _bounds.x + _bounds.width / 2;
            applet.line(originCenterX, _bounds.y, originCenterX, _bounds.y+_bounds.height);
            applet.rect(_slider.x, _slider.y, _slider.width, _slider.height);
        } else {
            int originCenterY = _bounds.y + _bounds.height / 2;
            applet.line(_bounds.x, originCenterY, _bounds.x + _bounds.width, originCenterY);
            applet.rect(_slider.x, _slider.y, _slider.width, _slider.height);
        }
    }
}
