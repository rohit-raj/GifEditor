import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Rohit on 14/11/16.
 */
public class GifEditor {
    public static void main(String[] args) {
        byte [] image = null;
        InputStream inputStream = new ByteArrayInputStream(image);
        ImageFrame[] imageFrames = null;
        try {
            imageFrames = GifCom.readGif(inputStream);
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        for (int i = 0; i < imageFrames.length; i++) {
            //code to put banner on the gif frames of the image
            BufferedImage images = new BufferedImage(100,100,1);
            imageFrames[i].setImage(images);
        }
        try {
            image = WriteAnimatedGif.saveAnimate(imageFrames);
        } catch (Exception e) {
            //Error processing the Image Stream
            System.out.println("Error in Gif Image frames patching");
            e.printStackTrace();
            //return ok(BlockDrawing.getEmptyImage()).as("image/png");
        }
    }
}
