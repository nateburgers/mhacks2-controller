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

    private PImage _background;

    private static int _pages = 3;
    private int _currentPage;
    private ArrayList<IControl> _pageControls;
    private ArrayList<Set<IControl>> _controlsByPage;

    public Main() {

        assert Utils.percentileInRange(0,100,55) == 0.55;

        _users = new HashSet<Integer>();
        _pageControls = new ArrayList<IControl>();
        _controlsByPage = new ArrayList<Set<IControl>>();
        for(int i=0; i<_pages; i++) {
            _controlsByPage.add(new HashSet<IControl>());
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{ "--present", "SomeSketch"});
    }

    public void setup(){
        _context = new SimpleOpenNI(this);
        _context.setMirror(true);
        _context.enableDepth();
        _context.enableRGB();
        if (_context.enableUser(this)) System.out.println("farts");

        ClassLoader loader = Main.class.getClassLoader();
        _background = loadImage(loader.getResource("resources/us_map.png").toString());

        for(int i=0; i<_pages; i++) {
            Button button = new Button(
                    new Utils.Rect(_cellSize*7,_cellSize*(i+1),_cellSize,_cellSize).inset(_inset));
            button.addListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.out.println("yay");
                    _currentPage = _pageControls.indexOf(actionEvent.getSource());
                    for (IControl control : _pageControls) {
                        if (control != actionEvent.getSource()) {
                            ((Button)control).setOn(false);
                        }
                    }
                }
            });
            _pageControls.add(button);
        }

        String[] capitals = Utils.capitals();
        for (int i=0; i < capitals.length; i += 3) {
            String name = capitals[i];
            float latitude = Float.valueOf(capitals[i+1]);
            float longitude = Float.valueOf(capitals[i+2]);

            float latitudePercentile = Utils.percentileInRange(24.f, 49.f, latitude);
            float longitudePercentile = Utils.percentileInRange(-124.f, -66.f, longitude);

            System.out.println("latitude: " + latitude + " percentile: " + latitudePercentile);

            int x = (int) (longitudePercentile * _width);
            int y = (int) (latitudePercentile * _height);


            System.out.println("x: " + x + " y: " + y);

            PointButton button = new PointButton(x,_height - y);
            _controlsByPage.get(0).add(button);
        }

//        for (int i=0; i < 50; i++) {
//            int x = (int) (Math.random() * _width);
//            int y = (int) (Math.random() * _height);
//            PointButton button = new PointButton(x,y);
//            _controlsByPage.get(0).add(button);
//        }

        smooth();
        frameRate(30.f);
        size(640, 480);
        background(255);
    }

    public void draw(){
        clear();
        fill(0);
        rect(0,0,_width,_height);

        _context.update();
        image(_background, 0, 0);

        PImage userImage = _context.userImage();
        userImage.filter(THRESHOLD, 0.9f);
        PImage rgbImage = _context.rgbImage();
        rgbImage.mask(userImage);
        image(rgbImage, 0, 0);

        for (IControl control : _pageControls) {
            control.update(_context, _users);
            control.drawInContext(this);
        }

        for (IControl control : _controlsByPage.get(_currentPage)) {
            control.update(_context, _users);
            control.drawInContext(this);
        }

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
