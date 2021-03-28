package model;

import Tools.Tools;

import java.awt.geom.Area;

/**
 * Un concreteObject est un objet concret dans le plan.
 * Il peut entrer en collision avec d'autres concreteObject, il a donc plusieurs caracteristiques :
 * <ul>
 *     <li>Des coordonnees x,y (le point en haut a gauche de sa hitbox)</li>
 *     <li>Une boite de collision</li>
 * </ul>
 */
abstract public class ConcreteObject {

    /**La position sur l axe X de l objet. En float**/
    protected float posX;
    /**La position sur l axe Y de l objet. En float**/
    protected float posY;


    /**
     * Un concreteObject est un objet concret dans le plan.
     * Il peut entrer en collision avec d'autres concreteObject, il a donc plusieurs caracteristiques :
     * <ul>
     *     <li>Des coordonnees x,y (le point en haut a gauche de sa hitbox)</li>
     *     <li>Une boite de collision</li>
     * </ul>
     */
    public ConcreteObject(){

    }

    /**
     * Renvoie la position sur l axe X de l objet
     * @return
     */
    public int getPosX(){
        return Math.round(posX);
    }

    /**
     * Renvoie la position sur l axe Y de l objet
     * @return
     */
    public int getPosY(){
        return Math.round(posY);
    }

    /**
     * Renvoie la hitBox de l objet.
     * <br/> La hitBox defini l aspect concret de l objet.
     * <br/>Tout test de collision se fait sur la hitBox
     * @return
     */
    abstract public Area getHitBox();

    /**
     * Verifie si le ConcreteObject entre en collision avec un autre.
     * <br/>2 ConcreteObject sont en collision si ils se chevauchent.
     * @param obj Le deuxieme ConcreteObject
     * @return true si il y a collision, false sinon
     */
    public boolean collision(ConcreteObject obj){
        return Tools.collision(this.getHitBox(), obj.getHitBox());
    }

}
