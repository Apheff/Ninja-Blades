package NinjaBlades.utils;


import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {

    protected static BufferedImage spriteSheet;
    public BufferedImage[] blades;

    public ImageLoader(){
        spriteSheet = null;
    }

    public static BufferedImage loadImage(String filename){
        String resourcePath = "img/" + filename;
        System.out.println("Loading image from path: " + resourcePath);
        try {      
            BufferedImage image = ImageIO.read(ImageLoader.class.getClassLoader().getResourceAsStream("img/" + filename));
            if(image == null){
                throw new NullPointerException("image not found:" + resourcePath);
            }
            return image;
        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading image: " + filename);
            return null;
        }
    }

    /* (General method to load frames from sprite sheet)
     *
     * makes an array of BufferedImages from a spritesheet which is found in the /img directory,
     * the loading image Methods
     */
    public static BufferedImage[] loadFrames(BufferedImage image, int startX, int startY, int frameCount, int frameWidth, int frameHeight) {
        BufferedImage[] frames = new BufferedImage[frameCount];
        for (int i = 0; i < frameCount; i++) {
            frames[i] = image.getSubimage(startX + (i * frameWidth), startY, frameWidth, frameHeight);
        }
        return frames;
    }
}
