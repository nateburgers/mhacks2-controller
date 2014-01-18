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
        return new PVector((vector.x / vector.z) * 640.f + 320.f,
                (vector.y / -vector.z) * 480.f + 240.f,
                (vector.z / 1000.f));
    }

    public static PVector easeToward(PVector a, PVector b, float percentage) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return new PVector(
                a.x + dx * percentage,
                a.y + dy * percentage
        );
    }

    public static float distance(PVector a, PVector b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return (float)Math.sqrt(dx*dx + dy*dy);
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
