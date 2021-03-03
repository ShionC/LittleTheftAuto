package vue;

import Tools.Tools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class VueUser {
    // ********************************** 1) Attributs **********************************
    // Taille du véhicule
    public static final int LARG_CAR = 60;
    public static int HAUT_CAR = 70;

    // Hauteur du véhicule sur la fenêtre
    public static int HAUTSCREEN_CAR = vue.Affichage.LARGEUR - LARG_CAR - 20;

    private final Affichage aff;


    //List Etat et Images pour User
    private HashMap<Integer, BufferedImage> listStatesUser;

    // ********************************** 2) Constructeur **********************************

    public VueUser(Affichage aff) {
        this.aff = aff;

        // Dessins du véhicule
        BufferedImage userStraight = Tools.getBIfromPath("src/Sprites/user.png");
        BufferedImage userLeft = Tools.getBIfromPath("src/Sprites/userleft.png");
        BufferedImage userRight = Tools.getBIfromPath("src/Sprites/userright.png");
        userStraight = Tools.scaleBI(userStraight, 0.5, 0.5);
        userLeft = Tools.scaleBI(userLeft, 0.5, 0.5);
        userRight = Tools.scaleBI(userRight, 0.5, 0.5);

        this.listStatesUser = new HashMap<>();
        this.listStatesUser.put(0, userStraight);
        this.listStatesUser.put(-1, userLeft);
        this.listStatesUser.put(1, userRight);

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
        g2.fill(getShapeCar());

        BufferedImage img = Tools.deepCopy(this.aff.user.getEtat().getCurrentImage());
        AffineTransform at = new AffineTransform();
        at.translate(aff.user.getPosX(), aff.user.getPosY());
        g2.drawImage(img, at, null);

    }

    public void initUser(){
        this.aff.user.getEtat().setImages(this.listStatesUser);
    }

    /**
     * Mise à jour la forme de user
     */
    public void updateCar() {

    }

}
