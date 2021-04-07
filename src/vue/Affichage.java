package vue;

import audio.Audio;
import controleur.Controleur;
import Tools.Tools;
import model.Data;
import model.Images;
import model.Route;
import model.User;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class Affichage extends JPanel {
    // ********************************** 1) Attributs **********************************

    // Largeur et longueur de la fenêtre

    private static Rectangle dimEcran = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

    public static final int LARGEUR = dimEcran.width-100;  //800;
    public static final int HAUTEUR = dimEcran.height-15;  //650; //Ou 800, mais c est trop grand pour l ecran de Mathilde

    public JFrame fenetre;

    public VueUser vueUser;
    User user;
    Route route;
    public VueBackground bmg;
    Controleur ctrl;
    public OutsideScreen outScreen;

    private PaintManager paintManager;

    private JPanel card;

    /**La partie est toujours en cours**/
    boolean partieEnCours;
    /**La partie est mise en pause**/
    boolean enPause;

    /**Decide si on affiche la hitBox**/
    boolean showHitbox = true;

    private ReentrantLock switchMutex = new ReentrantLock();

    public Affichage(JFrame fenetre, User user, Route route) {
        this.fenetre = fenetre;
        // Dimensions de la fenêtre

        this.setPreferredSize(new Dimension(LARGEUR, HAUTEUR));

        this.user = user;
        this.route = route;
        this.partieEnCours = false;

        this.vueUser = new VueUser(this);
        this.bmg = new VueBackground(this);
        this.outScreen = new OutsideScreen(this);
        this.paintManager = new PaintManager(this);
        this.paintManager.start();
        setFocusable(true);

        this.card = new JPanel(new CardLayout());

        this.card.add(this, "Game");
        this.card.add(this.outScreen, "OutScreen");
        fenetre.add(card);




    }




    // ********************************** 3) Méthodes **********************************


    /**
     * Lie le controleur a l affichage. Se fait des l initialisation du controleur
     * @param ctrl le controleur
     */
    public void setControleur(Controleur ctrl){
        this.ctrl = ctrl;

    }


    /**
     * Change l interface a l ecran et le controleur qui le controle
     * @param toInGame true si vers le jeu, false si vers le menu
     */
    public void switchInteface(boolean toInGame){
        try{
            this.switchMutex.lock();

            CardLayout cl = (CardLayout) this.card.getLayout();
            if(toInGame){
                cl.show(this.card, "Game");
                this.requestFocusInWindow();
                Audio.musicMenu.stop();
                Audio.musicPause.stop();
                Audio.musicInGame.play();
            } else {
                cl.show(this.card, "OutScreen");
                this.outScreen.requestFocusInWindow();
                Audio.musicPause.stop();
                Audio.musicInGame.stop();
                Audio.musicMenu.play();
            }

        } finally {
            this.switchMutex.unlock();
        }
    }

    /**
     * Met fin a la partie et aux mouvements.
     */
    public void endPartie(){
        if(this.partieEnCours){
            this.partieEnCours = false;
            this.update();
        }
    }

    /**
     * Commence la partie, sors de l ecran d accueil
     * <br/>Du plus, initialise les images dans User
     */
    public void startPartie(){
        this.partieEnCours = true;
        this.enPause = false;
    }

    /**
     * Commence une nouvelle partie : Defini une nouvelle Route, un nouveau User, et reinitialise tous les elements de decors
     * @param newUser Le nouveau user
     * @param newRoute La nouvelle route
     */
    public void newPartie(User newUser, Route newRoute){
        this.user = newUser;
        this.route = newRoute;
        this.bmg.init();
        this.vueUser.newPartie();
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
    public void resume(){
        if(! this.partieEnCours && this.enPause){
            this.partieEnCours = true;
            this.enPause = false;
            this.update();
        }
    }



    /**
     * Dessine l ecran de pause
     * @param g2
     */
    private void drawPauseScreen(Graphics2D g2){

        Font oldFont = g2.getFont();

        AffineTransform atPause = new AffineTransform();
        atPause.translate((Affichage.LARGEUR/2)-110, (Affichage.HAUTEUR/2)-300);
        g2.drawImage(Images.getPauseButton(), atPause, null);

        Font fontScore = new Font("Arial", Font.PLAIN, 18);
        int yScore = (Affichage.HAUTEUR/2);
        int xScore = (Affichage.LARGEUR/2)-20;;
        Tools.drawDoubleString("Score : "+ Data.getCurrentScore(), xScore, yScore, g2, fontScore, Color.BLACK, Color.WHITE);
        Tools.drawDoubleString("Meilleur score : "+ Data.getHighestScore(), xScore-40, yScore+50, g2, fontScore, Color.BLACK, Color.WHITE);
        Tools.drawDoubleString("Kilometrage : "+Data.getCurrentKilometrage(), xScore-40, yScore+100, g2, fontScore, Color.BLACK, Color.WHITE);


        String restart = "Reprendre la partie";
        int yRestart = (Affichage.HAUTEUR/2)+150;
        int xRestart = (Affichage.LARGEUR/2)-80;
        String stop = "Arreter la partie";
        int yStop = yRestart + 50;
        int xStop = xRestart +15;

        Font font = new Font("Arial", Font.BOLD, 20);
        Tools.drawDoubleString(restart, xRestart, yRestart, g2, font, Color.BLACK, Color.WHITE);
        Tools.drawDoubleString(stop, xStop, yStop, g2, font, Color.BLACK, Color.WHITE);



        int tailleOval = 30;
        g2.setColor(Color.RED);
        if(this.ctrl.getPauseChoice() == 1){
            AffineTransform at = new AffineTransform();
            at.translate(xRestart - tailleOval - 10, yRestart-20);
            g2.drawImage(Images.getCurseur(), at, null);
        } else if(this.ctrl.getPauseChoice() == 2){
            AffineTransform at = new AffineTransform();
            at.translate(xStop - tailleOval - 10, yStop-20);
            g2.drawImage(Images.getCurseur(), at, null);

        }

        g2.setFont(oldFont);
    }

    /**
     * Fonction pour dessiner
     * @param g
     */
    /*public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            super.paint(g);
            super.revalidate();
            super.repaint();
            this.bmg.drawFond(g2);

            this.bmg.drawBackground(g2);
            this.vueUser.drawConcurrent(g2);
            this.vueUser.drawCar(g2);
            this.vueUser.drawMessage(g2);
            this.bmg.drawData(g2);

        if(! this.partieEnCours){
            g2.setColor(new Color(193, 191, 177, 100));
            g2.fillRect(0, 0, Affichage.LARGEUR, Affichage.HAUTEUR);
        }
        if(! this.partieEnCours && this.enPause){
            this.drawPauseScreen(g2);
        }

    }*/

    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        super.paintComponent(g);
        //super.paint(g);
        super.revalidate();
        super.repaint();
        this.bmg.drawFond(g2);

        this.bmg.drawBackground(g2);
        this.vueUser.drawConcurrent(g2);
        this.vueUser.drawCar(g2);
        this.vueUser.drawMessage(g2);
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
    public synchronized void update() {
        //this.paintManager.repaint();
        if(this.partieEnCours){
            this.vueUser.update();
        }

    }


}
