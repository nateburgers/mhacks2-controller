import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 1/18/14.
 */
public class WundergroundWebcamResponse {

    WebCam[] webcams;

    class WebCam{

        private String CURRENTIMAGEURL;
    }


    public List<String> getImageUrls(){
        List<String> lst  = new ArrayList<String>();
        for( WebCam webcam : webcams ){

            lst.add( webcam.CURRENTIMAGEURL );
        }
        return lst;
    }
}
