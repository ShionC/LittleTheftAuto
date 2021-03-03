package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import Tools.Tools;
import vue.Affichage;
import vue.VueBackground;

public class Route {
    // ********************************** 1) Attributs **********************************
    /** Pour la ligne brisée principale**/
    private ArrayList<Point> listePoints = new ArrayList<>();
    /**Mutex de listePoints**/
    private final ReentrantLock routeMutex = new ReentrantLock();

    /**Valeur d un accumule**/
    private double sautCharge;

    /**Coordonnee du point de controle sur l axe Y**/
    private int y_ptCtrl = 0;

    /**La valeur en point que rapporte le depassement a user**/
    private int valueCtrl;

    private int kilometrage = 0;


    // ********************************** 2) Constructeur **********************************

    /**
     * Le model de la route, est constitue d une suite de points formant une ligne brisee
     */
    public Route(){
        //Initialiser listePoints.
        //Rappel : On peut utiliser Affichage.HAUTEUR car est statique
        int x = Affichage.LARGEUR/2;
        int y = Affichage.HAUTEUR;
        listePoints.add(new Point(x, y));
        //int range = 50; //Deviation de x autorisee
        while(this.listePoints.get(this.listePoints.size()-1).y >= VueBackground.horizon){ //Stop a l horizon
            this.updateRoute();
        }

        //Initialisation du saut. On peut penser que le saut "charge" jusqu a atteindre une valeur convenable (1)
        this.sautCharge = 0;

        //Initialisation du pt de ctrl. Initialise par TimeManager mais pour eviter les problemes
        this.y_ptCtrl = 0;

    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Mise à jour de la route, ajoute ou enleve des points de la liste au besoin
     */
    private void updateRoute() {
        synchronized (this.listePoints){
            try {
                this.routeMutex.lock();

                //Ajoute des points au besoin
                if(this.listePoints.get(this.listePoints.size()-1).y>=VueBackground.horizon){
                    int range = 90; //Deviation de x autorisee

                    int y = this.listePoints.get(this.listePoints.size()-1).y - Tools.rangedRandomInt(40, 200);
                    int x = this.listePoints.get(this.listePoints.size()-1).x;
                    //Cherche un y pas trop loin de l ancien, pour la beaute de la courbe
                    int newx;
                    if(x-range<0){
                        newx = Tools.rangedRandomInt(x, x+range);
                    } else {
                        newx = Tools.rangedRandomInt(x-range, x+range);
                    }
                    while((newx > Affichage.LARGEUR -50) || (newx < 50)){ //Zone d appartition de la courbe, voir avec affichage
                        if(newx < 50){
                            newx = Tools.rangedRandomInt(x, x+range);
                        } else {
                            newx = Tools.rangedRandomInt(x-range, x);
                        }
                    }
                    x = newx;
                    if(x>Affichage.LARGEUR){
                        System.out.println("y = "+y+" out of range "+Affichage.LARGEUR+" !!");
                    }
                    listePoints.add(new Point(x, y));

                }

                //Enleve des points au besoin
                if(this.listePoints.size()<1){
                    System.out.println("        La liste des points de la route ne s est pas actualisee !!");
                } else {
                    if(this.listePoints.get(1).y>Affichage.HAUTEUR){
                        if(this.listePoints.size()<2){
                            System.out.println("Liste des points de route < 2 !!!");
                        } else {
                            this.listePoints.remove(0);
                        }
                    }
                }

            } finally {
                this.routeMutex.unlock();
            }

        }


    }

    /**
     * Renvoie le nombre de kilometres parcourus depuis le debut de la course
     * @return
     */
    public int getKilometrage() {
        return kilometrage;
    }

    /**
     * Défilement de la route, modifie liste.
     * <br/>La modification de delacement dy est chargee jusqu a obtenir un int non null avant de deplacer les points
     * <br/>Met aussi a jour le point de controle
     * @param saut le deplacement de tous les points. Est mis a charger et lorsque il atteint une valeur > 1 fait bouger les points
     */
    public void moveRoute(double saut) {
        //Peut etre utiliser un semaphore pour eviter que 2 objets essaient de se co en meme temps, avec getRoute
        this.sautCharge += saut;
        if(this.sautCharge >=1){
            int currentSaut = (int) this.sautCharge; //La partie entiere de saut ici au moins 1
            this.sautCharge -= currentSaut; //On va utiliser currentSaut donc on l enleve a ce qui a deja charge

            //Modification des points
            synchronized (this.listePoints){
                try {
                    this.routeMutex.lock();
                    for(int i = 0; i<this.listePoints.size(); i++){
                        this.listePoints.get(i).move(this.listePoints.get(i).x, this.listePoints.get(i).y+currentSaut);
                    }
                    this.y_ptCtrl += currentSaut;
                    this.updateRoute();
                } finally {
                    this.routeMutex.unlock();
                }

            }
            this.kilometrage += currentSaut;


        }

    }

    /**
     * Renvoie la liste des points qui constituent la route.
     * <br/>Le point 0 est celui le plus bas dans le fenetre
     * @return l arrayList de la liste des points
     */
    public ArrayList<Point> getRoute() {
        synchronized (this.listePoints){
            try {
                this.routeMutex.lock();

                ArrayList<Point> newList = new ArrayList<>();
                for(int i = 0; i< this.listePoints.size(); i++){
                    newList.add((Point)this.listePoints.get(i).clone());
                }
                if(newList.size() == 0){ //Pour eviter les bugs
                    newList.add(new Point(Affichage.LARGEUR/2, Affichage.HAUTEUR));
                    newList.add(new Point(Affichage.LARGEUR/2, VueBackground.horizon));
                }

                return newList;

            } finally {
                this.routeMutex.unlock();
            }

        }
    }


    /**
     *
     * @param posY
     */
    /**
     * Remplace l'ancien point de contrôle par un nouveau
     * @param posY la distance du nouveau point de controle par rapport a user
     * @param valueCtrl en valeur du point de controle pour le score de user
     */
    public void newPtControle(int posY, int valueCtrl) {
        this.y_ptCtrl = posY;
        this.valueCtrl = valueCtrl;
    }

    /**
     * Renvoie la coordonnee y du point de contrôle
     * @return
     */
    public int getCtrl() {
        return this.y_ptCtrl;
    }

    /**
     * Renvoie la valeur du point de controle actuel
     * @return la valeur pour le score de user
     */
    public int getValueCtrl() {
        return valueCtrl;
    }
}
