/**
 * Created by nate on 1/18/14.
 */

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


    private String longAssStringContainingESPNTitlesSeparatedByASpecialSupriseAsciiCharacter = "";



    private WundergroundAnimation _animation = new WundergroundAnimation( "NY/New_York");


    private static PImage _background;
    private static PImage origBG;

    private static int _pages = 3;
    private int _currentPage;
    private ArrayList<IControl> _pageControls;
    private ArrayList<Set<IControl>> _controlsByPage;
    private Slider theSlider;

    private String _currentTitle = "";
    private PImage _backgroundNext;

    private int loopCount = 0;

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


        longAssStringContainingESPNTitlesSeparatedByASpecialSupriseAsciiCharacter ="";
        try {
            for( String s : (new ESPNApi()).getTitles() ){
                    longAssStringContainingESPNTitlesSeparatedByASpecialSupriseAsciiCharacter += s + (char)149;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ClassLoader loader = Main.class.getClassLoader();

        _background = origBG = _backgroundNext = loadImage(loader.getResource("resources/us_map.png").toString());

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


        // make a new slider
        theSlider = Slider.HorizontalSlider( new Utils.Rect(220, 350, 300, 50));
        _controlsByPage.get(2).add( theSlider );

//        theSlider.addListener( new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//                int frame = (int) ((((Slider)(e.getSource())).getValue() * 16)) + 8;  // It's magic!!!!
//                System.out.println( "showing frame " + frame );
//
//            }
//        });


        String[] capitals = Utils.capitals();
        for (int i=0; i < capitals.length; i += 3) {
            String name = capitals[i];
            float latitude = Float.valueOf(capitals[i+1]);
            float longitude = Float.valueOf(capitals[i+2]);

            float latitudePercentile = Utils.percentileInRange(24.f, 49.f, latitude);
            float longitudePercentile = Utils.percentileInRange(-124.f, -66.f, longitude);

            int x = (int) (longitudePercentile * _width);
            int y = (int) (latitudePercentile * _height);

            PointButton button = new PointButton(x,_height - y, name, 0);
            _controlsByPage.get(0).add(button);


            final String fuckIt = name;
            final PointButton shipIt = button;

            button.addListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    System.out.println("actived!!!!");
                    Set<IControl> controls = new HashSet<IControl>();
                    controls.add(new DetailControl(fuckIt, "today"));
                    _controlsByPage.set(1, controls);
                    _currentPage = 1;
                }
            });

            button.setOnHoverListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    PointButton button = (PointButton)actionEvent.getSource();
                    _currentTitle = button.getCityName();
                }
            });

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    Controller controller = new Controller();
                    try {
                        SimpleWundergroundResponse response = controller.getCityWeather(fuckIt);
                        double temp = response.getTemp();
                        String iconURL = response.getIcon();
                        Utils.cacheImage(iconURL, loadImage(iconURL));

                        shipIt.setTemperature((int) temp);
                        shipIt.setImage(iconURL);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(r).start();
        }

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

        int frameNo = (int)(-1 * theSlider.getValue() * 16);
        if( _currentPage == 2 ){
            _background = loadImage( "/tmp/tmpimg_" + frameNo + ".png" );
        }
        else if(_currentPage != 2 && _background != origBG){
            _background = origBG;
        }


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

        fill(0);
        textSize(26);
        rect(_width/2-50,4,textWidth(_currentTitle), 28);
        fill(255);



        text(_currentTitle, _width/2-50, 30);



        rect( 0, 455, _width, _height - 455 );
        fill(0,0,0); // black
        textSize( 16 );
        text(longAssStringContainingESPNTitlesSeparatedByASpecialSupriseAsciiCharacter.substring( loopCount / 12, loopCount / 12 + 90), 0, 470);

        fill(255, 255, 255);


        loopCount++;




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
        if(_users.size() <= 0) {
            context.startTrackingSkeleton(userId);
            _users.add(userId);
        }
    }

    public void onLostUser(SimpleOpenNI context, int userId){
        _users.remove(userId);
        _context.stopTrackingSkeleton(userId);
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


    public   void set_background( String uri ){
        System.out.println( "URI    " + uri );
        _backgroundNext = requestImage( uri );

    }
}
