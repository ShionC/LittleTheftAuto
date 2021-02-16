package controleur;

import vue.Affichage;

import java.awt.*;
import java.util.ArrayList;

import model.User;
import model.Route;
import game.Tools;
import vue.Affichage;

import javax.tools.Tool;

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
            double modVit = 0;
            boolean onRoad;
            if (Tools.collision(this.aff.vueUser.getShapeCar(), this.aff.bmg.getShapeRoute())) {
                this.user.isOnRoad = true;
                onRoad = true;
                if(this.user.getVitesse()<this.user.getVitesseMax()){
                    //Touver le bon point sur la route
                    ArrayList<Point> listRoute = this.route.getRoute();
                    Point p1 = listRoute.get(0);
                    Point p2 = listRoute.get(1);
                    int i = 1;
                    while (this.user.getPosY()<p2.y && i<listRoute.size()-1){
                        p1 = listRoute.get(i);
                        if(i+1 >= listRoute.size()){
                            System.out.println("Sortie de array");
                        }
                        p2 = listRoute.get(i+1);
                        i++;
                    }
                    i--;//Car on a fait +1 apres avoir change p1 et p2
                    ArrayList<Integer> listRange = this.aff.bmg.getRangeRoute();
                    if(listRange.size() == 0){
                        System.out.println("ListRange size == 0 !!!");
                    }
                    int xMax = Tools.findX(this.user.getPosY(), p2, p1); //Le centre de la route au niveau de User
                    int xMin = Tools.findX(this.user.getPosY(), new Point(p2.x-listRange.get(i),p2.y),
                            new Point(p1.x-listRange.get(i+1),p1.y)); //Calcul par la gauche
                    int rangeMax = xMax - xMin; //La distance maximale que user peut etre par rapport au centre de la route.

                    double modVitMin = 1; //Lorsque la distance est maximale, la vitesse est minimale

                    float dist = (float) Tools.distance(new Point(this.user.getPosX(),this.user.getPosY()),
                            new Point(xMax, this.user.getPosY()));
                    //Produit en croix
                    modVit = (modVitMin*dist)/(double) rangeMax;
                    if(modVit < 0){
                        System.out.println("Alerte ! ModVit < 0 on road!!!");
                    }

                    double maxModVit = 40; //La mod maximale de la vitesse
                    if(modVit>maxModVit){
                        modVit = maxModVit;
                    } else if(modVit < 0) {//On est deja tombees sur modVit = -111
                        modVit = 0;
                    }


                    //modVit =
                    //modVit = 5f;
                    if(this.user.getVitesse()+modVit == 0){
                        System.out.println("Vitesse nulle sur la route !!");
                    }
                    System.out.println("On Road");

                }


                } else {
                onRoad = false;
                this.user.isOnRoad = false;
                System.out.println("Out of Road");
                    if(this.user.getVitesse()>0){
                        modVit = -2;
                    }

                }

            this.user.modVitesse(modVit);
            System.out.println("Vitesse user : "+this.user.getVitesse());
            if(onRoad && this.user.getVitesse()==0){
                System.out.println("Problem vitesse 0 on Road");
            }

            //Deplace les diff objets

            //int modPos = Math.round(this.user.getVitesse()*(double)varTime/1000);// div 1000 car ce sont des mili secondes
            int modPos = Math.round((float)this.user.getVitesse()*(float)(varTime/1000));//xt = v*t + x0 -> modPos = v*t, t en secondes
            //int modPos = Math.round((float)this.user.getVitesse()*(float)(varTime/100));

            this.route.moveRoute(Math.round((float)this.user.getVitesse())); //Beaucoup trop rapide !!
            //this.route.moveRoute(modPos);

            //Test collision obstacles -> Diminue vitesse, pas de fin de jeu

            this.aff.update();
            try {
                Thread.sleep(this.varTime);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
