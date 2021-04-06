package vue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Cree une classe appelant repaint() de l affichage principal toutes les timeLapse milliseconds.
 */
public class PaintManager implements Runnable {

    /**L affichage principal**/
    private Affichage aff;

    /**La thread principale, repaint() l affichage toutes les timeLapse**/
    private Thread thread;
    /**Timer principal, repaint() l affichage toutes les timeLapse**/
    private Timer timer;

    /**Flag to stop the thread**/
    private boolean run = true;

    /**Le temps ecoule entre 2 affichages**/
    private final int timeLapse = 5;

    /**Mutex de paint**/
    private final ReentrantLock paintMutex = new ReentrantLock();

    /**
     * Cree une classe appelant repaint() de l affichage principal toutes les timeLapse milliseconds.
     * @param aff Affichage principal
     */
    public PaintManager(Affichage aff){
        this.aff = aff;
        this.thread = new Thread(this);
        //initTimer();
    }

    /**
     * Initialise le timer
     */
    private void initTimer(){
        this.timer = new Timer(timeLapse, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaintHorsSwing();
            }
        });
    }

    /**
     * Stop the run. This operation is definitive
     */
    public void stopRun(){
        this.run = false;
        //this.timer.stop();
    }

    /**
     * Begin the execution of the thread
     */
    public void start(){
        this.thread.start();
        //this.timer.start();
    }

    /**
     * Appelle la methode repaint() de l affichage
     * <br/>Protege par un mutex
     */
    public synchronized void repaint(){
        boolean withSwing = true;
        if(withSwing){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    repaintHorsSwing();
                }
            });
        } else {
            repaintHorsSwing();
        }

    }

    /**
     * Methode repaint mais hors de Swing.invokeLater
     */
    private void repaintHorsSwing(){
        try{
            this.paintMutex.lock();
            this.aff.repaint();
        } finally {
            this.paintMutex.unlock();
        }
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
