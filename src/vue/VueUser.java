package vue;

import game.Tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VueUser {
    // ********************************** 1) Attributs **********************************
    // Taille du véhicule
    public static final int LARG_CAR = 60;
    public static int HAUT_CAR = 70;

    // Hauteur du véhicule sur la fenêtre
    public static int HAUTSCREEN_CAR = vue.Affichage.LARGEUR - LARG_CAR - 20;

    private final Affichage aff;

    //Images véhicules :
    BufferedImage userStraight;
    BufferedImage userLeft;
    BufferedImage userRight;

    // ********************************** 2) Constructeur **********************************

    public VueUser(Affichage aff) {
        this.aff = aff;
        try {
            // Dessins du véhicule
            File us = new File("src/Sprites/user.png");
            File ul = new File("src/Sprites/userleft.png");
            File ur = new File("src/Sprites/userright.png");
            userStraight = ImageIO.read(us);
            userLeft = ImageIO.read(ul);
            userRight = ImageIO.read(ur);
            userStraight = Tools.scaleBI(userStraight, 0.5, 0.5);
            userLeft = Tools.scaleBI(userStraight, 0.5, 0.5);
            userRight = Tools.scaleBI(userStraight, 0.5, 0.5);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("l'image n'a pas pu etre lue");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Forme du véhicule
     * @return area
     */
    public Area getShapeCar() {
        return new Area(new Rectangle(aff.user.getPosX(), aff.user.getPosY(), LARG_CAR, HAUT_CAR));
    }

    // Affichage du véhicule dans une sous-méthode
    public void drawCar(Graphics2D g2) {
        g2.setColor(new Color(188, 32, 1));
        //g2.drawRect(aff.LARGEUR/2, HAUTSCREEN_CAR, HAUT_CAR, LARG_CAR);
        g2.fill(getShapeCar());

    }

    /**
     * Mise à jour la forme de user
     */
    public void updateCar() {

    }

}
