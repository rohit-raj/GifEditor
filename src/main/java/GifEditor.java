import sun.nio.ch.Net;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Rohit on 14/11/16.
 */
public class GifEditor {
    public static void main(String[] args) {
        byte [] image = null;
        String contextUrl = "";
        String ext = "";
        if(ext.equalsIgnoreCase("gif")) {
            image = Net.getUrlContent(contextUrl);
            InputStream inputStream = new ByteArrayInputStream(image);
            ImageFrame[] imageFrames = GifCom.readGif(inputStream);
            for (int i = 0; i < imageFrames.length; i++) {
                //code to put banner on the gif frames of the image
                BufferedImage images = Commons.putBannerOnAdKD(imageFrames[i].getImage());
                BufferedImage images = Commons.putBannerOnAdKD(imageFrames[i].getImage());
                imageFrames[i].setImage(images);
            }
            try {
                image = saveAnimate(imageFrames);
            } catch (Exception e) {
                //Error processing the Image Stream
                System.out.println("Error in Gif Image frames patching");
                e.printStackTrace();
                //return ok(BlockDrawing.getEmptyImage()).as("image/png");
            }
    }
}
