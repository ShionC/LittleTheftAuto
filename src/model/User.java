package model;

import Tools.ScrollingStates;
import Tools.Tools;
import vue.Affichage;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


public class User extends ConcreteObject implements Runnable {

    /**Le flag d arret de la thread**/
    protected boolean run = true;

    public boolean isOnRoad = true;

    private boolean derapage = true;

    /**Largeur de la hitbox**/
    protected int LARGEUR = 60;
    /**Hauteur de la hitbox**/
    protected int HAUTEUR = 120;



    /**La valeur max d un deplacement lateral**/
    protected int sautMax;

    /**La valeur actuelle d un saut. Comprise entre [-saut,saut]
     * Permet un derapage**/
    protected float inertie;

    /**Valeur de la mod d inertie lors d un mouvement**/
    protected float sautInertie = 3;

    /**Variation de temps entre 2 calculs**/
    protected int dt = 20;

    /**La vitesse de User, et par extention la vitesse de la route**/
    protected double vitesse;

    /**La vitesse maximale de User**/
    public static final double vitesseMax = 100;

    /**L etat actuel de user {-1,0,1}**/
    protected ScrollingStates etat;

    /**La thread principale qui lance le run**/
    private Thread userThread;


    // ********************************** 2) Constructeur **********************************

    /**
     * Cree un nouvel utilisateur qui peut bouger a droite et a gauche
     * <br/> Un user a une vitesse maximale (statique), une vitesse, une position x et y (y est statique) et
     * un etat qui indique son inclinaison
     * <br/>Sa methode run met constanment son etat a 0, cad qu elle redresse user.
     * User peut se deplacer a droite et a gauche et possede un effet de derapage.
     */
    public User(){
        this.posX = Affichage.LARGEUR/2;
        this.sautMax = 25;
        this.inertie = 0;
        this.vitesse = 20;

        this.posY = Affichage.HAUTEUR - this.HAUTEUR - 20;

        this.userThread = new Thread(this);

        this.etat = new ScrollingStates(Images.getListStateUser());
        this.etat.setModeTo0();
        this.etat.setGap(dt*11);
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Renvoie la largeur de la hitbox de User
     * @return
     */
    public int getLARGEUR(){
        return this.LARGEUR;
    }

    /**Renvoie la hauteur de la hitBox de User**/
    public int getHAUTEUR(){
        return this.HAUTEUR;
    }

    /**
     * Renvoie les coord x et y sous forme de point
     * @return
     */
    public Point getPos(){
        return new Point(getPosX(),getPosY());
    }

    /**
     * Reinitialise User
     * @param newPosX La position de user sur l axe x lors de sa renaissance. Si possible sur la route
     */
    public void rebirth(int newPosX){
        this.vitesse = 20;
        this.posX = newPosX;
        this.inertie = 0;
        this.etat.setCurrentState(0);
    }


    /**
     * Renvoie la vitesse de User, cad la vitesse a laquelle se deplace le decors
     * @return la vitesse
     */
    public double getVitesse(){return this.vitesse;}

    /**
     * Renvoie la vitesse maximale a laquelle peut aller user.
     * @return la vitesse max
     */
    public double getVitesseMax() {
        return vitesseMax;
    }



    /**
     * Modifie la vitesse de User en lui ajoutant la nouvelle vitesse
     * La nouvelle vitesse est comprise entre [0, vitesseMax]
     * @param vitesse le modificateur de la vitesse
     */
    public void modVitesse(double vitesse){
        this.vitesse += vitesse;
        if(this.isOnRoad && this.vitesse == 0){
            System.out.println("Vitesse nulle malgre la route !!");
        }
        //Limitation maximale et minimale de la vitesse
        if(this.vitesse<0){
            this.vitesse = 0;
        } else if (this.vitesse>this.vitesseMax){
            this.vitesse = this.vitesseMax;
        }
    }

    /**
     * Renvoie l etat de user, cad si il est penche et si oui dans quelle position
     * <br/>-1 : vers la gauche
     * <br/>0 : tout droit
     * <br/>1 : vers la droite
     * @return etat
     */
    public ScrollingStates getEtat() {
        return etat;
    }

    /**
     * Deplace user selon son inertie laterale
     */
    public void move(){
        if(derapage){
            this.posX += this.inertie;
        } else {
            float dt = ((float)this.dt)/1000f; //Conversion en secondes
            float dx = this.inertie*dt;
            this.posX += dx*4;
            System.out.println("Inertie : "+this.inertie);
            System.out.println("dx : "+dx);
        }

    }

    /**
     * Deplace la position de l utilisateur sur la droite sur l axe X
     */
    public void moveRight(){
        this.etat.setCurrentState(1);
        if(this.inertie<this.sautMax){
            if(this.inertie>0){
                this.inertie+=this.sautInertie;
            } else {
                this.inertie+=this.sautInertie; //Plus rapide lors de changement de direction
            }
        }

    }

    /**
     * Deplace la position de l utilisateur sur la gauche sur l axe X
     */
    public void moveLeft(){
        this.etat.setCurrentState(-1);
        if(this.inertie>-this.sautMax){
            if(this.inertie<0){
                this.inertie-=this.sautInertie;
            } else {
                this.inertie-=this.sautInertie; //Plus rapide lors de changement de direction
            }

        }
    }

    /**
     * Renvoie la valeur de inertie, cad la force qui pousse user dans une certaine direction
     * <br/>Elle permet de connaitre le deplacement lateral actuel de User.
     * <br/>Si inertie == 0, User ne se deplace pas
     * <br/>Si inertie<0, User se deplace a gauche
     * @return inertie/derapage
     */
    public int getInertie() {
        return Math.round(inertie);
    }


    /**
     * User fait un bond dans la direction indiquee et repars dans cette direction
     * @param intensite taille/intensite du rebond. Appartiens a {1,2,3,4}
     * @param right rebond vers la droite
     */
    public void rebond(int intensite, boolean right){
        if(right){
            this.etat.setCurrentState(1);
        } else {
            this.etat.setCurrentState(-1);
        }
        if(intensite == 1){
            if(right){
                this.posX += 10; //Effet rebond
                this.inertie=25;//Repars le l autre cote
            } else {
                this.posX -= 10;
                this.inertie=-25;
            }
        } else if(intensite == 2){
            if(right){
                this.posX += 10; //Effet rebond
                this.inertie=30;//Repars le l autre cote
            } else {
                this.posX -= 10;
                this.inertie=-30;
            }
        } else if(intensite == 3){
            if(right){
                this.posX += 30;
                this.inertie=35;
            } else {
                this.posX -= 30;
                this.inertie=-35;
            }
        } else if(intensite == 4){
            if(right){
                this.posX += 50;
                this.inertie=40;
            } else {
                this.posX -= 50;
                this.inertie=-40;
            }
        }

    }


    /**
     * Arrete l execution de la thread de user et de etat
     */
    public void stopRun(){
        this.run = false;
        this.etat.stopRun();
    }

    /**
     * Commence le thread de User et le defile de ses etats
     */
    public void startCar(){
        this.userThread.start();
        this.etat.start();
    }

    /**
     * Pause le defilement des etats de user
     */
    public void pause(){
        this.etat.pause();
    }

    /**
     * Recommence le defilement des etats de user
     */
    public void resume(){
        this.etat.resumeScrolling();
    }

    /**
     * Fait la modification de l inertie dans le run.
     * Si derapage == true, l inertie est raprochee de 0
     */
    protected void goBackInertie(){
        if(derapage){
            //Revient progressivement a this.inertie == 0

            int change = 2;
            if(this.inertie<0){
                if(this.inertie+change>0){ //Si depassement, evite tremblotement de user
                    this.inertie = 0;
                } else {
                    this.inertie+=change;
                }
            } else if(this.inertie>0){
                if(this.inertie-change<0){
                    this.inertie = 0;
                } else {
                    this.inertie-=change;
                }
            }
        }
    }

    /**
     * Deplace User lateralement en fonction de son inertie.
     * <br/>Prend en compte sa place dans l'ecran et gere les bordures
     */
    protected void gestionMoveLateral(){
        if(this.posX >= 0 && this.posX + this.LARGEUR <= Affichage.LARGEUR){
            //this.posX += this.inertie;
            this.move();
        } else if (this.posX <= 0){
            //this.posX += 30; //Effet rebond
            //this.inertie=50;//Repars le l autre cote
            //this.rebond(1,true);
            this.inertie = 0;
            this.posX = 0;
        } else //noinspection ConstantConditions
            if(this.posX + this.LARGEUR >= Affichage.LARGEUR){
                //this.posX -= 30;
                //this.inertie = -50;
                //this.rebond(1,false);
                this.inertie = 0;
                this.posX = Affichage.LARGEUR - this.LARGEUR;
            }
    }

    /**
     * Renvoie la hitBox de User.
     * <br/>Sa rotation depends de l etat de User
     * @return
     */
    public Area getHitBox(){
        Shape collisionBox = new Rectangle2D.Double(this.getPosX(), this.getPosY(), this.getLARGEUR(), this.getHAUTEUR());
        int currentEtat = this.etat.getCurrentState();
        double rotation = 0.4;
        if(currentEtat == -1){
            collisionBox = Tools.rotate(collisionBox, -rotation, Tools.Location.Down);
        } else if(currentEtat == 1){
            collisionBox = Tools.rotate(collisionBox, rotation, Tools.Location.Down);
        }
        return new Area(collisionBox);
    }

    @Override
    public void run() {
        while(run){

            this.goBackInertie();

            //Gestion bordure d ecran
            this.gestionMoveLateral();

            try {
                //noinspection BusyWait
                Thread.sleep(dt);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
