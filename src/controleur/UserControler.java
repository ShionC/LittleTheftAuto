package controleur;

import vue.Affichage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class UserControler implements KeyListener {

    private Controleur ctrl;

    /**
     * Controleur qui s occupe des interactions entre le joueur et User
     * @param ctrl
     */
    public UserControler(Controleur ctrl){
        this.ctrl = ctrl;
    }


    /**
     * Deplace user ainsi que les nuages dans VueBackground
     * @param right deplace vers la droite ?
     */
    void move(boolean right) {
        if(this.ctrl.partieEnCours){
            if (right) {
                if (this.ctrl.user.getPosX() + this.ctrl.user.getLARGEUR() < Affichage.LARGEUR) {
                    this.ctrl.user.moveRight();
                }
            } else {
                if (this.ctrl.user.getPosX() > 0) {
                    this.ctrl.user.moveLeft();
                }
                this.ctrl.aff.update();
            }
        }
    }


    /**
     * Defini les commandes au clavier pour faire bouger User avec la methode de KeyBinding
     */
    public void setCmds () {

        //Action a droite
        this.ctrl.aff.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");
        //KeyEvent.KEY_LOCATION_RIGHT
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.print("Jump !! \n");
                move(true);
                //System.out.println("\n     *Action a droite*");
            }
        };
        this.ctrl.aff.getActionMap().put("Right", action);

        //Action a gauche
        this.ctrl.aff.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
        //KeyEvent.KEY_LOCATION_RIGHT
        Action action2 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.print("Jump !! \n");
                move(false);
                //System.out.println("\n     *Action a gauche*");
            }
        };
        this.ctrl.aff.getActionMap().put("Left", action2);
    }




    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Deplacement
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            this.ctrl.keyCont.setDir(KeyContinue.Direction.LEFT);
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            this.ctrl.keyCont.setDir(KeyContinue.Direction.RIGHT);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //La verification supplementaire permet d eviter que lacher une touche annule une autre qui etait deja enfoncee
        //Evite donc un nouveau lag
        if(e.getKeyCode() == KeyEvent.VK_LEFT && this.ctrl.keyCont.getDir() != KeyContinue.Direction.RIGHT){
            this.ctrl.keyCont.setDir(KeyContinue.Direction.NOTHING);
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT && this.ctrl.keyCont.getDir() != KeyContinue.Direction.LEFT){
            this.ctrl.keyCont.setDir(KeyContinue.Direction.NOTHING);
        }
    }
}
