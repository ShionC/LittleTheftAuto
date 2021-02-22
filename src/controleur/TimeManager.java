package controleur;

import game.Tools;
import model.Route;
import model.User;
import vue.Affichage;
import vue.VueUser;

import java.time.Duration;
import java.time.Instant;

public class TimeManager extends Thread {

    /**Flag d arret du thread**/
    private boolean run = true;

    /**
     * La partie est en cours
     */
    private boolean inPartie = false;

    private Controleur ctrl;

    private Route route;

    /**Moment de depart du jeu**/
    private Instant startGame;

    /**Timer du point de controle**/
    private Timer timerPtCtrl;



    /**
     *
     * @param route
     */
    /**
     * Gere les points de controles et le deroule du temps.
     * <br/> Gere egalement la fin de partie dans le cas ou le timer est fini
     * @param ctrl le controleur principal
     */
    public TimeManager(Controleur ctrl){
        this.ctrl = ctrl;
        this.startGame = Instant.now();
        newPartie();
    }

    /**
     * Commence la partie en initialisant la route et user.
     * <br/> Permet a run de faire les calculs necessaire de la partie
     */
    void newPartie(){
        this.route = ctrl.route;
        this.inPartie = true;
        this.createNewPtCtrl();
    }

    /**
     * Termine la partie, ne fais plus aucune modification
     */
    void endPartie(){
        this.inPartie = false;
    }


    /**
     * Arrete l execution du run. A utiliser pour arreter le thread
     */
    public void stopRun(){
        this.run = false;
    }





    /**
     * Debute une partie, commence le timer de la course
     */
    public void startPartie(){
        this.createNewPtCtrl();
    }

    /**
     * Renvoie le timer du point de controle en cours
     * @return
     */
    public Timer getTimerPtCtrl() {
        return timerPtCtrl;
    }

    /**
     * Cree un nouveau point de controle sur la route et met en route le timer this.timerPtCtrl
     */
    private void createNewPtCtrl(){

        double modVitesse = this.ctrl.deplace.modVitesse;
        int distancePtCtrl = Tools.rangedRandomInt(10000, 15000); //Random
        //En considerant la vitesse max de user et la modification de la vitesse appliquee selon la formule x = v*t
        double marge = 40; //La marge que on laisse a user pour qu il n ai pas besoin de toujours aller a vitesse max
        long timer = (long) (distancePtCtrl/((User.vitesseMax - marge)*modVitesse)) + 2; //+2 sec bonus
        int valueCtrl = distancePtCtrl;

        this.route.newPtControle(distancePtCtrl, valueCtrl);
        this.timerPtCtrl = new Timer(timer);
    }

    @Override
    public void run() {
        while(run){

            if(inPartie){

                if(this.timerPtCtrl.isOver()){
                    if(User.posY <= this.route.getCtrl()){ //Si user depasse le point de controle
                        this.createNewPtCtrl();
                        //Augmente score de user ?
                    } else {
                        //Termine Fin de la partie
                        System.out.println("Course terminee, partie perdue !!");
                    }
                } else {
                    if(User.posY <= this.route.getCtrl()){ //Si user depasse le point de controle
                        this.createNewPtCtrl();
                        //Augmente score de user ?
                    }
                }

            }

            try {
                //noinspection BusyWait
                Thread.sleep(20);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
