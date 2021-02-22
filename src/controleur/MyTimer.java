package controleur;

import java.time.Duration;
import java.time.Instant;

public class MyTimer {

    /**Le debut du timer**/
    private Instant startTimer;

    /**Le temps que le timer prends**/
    private long time;


    /**
     * Cree un timer qui se declanche des son initialisation.
     * Le timer permet de gerer un decompte de temps
     * @param sec le temps que dure le timer <b>en secondes</b>
     */
    public MyTimer(long sec){
        this.startTimer = Instant.now();
        this.time = sec;
    }


    /**
     * Renvoie la duree du timer
     * @return la duree du timer <b>en secondes</b>
     */
    public long getTimer(){
        return this.time;
    }

    /**
     * Ajoute le nouveau temps au timer
     * @param sec le temps <b>en secondes</b>
     */
    public void addTime(long sec){
        this.time += sec;
    }

    /**
     * Retire le nouveau temps au timer
     * @param sec le temps <b>en secondes</b>
     */
    public void minusTime(long sec){
        this.time -= sec;
    }

    /**
     * Verifie si le timer est termine
     * @return true si le timer est termine, false sinon
     */
    public boolean isOver(){
        Duration d = Duration.between(this.startTimer, Instant.now());
        return d.toSeconds() >= this.time;
    }


    /**
     * Renvoie le temps restant au timer
     * @return le temps restant sous forme de Duration
     */
    public Duration getLeftoverTime(){
        return Duration.between(Instant.now(), this.startTimer.plusSeconds(this.time));
    }

    /**
     * Renvoie le temps restant du timer sous forme <i>h::min::sec</i>
     * @return un string
     */
    public String toString(){
        Duration d = this.getLeftoverTime();
        int h = d.toHoursPart();
        int m = d.toMinutesPart();
        int sec = d.toSecondsPart();
        String str = "";
        if( h!=0 ) {
            str += String.valueOf(h) + "::";
        }
        if( m!=0 ) {
            str += String.valueOf(m) + "::";
        }
        if( sec!=0 ) {
            str += String.valueOf(sec);
        }
        return str;
    }

    /**
     * Renvoie le temps restant du timer sous forme <i><b>x</b>h <b>x</b>min <b>x</b>sec</i>
     * @return un string
     */
    public String toStringVerbose(){
        Duration d = this.getLeftoverTime();
        int h = d.toHoursPart();
        int m = d.toMinutesPart();
        int sec = d.toSecondsPart();
        String str = "";
        if( h!=0 ) {
            str += String.valueOf(h) + "h ";
        }
        if( m!=0 ) {
            str += String.valueOf(m) + "min ";
        }
        if( sec!=0 ) {
            str += String.valueOf(sec) + "sec ";
        }
        return str;
    }

}
