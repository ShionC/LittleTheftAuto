package Tools;

import java.time.Duration;
import java.time.Instant;

public class Chrono {

    private Instant startChrono;
    private Instant startPause;


    private Duration count;
    /**Le calcul de temps se fait**/
    private boolean running;
    /**Le chrono a ete stoppe**/
    private boolean stopped;

    /**
     * Un chronometre qui calcule le temps ecoule. Il commence avec start
     * <br/> Le chronometre peut se mettre en pause.
     */
    public Chrono(){

    }

    /**
     * Demarre le chrono
     */
    public void start(){
        this.startChrono = Instant.now();
        this.running = true;
        this.stopped = false;
    }

    /**
     * Arrete definitivement le chrono
     */
    public void stop(){
        this.count = Duration.between(this.startChrono, Instant.now());
        this.running = false;
        this.stopped = true;
    }

    /**
     * Met en pause le chrono.
     * <br/>Si le chrono a deja ete arrete, il ne se passe rien
     */
    public void pause(){
        if(! this.stopped){
            this.startPause = Instant.now();
            this.count = Duration.between(this.startChrono, Instant.now());
            this.running = false;
        }

    }

    /**
     * Reprend le chrono apres une pause.
     * <br/>Si le chrono a deja ete arrete, il ne se passe rien
     */
    public void resume(){
        if(!this.stopped){
            if(this.running){
                throw new RuntimeException("Chrono not paused");
            } else {
                this.running = true;
                Instant restart = Instant.now();
                long pause = Duration.between(this.startPause, restart).toSeconds();
                this.startChrono = this.startChrono.plusSeconds(pause);
            }
        }

    }

    /**
     * Renvoie la duree du chrono
     * @return
     */
    public Duration getElapsedTime(){
        if(running){
            this.count = Duration.between(this.startChrono, Instant.now());
        }
        return this.count;
    }


}
