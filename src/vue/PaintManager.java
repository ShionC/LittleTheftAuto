package vue;

/**
 * Cree une classe appelant repaint() de l affichage principal toutes les timeLapse milliseconds.
 */
public class PaintManager implements Runnable {

    /**L affichage principal**/
    private Affichage aff;

    /**La thread principale, repaint() l affichage toutes les timeLapse**/
    private Thread thread;

    /**Flag to stop the thread**/
    private boolean run = true;

    /**Le temps ecoule entre 2 affichages**/
    private final int timeLapse = 5;

    /**
     * Cree une classe appelant repaint() de l affichage principal toutes les timeLapse milliseconds.
     * @param aff Affichage principal
     */
    public PaintManager(Affichage aff){
        this.aff = aff;
        this.thread = new Thread(this);
    }

    /**
     * Stop the run. This operation is definitive
     */
    public void stopRun(){
        this.run = false;
    }

    /**
     * Begin the execution of the thread
     */
    public void start(){
        this.thread.start();
    }

    /**
     * Appelle la methode repaint() de l affichage
     */
    public void repaint(){
        this.aff.repaint();
    }

    @Override
    public void run() {
        while(run){
            if(this.aff.partieEnCours){

                repaint();

            }
            try {
                //noinspection BusyWait
                Thread.sleep(timeLapse);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
