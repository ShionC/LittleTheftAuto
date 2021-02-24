package controleur;

import vue.OutsideScreen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Accueil implements KeyListener {

    private Controleur ctrl;

    private OutsideScreen outScreen;

    /**
     * Affiche l ecran de fin de jeu, pas les stats
     */
    public boolean endGame = false;

    public boolean premierEcran;

    public boolean regles = false;

    public boolean stats = false;

    /**Le nombre de choix disponibles a l ecran**/
    private int nbChoix;

    private int currentChoice;

    public Accueil(Controleur ctrl){
        this.ctrl = ctrl;
        this.outScreen = this.ctrl.aff.outScreen;

        this.premierEcran = true;
        this.currentChoice = 1;
        this.outScreen.addKeyListener(this);
        this.outScreen.setAccueil(this);

        this.goToFirstScreen();
    }


    /**
     * Renvoie le choix actuel
     * @return le int correspondant au numero du choix
     */
    public int getCurrentChoice(){
        return this.currentChoice;
    }

    /**
     * Update la vue de l acceuil pour la faire arriver a la fin de la partie.
     */
    public void goToAccueil(){
        this.endGame = true;
        //this.premierEcran = false;
        this.stats = false;
        this.regles = false;
        this.currentChoice = 1;
        this.nbChoix = 4;
        this.outScreen.update();
    }

    public void goToStats(){
        this.stats = true;
        this.endGame = false;
        this.regles = false;
        this.currentChoice = 1;
        this.nbChoix = 1;
        this.outScreen.update();
    }

    public void goToRegles(){
        this.regles = true;
        this.endGame = false;
        this.stats = false;
        this.currentChoice = 1;
        this.nbChoix = 1;
        this.outScreen.update();
    }

    public void goToFirstScreen(){
        this.premierEcran = true;
        this.endGame = false;
        this.stats = false;
        this.regles = false;
        this.currentChoice = 1;
        this.nbChoix = 3;
        this.outScreen.update();
    }

    /**
     * Selectionne le choix suivant dans le menu pause
     * @param after true si le choix est le suivant, false si il s agit du precedent
     */
    private void nextChoice(boolean after){
        if(after){
            if(this.currentChoice >= this.nbChoix){
                this.currentChoice = 1;
            } else {
                this.currentChoice++;
            }
        } else {
            if(this.currentChoice <= 1){
                this.currentChoice = this.nbChoix;
            } else {
                this.currentChoice--;
            }
        }
        this.outScreen.update();
    }

    /**
     * Effectue l action correspondant au choix actuel
     * <br/><b>Si endGame</b>
     * <br/><u>Choix 1 :</u> Reprendre la partie
     * <br/><u>Choix 2 :</u> Voir les stats
     * <br/><b>Si stats</b>
     *<br/><u>Choix 1 :</u> Revenir sur l ecran d acceuil
     */
    private void actionChoice(){
        if(this.premierEcran == false){
            System.out.println("Go to endGame screen");
            //this.goToAccueil();

        }
        if(this.endGame){
            if(this.currentChoice == 1){
                this.ctrl.newPartie();
                this.ctrl.startPartie();
            } else if(this.currentChoice == 2){
                this.goToRegles();
            } else if(this.currentChoice == 3){
                this.goToStats();
            } else if(this.currentChoice == 4){
                this.ctrl.aff.fenetre.dispose();
                System.exit(0);
            }
        } else if(this.premierEcran && !this.regles){
            if(this.currentChoice == 1){
                this.premierEcran = false;
                //this.ctrl.newPartie();
                this.ctrl.startPartie();
            } else if(this.currentChoice == 2){
                this.goToRegles();
            } else if(this.currentChoice == 3){
                this.ctrl.aff.fenetre.dispose();
                System.exit(0);
            }
        } else if(this.stats || this.regles){
            if(this.currentChoice == 1){
                if(premierEcran){
                    this.goToFirstScreen();
                } else {
                    this.goToAccueil();
                }

            }
        }

    }


    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Key typed Accueil");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("Key pressed Accueil");
        if(e.getKeyCode() == KeyEvent.VK_UP){
            System.out.println("UP");
            this.nextChoice(false);
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            System.out.println("DOWN");
            this.nextChoice(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            System.out.println("ENTER");
            this.actionChoice();
        }
        System.out.println("Current choice = "+this.currentChoice);
        System.out.println("Max choice = "+this.nbChoix);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
