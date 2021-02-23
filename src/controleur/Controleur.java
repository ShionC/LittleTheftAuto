package controleur;

import model.Data;
import model.Route;
import model.User;
import vue.Affichage;
import vue.VueUser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controleur implements KeyListener {

    // ********************************** 1) Attributs **********************************

    Affichage aff;
    User user;
    Route route;

    TimeManager timeManager;
    Deplace deplace;

    KeyContinue keyCont;

    /**
     * La partie est en cours
     */
    boolean partieEnCours;

    /**La partie est mise en pause**/
    boolean enPause;

    private int pauseChoice = 0;


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

        this.partieEnCours = true;
        this.enPause = false;

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
     * Met fin a la partie et aux mouvements.
     */
    public void endPartie(){

            System.out.println("End game ctrl");
            this.partieEnCours = false;
            this.enPause = false; //La fin de partie n est pas une pause
            this.timeManager.getTimerPtCtrl().pause();
            this.aff.endPartie();
            Data.push();

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
        }
    }

    /**
     * Reprends la partie pausee et l indique a l affichage
     */
    public void restart(){
        if(! this.partieEnCours && this.enPause){
            this.partieEnCours = true;
            this.enPause = false;
            this.timeManager.getTimerPtCtrl().restart();
            this.aff.restart();
        }
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
            this.restart();
        } else if(this.pauseChoice == 2){
            this.endPartie();
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
            System.out.println("Key typed");
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.out.println("Mettre en pause");
            if(! this.enPause){
                this.pause();
            } else {
                this.restart();
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
            System.out.println("UP");
        }

        //Pause
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.out.println("Mettre en pause");
            if(! this.enPause){
                this.pause();
            } else {
                this.restart();
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
