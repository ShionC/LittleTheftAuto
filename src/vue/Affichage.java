package vue;

import controleur.Controleur;
import model.Route;
import model.User;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class Affichage extends JPanel {
    // ********************************** 1) Attributs **********************************

    // Largeur et longueur de la fenêtre
    public static final int LARGEUR = 600;
    public static final int HAUTEUR = 650; //Ou 800, mais c est trop grand pour l ecran de Mathilde

    public VueUser vueUser;
    User user;
    Route route;
    public VueBackground bmg;
    Controleur ctrl;

    public Affichage(Controleur controleur, User user, Route route) {
        // Dimensions de la fenêtre
        this.setPreferredSize(new Dimension(LARGEUR, HAUTEUR));
        this.ctrl = controleur;
        this.user = user;
        this.route = route;

        this.vueUser = new VueUser(this);
        this.bmg = new VueBackground(this);
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Fonction pour dessiner
     * @param g
     */
    public void paint(Graphics g) {
        super.paint(g);
        super.revalidate();
        super.repaint();
        Graphics2D g2 = (Graphics2D) g;
        this.bmg.drawBackground(g2);
        this.vueUser.drawCar(g2);
        this.bmg.drawData(g2);

        //afficheCar(g);
    }

    /**
     * Met a jour l affichage
     */
    public void update() {
        repaint();
        //System.out.println("\n     *Update*");
    }

}
