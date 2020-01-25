package main;

import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

public class GIFMaker {
    private static ImageWriter writer;
    private static ImageWriteParam param;
    private static ScheduledExecutorService pool;
    private static List<WritableImage> images;
    private static final int DEFAULT_TIME = 10000;
    private static Pane drawingPane;

    static{
        images = new ArrayList<>();
    }

    public static void createGif ()
    {

    }


    private static ImageWriter getWriter() throws IIOException{
        Iterator<ImageWriter> iterator = ImageIO.getImageWritersBySuffix("gif");
        if(!iterator.hasNext())
            throw  new IIOException("No image writers");
        else
            return iterator.next();
    }

    public static void shutDown(){
        pool.shutdownNow();
        images.clear();
    }

    public static void takeSnapshots(int time){
        pool = Executors.newScheduledThreadPool(1);
//        pool.schedule(()->
//        {
//            Thread.sleep();
//        })
    }

}


