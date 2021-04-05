package controleur;

import audio.Audio;
import model.*;
import vue.Affichage;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import Tools.Tools;
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

    /**Le score obtenu lorsqu'un concurrent est depasse**/
    int scoreConcurrent = 50;

    /**La deceleration lorsque User sors de la route**/
    private int freinHorsRoute = 2;

    /**La distance min entre User et un concurrent pour entendre le son lorsque ils se croisent**/
    public int distConcForVroom = 400;


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
     * @return dx = = v*t, la modification de la position pour t <b>secondes</b>
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
     */
    private void calculVitObj(User obj){
        //Calcul de la vitesse
        double modVit = 0;
        //boolean onRoad;
        //if (Tools.collision(obj.getHitBox(), this.route.getHitBox())) {
        if (obj.collision(this.route)) {
            Area hitBoxObj = obj.getHitBox();
            obj.isOnRoad = true;
            //onRoad = true;
            if(obj.getVitesse()<obj.getVitesseMax()){
                //Touver le bon point sur le segment de route
                ArrayList<Point> listRoute = this.route.getRoute();
                boolean modifier;
                int i = Tools.findIdxFirstInfByY(obj.getPos(), listRoute);
                i--; //Car on veux dernier point superieur
                modifier = i>=0;
                if( ! modifier){
                    modVit = 0;
                } else {
                    Point p1 = listRoute.get(i);
                    Point p2 = listRoute.get(i+1);
                    ArrayList<Integer> listRange = this.route.getRangeRoute();
                    if(listRange.size() == 0){
                        System.out.println("ListRange size == 0 !!!");
                    }
                    float xCenter = Tools.findX(obj.getPosY(), p2, p1); //Le centre de la route au niveau de User
                    float xLeft = Tools.findX(obj.getPosY(), new Point(p2.x-listRange.get(i),p2.y),
                            new Point(p1.x-listRange.get(i+1),p1.y)); //Calcul par la gauche, le point sur le bord de la route
                    float rangeMax = xCenter - xLeft; //La distance maximale que user peut etre par rapport au centre de la route.

                    double modVitMin = 1; //Lorsque la distance est maximale, la vitesse est minimale

                    Point2D.Double milieuObj = new Point2D.Double(hitBoxObj.getBounds2D().getCenterX(),
                            hitBoxObj.getBounds2D().getCenterY());
                    double dist = milieuObj.distance(xCenter, milieuObj.y); //La distance du milieu de obj par rapport qu milieu de la route
                    dist = rangeMax-dist; //La distance de user par rapport aux bordS
                    //Produit en croix
                    modVit = (modVitMin*dist)/(double) rangeMax;

                }


                if(modVit < 0){
                    //System.out.println("Alerte ! ModVit < 0 on road!!!");
                }

                double maxModVit = 40; //La mod maximale de la vitesse
                if(modVit>maxModVit){
                    modVit = maxModVit;
                } else if(modVit < 0) {//On est deja tombees sur modVit = -111
                    modVit = 0;
                }


            }


        } else {
            //onRoad = false;
            obj.isOnRoad = false;
            //System.out.println("Out of Road");
            double decceleration = this.freinHorsRoute;
            if(obj.getVitesse()>=decceleration){
                modVit = -decceleration;
            } else {
                modVit = -(decceleration- obj.getVitesse());
            }

        }

        obj.modVitesse(modVit);
        //System.out.println("Vitesse user : "+this.user.getVitesse());

    }

    @Override
    public void run() {
        while(run){

            if(this.ctrl.partieEnCours){


                /*---------------Deplacement----------*/

                //Calcul de la vitesse
                this.calculVitObj(this.user);

                //Calcul de la position selon les formules physiques
                ////xt = v*t + x0 -> modPos = v*t, t en secondes
                double modPos = calcul_dPos(this.user.getVitesse(),varTime);

                //Verification de game over
                if(this.user.getVitesse() == 0){
                    this.ctrl.endPartie(1);
                }

                //Application de la modification de la position
                //Deplace les diff objets
                this.route.moveRoute(modPos*modVitesse);
                Data.setCurrentKilometrage(this.route.getKilometrage());

                //Decors
                if(this.user.getPosX() > 50 && this.user.getPosX() + this.user.getLARGEUR() < Affichage.LARGEUR-50){
                    int inertieUser = this.user.getInertie();
                    if(inertieUser != 0){
                        this.aff.bmg.moveDecors(inertieUser>0);
                    }
                }

                //Obstacles
                ArrayList<Obstacle> listObstacles = this.aff.bmg.getListObstacles();
                try {
                    this.aff.bmg.obstacleMutex.lock();

                    for(Obstacle obs : listObstacles){

                        //Deplacement sur l axe X (effet profondeur)
                        // pos X du centre de la route
                        Point2D pRoute = this.route.getPointRoutePara(obs.getPos());
                        //obs.savePosXCenterRoute((float) pRoute.getX());
                        //Range route
                        int newRange = this.aff.bmg.getRange(obs.getPos());
                        float oldRange = obs.getSavedRangeRoute();
                        float facRange;
                        if(oldRange != 0){
                            //facRange = newRange / oldRange;
                            facRange = newRange - oldRange;
                        } else {
                            facRange = 2;
                        }

                        obs.saveRangeRoute(newRange);
                        //Dist to route
                        obs.updateDistToRoute(facRange);

                        //Mod obstacle, deplacement sur l axe Y
                        obs.move(modPos*modVitesse);
                    }

                } finally {
                    this.aff.bmg.obstacleMutex.unlock();
                }


                //Concurrents

                ArrayList<Concurrent> listConcurrents = this.aff.vueUser.getConcurrents();

                try {
                    this.aff.vueUser.concurrentMutex.lock();

                    for(Concurrent c : listConcurrents){
                        //Ralentissement par User
                        c.slowDown((float) (modPos*modVitesse));

                        //Vitesse concurrent
                        if(c.getPosY() > VueBackground.horizon && c.getPosY() < Affichage.HAUTEUR){
                            this.calculVitObj(c);
                        } else { //Si User est au dessus de l horizon, sa vitesse max est 10
                            if(c.getVitesse()>10){
                                c.modVitesse(-1);
                            }
                        }
                        double modPosC = this.calcul_dPos(c.getVitesse(), this.varTime);
                        c.moveUp((float) (modPosC*modVitesse));

                    }

                } finally {
                    this.aff.vueUser.concurrentMutex.unlock();
                }




                /*----------------Collision--------------*/

                //TODO obstacles & concurrents
                //Deceleration selon obstacle :


                double decObs = -30; //Pour les obstacles
                double decConc = -20; //Pour les concurrents

                //Test collision obstacles -> Diminue vitesse, pas de test fin de jeu
                try {
                    this.aff.bmg.obstacleMutex.lock();

                    for(Obstacle obs : listObstacles){
                        //Si collision entre voiture et obstacle
                        if(Tools.collision(this.user.getHitBox(), obs.getHitBox())){
                            //Decelere User
                            this.user.modVitesse(decObs);
                            //Rebond de user de l autre cote de l obstacle
                            this.user.rebond(2, ! obs.isRightPoint(this.user.getPos()));
                        }
                    }

                } finally {
                    this.aff.bmg.obstacleMutex.unlock();
                }


                try {
                    this.aff.vueUser.concurrentMutex.lock();

                    for(Concurrent c : listConcurrents){
                        if(this.user.collision(c)){
                            this.user.modVitesse(decConc);
                            boolean isRight = this.user.getPosX()>c.getPosX();
                            this.user.rebond(2, isRight);
                            c.rebond(3, ! isRight);
                            Audio.jingleCollision.play();

                        }
                        if(! c.isHS()){
                            if(c.getPosY()>this.user.getPosY()){
                                c.goHS();
                                Data.addScore(scoreConcurrent);
                                this.ctrl.aff.vueUser.writeMessage("+"+scoreConcurrent);

                                //Ne s entends que si assez proche
                                if(Tools.distance(this.user.getPos(), c.getPos())<distConcForVroom){
                                    Audio.jingleOvertakeCar.play();
                                }

                            }
                        }
                    }

                } finally {
                    this.aff.vueUser.concurrentMutex.unlock();
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
