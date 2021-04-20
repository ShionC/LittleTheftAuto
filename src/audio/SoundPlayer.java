package audio;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.*;

/*----------This class is made with the JLayer library, an add-on from Swing------------*/

/**
 * Create a sound player wich play a unique sound.
 * <br/>This sound can be paused, resumed, stopped, reinitialised and looped.
 * <br/>Using this class need the JLayer library
 * @author Mathilde LASSEIGNE
 */
public class SoundPlayer {

    /**Main stop flag of the class**/
    private boolean playing = true;

    private FileInputStream fileInputStream;
    private BufferedInputStream bufferedInputStream;
    private File myFile = null;

    private long totalLength;
    private long pause;

    /**Main componment of sound**/
    private Player player;

    private PlayingAudio runningSound = null;

    /**Is paused by the pause method**/
    private boolean enPause = false;
    /**Is stopped by the stop method**/
    private boolean stopped = false;
    /**The sound is looping**/
    private boolean looping;

    /**
     * Create a sound player wich play a unique sound.
     * <br/>This sound can be paused, resumed, stopped and reinitialised.
     * @param file the file countaining the sound
     * @param looping is the sound looping ?
     */
    public SoundPlayer(File file, boolean looping){
        init(file, looping);
    }

    /**
     * Create a sound player wich play a unique sound.
     * <br/>This sound can be paused, resumed, stopped and reinitialised.
     * @param path the path from where the file can be extracted
     * @param looping is the sound looping ?
     */
    public SoundPlayer(String path, boolean looping){
        File f = new File(path);
        init(f, looping);
    }

    /**
     * Create a sound player wich play a unique sound.
     * <br/>This sound can be paused, resumed, stopped and reinitialised.
     * <br/> By default, the sound isnt looping.
     * @param file the file countaining the sound
     */
    public SoundPlayer(File file){
        init(file, false);
    }

    /**
     * Create a sound player wich play a unique sound.
     * <br/>This sound can be paused, resumed, stopped and reinitialised.
     * <br/> By default, the sound isnt looping.
     * @param path the path from where the file can be extracted
     */
    public SoundPlayer(String path){
        File f = new File(path);
        init(f, false);
    }

    /**
     * Init
     * @param file
     * @param looping
     */
    private void init(File file, boolean looping){
        this.looping = looping;
        this.myFile = file;
    }

   /* private File getFile(String pathName){
        return getClass().getClassLoader().getResourceAsStream(pathName);
    }


    */



    /**
     * Stop definitively the SoundPlayer
     */
    public void close() {
        if (player != null) player.close();
        if(runningSound != null) runningSound.stopRun();
        this.playing = false;
    }

    /**
     * Stop the sound currently playing.
     * <br/>Stopping after a pause make <i>resume()</i> ineffective
     */
    public void stop(){
        if(player!=null){
            player.close();
            stopped = true;
        }
    }

    /**
     * Pause the sound currently playing.
     * <br/>Can be resumed with <i>resume()</i>
     */
    public void pause(){
        if(playing){
            if(player!=null && !this.enPause && !this.stopped){
                try {
                    pause=fileInputStream.available();
                    player.close();
                    this.enPause = true;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * Check if the sound is paused
     * @return
     */
    public boolean isPaused() {
        return enPause;
    }

    /**
     * Check if the sound is playing, meaning it is neither stopped nor paused nor completed.
     * @return
     */
    public boolean isSoundPlaying(){
        return !enPause && !stopped && !isCompleted();
    }

    /**
     * Check if the sound is completed.
     * <br/> If looping, always false
     * @return
     */
    public boolean isCompleted(){
        if(player != null && !looping){
            return player.isComplete();
        }
        return false;
    }

    /**
     * Return the main componment of the sound
     * @return
     */
    public Player getPlayer(){
        return this.player;
    }

    /**
     * Play the sound from the begining.
     */
    public void play(){
        if(playing){
            stopped = false;
            enPause = false;
            if(runningSound != null){
                runningSound.stopRun();
            }

            runningSound = new PlayingAudio(false, looping);

            runningSound.start();
        }

    }

    /**
     * Resume the sound currently playing where it was stopped.
     */
    public void resume(){
        if(this.playing){
            if(enPause && !stopped){

                if(runningSound != null){
                    runningSound.stopRun();
                }
                runningSound = new PlayingAudio(true, looping);

                runningSound.start();

                this.enPause = false;
            }
        }


    }



    /*-----------------------------------------Classe interne-----------------------------------------------------*/





    private class PlayingAudio extends Thread{

        private boolean restart;
        /**Flag to stop the thread**/
        private boolean run = true;

        private boolean looping;

        /**
         * Allow looping sounds and restarting from a point
         * @param isRestarting is the sound resuming
         * @param looping must the sound loop ?
         */
        public PlayingAudio(boolean isRestarting, boolean looping){
            restart = isRestarting;
            this.looping = looping;
        }

        /**
         * Stop the run. This operation is definitive
         */
        public void stopRun(){
            this.run = false;
            if (player != null){
                player.close();
            }
        }

        @Override
        public void run() {
            try {
                do {
                    if(player != null){
                        player.close();
                    }
                    fileInputStream=new FileInputStream(myFile);
                    bufferedInputStream = null;
                    bufferedInputStream=new BufferedInputStream(fileInputStream);
                    player=new Player(bufferedInputStream);
                    if(restart){
                        fileInputStream.skip(totalLength-pause);
                    } else {
                        totalLength=fileInputStream.available();
                    }
                    player.play();
                    if(restart)
                        restart = false;
                } while (this.looping && !enPause && !stopped && this.run);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JavaLayerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) { System.out.println(e); }
        }
    }
}
