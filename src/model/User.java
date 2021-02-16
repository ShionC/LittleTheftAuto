package model;

import vue.Affichage;
import vue.VueUser;

public class User extends Thread {

    /**Le flag d arret de la thread**/
    private boolean run;

    public boolean isOnRoad = true;

    /**
     * La position sur l axe X de User
     */
    private int posX;
    /**La position sur l axe Y de User**/
    private int posY;
    /**La valeur d un deplacement lateral**/
    private int saut;

    /**La vitesse de User, et par extention la vitesse de la route**/
    private double vitesse;

    /**L etat actuel de user {-1,0,1}**/
    private int etat = 0;
    /**Le temps qu il faut attendre pour que l etat revienne a 0**/
    private final int waitEtat = 2;
    /**Le temps dattente actuel**/
    private int currentWaitEtat = 0;

    // ********************************** 2) Constructeur **********************************

    /**
     * Cree un nouvel utilisateur qui peut bouger a droite et a gauche
     * Sa methode thread met constanment son etat a 0, cad qu elle redresse user
     */
    public User(){
        this.posX = Affichage.LARGEUR/2;
        this.posY = Affichage.HAUTEUR - VueUser.HAUT_CAR - 20;
        this.saut = 10;
        this.vitesse = 10;
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Renvoie la vitesse de User, cad la vitesse a laquelle se deplace le decors
     * @return
     */
    public double getVitesse(){return this.vitesse;};

    /**
     * Modifie la vitesse de User en lui ajoutant la nouvelle vitesse
     * @param vitesse le modificateur de la vitesse
     * @return
     */
    public void modVitesse(double vitesse){
        this.vitesse += vitesse;
        if(this.isOnRoad && this.vitesse == 0){
            System.out.println("Vitesse nulle malgre la route !!");
        }
        if(this.vitesse<0){
            this.vitesse = 0;
        }
    };

    /**
     * Renvoie l etat de user, cad si il est penche et si oui dans quelle position
     * -1 : vers la gauche
     * 0 : tout droit
     * 1 : vers la droite
     * @return
     */
    public int getEtat() {
        return etat;
    }

    /**
     * Deplace la position de l utilisateur sur la droite sur l axe X
     */
    public void moveRight(){
        this.posX += this.saut;
        this.etat = 1;
        this.currentWaitEtat = 0;
    }

    /**
     * Deplace la position de l utilisateur sur la gauche sur l axe X
     */
    public void moveLeft(){
        this.posX -= saut;
        this.etat = -1;
        this.currentWaitEtat = 0;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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
            if(this.currentWaitEtat < this.waitEtat){
                this.currentWaitEtat++;
            } else if (this.currentWaitEtat == this.waitEtat){
                this.etat = 0;
            }

            try {
                Thread.sleep(40);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
