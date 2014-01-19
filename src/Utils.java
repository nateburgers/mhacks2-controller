import SimpleOpenNI.SimpleOpenNI;
import processing.core.PImage;
import processing.core.PVector;

import java.util.HashMap;

/**
 * Created by nate on 1/18/14.
 */
public class Utils {

    private static HashMap<String, PImage> _imageCache = new HashMap<String, PImage>();

    public static void cacheImage(String url, PImage image) {
        _imageCache.put(url, image);
    }

    public static PImage getImage(String url) {
        return _imageCache.get(url);
    }

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
            return new Rect(this.x + inset,
                    this.y + inset,
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

    public static float percentileInRange(float a, float b, float x) {
        float percentile = (x - a) / (b - a);
        return percentile;
    }

    public static String[] capitals() {
        String[] capitals = {
                "RI/Providence","41.817398","-71.453674",
                "MA/Boston","42.370567","-71.026964",
                "NV/Lincoln","44.051981","-71.6606",
                "NH/Concord","43.213705","-71.53774",
                "ME/Augusta","44.344406","-69.76345",
                "VA/Richmond","44.392443","-72.95936",
                "VT/Montpelier","44.19906","-72.559638",
                "CT/Hartford","41.78007","-72.677099",
                "NJ/Trenton","40.280531","-74.712018",
                "NY/Albany","42.614852","-73.970812",
                "PA/Harrisburg","40.261839","-76.88279",
                "DE/Dover","39.16426","-75.51163",
                "MD/Annapolis","38.997511","-76.49803",
                "WI/Madison","38.038145","-81.79154",
                "WV/Charleston","38.350647","-81.63028",
                "NC/Raleigh","35.898538","-78.738904",
                "SC/Columbia","33.998454","-81.03519",
                "GA/Atlanta","33.844371","-84.47405",
                "FL/Tallahassee","30.431283","-84.26903",
                "AL/Montgomery","32.356988","-86.257817",
                "TN/Nashville","36.164556","-86.77738",
                "MS/Jackson","32.292396","-90.18328",
                "KY/Frankfort","38.201649","-84.86935",
                "OH/Columbus","40.097796","-83.02076",
                "OR/Salem","40.898658","-80.86094",
                "IN/Indianapolis","39.775125","-86.10839",
                "MI/Lansing","42.599184","-84.371973",
                "IA/Des_Moines","41.672687","-93.572173",
                "MN/Saint_Paul","44.964852","-93.08397",
                "SD/Pierre","44.425356","-100.29145",
                "ND/Bismarck","46.83962","-100.7723",
                "MT/Helena","46.588803","-112.04193",
                "MO/Jefferson_City","46.376532","-112.14074",
                "IL/Springfield","39.80095","-89.64999",
                "KS/Topeka","38.988075","-95.780662",
                "LA/Baton_Rouge","30.44884","-91.18633",
                "AR/Little_Rock","34.745692","-92.27987",
                "OK/Oklahoma_City","35.491608","-97.562817",
                "TX/Austin","30.326374","-97.771258",
                "CO/Denver","39.726303","-104.856808",
                "WY/Cheyenne","41.141281","-104.80208",
                "ID/Boise","43.603768","-116.272921",
                "UT/Salt_Lake_City","40.754746","-111.89875",
                "AZ/Phoenix","33.703967","-112.351835",
                "NM/Santa_Fe","35.755312","-105.99936",
                "NV/Carson_City","39.147877","-119.74536",
                "CA/Sacramento","38.380456","-121.555406",
                "WA/Olympia","47.014718","-122.8819"
        };
        return capitals;
    }
}
