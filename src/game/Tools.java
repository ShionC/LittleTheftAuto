package game;

import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;

public class Tools {

    public Tools(){

    }


    /**
     * Cree un int random compris dans un certain intervalle [rangeMin, rangeMax]
     * @param rangeMin
     * @param rangeMax La borne maximale du random (doit etre positive !!)
     * @return la valeur du int
     */
    public static int rangedRandomInt(int rangeMin, int rangeMax) {
        Random r = new Random();
        int randomValue = rangeMin + r.nextInt(rangeMax - rangeMin);
        return randomValue;
    }

    /**
     * Cree une copie d un BufferedImage modifiable sans interferer avec l image source
     * @param bi BufferedImage source
     * @return
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage scaleBI(BufferedImage before, double scaleX, double scaleY){
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        //at.scale(2.0, 2.0);
        at.scale(scaleX, scaleY);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(before, after);
        return after;
    }

    /**
     * Verifie si il y a une collision entre le segment [p1,p2] et l'ovale
     * @param p1 Le premier point relatif du segment
     * @param p2 Le deuxieme point relatif du segment
     * @return
     */
    public static int findY(int x, Point p1, Point p2){
        float pente = (p2.y - p1.y) / ((float)p2.x - (float)p1.x);
        float y = p1.y - pente *(float)(p1.x - x);
        return (int) y;
    }

}
