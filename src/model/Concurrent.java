package model;

import Tools.Tools;
import vue.Affichage;
import vue.VueUser;

public class Concurrent extends User{

    /**Dimension de la shape**/
    int LARGEUR = 60;
    int HAUTEUR = 120;

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
     * Deplace Concurrent lateralement en fonction de son inertie.
     * <br/>Prend en compte sa place dans l'ecran et gere les bordures
     */
    private void gestionMoveLateral(){
        if(super.posX >= 0 && this.posX + this.LARGEUR <= Affichage.LARGEUR){
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
}
