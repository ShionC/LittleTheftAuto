package vue;

import java.awt.*;
import java.awt.geom.Area;

public class VueUser {
    // ********************************** 1) Attributs **********************************
    // Taille du véhicule
    public static final int LARG_CAR = 50;
    public static int HAUT_CAR = 50;

    // Hauteur du véhicule sur la fenêtre
    public static int HAUTSCREEN_CAR = vue.Affichage.LARGEUR - LARG_CAR - 10;

    private Affichage aff;

    // ********************************** 2) Constructeur **********************************

    public VueUser(Affichage aff) {
        this.aff = aff;
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Forme du véhicule
     * @return
     */
    public Area getShapeCar() {
        return new Area(new Rectangle(aff.user.getPosX(), aff.user.getPosY(), LARG_CAR, HAUT_CAR));
    }

    // Affichage du véhicule dans une sous-méthode
    public void drawCar(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        //g2.drawRect(aff.LARGEUR/2, HAUTSCREEN_CAR, HAUT_CAR, LARG_CAR);
        g2.fill(getShapeCar());

    }

    /**
     * Mise à jour la forme de user
     */
    public void updateCar() {

    }

}
