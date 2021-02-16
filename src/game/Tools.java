package game;

import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.awt.geom.Area;
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
        int randomValue = 0;
        if(rangeMin<0 ||rangeMax<=0||(rangeMax-rangeMin<=0)){
            System.out.println("Argument de random doit etre positif !!");
            if(rangeMin<0){
                System.out.println(rangeMin+" est negatif");
            }
            return rangeMin;
        } else {
            Random r = new Random();
            randomValue = rangeMin + r.nextInt(rangeMax - rangeMin);

        }
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
     * Trouve le point y correspondant a la coord (x,y) sur le segment [p1,p2]
     * @param x la coord x du point a chercher
     * @param p1 Le premier point relatif du segment
     * @param p2 Le deuxieme point relatif du segment
     * @return la coord y du point cherche
     */
    public static int findY(int x, Point p1, Point p2){
        float pente = (p2.y - p1.y) / ((float)p2.x - (float)p1.x);
        //float y = p1.y - pente *(float)(p1.x - x);
        float y = pente * (float)(x - p1.x) + p1.y;
        if((y<p1.y&&y<p2.y)||(y>p1.y&&y>p2.y)){
            System.out.println("Y hors segment !!");
        }
        
        return Math.round(y);
    }

    /**
     * Trouve le point x correspondant a la coord (x,y) sur le segment [p1,p2]
     * @param y la coord y du point a chercher
     * @param p1 Le premier point relatif du segment
     * @param p2 Le deuxieme point relatif du segment
     * @return la coord x du point cherche
     */
    public static int findX(int y, Point p1, Point p2){
        float pente = (p2.y - p1.y) / ((float)p2.x - (float)p1.x);
        float x = (y - p1.y) / pente + p1.x;
        if((x<p1.x&&x<p2.x)||(x>p1.x&&x>p2.x)){
            System.out.println("X hors segment !!");
        }
            
        return Math.round(x);
    }

    /**
     * Calcul de la distance entre 2 points
     * @param p1
     * @param p2
     * @return
     */
    public static double distance(Point p1, Point p2){
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx*dx+dy*dy);
    }


    /**
     * Verifie si il y a une collision entre les 2 objets definis par une Area, cad si ils se chevauchent.
     * Une Area peut etre construite a partir d une Shape avec Area a = new Area(Shape s)
     * @param a1 le premier objet a verifier
     * @param a2 le 2e objet a verifier
     * @return
     */
    public static boolean collision(Area a1, Area a2){
        if( a1.getBounds().intersects(a2.getBounds()) ){//Verifie dabord les boxes pour eviter trop de calcul
            Area a = new Area(a1);
            a.intersect(new Area(a2));
            if(!a.isEmpty()){
                return true;
            }
        }
        return false;
    }
}
