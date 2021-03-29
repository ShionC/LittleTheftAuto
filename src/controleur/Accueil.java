package controleur;

import audio.Audio;
import vue.OutsideScreen;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class Accueil implements KeyListener {

    private Controleur ctrl;

    private OutsideScreen outScreen;

    /**
     * La raison pour laquelle la partie a ete arretee
     * O : Partie stopee
     * <br/>1 : Vitesse == 0
     * <br/>2 : PtCtrl non atteint
     * **/
    public int typeGameOver;


    public String keyEndGame = "EndGame";
    public String keyFirstScreen = "FirstScreen";
    public String keyStats = "Stats";
    public String keyRegles = "Regles";
    public String keyCredits = "Credits";


    public HashMap<String, Boolean> menus = new HashMap<>();

    /**Si l ecran d accueil du debut du jeu est toujours valable**/
    public boolean premierEcan;


    /**Le nombre de choix disponibles a l ecran**/
    private int nbChoix;

    private int currentChoice;

    public Accueil(Controleur ctrl){
        this.ctrl = ctrl;
        this.outScreen = this.ctrl.aff.outScreen;

        this.premierEcan = true;
        this.currentChoice = 1;

        //Init menus
        this.menus.put(keyRegles, false);
        this.menus.put(keyStats, false);
        this.menus.put(keyFirstScreen, true);
        this.menus.put(keyEndGame, false);
        this.menus.put(keyCredits, false);

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
     * Met a true le menu correspondant a key dans la HashMap, et false les autres
     * @param key le string correspondant a la cle de modification du menu
     */
    private void selectMenu(String key){
        for (String i : this.menus.keySet()) {
            this.menus.put(i, false);
        }
        this.menus.put(key, true);
    }


    /**
     * Update la vue de l acceuil pour la faire arriver a la fin de la partie.
     */
    public void goToAccueil(){
        this.selectMenu(keyEndGame);
        this.currentChoice = 1;
        this.nbChoix = 5;
        this.outScreen.update();
    }

    public void goToStats(){
        this.selectMenu(keyStats);
        this.currentChoice = 1;
        this.nbChoix = 1;
        this.outScreen.update();
    }

    public void goToRegles(){
        this.selectMenu(keyRegles);
        this.currentChoice = 1;
        this.nbChoix = 1;
        this.outScreen.update();
    }

    /**
     * Montre le premier ecran d accueil, uniquement montre lorsque aucune partie n a ete lancee.
     */
    public void goToFirstScreen(){
        if(this.premierEcan){
            this.selectMenu(keyFirstScreen);
            this.currentChoice = 1;
            this.nbChoix = 4;
            this.outScreen.update();
        }
    }

    public void goToCredits(){
        this.selectMenu(keyCredits);
        this.currentChoice = 1;
        this.nbChoix = 1;
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
        Audio.jingleScrolling.play();
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
        if(this.menus.get(keyEndGame)){
            if(this.currentChoice == 1){
                this.ctrl.newPartie();
                this.ctrl.startPartie();
            } else if(this.currentChoice == 2){
                this.goToRegles();
            } else if(this.currentChoice == 3){
                this.goToStats();
            } else if(this.currentChoice == 4){
                this.goToCredits();
            } else if(this.currentChoice == 5){
                this.ctrl.aff.fenetre.dispose();
                System.exit(0);
            }
        } else if(this.menus.get(keyFirstScreen) && this.premierEcan){
            if(this.currentChoice == 1){
                this.premierEcan = false;
                this.ctrl.startPartie();
            } else if(this.currentChoice == 2){
                this.goToRegles();
            } else if(this.currentChoice == 3){
                this.goToCredits();
            } else if(this.currentChoice == 4){
                this.ctrl.aff.fenetre.dispose();
                System.exit(0);
            }
        } else if(this.menus.get(keyStats) || this.menus.get(keyRegles) || this.menus.get(keyCredits)){
            if(this.currentChoice == 1){
                if(premierEcan){
                    this.goToFirstScreen();
                } else {
                    this.goToAccueil();
                }

            }
        }
        Audio.jingleSelection.play();

    }


    @Override
    public void keyTyped(KeyEvent e) {
        //System.out.println("Key typed Accueil");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("Key pressed Accueil");
        if(e.getKeyCode() == KeyEvent.VK_UP){
            //System.out.println("UP");
            this.nextChoice(false);
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            //System.out.println("DOWN");
            this.nextChoice(true);
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            //System.out.println("ENTER");
            this.actionChoice();
        }
        //System.out.println("Current choice = "+this.currentChoice);
        //System.out.println("Max choice = "+this.nbChoix);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
