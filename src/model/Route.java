package model;

import java.awt.*;
import java.util.ArrayList;

import game.Tools;
import vue.Affichage;

public class Route {
    // ********************************** 1) Attributs **********************************
    /** Pour la ligne brisée principale**/
    private ArrayList<Point> listePoints = new ArrayList<>();
    /**Valeur d un saut**/
    private int saut;
    /**Coordonnee du point de controle sur l axe Y**/
    private int y_ptCtrl;

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
        int range = 100; //Deviation de x autorisee
        while(y >= 0){
            y = y - Tools.rangedRandomInt(40, 100);

            //Cherche un y pas trop loin de l ancien, pour la beaute de la courbe
            int newx = Tools.rangedRandomInt(x-range, x+range);
            while((newx > Affichage.LARGEUR -50) || (newx < 50)){ //Zone d appartition de la courbe, voir avec affichage
                newx = Tools.rangedRandomInt(y-range, y+range);
            }
            x = newx;

            if(x>Affichage.LARGEUR){
                System.out.println("y = "+y+" out of range "+Affichage.LARGEUR+" !!");
            }

            listePoints.add(new Point(x, y));
        }

    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Mise à jour de la route
     */
    private void updateRoute() {
        //Ajoute ou enleve des points au besoin
    }


    /**
     * Défilement de la route, modifie listePoint
     * Mettre aussi a jour le point de controle !
     */
    public void moveRoute() {
        //...
        this.y_ptCtrl -= saut;
        this.updateRoute();
    }

    /**
     * Renvoie la liste des points qui constituent la route.
     * @return
     */
    public ArrayList<Point> getRoute() {
        return this.listePoints;
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
