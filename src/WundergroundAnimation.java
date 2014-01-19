import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;


/**
 * Created by lee on 1/18/14.
 */
public class WundergroundAnimation {

    final ArrayList<String> images;


    final ReentrantLock lock;


    public WundergroundAnimation(String locString ){


        this.images = new ArrayList<String>();

        lock = new ReentrantLock();

        final String url = "http://api.wunderground.com/api/310edd12b9023998/satellite/q/" + locString + ".png?width=640&height=480&basemap=1&num=1&frame=";


        Thread t = new Thread(){


            @Override
            public void run(){

                // always the first lock
                lock.lock();

                // first for loop; download 15 frames
                for( int i = 0; i < 16; i++ ){
                    String frameURL = url + i;
                    System.out.println( frameURL );
                    HttpClient cli = new DefaultHttpClient();

                    HttpGet request = new HttpGet( frameURL );
                    request.setHeader("User-Agent", "Mozilla/5.0");

                    try{
                        HttpResponse response = cli.execute( request );

                        BufferedInputStream rd = new BufferedInputStream( response.getEntity().getContent() );

                        // String file_uri = "../src/data/img_" + i + ".gif";
                        ClassLoader cl = Main.class.getClassLoader();
                        String file_uri = "/tmp/tmpimg_" + i + ".png" ;

                        System.out.println( file_uri );







                        FileOutputStream out = new FileOutputStream( file_uri );

                        int chr;
                        while( (chr = rd.read()) != -1 ){
                            out.write( chr );
                        }


                        out.close();

                        images.add( file_uri );


                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }



                lock.unlock();

            }
        };


        t.start();


    }


    public String getFrame( int i ){

        while( lock.isLocked() ){
            Thread.yield();
        }
        lock.lock();
        return this.images.get( i );


    }


    public int size(){
        return this.images.size();
    }
}
