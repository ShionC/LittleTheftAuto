package Tools;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

public class Tools {

    public Tools(){

    }

    public static enum Location {Center, Up, Down, Right, LowerRight, UpperRight, Left, LowerLeft, UpperLeft};


    /*-----------------------------------------RANDOM----------------------------------------------------*/

    /**
     * Cree un int random compris dans un certain intervalle [rangeMin, rangeMax]
     * @param rangeMin La borne minimale du random
     * @param rangeMax La borne maximale du random (doit etre positive !!)
     * @return la valeur du int
     */
    public static int rangedRandomInt(int rangeMin, int rangeMax) {
        int randomValue;
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
     * Cree un double random compris dans un certain intervalle [rangeMin, rangeMax]
     * @param rangeMin La borne minimale du random
     * @param rangeMax La borne maximale du random (doit etre positive !!)
     * @return la valeur du double
     */
    public static double rangedRandomDouble(double rangeMin, double rangeMax) {
        double randomValue;
        if(rangeMin<0 ||rangeMax<=0||(rangeMax-rangeMin<=0)){
            System.out.println("Argument de random doit etre positif !!");
            if(rangeMin<0){
                System.out.println(rangeMin+" est negatif");
            }
            return rangeMin;
        } else {
            Random r = new Random();
            randomValue = rangeMin + (r.nextDouble()*(rangeMax-rangeMin));

        }
        return randomValue;
    }

    /*-----------------------------------------CALCUL----------------------------------------------------*/

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
    public static float findX(int y, Point p1, Point p2){
        float pente = (p2.y - p1.y) / ((float)p2.x - (float)p1.x);
        float x = (y - p1.y) / pente + p1.x;
        if((x<p1.x&&x<p2.x)||(x>p1.x&&x>p2.x)){
            System.out.println("X hors segment !!");
        }
            
        //return Math.round(x);
        return x;
    }

    /**
     * Calcul de la distance entre 2 points
     * @param p1 le 1er point
     * @param p2 le 2e point
     * @return la distance
     */
    public static double distance(Point p1, Point p2){
        double dx = p1.x - p2.x;
        double dy = p1.y - p2.y;
        return Math.sqrt(dx*dx+dy*dy);
    }

    /**
     * Create a point corresponding to the difference between point a & b for each coordinate
     * <br/>Each point use the formula ax-bx
     * @param a first point
     * @param b second point
     * @return distance between 2 points for the X and Y axis
     */
    public static Point2D distance(Point2D a, Point2D b){
        return new Point2D.Double(a.getX()-b.getX(), a.getY()-b.getY());
    }


    /*-----------------------------------------LISTS----------------------------------------------------*/



    /**
     * Dans une liste de points, retrouve l index du premier point inferieur a p <i>sur l axe Y</i>
     * @param p le point a comparer
     * @param list Une liste de point dont l axe y est <b>decroissant</b>
     * @return -1 si p.y <i>plus grand</i> que le premier de la liste et -2 si p.y <i>plus petit</i> que le dernier
     */
    public static int findIdxFirstInfByY(Point p, ArrayList<Point> list){
        Point p1 = list.get(0); //p1>p2
        Point p2 = list.get(1);
        int i = 1;
        while (p.y<p2.y && i<list.size()-1){
            if(p.y>=p1.y){ //Sortie par le debut de la liste
                return -1;
            } else if(i+1 >= list.size()){  //Sortie par la fin de la liste
                return -2;
            } else {
                p1 = list.get(i);
                p2 = list.get(i+1);
            }

            i++;
        }
        return i;
    }



    /*-----------------------------------------TO STRING----------------------------------------------------*/


    /**
     * Renvoie le temps restant du timer sous forme <i><b>x</b>h <b>x</b>min <b>x</b>sec</i>
     * @param d la duree
     * @return le string correspondant
     */
    public static String toStringDuration(Duration d){
        int h = d.toHoursPart();
        int m = d.toMinutesPart();
        int sec = d.toSecondsPart();
        String str = "";
        if( h!=0 ) {
            str += String.valueOf(h) + "h ";
        }
        if( m!=0 ) {
            str += String.valueOf(m) + "min ";
        }
        if( sec!=0 ) {
            str += String.valueOf(sec) + "sec ";
        }
        return str;
    }

    /**
     * Convertit le int en string tout en simplifiant l affichange pour les grands nombres a l aide des abreviations k et M
     * @param val
     * @return
     */
    public static String toStringInt(int val){
        int rest;
        String str = Integer.toString(val);
        if(val >= 10000){
            rest = val/1000;
            str = rest + "k";
        }
        if(val >= 1000000){
            rest = val/1000000;
            str = rest + "M";
        }
        return str;
    }

    /*-----------------------------------------IMAGES----------------------------------------------------*/

    /**
     * Cree une copie d un BufferedImage modifiable sans interferer avec l image source
     * @param bi BufferedImage source
     * @return la copie
     */
    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    /**
     * Scale the Buffered image to a desired scale
     * <br/>The coordinates of the rectangle returned by getBounds2D(BufferedImage) are not
     * necessarily the same as the coordinates of the BufferedImage returned by this method.
     * @param before The original BufferedImage
     * @param scaleX the factor by which coordinates are scaled along the X axis direction
     * @param scaleY the factor by which coordinates are scaled along the Y axis direction
     * @return
     */
    public static BufferedImage scaleBI(BufferedImage before, double scaleX, double scaleY){
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scaleX, scaleY);
        AffineTransformOp scaleOp =
                new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(before, after);
        return after;
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    public static BufferedImage getResizedImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    /**
     * Transforme une ImageIcon en BufferedImage
     * @param img l ImageIcon
     * @return
     */
    public static BufferedImage imageIconToBuffImage(ImageIcon img){
        BufferedImage bi = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        //paint the Icon to the BufferedImage.
        img.paintIcon(null, g, 0,0);
        g.dispose();
        return bi;
    }

    /**
     * Create a BufferedImage from a path leading to the image
     * @param pathName the path
     * @return the BufferedImage found. Raise an exception if the image couldn't be found
     */
    public static BufferedImage getBIfromPath(String pathName) {
        BufferedImage bi = null;
        try{
            File c = new File(pathName);
            bi = ImageIO.read(c);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Image couldn't be read");
        } catch (Exception e){
            e.printStackTrace();
        }
        if(bi.getHeight()==-1 || bi.getWidth()==-1){
            throw new RuntimeException("Image couldn't be read");
        }

        return bi;
    }


    /*-----------------------------------------AREA----------------------------------------------------*/


    /**
     * Verifie si il y a une collision entre les 2 objets definis par une Area, cad si ils se chevauchent.
     * <br/>Une Area peut etre construite a partir d une Shape avec Area a = new Area(Shape s)
     * @param a1 le premier objet a verifier
     * @param a2 le 2e objet a verifier
     * @return true si les 2 area se chevauchent, false sinon
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


    /**
     * Rotate a shape while keeping the same center
     * @param s the shape to rotate
     * @param angle the angle of the rotation
     * @return the rotated Shape. Has the same center as the original
     */
    public static Shape rotate(Shape s, double angle){
        //Rotate
        AffineTransform ro = new AffineTransform();
        ro.rotate(angle);
        Shape tmp = ro.createTransformedShape(s);
        //Translate back
        Rectangle2D aBounds = s.getBounds2D();
        Rectangle2D tmpBounds = tmp.getBounds2D();
        //Supperpose les centers
        Point2D.Double centerA = new Point2D.Double(aBounds.getCenterX(), aBounds.getCenterY());
        Point2D.Double centerTmp = new Point2D.Double((tmpBounds.getCenterX()), tmpBounds.getCenterY());
        Point2D.Double distToNewCenter = new Point2D.Double(centerA.x-centerTmp.x, centerA.y-centerTmp.y);
        AffineTransform tx = new AffineTransform();
        tx.translate(distToNewCenter.x, distToNewCenter.y);
        Shape newShape = tx.createTransformedShape(tmp);

        return newShape;
    }


    /**
     * Rotate a shape on itself while fixing a point on the shape.
     * <br/>Rotating by a positive angle rotates points on the positive X axis toward the positive Y axis.
     * @param s the shape to rotate
     * @param angle the angle of rotation mesured in radians
     * @param location location to fixe. Use enum Location. Default is center
     * @return the rotated Shape. Has a certain point fixed to the original
     */
    public static Shape rotate(Shape s, double angle, Location location){
        //Rotate
        AffineTransform ro = new AffineTransform();
        ro.rotate(angle);
        Shape tmp = ro.createTransformedShape(s);
        //Translate back
        Rectangle2D aBounds = s.getBounds2D();
        Point2D.Double fixedA;
        //Supperpose les points
        if (location == Location.Up){
            fixedA = new Point2D.Double(aBounds.getX()+(aBounds.getWidth()/2), aBounds.getY());
        } else if (location == Location.Down){
            fixedA = new Point2D.Double(aBounds.getX()+(aBounds.getWidth()/2), aBounds.getY()+aBounds.getHeight());
        } else if (location == Location.Right){
            fixedA = new Point2D.Double(aBounds.getX()+aBounds.getWidth(), aBounds.getY()+(aBounds.getHeight()/2));
        } else if (location == Location.Left){
            fixedA = new Point2D.Double(aBounds.getX(), aBounds.getY()+(aBounds.getHeight()/2));
        } else if (location == Location.UpperRight){
            fixedA = new Point2D.Double(aBounds.getX()+aBounds.getWidth(), aBounds.getY());
        } else if (location == Location.LowerRight){
            fixedA = new Point2D.Double(aBounds.getX()+aBounds.getWidth(), aBounds.getY()+aBounds.getHeight());
        } else if (location == Location.UpperLeft){
            fixedA = new Point2D.Double(aBounds.getX(), aBounds.getY());
        } else if(location == Location.LowerLeft){
            fixedA = new Point2D.Double(aBounds.getX(), aBounds.getY()+aBounds.getHeight());
        } else { //Center
            fixedA = new Point2D.Double(aBounds.getCenterX(), aBounds.getCenterY());
        }

        //Fait le calcul par rapport a un point model qui fait une rotation.
        //getBounds ne permet pas d avoir les bonnes position a par pour center, car est deforme par rapport a original
        Point2D.Double fixedOriginTemp = (Point2D.Double) ro.transform(fixedA, null);

        Point2D.Double distToOrigin = (Point2D.Double) distance(fixedA, fixedOriginTemp);
        AffineTransform tx = new AffineTransform();
        tx.translate(distToOrigin.x, distToOrigin.y);

        Shape LastNewShape = tx.createTransformedShape(tmp);

        return LastNewShape;
    }

    /*-----------------------------------------GRAPHIC----------------------------------------------------*/


    /**
     * Dessine un string sur un graphics de 2 couleurs superposees mais legerement decalees
     * @param str le string a afficher
     * @param x la position x sur l interface graphique
     * @param y la position y sur l interface graphique
     * @param g2 l interface graphique
     * @param font Le style de string
     * @param c1 La couleur du dessus
     * @param c2 La couleur du dessous
     */
    public static void drawDoubleString(String str, int x, int y, Graphics2D g2, Font font, Color c1, Color c2){
        Font oldFont = g2.getFont();
        Color oldColor = g2.getColor();

        g2.setFont(font);
        g2.setColor(c2);
        g2.drawString(str, x+1, y+1);
        g2.setColor(c1);
        g2.drawString(str, x, y);

        g2.setColor(oldColor);
        g2.setFont(oldFont);
    }

    /**
     * Dessine une ligne en alternant entre 2 couleurs
     * <br/>Pour rappel la ligne se trouve au milieu de la height specifiee dans le Stroke
     * @param g2 graphical context
     * @param line La ligne a afficher
     * @param c1 La couleur la + a droite
     * @param c2 La 2e couleur
     * @param stroke A dashed stroke
     */
    public static void drawDashedLineWith2Colors(Graphics2D g2, Line2D.Double line, Color c1, Color c2, BasicStroke stroke){
        Font oldFont = g2.getFont();
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();

        float[] dashingPattern2 = stroke.getDashArray();
        //Reverse array
        float tmp = dashingPattern2[0];
        for(int i = 1; i<dashingPattern2.length; i++){
            dashingPattern2[i-1] = dashingPattern2[i];
        }
        dashingPattern2[dashingPattern2.length-1] = tmp;

        BasicStroke stroke2 = new BasicStroke(stroke.getLineWidth(),stroke.getEndCap(),stroke.getLineJoin(),
                stroke.getMiterLimit(),dashingPattern2,stroke.getDashPhase()+dashingPattern2[0]);
        //Draw
        g2.setColor(c1);
        g2.setStroke(stroke);
        g2.draw(line);

        g2.setColor(c2);
        g2.setStroke(stroke2);
        g2.draw(line);

        g2.setColor(oldColor);
        g2.setFont(oldFont);
        g2.setStroke(oldStroke);
    }
}
