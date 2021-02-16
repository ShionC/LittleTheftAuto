package controleur;

public class KeyContinue extends Thread {

    private Controleur ctrl;

    public enum Direction {LEFT, NOTHING, RIGHT};

    private Direction dir;

    private boolean run = true;

    //Source :
    //https://www.developpez.net/forums/d581407/java/interfaces-graphiques-java/awt-swing/keypressed-supprimer-pause/

    /**
     * Permet la gestion du clavier en continue. Elle elimine la pause de 1.5 sec lors de l appui de touches
     * <br/> Cette classe est celle effectue les actions sur ordre du ontroleur et par l intermediaire de ses methodes.
     * @param ctrl
     */
    KeyContinue(Controleur ctrl){
        this.ctrl = ctrl;
    }

    /**
     * Arrete l execution du run. A utiliser pour arreter le thread
     */
    public void stopRun(){
        this.run = false;
    }

    /**
     * Effectue en continu l action necessaire en fonction de la direction.
     * <br/>Pour arreter cette action il est necessaire d utiliser <i><b>setDir(Direction.RIEN)</b></i>
     * @param dir La direction correspondant a l action a effectuer. Pour rappel Direction E {GAUCHE,RIEN,DROITE}
     */
    public void setDir(Direction dir) {
        this.dir = dir;
    }

    /**
     * Renvoie la direction actuelle utilisee
     * @return
     */
    public Direction getDir() {
        return dir;
    }

    @Override
    public void run() {
        while( run ) {
            if( dir == Direction.LEFT) {
                //System.out.println("Left");
                this.ctrl.move(false);
            }
            else if( dir == Direction.RIGHT) {
                //System.out.println("Right");
                this.ctrl.move(true);
            }


            try { Thread.sleep( 10 ); }
            catch( InterruptedException e ) {
                e.printStackTrace();
            }
        }
    }
}
