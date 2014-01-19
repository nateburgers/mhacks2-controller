/**
 * Created by lee on 1/18/14.
 */
public class AnimationTest {

    public static void main(String[] args){

        System.out.println("hello");
        WundergroundAnimation anim = new WundergroundAnimation( "NY/New_York");

        System.out.println( anim.getFrame(2) );

    }
}
