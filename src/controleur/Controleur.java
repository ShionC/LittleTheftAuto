package controleur;

import Tools.Tools;
import Tools.MyTimer;
import model.Data;
import model.Route;
import model.User;
import vue.Affichage;
import vue.VueUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Controleur implements KeyListener {

    // ********************************** 1) Attributs **********************************

    Affichage aff;
    User user;
    Route route;

    public Accueil accueil;

    TimeManager timeManager;
    Deplace deplace;

    KeyContinue keyCont;

    /**
     * La partie est en cours
     */
    boolean partieEnCours;

    /**La partie est mise en pause**/
    boolean enPause;

    private int pauseChoice = 1;

    /**True si on a commence une partie. False si on est sur un ecran d acceuil**/
    public boolean inPartie;


    // ********************************** 2) Constructeur **********************************

    /**
     * Le controleur principal
     * @param aff l affichage principal
     * @param user la voiture controlee par le joueur pour la 1ere partie
     * @param route Le circuit automobile pour la 1ere partie
     */
    public Controleur(Affichage aff, User user, Route route){
        this.user = user;
        this.route = route;
        this.aff = aff;
        this.aff.setControleur(this);
        this.aff.addKeyListener(this);

        this.partieEnCours = false;
        this.enPause = false;

        this.accueil = new Accueil(this);
        this.inPartie = false;

        this.keyCont = new KeyContinue(this);
        this.keyCont.start();

        this.deplace = new Deplace(this);
        deplace.start();

        this.timeManager = new TimeManager(this);
        timeManager.start();

    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Renvoie le timer du point de controle en cours
     * @return le timer
     */
    public MyTimer getTimerPtCtrl(){
        return this.timeManager.getTimerPtCtrl();
    }


    /**
     *
     */
    /**
     * Met fin a la partie et aux mouvements, et deplace vers l ecran de fin de jeu
     * <br/>Transfere toutes les donnees a Data
     * @param typeGameOver 0 : Partie stopee, 1 : Vitesse == 0, 2 : PtCtrl non atteint
     */
    public void endPartie(int typeGameOver){

            //System.out.println("End game ctrl");
            this.accueil.typeGameOver = typeGameOver;
            this.partieEnCours = false;
            this.enPause = false; //La fin de partie n est pas une pause
            this.timeManager.getTimerPtCtrl().pause();
            this.aff.endPartie();
            this.user.stopRun();
            this.keyCont.setDir(KeyContinue.Direction.NOTHING);
            //Arreter les concurrents
            Data.push();
            this.accueil.goToAccueil();
            this.aff.switchInteface(false);

    }

    /**
     * Commence la partie, sors de l ecran d accueil.
     * Commence le thread de user
     */
    public void startPartie(){
        this.aff.switchInteface(true);
        this.partieEnCours = true;
        this.enPause = false;
        this.timeManager.startPartie();
        this.user.start();
        this.aff.startPartie();
        this.pauseChoice = 1;
        Data.newPartie();
    }

    /**
     * Commence une nouvelle partie :
     * Defini une nouvelle Route, un nouveau User, et reinitialise tous les elements de decors
     */
    public void newPartie(){
        this.user = new User();
        this.route = new Route();
        this.aff.newPartie(this.user, this.route);
        this.deplace.newPartie();
        this.timeManager.newPartie();
    }



    /**
     * Met la partie en pause, pause tous les controleurs et indique a l affichage la pause
     */
    public void pause(){
        if(this.partieEnCours && ! this.enPause){
            this.partieEnCours = false;
            this.enPause = true;
            this.timeManager.getTimerPtCtrl().pause();
            this.aff.pause();
            Data.pausePartie();
        }
    }

    /**
     * Reprends la partie pausee et l indique a l affichage
     */
    public void resume(){
        if(! this.partieEnCours && this.enPause){
            this.partieEnCours = true;
            this.enPause = false;
            this.pauseChoice = 1;
            this.timeManager.getTimerPtCtrl().resume();
            this.aff.restart();
            Data.resumePartie();
        }
    }

    /**
     * Fait renaitre user sur le milieu de la route avec une certaine vitesse.
     * <br/>Enleve 1 au nombre de vie;
     */
    private void rebirth(){
        //this.vieRestante--;
        ArrayList<Point> listRoute = this.route.getRoute();
        int i = Tools.findIdxFirstInfByY(new Point(this.user.getPosX(), this.user.getPosY()), listRoute);
        this.user.rebirth(Tools.findX(this.user.getPosY(), listRoute.get(i-1), listRoute.get(i)));
    }

    /**
     * Selectionne le choix suivant dans le menu pause
     * @param after true si le choix est le suivant, false si il s agit du precedent
     */
    private void nextPauseChoice(boolean after){
        int maxPauseChoice = 2;
        if(after){
            if(this.pauseChoice == maxPauseChoice){
                this.pauseChoice = 1;
            } else {
                this.pauseChoice++;
            }
        } else {
            if(this.pauseChoice == 1){
                this.pauseChoice = maxPauseChoice;
            } else {
                this.pauseChoice--;
            }
        }

    }

    /**
     * Renvoie le choix selectionne sur le menu de pause.
     * <br/>Le choix 0 signifie que rien n'est selectionne
     * <br/><u>Choix 1 :</u> Reprendre la partie
     * <br/><u>Choix 2 :</u> Arreter la partie
     * @return
     */
    public int getPauseChoice(){
        return this.pauseChoice;
    }

    /**
     * Effectue l action correspondant au choix actuel
     * <br/>Le choix 0 signifie que rien n'est selectionne
     * <br/><u>Choix 1 :</u> Reprendre la partie
     * <br/><u>Choix 2 :</u> Arreter la partie
     */
    private void actionPauseChoice(){
        if(this.pauseChoice == 1){
            this.resume();
        } else if(this.pauseChoice == 2){
            this.endPartie(0);
        }
    }




    /**
     * Met à jour le modèle et la vue
     */
    public void update() {

    }

    /**
     * Deplace user ainsi que les nuages dans VueBackground
     * @param right
     */
    void move(boolean right) {
        if(this.partieEnCours){
            if (right) {
                if (user.getPosX() + VueUser.LARG_CAR < Affichage.LARGEUR) {
                    user.moveRight();
                    //aff.bmg.moveDecors(! right);
                }
            } else {
                if (user.getPosX() > 0) {
                    user.moveLeft();
                    //aff.bmg.moveDecors(! right);
                }
                aff.update();
            }
        }
    }

        /**
         * Defini les commandes au clavier pour faire bouger User avec la methode de KeyBinding
         */
        public void setCmds () {

            //Action a droite
            this.aff.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");
            //KeyEvent.KEY_LOCATION_RIGHT
            Action action = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //System.out.print("Jump !! \n");
                    move(true);
                    //System.out.println("\n     *Action a droite*");
                }
            };
            this.aff.getActionMap().put("Right", action);

            //Action a gauche
            this.aff.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
            //KeyEvent.KEY_LOCATION_RIGHT
            Action action2 = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //System.out.print("Jump !! \n");
                    move(false);
                    //System.out.println("\n     *Action a gauche*");
                }
            };
            this.aff.getActionMap().put("Left", action2);
        }

    @Override
    public void keyTyped(KeyEvent e) {
            System.out.println("Key typed Ctrl");
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.out.println("Mettre en pause");
            if(! this.enPause){
                this.pause();
            } else {
                this.resume();
            }
        }

        if(this.enPause){
            if(e.getKeyCode() == KeyEvent.VK_UP){
                this.nextPauseChoice(false);
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN){
                this.nextPauseChoice(true);
            }
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                this.actionPauseChoice();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
            //Deplacement
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            this.keyCont.setDir(KeyContinue.Direction.LEFT);
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            this.keyCont.setDir(KeyContinue.Direction.RIGHT);
        }

        if(e.getKeyCode() == KeyEvent.VK_UP){
            //System.out.println("UP");
        }

        //Pause
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.out.println("Mettre en pause");
            if(! this.enPause){
                this.pause();
            } else {
                this.resume();
            }
        }

        if(this.enPause){
            if(e.getKeyCode() == KeyEvent.VK_UP){
                this.nextPauseChoice(false);
            }
            if(e.getKeyCode() == KeyEvent.VK_DOWN){
                this.nextPauseChoice(true);
            }
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
                this.actionPauseChoice();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //La verification supplementaire permet d eviter que lacher une touche annule une autre qui etait deja enfoncee
        //Evite donc un nouveau lag
        if(e.getKeyCode() == KeyEvent.VK_LEFT && this.keyCont.getDir() != KeyContinue.Direction.RIGHT){
            this.keyCont.setDir(KeyContinue.Direction.NOTHING);
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT && this.keyCont.getDir() != KeyContinue.Direction.LEFT){
            this.keyCont.setDir(KeyContinue.Direction.NOTHING);
        }

    }
}
