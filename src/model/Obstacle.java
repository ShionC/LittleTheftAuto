package model;

import Tools.Tools;
import vue.Affichage;
import model.Route;
import vue.VueBackground;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Obstacle extends ConcreteObject {

    // Type d'obstacle
    private int type;

    /**True si la position de l obstacle est a droite de la route, false sinon**/
    private boolean rightRoute;

    /**La distance separant l obstacle de la route. <br/>Parametre de profondeur**/
    private float distToRoute;

    //Largeur et hauteur de la hitbox initiales sans scale
    private int LARGEUR;
    private int HAUTEUR;

    // L'image correspondant à l'obstacle en fonction du type d'obstacle
    private BufferedImage img;

    public Obstacle(){
        this.chooseTypeObstacle();
        this.posY = VueBackground.horizon;
        this.posX = (float) Tools.rangedRandomDouble(50, Affichage.LARGEUR-50);
        this.sizeObstacle();
        this.calculeDistToRoute();
        this.img = Images.getObstacleimg(this.type);
    }

    // Tous les getters _________________________________________________________________________

    public int getPosX() {
        return (int) this.posX;
    }

    public int getPosY() {
        return (int) this.posY;
    }

    public BufferedImage getImg() { return this.img; }

    @Override
    /**
     * Renvoie la hitBox de l'obstacle.
     * @return
     */
    public Area getHitBox() {
        Shape collisionBox = new Rectangle2D.Double(this.getPosX(), this.getPosY(), this.getLARGEUR(), this.getHAUTEUR());
        return new Area(collisionBox);
    }

    /**
     * Renvoie les coord x et y sous forme de point
     * @return
     */
    public Point getPos(){
        return new Point(getPosX(),getPosY());
    }

    public boolean isRightRoute() {
        return rightRoute;
    }

    /**
     * Renvoie la largeur de la hitbox de User
     * @return
     */
    public double getLARGEUR(){
        return this.LARGEUR * this.getScale();
    }

    /**Renvoie la hauteur de la hitBox de User**/
    public double getHAUTEUR(){
        return this.HAUTEUR * this.getScale();
    }

    public float getDistToRoute() {
        return distToRoute;
    }

    /**
     * Renvoie le type d obstacle
     * @return un int
     */
    public int getType() {
        return type;
    }

    // Autres méthodes __________________________________________________________________________

    private void chooseTypeObstacle() {
        int maxType = 9;
        this.type = Tools.rangedRandomInt(1,maxType);
    }

    private void calculeDistToRoute() {
        // Initialisation
        if (!isRightRoute()) {
            this.distToRoute = Affichage.LARGEUR / 2 - this.posX;
        } else {
            this.distToRoute = this.posX - Affichage.LARGEUR / 2;
        }
    }

    public double getScale() {
        double initScale = 1;
        double initPos = Affichage.HAUTEUR - this.HAUTEUR - 20;
        double scale = (initScale * this.posY)/initPos;

        return scale;
    }

    /**
     * Verifie si l objet est a droite ou a gauche du point
     * @param p
     * @return
     */
    public boolean isRightPoint(Point p){
        return this.posX > p.x;
    }

    /** Taille de la hitbox en fonction du type d'obstacle **/
    public void sizeObstacle() {
        if (this.type == 1) {
            // rock1
            this.LARGEUR = 150;
            this.HAUTEUR = 105;
        } else if (this.type == 2) {
            // rock2
            this.LARGEUR = 150;
            this.HAUTEUR = 101;
        } else if (this.type == 3) {
            // rock3
            this.LARGEUR = 200;
            this.HAUTEUR = 129;
        } else if (this.type == 4) {
            // rock4
            this.LARGEUR = 200;
            this.HAUTEUR = 126;
        } else if (this.type == 5) {
            // tree1
            this.LARGEUR = 126;
            this.HAUTEUR = 170;
        } else if (this.type == 6) {
            // tree2
            this.LARGEUR = 141;
            this.HAUTEUR = 185;
        } else if (this.type == 7) {
            // tree3
            this.LARGEUR = 150;
            this.HAUTEUR = 190;
        } else if (this.type == 8) {
            // rockground1
            this.LARGEUR = 200;
            this.HAUTEUR = 115;
        } else {
            // rockground2
            this.LARGEUR = 182;
            this.HAUTEUR = 127;
        }

    }

    /**
     * Deplace l obstacle en prenant en compte la profondeur
     * @param distToRoute le déplacement lateral, permet de donner une impression de profondeur. Correspond a la distance a la route a ce point
     * @param dy la valeur de deplacement sur l axe Y
     */
    public void move(float distToRoute, double dy){
        // Déplacement sur l'axe X
        // la valeur de déplacement sur l'axe X
        double dx = 1;
        //Si l'obstacle est à gauche de la route
        if (!this.isRightRoute()) {
            // L'obstacle se déplace vers la gauche au fur et à mesure
            this.posX -= dx;
        } else {
            this.posX += dx;
        }

        // Déplacement sur l'axe Y : même vitesse que la route
        this.posY += dy;
    }
}
