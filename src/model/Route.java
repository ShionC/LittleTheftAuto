package model;

import java.awt.*;
import java.util.ArrayList;

import game.Tools;
import vue.Affichage;
import vue.VueBackground;

public class Route {
    // ********************************** 1) Attributs **********************************
    /** Pour la ligne brisée principale**/
    private ArrayList<Point> listePoints = new ArrayList<>();

    /**Valeur d un accumule**/
    private double sautCharge;

    /**Coordonnee du point de controle sur l axe Y**/
    private int y_ptCtrl = 0;

    private int kilometrage = 0;

    // ********************************** 2) Constructeur **********************************

    /**
     * Le model de la route, est constitue d une suite de points formant une ligne brisee
     */
    public Route(){
        //Initialiser listePoints.
        //Rappel : On peut utiliser Affichage.HAUTEUR car est statique
        int x = Affichage.LARGEUR/2;
        int y = Affichage.HAUTEUR;
        listePoints.add(new Point(x, y));
        //int range = 50; //Deviation de x autorisee
        while(this.listePoints.get(this.listePoints.size()-1).y >= VueBackground.horizon){ //Stop a l horizon
            /*
            y = y - Tools.rangedRandomInt(40, 100);

            //Cherche un y pas trop loin de l ancien, pour la beaute de la courbe
            int newx = Tools.rangedRandomInt(x-range, x+range);
            while((newx > Affichage.LARGEUR -50) || (newx < 50)){ //Zone d appartition de la courbe, voir avec affichage
                newx = Tools.rangedRandomInt(x-range, x+range);
            }
            x = newx;

            if(x>Affichage.LARGEUR){
                System.out.println("y = "+y+" out of range "+Affichage.LARGEUR+" !!");
            }

            listePoints.add(new Point(x, y));

             */
            this.updateRoute();
        }

        //Initialisation du saut. On peut penser que le saut "charge" jusqu a atteindre une valeur convenable (1)
        this.sautCharge = 0;

    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Mise à jour de la route, ajoute ou enleve des points de la liste au besoin
     */
    private void updateRoute() {
        synchronized (this.listePoints){
            //Ajoute des points au besoin
            if(this.listePoints.get(this.listePoints.size()-1).y>=VueBackground.horizon){
                int range = 90; //Deviation de x autorisee

                int y = this.listePoints.get(this.listePoints.size()-1).y - Tools.rangedRandomInt(40, 200);
                int x = this.listePoints.get(this.listePoints.size()-1).x;
                //Cherche un y pas trop loin de l ancien, pour la beaute de la courbe
                int newx;
                if(x-range<0){
                    newx = Tools.rangedRandomInt(x, x+range);
                } else {
                    newx = Tools.rangedRandomInt(x-range, x+range);
                }
                while((newx > Affichage.LARGEUR -50) || (newx < 50)){ //Zone d appartition de la courbe, voir avec affichage
                    if(newx < 50){
                        newx = Tools.rangedRandomInt(x, x+range);
                    } else {
                        newx = Tools.rangedRandomInt(x-range, x);
                    }
                }
                x = newx;
                if(x>Affichage.LARGEUR){
                    System.out.println("y = "+y+" out of range "+Affichage.LARGEUR+" !!");
                }
                listePoints.add(new Point(x, y));

            }

            //Enleve des points au besoin
            if(this.listePoints.size()<1){
                System.out.println("        La liste des points de la route ne s est pas actualisee !!");
            } else {
                if(this.listePoints.get(1).y>Affichage.HAUTEUR){
                    if(this.listePoints.size()<2){
                        System.out.println("Liste des points de route < 2 !!!");
                    } else {
                        this.listePoints.remove(0);
                    }
                }
            }
        }


    }

    /**
     * Renvoie le nombre de kilometres parcourus depuis le debut de la course
     * @return
     */
    public int getKilometrage() {
        return kilometrage;
    }

    /**
     * Défilement de la route, modifie liste.
     * <br/>La modification de delacement dy est chargee jusqu a obtenir un int non null avant de deplacer les points
     * <br/>Mettre aussi a jour le point de controle
     * @param saut le deplacement de tous les points. Est mis a charger et lorsque il atteint une valeur > 1 fait bouger les points
     */
    public void moveRoute(double saut) {
        //Peut etre utiliser un semaphore pour eviter que 2 objets essaient de se co en meme temps, avec getRoute
        this.sautCharge += saut;
        if(this.sautCharge >=1){
            int currentSaut = (int) this.sautCharge; //La partie entiere de saut ici au moins 1
            this.sautCharge -= currentSaut; //On va utiliser currentSaut donc on l enleve a ce qui a deja charge

            //Modification des points
            synchronized (this.listePoints){
                for(int i = 0; i<this.listePoints.size(); i++){
                    this.listePoints.get(i).move(this.listePoints.get(i).x, this.listePoints.get(i).y+currentSaut);
                }
                this.y_ptCtrl += currentSaut;
                this.updateRoute();
            }
            this.kilometrage += currentSaut;


        }
        /*
        synchronized (this.listePoints){
            for(int i = 0; i<this.listePoints.size(); i++){
                this.listePoints.get(i).move(this.listePoints.get(i).x, this.listePoints.get(i).y+saut);
            }
            this.y_ptCtrl += saut;
            this.updateRoute();
        }
        this.kilometrage += saut;

         */

    }

    /**
     * Renvoie la liste des points qui constituent la route.
     * <br/>Le point 0 est celui le plus bas dans le fenetre
     * @return l arrayList de la liste des points
     */
    public ArrayList<Point> getRoute() {
        synchronized (this.listePoints){
            ArrayList<Point> newList = new ArrayList<>();
            for(int i = 0; i< this.listePoints.size(); i++){
                newList.add((Point)this.listePoints.get(i).clone());
            }
            if(newList.size() == 0){ //Pour eviter les bugs
                newList.add(new Point(Affichage.LARGEUR/2, Affichage.HAUTEUR));
                newList.add(new Point(Affichage.LARGEUR/2, VueBackground.horizon));
            }

            return newList;
        }
    }

    /**
     * Remplace l'ancien point de contrôle par un nouveau
     */
    public void newPtControle() {

    }

    /**
     * Renvoie la coordonnee y RELATIVE du point de contrôle
     * @return
     */
    public int getCtrl() {
        return this.y_ptCtrl;
    }



}
