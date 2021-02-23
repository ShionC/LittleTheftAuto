package vue;

import controleur.Controleur;
import game.Tools;
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

    /**La partie est toujours en cours**/
    boolean partieEnCours;
    /**La partie est mise en pause**/
    boolean enPause;

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
     * Met la partie en pause, arrete tous les threads necessaires et affiche l ecran de pause
     */
    public void pause(){
        if(this.partieEnCours && ! this.enPause){
            this.partieEnCours = false;
            this.enPause = true;
            this.update();
        }
    }

    /**
     * Reprends la partie pausee, enleve l ecran de pause
     */
    public void restart(){
        if(! this.partieEnCours && this.enPause){
            this.partieEnCours = true;
            this.enPause = false;
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
     * Dessine l ecran de pause
     * @param g2
     */
    private void drawPauseScreen(Graphics2D g2){
        Font oldFont = g2.getFont();
        String restart = "Reprendre la partie";
        int yRestart = (Affichage.HAUTEUR/2)-100;
        int xRestart = (Affichage.LARGEUR/2)-50;
        String stop = "Arreter la partie";
        int yStop = yRestart + 60;
        int xStop = (Affichage.LARGEUR/2)-50;

        Font font = new Font("Arial", Font.BOLD, 20);
        Tools.drawDoubleString(restart, xRestart, yRestart, g2, font, Color.BLACK, Color.WHITE);
        Tools.drawDoubleString(stop, xStop, yStop, g2, font, Color.BLACK, Color.WHITE);

        int tailleOval = 30;
        g2.setColor(Color.RED);
        if(this.ctrl.getPauseChoice() == 1){
            g2.fillOval(xRestart - tailleOval - 10, yRestart-20, tailleOval, tailleOval);
        } else if(this.ctrl.getPauseChoice() == 2){
            g2.fillOval(xStop - tailleOval - 10, yStop-20, tailleOval, tailleOval);
        }

        g2.setFont(oldFont);
    }

    /**
     * Fonction pour dessiner
     * @param g
     */
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

            super.paint(g);
            super.revalidate();
            super.repaint();
            this.bmg.drawBackground(g2);
            this.vueUser.drawCar(g2);
            this.bmg.drawData(g2);

        if(! this.partieEnCours){
            g2.setColor(new Color(193, 191, 177, 100));
            g2.fillRect(0, 0, Affichage.LARGEUR, Affichage.HAUTEUR);
        }
        if(! this.partieEnCours && this.enPause){
            this.drawPauseScreen(g2);
        }

    }

    /**
     * Met a jour l affichage
     */
    public void update() {
        repaint();
        if(this.partieEnCours){
            //Les updates
        }

        //System.out.println("\n     *Update*");
    }


}
