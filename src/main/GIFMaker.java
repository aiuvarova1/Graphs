package main;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.util.Iterator;

public class GIFMaker {
    private static ImageWriter writer;
    private static ImageWriteParam param;

    public static void createGif ()
    {
        try {
            writer = getWriter();
            //writer.prepareWriteSequence();
        }catch(IIOException ex){
            ex.printStackTrace();
        }
    }


    private static ImageWriter getWriter() throws IIOException{
        Iterator<ImageWriter> iterator = ImageIO.getImageWritersBySuffix("gif");
        if(!iterator.hasNext())
            throw  new IIOException("No image writers");
        else
            return iterator.next();
    }

}


