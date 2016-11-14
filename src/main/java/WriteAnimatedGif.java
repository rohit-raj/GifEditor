/**
 * Created by Rohit on 14/11/16.
 */
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import org.w3c.dom.Node;

public class WriteAnimatedGif {
    public static void configure(IIOMetadata meta, String delayTime, int imageIndex) {
        String metaFormat = meta.getNativeMetadataFormatName();

        if (!"javax_imageio_gif_image_1.0".equals(metaFormat)) {
            throw new IllegalArgumentException(
                    "Unfamiliar gif metadata format: " + metaFormat);
        }

        Node root = meta.getAsTree(metaFormat);

        //find the GraphicControlExtension node
        Node child = root.getFirstChild();
        while (child != null) {
            if ("GraphicControlExtension".equals(child.getNodeName())) {
                break;
            }
            child = child.getNextSibling();
        }

        IIOMetadataNode gce = (IIOMetadataNode) child;
        gce.setAttribute("userInputFlag", "FALSE");
        gce.setAttribute("delayTime", delayTime);

        //only the first node needs the ApplicationExtensions node
        if (imageIndex == 0) {
            IIOMetadataNode aes = new IIOMetadataNode("ApplicationExtensions");
            IIOMetadataNode ae = new IIOMetadataNode("ApplicationExtension");
            ae.setAttribute("applicationID", "NETSCAPE");
            ae.setAttribute("authenticationCode", "2.0");
            byte[] uo = new byte[]{
                0x1, 0x0, 0x0
            };
            ae.setUserObject(uo);
            aes.appendChild(ae);
            root.appendChild(aes);
        }

        try {
            meta.setFromTree(metaFormat, root);
        } catch (IIOInvalidTreeException e) {
            //shouldn't happen
            throw new Error(e);
        }
    }

    public static byte[] saveAnimate(ImageFrame[] frames) throws Exception {

        ImageWriter iw = ImageIO.getImageWritersByFormatName("gif").next();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        ImageOutputStream iOS = new MemoryCacheImageOutputStream(bos);
        //ImageOutputStream ios = ImageIO.createImageOutputStream(file);
        iw.setOutput(iOS);
        iw.prepareWriteSequence(null);

        for (int i = 0; i < frames.length; i++) {
            BufferedImage src = frames[i].getImage();
            Integer dTime = frames[i].getDelay();
            String delayTime = dTime.toString();
            ImageWriteParam iwp = iw.getDefaultWriteParam();

            IIOMetadata metadata = iw.getDefaultImageMetadata(new ImageTypeSpecifier(src), iwp);
            configure(metadata, delayTime, i);
            IIOImage ii = new IIOImage(src, null, metadata);

            iw.writeToSequence(ii, (ImageWriteParam) null);

        }

        byte [] image = null;
        iw.endWriteSequence();
        iOS.seek(0);
        while (true) {
            try {
                baos.write(iOS.readByte());
            } catch (EOFException e) {
                //End of Image Stream
                break;
            } catch (IOException e) {
                //Error processing the Image Stream
                break;
            }
        }
        image = baos.toByteArray();
        iOS.close();
        return image;
    }
}