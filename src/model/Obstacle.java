package model;

import java.awt.*;

public class Obstacle {

    /**True si la position de l obstacle est a droite de la route, false sinon**/
    private boolean rightRoute;

    /**La distance separant l obstacle de la route. <br/>Parametre de profondeur**/
    private int distToRoute;

    //TODO les autres coord
    private int posY;
    private int posX;

    //Pour creer les shapes en fonction du type d obstacle
    private final int LargeurInit;
    private final int HauteurInit;
    //Largeur avec scale
    private int LARGEUR;
    private int HAUTEUR;

    private int type;


    public Obstacle(){
        //TODO
        this.LargeurInit = 0; //TODO
        this.HauteurInit = 0;
        this.LARGEUR = this.LargeurInit;
        this.HAUTEUR = this.HauteurInit;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public boolean isRightRoute() {
        return rightRoute;
    }

    public int getHAUTEUR() {
        return HAUTEUR;
    }

    public int getLARGEUR() {
        return LARGEUR;
    }

    public int getDistToRoute() {
        return distToRoute;
    }

    /**
     * Modifie LARGEUR et HAUTEUR.
     * <br/> Modifie la taille de Obstacle
     * <br/>Permet a la shape de objet de correspondre a la taille de l image
     * @param fac
     */
    public void scale(double fac){
        //TODO mod LARGEUR et HAUTEUR en fonction de init.
    }

    /**
     * Renvoie le type d obstacle
     * @return un int
     */
    public int getType() {
        return type;
    }

    /**
     * Verifie si l objet est a droite ou a gauche du point
     * @param p
     * @return
     */
    public boolean isRightPoint(Point p){
        //TODO
        return false;
    }

    /**
     * Deplace l obstacle en prenant en compte la profondeur
     * @param newRange le déplacement lateral, permet de donner une impression de profondeur. Correspond a la distance a la route a ce point
     * @param dy la valeur de deplacement sur l axe Y
     */
    public void move(int newRange, double dy){
        //TODO, dx depends de rightRoute, permet de faire la profondeur
        //Faire la mod dy comme pour la route
        //Utiliser distToRoute
        //Prendre en compte la largeur

    }
}
