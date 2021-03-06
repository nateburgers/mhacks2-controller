/**
 * Created by nate on 1/18/14.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import SimpleOpenNI.*;
import processing.core.*;

public class Main extends PApplet {

    private SimpleOpenNI _context;
    private static int _width = 640;
    private static int _height = 480;
    private static int _cellSize = _width / 8;
    private static int _inset = 10;
    private Set<Integer> _users;
    private ArrayList<Button> _buttons;
    private ArrayList<Slider> _sliders;
    private Slider _volumeSlider;

    public Main() {
        _users = new HashSet<Integer>();
        _buttons = new ArrayList<Button>();
        _sliders = new ArrayList<Slider>();

        for (int i=0; i < 5; i++) {
            for(int j=0; j < 5; j++) {
                Button button = new Button(new Utils.Rect(i*_cellSize,
                        j*_cellSize, _cellSize, _cellSize).inset(_inset));
                button.addListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        System.out.println("you touched me! eww");
                    }
                });
                _buttons.add(button);
            }
        }

        for (int i=5; i<8; i++) {
            Slider slider = Slider.VerticalSlider(new Utils.Rect(i * _cellSize,
                    0, _cellSize, _cellSize * 5).inset(_inset));
            _sliders.add(slider);
            slider.setValue(0.5f);
        }

        _volumeSlider = Slider.HorizontalSlider(new Utils.Rect(
                0, _cellSize*5, _cellSize*8, _cellSize
        ).inset(_inset));
        _volumeSlider.setValue(0.5f);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{ "--present", "SomeSketch"});
    }

    public void setup(){
        _context = new SimpleOpenNI(this);
        _context.setMirror(true);
        _context.enableDepth();
        if (_context.enableUser(this)) System.out.println("farts");

        smooth();
        frameRate(30.f);
        size(640, 480);
        background(255);
    }

    public void draw(){
        clear();
        _context.update();

        image(_context.depthImage(), 0, 0);

        for (Slider slider : _sliders) {
            slider.update(_context, _users);
            slider.drawInContext(this);
        }
        for (Button button : _buttons) {
            button.update(_context, _users);
            button.drawInContext(this);
        }

        _volumeSlider.update(_context, _users);
        _volumeSlider.drawInContext(this);

        for(int userId : _users) {
            if (_context.isTrackingSkeleton(userId)) {
                drawSkeleton(userId);
            }
        }
    }

    public void drawSkeleton(int userId) {
        fill(255, 255, 0);
        PVector leftHandPosition = new PVector();
        _context.getJointPositionSkeleton(userId, SimpleOpenNIConstants.SKEL_LEFT_HAND, leftHandPosition);
        drawPoint(Utils.normalizedVector(leftHandPosition));

        fill(0, 255, 255);
        PVector rightHandPosition = new PVector();
        _context.getJointPositionSkeleton(userId, SimpleOpenNIConstants.SKEL_RIGHT_HAND, rightHandPosition);
        drawPoint(Utils.normalizedVector(rightHandPosition));
    }

    public void drawPoint(PVector victor){
        ellipse(victor.x, victor.y, 20,20);
    }

    public void onNewUser(SimpleOpenNI context, int userId){
        System.out.println("Tracking user " + userId);
        context.startTrackingSkeleton(userId);
        _users.add(userId);
    }

    public void onLostUser(SimpleOpenNI context, int userId){
        System.out.println("Lost user " + userId);
    }

    public void onVisibleUser(SimpleOpenNI context, int userId){
    }

    public void onNewHand(SimpleOpenNI context, int handId, PVector vector){
        System.out.println("lasdfasdf");
    }

    public void onTrackedHand(SimpleOpenNI context, int handId, PVector vector){
        System.out.println("asfdads");
    }

    public void onLostHand(SimpleOpenNI context, int handId){
        System.out.println("asfdasfasfd");
    }

    public void onNewGesture(SimpleOpenNI context, int gId){
        System.out.println("asdfasdfasdfasdf");
    }
}
