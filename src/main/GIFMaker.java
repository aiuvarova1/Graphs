package main;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;


import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.*;


public class GIFMaker {

    private static List<WritableImage> images;
    public static final int DEFAULT_TIME = 10000;
    private static Timer timer;

     private static final int DELAY_TIME = 60;

    private static ImageOutputStream out;


    static{
        images = new ArrayList<>();
    }

    public static void createGif ()
    {
        timer.cancel();

        if(images.isEmpty())
            return;

        specifyOutput();

        if(out == null)
            return;


        BufferedImage buf = SwingFXUtils.fromFXImage(images.get(0),null);
        ImageWriter writer = ImageIO.getImageWritersBySuffix("gif").next();
        ImageWriteParam param = writer.getDefaultWriteParam();

        ImageTypeSpecifier sp = ImageTypeSpecifier.createFromBufferedImageType(buf.getType());
        IIOMetadata metadata = writer.getDefaultImageMetadata(sp,param);

        try {
            specifyMetaData(metadata);
            writer.setOutput(out);
            writer.prepareWriteSequence(null);

            for (WritableImage image : images) {
                buf = SwingFXUtils.fromFXImage(image, null);
                writer.writeToSequence(new IIOImage((RenderedImage) buf, null, metadata), param);
            }

            writer.endWriteSequence();
            out.close();
        }
        catch(IOException ex)
        {
            PopupMessage.showMessage("Failed to write the file");
            ex.printStackTrace();
        }

    }

    private static void specifyMetaData(IIOMetadata metadata) throws IOException{

        String metaFormatName = metadata.getNativeMetadataFormatName();
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metaFormatName);

        IIOMetadataNode graphicsControlExtensionNode = getNode(root, "GraphicControlExtension");
        graphicsControlExtensionNode.setAttribute("disposalMethod", "none");
        graphicsControlExtensionNode.setAttribute("userInputFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("transparentColorFlag", "FALSE");
        graphicsControlExtensionNode.setAttribute("delayTime", Integer.toString(DELAY_TIME / 10));
        graphicsControlExtensionNode.setAttribute("transparentColorIndex", "0");

        IIOMetadataNode appExtensionsNode = getNode(root, "ApplicationExtensions");
        IIOMetadataNode child = new IIOMetadataNode("ApplicationExtension");

        child.setAttribute("applicationID", "NETSCAPE");
        child.setAttribute("authenticationCode", "2.0");

        child.setUserObject(new byte[]{ 0x1, (byte) 0, (byte) 0});
        appExtensionsNode.appendChild(child);
        metadata.setFromTree(metaFormatName, root);
    }

    private static IIOMetadataNode getNode(IIOMetadataNode rootNode, String nodeName){
        int nNodes = rootNode.getLength();
        for (int i = 0; i < nNodes; i++){
            if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)){
                return (IIOMetadataNode) rootNode.item(i);
            }
        }
        IIOMetadataNode node = new IIOMetadataNode(nodeName);
        rootNode.appendChild(node);
        return(node);
    }



    private static void specifyOutput(){
        PopupMessage.fixMessage("Please,wait...");
        Drawer.getInstance().enableDialog(true);
        File file = FileManager.getGifFile();

        if(file == null)
            return;


        try {
            out = new FileImageOutputStream(file);
        }catch(FileNotFoundException ex1)
        {
            PopupMessage.showMessage("File not found");
        }catch(IOException ex)
        {
            PopupMessage.showMessage("Failed to open the file");
        }

    }



    public static void takeSnapshots(int time){
        out = null;
        images.clear();
        timer = new Timer();
        long begin = System.currentTimeMillis();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(System.currentTimeMillis() > begin + time ||
                        !Visualizer.isRunning())
                    timer.cancel();
                Platform.runLater(()->
                {
                    images.add(Drawer.getInstance().takeSnap());
                });

            }
        },10, DELAY_TIME);

    }

}


