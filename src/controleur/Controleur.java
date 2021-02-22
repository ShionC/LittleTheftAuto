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
        /*
        if(this.partieEnCours){
            System.out.println("End game ctrl");
            this.partieEnCours = false;
            this.aff.endPartie();
            Data.push();
        }

         */
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

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            this.keyCont.setDir(KeyContinue.Direction.LEFT);
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            this.keyCont.setDir(KeyContinue.Direction.RIGHT);
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
