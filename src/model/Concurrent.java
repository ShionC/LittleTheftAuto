package model;

import Tools.Tools;
import vue.Affichage;
import vue.VueBackground;
import vue.VueUser;

public class Concurrent extends User{


    /**Le type du concurrent (couleur)**/
    private int type;

    /**
     * Cree un concurrent.
     * <br/>Un concurrent est un User spécifique :
     * Il est influence par la vitesse de User mais possede sa propre vitesse.
     * <br/> Sa methode run() le deplace a droite et a gauche aleatoirement.
     */
    public Concurrent(){
        super();
        this.chooseType();
        this.posY = VueBackground.horizon;
        this.posX = (float) Tools.rangedRandomDouble(50, Affichage.LARGEUR-50);
        this.etat.setImages(Images.getConcurrentImg(this.type));
    }

    /**
     * Choisi un type aleatoirement
     */
    private void chooseType(){
        int maxType = 3;
        this.type = Tools.rangedRandomInt(1, maxType);
    }

    /**
     * Renvoie le type du concurrent. Affecte principalement sa couleur
     * @return
     */
    public int getType(){
        return this.type;
    }

    /**
     * Deplace le concurrent vers le haut
     * @param dy
     */
    public void moveUp(float dy){
        if(this.posY >0){
            this.posY -= dy;
        }
    }

    /**
     * Deplacement sur l axe Y dans le sens inverse de l avancement
     * @param dy
     */
    public void slowDown(float dy){
        this.posY += dy;
    }

}
