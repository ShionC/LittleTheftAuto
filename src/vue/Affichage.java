package vue;

import controleur.Controleur;
import model.Route;
import model.User;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    boolean partieEnCours;

    public Affichage(User user, Route route) {
        // Dimensions de la fenêtre
        this.setPreferredSize(new Dimension(LARGEUR, HAUTEUR));

        this.user = user;
        this.route = route;
        this.partieEnCours = true;

        this.vueUser = new VueUser(this);
        this.bmg = new VueBackground(this);
        setFocusable(true);
    }




    // ********************************** 3) Méthodes **********************************

    /**
     * Met fin a la partie et aux mouvements.
     */
    public void endPartie(){
        if(this.partieEnCours){
            System.out.println("End game aff");
            this.partieEnCours = false;
            this.update();
        }
    }

    /**
     * Lie le controleur a l affichage. Se fait des l initialisation du controleur
     * @param ctrl le controleur
     */
    public void setControleur(Controleur ctrl){
        this.ctrl = ctrl;
    }

    /**
     * Fonction pour dessiner
     * @param g
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(! this.partieEnCours){
            g2.setColor(new Color(193, 191, 177, 100));
            g2.fillRect(0, 0, Affichage.LARGEUR, Affichage.HAUTEUR);
        } else {
            super.paint(g);
            super.revalidate();
            super.repaint();
            this.bmg.drawBackground(g2);
            this.vueUser.drawCar(g2);
            this.bmg.drawData(g2);
        }

    }

    /**
     * Met a jour l affichage
     */
    public void update() {
        if(this.partieEnCours){
            repaint();
        }

        //System.out.println("\n     *Update*");
    }


}
