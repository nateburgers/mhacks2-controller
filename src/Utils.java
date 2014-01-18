import SimpleOpenNI.SimpleOpenNI;
import processing.core.PVector;

/**
 * Created by nate on 1/18/14.
 */
public class Utils {

    public static class Rect {
        public int x;
        public int y;
        public int width;
        public int height;
        public Rect(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        public PVector center() {
            return new PVector(this.x + this.width / 2,
                    this.y + this.height / 2);
        }
        public boolean contains(PVector vector) {
            return x + width > vector.x && vector.x > x &&
                    y + height > vector.y && vector.y > y;
        }
        public Rect inset(int inset){
            return new Rect(this.x - inset,
                    this.y - inset,
                    this.width - inset*2,
                    this.height - inset*2);
        }
        public Rect centered(PVector point) {
            return new Rect((int)point.x - this.width / 2,
                    (int)point.y - this.height / 2,
                    this.width,
                    this.height);
        }
    }

    public static PVector normalizedVector(PVector vector) {
        return new PVector((vector.x / 2000.f + 0.5f) * 640.f,
                (vector.y / -1000.f + 0.5f) * 480.f,
                (vector.z / 1000.f));
    }

    public static PVector normalizedLeftHand(SimpleOpenNI context, int userId) {
        PVector victor = new PVector();
        context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_LEFT_HAND, victor);
        return normalizedVector(victor);
    }

    public static PVector normalizedRightHand(SimpleOpenNI context, int userId) {
        PVector victor = new PVector();
        context.getJointPositionSkeleton(userId, SimpleOpenNI.SKEL_RIGHT_HAND, victor);
        return normalizedVector(victor);
    }
}
