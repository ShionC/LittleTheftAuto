package controleur;

import Tools.Tools;
import Tools.MyTimer;
import model.Data;
import model.Route;
import model.User;
import vue.Affichage;

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

    UserControler uCtrl;
    KeyContinue keyCont;

    /**
     * La partie est en cours, ni en pause, ni arretee
     */
    boolean partieEnCours;

    /**La partie est mise en pause**/
    boolean enPause;

    private int pauseChoice = 1;

    /**True si on a commence une partie. False si on est sur un ecran d acceuil**/
    public boolean inPartie;


    // ********************************** 2) Constructeur **********************************

    /**
     * Le controleur principal. Gere la pause, le debut et la fin de partie,
     * ainsi que la creation d une nouvelle partie
     * <br/> Cree tous les differents controleurs a son instanciation
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

        this.uCtrl = new UserControler(this);
        this.aff.addKeyListener(this.uCtrl);

        this.keyCont = new KeyContinue(this.uCtrl);
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
        this.aff.startPartie();
        this.partieEnCours = true;
        this.enPause = false;
        this.timeManager.startPartie();
        this.user.startUser();
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
            this.user.pause();
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
            this.aff.resume();
            this.user.resumeUser();
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
     * <ul>
     *     <li><u>Choix 0 :</u> signifie que rien n'est selectionne</li>
     *     <li><u>Choix 1 :</u> Reprendre la partie</li>
     *     <li><u>Choix 2 :</u> Arreter la partie</li>
     * </ul>
     *
     * @return
     */
    public int getPauseChoice(){
        return this.pauseChoice;
    }

    /**
     * Effectue l action correspondant au choix actuel
     * <ul>
     *     <li><u>Choix 0 :</u> signifie que rien n'est selectionne</li>
     *     <li><u>Choix 1 :</u> Reprendre la partie</li>
     *     <li><u>Choix 2 :</u> Arreter la partie</li>
     * </ul>
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

    @Override
    public void keyTyped(KeyEvent e) {
            //System.out.println("Key typed Ctrl");
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

        //Pause
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            //System.out.println("Mettre en pause");
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

    }
}
