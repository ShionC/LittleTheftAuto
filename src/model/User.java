package model;

import vue.Affichage;
import vue.VueUser;

public class User {

    /**
     * La position sur l axe X de User
     */
    private int posX;
    /**La position sur l axe Y de User**/
    private int posY;
    /**La valeur d un deplacement**/
    private int saut;

    // ********************************** 2) Constructeur **********************************

    /**
     * Cree un nouvel utilisateur qui peut bouger a droite et a gauche
     */
    public User(){
        this.posX = Affichage.LARGEUR/2;
        this.posY = Affichage.HAUTEUR - VueUser.HAUT_CAR - 20;
        this.saut = 10;
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Deplace la position de l utilisateur sur la droite sur l axe X
     */
    public void moveRight(){
        this.posX -= this.saut;
    }

    /**
     * Deplace la position de l utilisateur sur la gauche sur l axe X
     */
    public void moveLeft(){
        this.posX += saut;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
