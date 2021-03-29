package model;

import Tools.Tools;
import vue.Affichage;
import vue.VueBackground;
import vue.VueUser;

public class Concurrent extends User{


    /**Le type du concurrent (couleur)**/
    private int type;

    /**Le temps qu il faut attendre avant de faire un nouveau deplacement lateral**/
    private int waitMove = 1;
    /**La direction du mouvement en cours. True si vers la droite**/
    private boolean dirCurrentMove = true;

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

    /**
     * Se deplace pour une duree random d un cote random
     */
    private void randomMoveLateral(){
        if(! this.etat.isPaused()){
            if(this.waitMove > 0){
                this.waitMove --;
                if (this.dirCurrentMove) {
                    if (getPosX() + getLARGEUR() < Affichage.LARGEUR) {
                        moveRight();
                    }
                } else {
                    if (getPosX() > 0) {
                        moveLeft();
                    }
                }

            } else {
                this.waitMove = Tools.rangedRandomInt(2, 15);
                int rand = Tools.rangedRandomInt(0,100);
                if(rand <=50){
                    this.dirCurrentMove = true;
                } else {
                    this.dirCurrentMove = false;
                }
            }
        }

    }


    @Override
    public void run(){
        while(run){

            this.goBackInertie();

            this.randomMoveLateral();
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
