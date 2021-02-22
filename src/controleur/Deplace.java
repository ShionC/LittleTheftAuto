package controleur;

import model.Obstacle;
import vue.Affichage;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

import model.User;
import model.Route;
import game.Tools;
import vue.VueBackground;
import vue.VueUser;

public class Deplace extends Thread {

    /**Flag d arret du thread**/
    private boolean run = true;

    /**
     * La route se met a jour toutes les varTime milisecondes
     */
    private int varTime = 40;

    //Lien vers les autres classes
    private Controleur ctrl;

    /**Est a l origine de la vitesse**/
    private User user;
    /**Est deplace selon la vitesse de User**/
    private Route route;
    /**Permet de faire la modification**/
    private Affichage aff;

    /**La modification que on applie au deplacement, pour gerer la vitesse selon les besoins.
     * <br/> /!\ min = 1 !!**/
    double modVitesse = 1;


    /**
     * Ce thread calcule la vitesse de la route,
     * et fait se deplacer tous les objets en fonction de la vitesse a laquelle ils sont relies.
     * @param ctrl le controleur principal
     */
    public Deplace(Controleur ctrl){
        //Lie toutes les autres classes
        this.ctrl = ctrl;
        this.aff = ctrl.aff;
        this.newPartie();

        this.modVitesse = 5;
    }

    /**
     * Arrete l execution du run. A utiliser pour arreter le thread
     */
    public void stopRun(){
        this.run = false;
    }

    /**
     * Commence la partie en initialisant la route et user.
     * <br/> Permet a run de faire les calculs necessaire de la partie
     */
    void newPartie(){
        this.user = ctrl.user;
        this.route = ctrl.route;
    }



    /**
     * Fait le calcul physique de la modification de la position selon la vitesse :
     * <br/>xt = dx + x0,
     * <br/>xt = v*t + x0
     * @param vitesse v
     * @param dt dt la periode de temps sur laquelle la modification s effectue. En <b>milisecondes</b>
     * @return dx, la modification de la position pour t <b>secondes</b>
     */
    private double calcul_dPos(double vitesse, double dt){
        double t = dt/1000; //Conversion en secondes
        double dx = vitesse*t;
        return dx;
    }

    /**
     * Calcul et mise a jour de la vitesse en fonction de la position de l objet sur la route
     * <br/>Methode universelle pour toutes les classes heritant de User
     * <br/>Utilisable egalement pour les concurrents
     * @param obj L objet pour lequel la vitesse doit etre modifie
     * @param shapeObj La forme de l objet sur son affichage
     */
    private void calculVitObj(User obj, Area shapeObj){
        //Calcul de la vitesse
        double modVit = 0;
        //boolean onRoad;
        if (Tools.collision(shapeObj, this.aff.bmg.getShapeRoute())) {
            obj.isOnRoad = true;
            //onRoad = true;
            if(obj.getVitesse()<obj.getVitesseMax()){
                //Touver le bon point sur le segment de route
                boolean modifier = true;
                ArrayList<Point> listRoute = this.route.getRoute();
                Point p1 = listRoute.get(0);
                Point p2 = listRoute.get(1);
                int i = 1;
                while (obj.getPosY()<p2.y && i<listRoute.size()-1){
                    if(i+1 >= listRoute.size()){  //Pour le cas des concurrents qui vont devant et peuvent sortir de Array
                        modifier = false;
                        break;
                    } else if (modifier) {
                        p1 = listRoute.get(i);
                        if(i+1 >= listRoute.size()){  //Juste au cas ou
                            System.out.println("Sortie de array");
                        }
                        p2 = listRoute.get(i+1);
                    }

                    i++;
                }
                i--; //Car on a fait +1 apres avoir change p1 et p2.
                if( ! modifier){
                    modVit = 0;
                } else {
                    ArrayList<Integer> listRange = this.aff.bmg.getRangeRoute();
                    if(listRange.size() == 0){
                        System.out.println("ListRange size == 0 !!!");
                    }
                    int xMax = Tools.findX(obj.getPosY(), p2, p1); //Le centre de la route au niveau de User
                    int xMin = Tools.findX(obj.getPosY(), new Point(p2.x-listRange.get(i),p2.y),
                            new Point(p1.x-listRange.get(i+1),p1.y)); //Calcul par la gauche
                    int rangeMax = xMax - xMin; //La distance maximale que user peut etre par rapport au centre de la route.

                    double modVitMin = 1; //Lorsque la distance est maximale, la vitesse est minimale

                    float dist = (float) Tools.distance(new Point(obj.getPosX(),obj.getPosY()),
                            new Point(xMax, obj.getPosY()));
                    //Produit en croix
                    modVit = (modVitMin*dist)/(double) rangeMax;

                }


                if(modVit < 0){
                    System.out.println("Alerte ! ModVit < 0 on road!!!");
                }

                double maxModVit = 40; //La mod maximale de la vitesse
                if(modVit>maxModVit){
                    modVit = maxModVit;
                } else if(modVit < 0) {//On est deja tombees sur modVit = -111
                    modVit = 0;
                }


                if(this.user.getVitesse()+modVit == 0){
                    System.out.println("Vitesse nulle sur la route !!");
                }
                //System.out.println("On Road");

            }


        } else {
            //onRoad = false;
            obj.isOnRoad = false;
            //System.out.println("Out of Road");
            if(obj.getVitesse()>0){
                modVit = -2;
            }

        }

        obj.modVitesse(modVit);
        //System.out.println("Vitesse user : "+this.user.getVitesse());

    }

    @Override
    public void run() {
        while(run){

            if(this.ctrl.partieEnCours){


                //Calcul de la vitesse
                this.calculVitObj(this.user, this.aff.vueUser.getShapeCar());

                //Calcul de la position selon les formules physiques
                ////xt = v*t + x0 -> modPos = v*t, t en secondes
                double modPos = calcul_dPos(this.user.getVitesse(),varTime);


                //Application de la modification de la position
                //Deplace les diff objets
                this.route.moveRoute(modPos*modVitesse);

                //Decors
                if(this.user.getPosX() > 50 && this.user.getPosX() + VueUser.LARG_CAR < Affichage.LARGEUR-50){
                    int inertieUser = this.user.getInertie();
                    if(inertieUser != 0){
                        this.aff.bmg.moveDecors(inertieUser>0);
                    }
                }

            /*Algo transfere dans la methode getRange() de VueBackground

            ArrayList<Obstacle> listObstacles = this.aff.bmg.getListObstacles();
            ArrayList<Integer> listRange = this.aff.bmg.getRangeRoute();
            ArrayList<Integer> rangeObstacle = new ArrayList<>();

            ArrayList<Point> listRoute = this.route.getRoute();

            for (Obstacle obs: listObstacles) {


                //Touver le bon point sur le segment de route
                boolean inRoute = true; //Si l obstacle est toujours au niveau de la route
                Point p1 = listRoute.get(0); //p1>p2
                Point p2 = listRoute.get(1);
                int i = 1;
                while (obs.getPosY()<p2.y && i<listRoute.size()-1){
                    if(obs.getPosY()>=p1.y){ //Sortit par le bas
                        inRoute = false;
                        break;
                    }
                    if(i+1 >= listRoute.size()){  //Pour le cas des objets au dessus de l horizon
                        inRoute = false;
                        break;
                    } else if (inRoute) {
                        p1 = listRoute.get(i);
                        if(i+1 >= listRoute.size()){  //Juste au cas ou
                            System.out.println("Sortie de array");
                        }
                        p2 = listRoute.get(i+1);
                    }

                    i++;
                }
                i--; //Car on a fait +1 apres avoir change p1 et p2.

                //Mod de rangeObstacles
                if(inRoute){

                    int range = (listRange.get(i)*obs.getPosY())/(p1.y- VueBackground.horizon +50);//Produit en croix
                    rangeObstacle.add(range);
                }

                 */

                ArrayList<Obstacle> listObstacles = this.aff.bmg.getListObstacles();
                for(Obstacle obs : listObstacles){
                    int range = this.aff.bmg.getRange(new Point(obs.getPosX(),obs.getPosY()));

                    //Mod obstacle
                    if(range != -1){
                        obs.move(range, modPos*modVitesse);
                    } else {
                        obs.move(obs.getDistToRoute(), modPos*modVitesse);
                    }

                }



                //TODO obstacles & concurrents
                //Deceleration selon obstacle :

                double decObs = -30; //Pour les obstacles
                double decConc = 0; //Pour les concurrents

                //Test collision obstacles -> Diminue vitesse, pas de test fin de jeu
                for(Obstacle obs : listObstacles){
                    //Si collision entre voiture et obstacle
                    if(Tools.collision(this.aff.vueUser.getShapeCar(), this.aff.bmg.getShapeObstacle(obs))){
                        //Decelere User
                        this.user.modVitesse(decObs);
                        //Rebond de user de l autre cote de l obstacle
                        this.user.rebond(2, ! obs.isRightPoint(new Point(this.user.getPosX(), this.user.getPosY())));
                    }
                }

                this.aff.bmg.updateObstacles();
                this.aff.update();

            }


            try {
                Thread.sleep(this.varTime);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
