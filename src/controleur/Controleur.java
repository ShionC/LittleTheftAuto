package controleur;

import model.Route;
import model.User;
import vue.Affichage;
import vue.VueUser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Controleur {

    // ********************************** 1) Attributs **********************************

    Affichage aff;
    User user;
    Route route;

    // ********************************** 2) Constructeur **********************************

    public Controleur(User user, Route route){
        this.user = user;
        this.route = route;
    }

    // ********************************** 3) Méthodes **********************************

    public void setAffichage(Affichage aff){
        this.aff = aff;
    }

    /**
     * Met à jour le modèle et la vue
     */
    public void update() {

    }

    /**
     * Deplace user
     * @param right
     */
    private void move(boolean right){
        if(right){
            if(user.getPosX()+ VueUser.LARG_CAR<Affichage.LARGEUR){
              user.moveRight();
            }
        } else {
            if(user.getPosX()>0){
                user.moveLeft();
            }
        }
        aff.update();
    }

    /**
     * Defini les commandes au clavier pour faire bouger User
     */
    public void setCmds(){

        //Action a droite
        this.aff.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Right");
        //KeyEvent.KEY_LOCATION_RIGHT
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //System.out.print("Jump !! \n");
                move(true);
                //System.out.println("\n     *Action a droite*");
            }
        };
        this.aff.getActionMap().put("Right", action);

        //Action a gauche
        this.aff.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Left");
        //KeyEvent.KEY_LOCATION_RIGHT
        Action action2 = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //System.out.print("Jump !! \n");
                move(false);
                //System.out.println("\n     *Action a gauche*");
            }
        };
        this.aff.getActionMap().put("Left", action2);
    }
}
