import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageGenerator {

    public static void main(String[] args) {
        for(int i=0;i<1;i++){
            generateIME();
        }
    }

    public static void generateIME(){
        /*String text0 = String.valueOf(Math.round(Math.random()*1000));
        String text1 = String.valueOf(Math.round(Math.random()*1000));
        String text = text0+text1;
        for(int i=text.length();i<=6;i++){
            text = text+i;
        }
        */
        Random rand = new Random();
        int num = rand.nextInt(900000) + 100000;
        int num1 = rand.nextInt(900000) + 100000;
        int num2 = rand.nextInt(900) + 100;

        // System.out.println(num);
        String text = String.valueOf(num)+String.valueOf(num1)+String.valueOf(num2);
        String text0="11";
        //double number = Math.abs(Math.random() * 100000);
        /*
           Because font metrics is based on a graphics context, we need to create
           a small, temporary image so we can ascertain the width and height
           of the final image
         */
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 48);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();
        //System.out.println(width+"==="+height);
        img = new BufferedImage(250, 50, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        /*g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);*/
        g2d.setFont(new Font("Jokerman", Font.PLAIN, 0));
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setBackground(new Color(606060));
        System.out.println(fm.getAscent());
        g2d.drawString(text, 40, fm.getAscent());
        g2d.dispose();

        BufferedImage image = new BufferedImage(200, 40, BufferedImage.TYPE_BYTE_INDEXED);

        Graphics2D graphics = image.createGraphics();

        // Set back ground of the generated image to white
        graphics.setColor(Color.DARK_GRAY);
        //graphics.setColor(new Color(606060));
        graphics.fillRect(0, 0, 200, 40);

        // set gradient font of text to be converted to image
        GradientPaint gradientPaint = new GradientPaint(10, 5, Color.white, 20, 10, Color.white, true);
        graphics.setPaint(gradientPaint);
        Font font1 = new Font("Arial", Font.BOLD, 14);
                graphics.setFont(font1);

        // write 'Hello World!' string in the image
        graphics.drawString(text, 10, 20);

        // release resources used by graphics context
        graphics.dispose();
        try {
            ImageIO.write(image, "png", new File("C:\\Users\\lambapi\\Documents\\MyJabberFiles\\bibin.thiruvoth@verizon.com\\peronal\\AIModel\\IME\\"+text+".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
