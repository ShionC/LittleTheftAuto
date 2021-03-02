package model;

import controleur.Controleur;
import vue.Affichage;
import vue.VueUser;

import java.awt.*;


public class User extends Thread {

    /**Le flag d arret de la thread**/
    private boolean run = true;

    public boolean isOnRoad = true;

    private boolean derapage = true;

    /**
     * La position sur l axe X de User
     */
    private float posX;
    /**La position sur l axe Y de User**/
    public static final int posY = Affichage.HAUTEUR - VueUser.HAUT_CAR - 20;
    /**La valeur max d un deplacement lateral**/
    private final int saut;

    /**La valeur actuelle d un saut. Comprise entre [-saut,saut]
     * Permet un derapage**/
    private float inertie;

    /**Valeur de la mod d inertie lors d un mouvement**/
    private float sautInertie = 3;

    /**Variation de temps entre 2 calculs**/
    private int dt = 20;

    /**La vitesse de User, et par extention la vitesse de la route**/
    private double vitesse;

    /**La vitesse maximale de User**/
    public static final double vitesseMax = 100;

    /**L etat actuel de user {-1,0,1}**/
    private int etat = 0;
    /**Le temps qu il faut attendre pour que l etat revienne a 0**/
    private final int waitEtat = 2;
    /**Le temps dattente actuel**/
    private int currentWaitEtat = 0;



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
        this.saut = 25;
        this.inertie = 0;
        this.vitesse = 20;
    }

    // ********************************** 3) Méthodes **********************************

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
     * Reinitialise User
     * @param newPosX La position de user sur l axe x lors de sa renaissance. Si possible sur la route
     */
    public void rebirth(int newPosX){
        this.vitesse = 20;
        this.posX = newPosX;
        this.inertie = 0;
        this.etat = 0;
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
    public int getEtat() {
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
        //this.posX += this.saut;
        this.etat = 1;
        this.currentWaitEtat = 0;
        if(this.inertie<this.saut){
            if(this.inertie>0){
                this.inertie+=this.sautInertie;
            } else {
                this.inertie+=this.sautInertie; //Plus rapide lors de changement de direction
            }
        }
        //this.posX += this.inertie;  //Le faire dans run()

    }

    /**
     * Deplace la position de l utilisateur sur la gauche sur l axe X
     */
    public void moveLeft(){
        //this.posX -= saut;
        this.etat = -1;
        this.currentWaitEtat = 0;
        if(this.inertie>-this.saut){
            if(this.inertie<0){
                this.inertie-=this.sautInertie;
            } else {
                this.inertie-=this.sautInertie; //Plus rapide lors de changement de direction
            }

        }
        //this.posX += this.inertie; //Le faire dans run()
    }

    /**
     * Renvoie la valeur de inertie, cad la force qui pousse user dans une certaine direction
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
            this.etat = 1;
            this.currentWaitEtat = 0;
        } else {
            this.etat = -1;
            this.currentWaitEtat = 0;
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

    public int getPosX() {
        return Math.round(posX);
    }

    public int getPosY() {
        return posY;
    }

    /**
     * Renvoie les coord x et y sous forme de point
     * @return
     */
    public Point getPos(){
        return new Point(getPosX(),getPosY());
    }

    /**
     * Arrete l execution de la thread
     */
    public void stopRun(){
        this.run = false;
    }


    @Override
    public void run() {
        while(run){

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



            //Gestion bordure d ecran
            if(this.posX >= 0 && this.posX + VueUser.LARG_CAR <= Affichage.LARGEUR){
                //this.posX += this.inertie;
                this.move();
            } else if (this.posX <= 0){
                //this.posX += 30; //Effet rebond
                //this.inertie=50;//Repars le l autre cote
                //this.rebond(1,true);
                this.inertie = 0;
                this.posX = 0;
            } else //noinspection ConstantConditions
                if(this.posX + VueUser.LARG_CAR >= Affichage.LARGEUR){
                //this.posX -= 30;
                //this.inertie = -50;
                //this.rebond(1,false);
                    this.inertie = 0;
                    this.posX = Affichage.LARGEUR - VueUser.LARG_CAR;
            }


            //Modif etat
            if(this.currentWaitEtat < this.waitEtat){
                this.currentWaitEtat++;
            } else if (this.currentWaitEtat == this.waitEtat && this.inertie == 0){ //On est pas en train de deraper
                this.etat = 0;
            }

            try {
                //noinspection BusyWait
                Thread.sleep(dt);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
