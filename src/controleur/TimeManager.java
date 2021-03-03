package controleur;

import Tools.Tools;
import Tools.MyTimer;
import model.Data;
import model.Route;
import model.User;

public class TimeManager extends Thread {

    /**Flag d arret du thread**/
    private boolean run = true;


    private Controleur ctrl;

    private Route route;


    /**Timer du point de controle**/
    private MyTimer timerPtCtrl;



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
        newPartie();
    }

    /**
     * Commence la partie en initialisant la route et user.
     * <br/> Permet a run de faire les calculs necessaire de la partie
     */
    void newPartie(){
        this.route = ctrl.route;
        this.createNewPtCtrl();
    }



    /**
     * Arrete l execution du run. A utiliser pour arreter le thread
     */
    public void stopRun(){
        this.run = false;
    }





    /**
     * Debute une partie, commence le timer de la course en creant un nouveau point de controle
     */
    public void startPartie(){
        this.createNewPtCtrl();
    }

    /**
     * Renvoie le timer du point de controle en cours
     * @return
     */
    public MyTimer getTimerPtCtrl() {
        return timerPtCtrl;
    }

    /**
     * Cree un nouveau point de controle sur la route et met en route le timer this.timerPtCtrl
     */
    private void createNewPtCtrl(){

        double modVitesse = this.ctrl.deplace.modVitesse;
        int distancePtCtrl = 0;
        long timer = 0;
        double marge = 40; //La marge que on laisse a user pour qu il n ai pas besoin de toujours aller a vitesse max
        long bonus = 0; //Bonus de temps
        //En considerant la vitesse max de user et la modification de la vitesse appliquee selon la formule x = v*t
        /*
        distancePtCtrl = Tools.rangedRandomInt(10000, 15000); //Random
        timer = (long) (distancePtCtrl/((User.vitesseMax - marge)*modVitesse)) + bonus; //+2 sec bonus
         */
        timer = Tools.rangedRandomInt(15, 45); //Random
        distancePtCtrl = (int) Math.round(((User.vitesseMax - marge)*modVitesse) * timer);
        timer += bonus; //+2 sec bonus

        int valueCtrl = distancePtCtrl/100; //Facteur arbitraire
        int posYUser = this.ctrl.user.getPosY();
        distancePtCtrl = posYUser - distancePtCtrl;

        this.route.newPtControle(distancePtCtrl, valueCtrl);
        this.timerPtCtrl = new MyTimer(timer);
    }

    @Override
    public void run() {
        while(run){

            if(this.ctrl.partieEnCours){

                if(this.timerPtCtrl.isOver()){
                    if(this.ctrl.user.getPosY() <= this.route.getCtrl()){ //Si user depasse le point de controle
                        Data.addScore(this.route.getValueCtrl());
                        Data.addCtrlPt();
                        this.ctrl.aff.vueUser.writeMessage("+"+this.route.getValueCtrl());
                        this.createNewPtCtrl();
                        //Augmente score de user ?
                    } else {
                        //Termine Fin de la partie
                        this.ctrl.endPartie(2);
                        //System.out.println("Course terminee, partie perdue !!");
                    }
                } else {
                    if(this.ctrl.user.getPosY() <= this.route.getCtrl()){ //Si user depasse le point de controle
                        Data.addScore(this.route.getValueCtrl());
                        Data.addCtrlPt();
                        this.ctrl.aff.vueUser.writeMessage("+"+this.route.getValueCtrl());
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
