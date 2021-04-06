package model;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import Tools.Tools;
import vue.Affichage;

import static vue.VueBackground.horizon;

public class Route extends ConcreteObject {
    //*********************************** 1) Attributs **********************************/
    /** Pour la ligne brisée principale
     * <br/> l idx 0 correspond au plus grand point sur l axe Y (en bas de l ecran).
     * <br/>Cette liste est donc decroissante
     */
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

    /*--------Mise en place de la profondeur----*/

    /**La liste de la largeur de la route pour chaque point. <br/>Permet un effet de profondeur**/
    private ArrayList<Integer> rangeroute = new ArrayList<>();
    /**Mutex de rangeRoute**/
    private final ReentrantLock rangeMutex = new ReentrantLock();


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

        while(this.listePoints.get(this.listePoints.size()-1).y >= horizon){ //Stop a l horizon
            this.updateRoute();
        }
        this.updateRangeRoute();

        //Initialisation du saut. On peut penser que le saut "charge" jusqu a atteindre une valeur convenable (1)
        this.sautCharge = 0;

        //Initialisation du pt de ctrl. Initialise par TimeManager mais pour eviter les problemes
        this.y_ptCtrl = 0;

    }


    // ********************************** 3) Méthodes **********************************



    /*-----------------Route principale---------------------*/

    /**
     * Mise à jour de la route, ajoute ou enleve des points de la liste au besoin
     */
    private void updateRoute() {
        synchronized (this.listePoints){
            try {
                this.routeMutex.lock();

                //Ajoute des points au besoin
                if(this.listePoints.get(this.listePoints.size()-1).y>= horizon){
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
                    //Empeche la route d approcher les bords
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
     * <br/>Met aussi a jour le point de controle ainsi que rangeRoute
     * @param saut le deplacement de tous les points. Est mis a charger et lorsque il atteint une valeur > 1 fait bouger les points
     */
    public void moveRoute(double saut) {
        //Peut etre utiliser un semaphore pour eviter que 2 objets essaient de se co en meme temps, avec getRoute
        this.sautCharge += saut;
        if(this.sautCharge >=1){   //La route avance
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
            this.updateRangeRoute();


        }

    }

    /**
     * Renvoie un clone de la liste des points qui constituent la route.
     * <br/> l idx 0 correspond au plus grand point sur l axe Y (en bas de l ecran).
     * <br/>Cette liste est donc decroissante
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
                    newList.add(new Point(Affichage.LARGEUR/2, horizon));
                }

                return newList;

            } finally {
                this.routeMutex.unlock();
            }

        }
    }

    /**
     * Renvoie le point appartenant a la ligne brisee correspondant au seul point de la liste parallele au point p
     * @param p le point de comparaison
     * @return le point appartenant a la route situe sur le meme Y
     */
    public Point2D getPointRoutePara(Point p){
        ArrayList<Point> listRoute = this.getRoute();
        int i = Tools.findIdxFirstInfByY(p, listRoute);
        Point2D res;
        if(i > 0){
            res = new Point2D.Float(Tools.findX(p.y, listRoute.get(i), listRoute.get(i-1)), p.y);
        } else if(i == -1){
            res = listRoute.get(0);
        } else if(i == -2){
            res = listRoute.get(listRoute.size()-1);
        } else {
            res = new Point2D.Float(-1, -1);
        }
        return res;
    }


    /*--------------------Shape et profondeur de la route---------------------*/

    /**
     * Defini rangeRoute en fonction de la liste de points actuelle.
     * <br/>Range route defini la distance entre le milieu de la route et son cote gauche pour chaque point.
     * <br/>Permet un effet de profondeur
     */
    private void updateRangeRoute(){
        float rangeInit = 100; //Lorsque y=Affichage.HAUTEUR alors de chaque cote de la route il y a cette valeur
        ArrayList<Point> list = this.getRoute();
        synchronized (this.rangeroute){
            try{
                this.rangeMutex.lock();
                //Initialisation de rangeRoute
                ArrayList<Integer> newRange = new ArrayList<>();
                //this.rangeroute.clear();
                for(int i = 0; i<list.size(); i++){
                    Point p = list.get(i);
                    if(p.y < horizon){
                        if(i==0){
                            p.move((int) Tools.findX(horizon, list.get(i+1), p), horizon);
                        } else {
                            p.move((int) Tools.findX(horizon,p,list.get(i-1)), horizon); //N affiche que les points sous l horizon !
                        }

                    } else if(p.y>Affichage.HAUTEUR){
                        if(i == list.size()-1){
                            p.move((int) Tools.findX(Affichage.HAUTEUR, p, list.get(i-1)), Affichage.HAUTEUR);
                        } else {
                            p.move((int) Tools.findX(Affichage.HAUTEUR,list.get(i+1),p), Affichage.HAUTEUR); //N affiche que les points dans l ecran !
                        }

                    }
                    //int range = Math.round((rangeInit*(float) p.y)/(float) Affichage.HAUTEUR); //Produit en croix
                    int range = Math.round((rangeInit*(float) (p.y-horizon+50))/(float) (Affichage.HAUTEUR-horizon+50)); //Produit en croix
                    if(range <1){
                        range = 1; //Mettre une largeur minimale de 1 pour pouvoir dessiner un beau polygone
                    }
                    newRange.add(range);
                }
                this.rangeroute = newRange;


            } finally {
                this.rangeMutex.unlock();
            }

        }
    }

    /**
     * Renvoie un clone de la distance entre le bord de la route et le milieu de la route pour chaque point correspondant a la route
     * <br/>Protege par un mutex
     * @return range list
     */
    public ArrayList<Integer> getRangeRoute(){
        synchronized (this.rangeroute){
            try{
                this.rangeMutex.lock();
                //return (ArrayList<Integer>) this.rangeroute.clone();
                ArrayList<Integer> newList = new ArrayList<>();
                for(int i = 0; i< this.rangeroute.size(); i++){
                    newList.add((Integer) this.rangeroute.get(i).intValue());
                }
                if(newList.size() == 0){ //Pour eviter les bugs
                    newList.add(50);
                }

                return newList;
            } finally {
                this.rangeMutex.unlock();
            }

        }
    }


    @Override
    public Area getHitBox() {
        synchronized (this.rangeroute){
            try{
                this.rangeMutex.lock();
                this.routeMutex.lock();

                updateRangeRoute();
                ArrayList<Integer> rangeRoute = this.getRangeRoute();

                ArrayList<Point> list = this.getRoute();
                int sizeTab = (list.size()*2)+1; //+1 pour fermer le polygon
                int[] tabX = new int[sizeTab];
                int[] tabY = new int[sizeTab];
                //Initialisation du 1er point
                Point oldP = list.get(0); //On a toujours olp > p
                if(oldP.y>Affichage.HAUTEUR){
                    oldP.move((int) Tools.findX(Affichage.HAUTEUR,list.get(1),oldP), Affichage.HAUTEUR); //N affiche que les points dans l ecran !
                }
                tabX[0] = oldP.x-rangeRoute.get(0);
                tabX[sizeTab-2] = oldP.x+rangeRoute.get(0); //Remplir en partant de la fin
                tabY[0] = oldP.y;
                tabY[sizeTab-2] = oldP.y;

                for(int i = 1; i<list.size(); i++){

                    Point p = list.get(i);
                    if(p.y < horizon){
                        p.move((int) Tools.findX(horizon,p,oldP), horizon); //N affiche que les points sous l horizon !
                    }
                    //Le point 0 est le point en bas a
                    tabX[i] = p.x-rangeRoute.get(i);
                    tabX[sizeTab-2-i] = p.x+rangeRoute.get(i); //Remplir en partant de la fin
                    tabY[i] = p.y;
                    tabY[sizeTab-2-i] = p.y;

                    oldP = p;
                }
                tabX[sizeTab-1] = tabX[0];//Fermer le polygon
                tabY[sizeTab-1] = tabY[0];

                return new Area(new Polygon(tabX,tabY, sizeTab));

            } finally {
                this.routeMutex.unlock();
                this.rangeMutex.unlock();
            }

        }


    }


    /*----------------------------Point de controle------------------------*/

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
