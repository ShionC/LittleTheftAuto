package model;

import Tools.Tools;
import vue.Affichage;
import vue.VueBackground;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Obstacle extends ConcreteObject {

    /**Le type d'obstacle**/
    private int type;

    /**La position sur l axe Y de l obstacle a son apparition.
     * <br/> Valable quel que soit le type d obstacle
     */
    public static float initY = VueBackground.horizon;

    /**True si la position de l obstacle est a droite de la route, false sinon**/
    private boolean rightRoute;

    /**La distance separant l obstacle de la route. <br/>Parametre de profondeur**/
    private float distToRoute;

    //Largeur et hauteur de la hitbox initiales sans scale
    /**Les dimensions de la hitBox sans scale**/
    private int LARGEUR, HAUTEUR;

    /**La position X du centre de la route pour posY**/
    private float posXCenterRoute;
    /**La sauvegarde de la derniere range de la route pour posY**/
    private float savedRangeRoute;
    /**L'image correspondant à l'obstacle en fonction du type d'obstacle**/
    private BufferedImage img;

    /**Sert a gerer l eloignement des obstacles sur l axe X**/
    private float modFacDistanceRoute = 2;//0.9f;

    /**
     * Cree un obstacle initialise aleatoirement sur l axe X sur la map et a la position <i>initY</i> sur l axeY
     * <br/>Cet obstacle possede une hitbox.
     * <br/>Son deplacement suit les regles suivantes :
     * <ul>
     *     <li>son deplacement sur l axe Y est influence par la vitesse de User</li>
     *     <li>son deplacement sur l axe X suit la range de la route pour un effet de perspective</li>
     * </ul>
     * @param posXCenterRoute
     * @param currentRange
     */
    public Obstacle(float posXCenterRoute, float currentRange){

        this.posXCenterRoute = posXCenterRoute;
        this.savedRangeRoute = currentRange;

        this.chooseTypeObstacle();
        this.posY = initY;
        this.posX = (float) Tools.rangedRandomDouble(50, Affichage.LARGEUR-50);
        this.rightRoute = this.isRightPoint(new Point((int) posXCenterRoute, (int) this.posY));
        this.initSizeObstacle();
        this.initDistToRoute();
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
    public synchronized Area getHitBox() {
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

    /**
     * Renvoie la valeur du scale de l image / hitbox par rapport a sa position sur la route
     * <br/>Methode de gestion de perspective
     * @return
     */
    public double getScale() {
        double initScale = 1;
        double initPos = Affichage.HAUTEUR - this.HAUTEUR;
        double scale = (initScale * this.posY)/initPos;

        return scale;
    }

    /**
     * Renvoie la derniere valeur sauvegardee de la rangeRoute correspondant a la position sur l axe Y de l obstacle
     * @return
     */
    public float getSavedRangeRoute() {
        return savedRangeRoute;
    }

    /**
     * Enregistre la valeur actuelle de la rangeRoute correspondant a la position sur l axe Y de l obstacle
     * @param range la rangeRoute a sauvegarder
     */
    public void saveRangeRoute(float range){
        this.savedRangeRoute = range;
    }

    /**
     * Sauvegarde la position sur l axe X du milieu de la route a la position sur l axe Y de l obstacle
     * @param posXCenterRoute
     */
    public void savePosXCenterRoute(float posXCenterRoute){
        this.posXCenterRoute = posXCenterRoute;
    }


    // Init ----------------------------------------------------------------------------------


    /**
     * Choisi le type d obstacle
     */
    private void chooseTypeObstacle() {
        int maxType = 9;
        this.type = Tools.rangedRandomInt(1,maxType);
    }

    /** Initialise la taille de la hitbox en fonction du type d'obstacle **/
    private void initSizeObstacle() {
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
     * Initialise la distance a la route en fonction de posX et posXCenterRoute
     */
    private void initDistToRoute(){
        if (isRightRoute()) {
            this.distToRoute = this.posX - this.posXCenterRoute;
        } else {
            this.distToRoute = this.posXCenterRoute - this.posX;
        }
    }


    // Autres méthodes __________________________________________________________________________



    /**
     * Verifie si l objet est a droite ou a gauche du point
     * @param p
     * @return
     */
    public boolean isRightPoint(Point p){
        return this.posX > p.x;
    }

    // Deplacement ------------------------------------------------------------


    /**
     * Met a jour posX en fonction de distToRoute et posXCenterRoute
     */
    private void updatePosX(){
        if (isRightRoute()) {
            this.posX = this.posXCenterRoute + this.distToRoute;
        } else {
            this.posX = (float) (this.posXCenterRoute - this.distToRoute - this.getLARGEUR());
        }
    }

    /**
     * Calcule la distance a la route en la modifiant par rapport au facteur donne
     * <br/> Met a jour posX en fonction des calculs trouves
     * <br/>Permet un effet de profondeur
     * @param facteurMod le facteur de deplacement entre l ancienne position sur l axe X et la nouvelle
     */
    public void updateDistToRoute(float facteurMod) {
        //this.distToRoute = this.distToRoute * facteurMod;//(facteurMod*this.modFacDistanceRoute);
        /*if(facteurMod*this.modFacDistanceRoute>1){
            this.distToRoute = this.distToRoute * facteurMod*this.modFacDistanceRoute;
        } else {
            this.distToRoute = this.distToRoute * facteurMod;
        }

         */
        this.distToRoute = this.distToRoute + (facteurMod*this.modFacDistanceRoute);
        this.updatePosX();
    }

    /**
     * Deplace l obstacle sur l axe Y
     * @param dy la valeur de deplacement sur l axe Y
     */
    public void move(double dy){
        // Déplacement sur l'axe X
        // la valeur de déplacement sur l'axe X
        /*double dx = 1;
        //Si l'obstacle est à gauche de la route
        if (!this.isRightRoute()) {
            // L'obstacle se déplace vers la gauche au fur et à mesure
            this.posX -= dx;
        } else {
            this.posX += dx;
        }

         */

        // Déplacement sur l'axe Y : même vitesse que la route
        this.posY += dy;
    }
}
