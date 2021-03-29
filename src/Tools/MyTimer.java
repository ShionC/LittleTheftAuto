package Tools;

import java.time.Duration;
import java.time.Instant;

/**
 * Cree un timer qui se declanche des son initialisation.
 * Le timer permet de gerer un decompte de temps
 * @author Mathilde LASSEIGNE
 */
public class MyTimer {

    /**Le debut du timer**/
    private Instant startTimer;

    /**Le temps que le timer prends**/
    private long time;

    /**Le debut de la pause**/
    private Instant startPause;

    /**Est en pause**/
    private boolean pause;


    /**
     * Cree un timer qui se declanche des son initialisation.
     * Le timer permet de gerer un decompte de temps
     * @param sec le temps que dure le timer <b>en secondes</b>
     */
    public MyTimer(long sec){
        this.startTimer = Instant.now();
        this.time = sec;
        this.pause = false;
    }


    /**
     * Met en pause le timer, le decompte ne se fait plus.
     * <br/>Le timer peut etre repris par restart()
     */
    public void pause(){
        this.pause = true;
        this.startPause = Instant.now();
    }

    /**
     * Reprends le timer apres une pause, au moment ou il est etait avant la pause.
     */
    public void resume(){
        if(!this.pause){
            throw new RuntimeException("Timer not paused");
        } else {
            this.pause = false;
            Instant restart = Instant.now();
            long pause = Duration.between(this.startPause, restart).toSeconds();
            this.startTimer = this.startTimer.plusSeconds(pause);//Ajoute la longueur de la pause au temps initial
        }
    }

    /**
     * Verifie si le timer est en pause
     * @return true si le timer est en pause, false sinon
     */
    public boolean isPaused(){
        return this.pause;
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
        Duration d;
        if(pause){
            d = Duration.between(this.startTimer, this.startPause);
        } else {
            d = Duration.between(this.startTimer, Instant.now());
        }
        return d.toSeconds() >= this.time;
    }


    /**
     * Renvoie le temps restant au timer
     * @return le temps restant sous forme de Duration
     */
    public Duration getLeftoverTime(){
        Duration d;
        if(pause){
            d = Duration.between(this.startPause, this.startTimer.plusSeconds(this.time));
        } else {
            d = Duration.between(Instant.now(), this.startTimer.plusSeconds(this.time));
        }
        return d;
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
