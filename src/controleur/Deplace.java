package controleur;

import vue.Affichage;

import java.awt.*;
import model.User;
import model.Route;
import game.Tools;
import vue.Affichage;

public class Deplace extends Thread {

    /**Flag d arret du thread**/
    private boolean run = true;

    /**
     * La route se met a jour toutes les varTime milisecondes
     */
    private int varTime = 40;

    //Lien vers les autres classes

    private User user;
    private Route route;
    private Affichage aff;

    /**
     * Ce thread calcule la vitesse de la route,
     * et fait se deplacer tous les objets en fonction de la vitesse a laquelle ils sont relies.
     * @param user Est a l origine de la vitesse.
     * @param route Est deplace selon la vitesse de User
     * @param aff Permet de faire la modification
     */
    public Deplace(User user, Route route, Affichage aff){
        //Lie toutes les autres classes
        this.user = user;
        this.route = route;
        this.aff = aff;
    }

    /**
     * Arrete l execution du run. A utiliser pour arreter le thread
     */
    public void stopRun(){
        this.run = false;
    }


    @Override
    public void run() {
        while(run){

            //Calcul de la vitesse
            float modVit = 0;
            this.user.modVitesse(modVit);
            //int modPos = Math.round(this.user.getVitesse()*(double)varTime/1000);// div 1000 car ce sont des mili secondes
            //int modPos = Math.round((float)this.user.getVitesse()*(float)(varTime/1000));

            //Deplace les diff objets
            this.route.moveRoute(Math.round((float)this.user.getVitesse()));

            this.aff.update();
            try {
                Thread.sleep(this.varTime);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
