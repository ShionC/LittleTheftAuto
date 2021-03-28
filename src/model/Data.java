package model;

import Tools.Chrono;

import java.time.Duration;
import java.time.Instant;

/**
 * Liste de toutes les donnees recoltees lors du jeu.
 * <br/>Classe statique
 */
public class Data {

    /**Le meilleur score**/
    private static int highestScore = 0;
    /**Le score de la partie en cours**/
    private static int currentScore = 0;

    /**Le nombre de parties jouees**/
    private static int nbParties = 0;

    /**Le plus grand nombre de points de controles passes**/
    private static int highestNbCtrlPt = 0;

    /**Le nombre de points de controles passes**/
    private static int currentNbCtrlPt = 0;
    /**La distance parcourue sur la partie en cours**/
    private static int currentKilometrage = 0;
    /**Le kilometrage max atteint**/
    private static int highestKilometrage = 0;


    /**Le debut du jeu**/
    private static Chrono game;

    /**Le debut de la partie**/
    private static Chrono partie;
    /**Le plus grand temps passe sur une partie**/
    private static Duration highestDurationPartie;

    /**Une partie est en cours**/
    private static boolean inPartie;

    public Data(){

    }

    /**
     * Compare les data de la partie en cours avec les meilleurs, et met a jour les statistiques
     */
    public static void push(){
        partie.stop();
        Duration currentDurationPartie = partie.getElapsedTime();
        if(currentDurationPartie.compareTo(highestDurationPartie)>0){
            highestDurationPartie = currentDurationPartie;
        }

        if(currentScore>highestScore){
            highestScore = currentScore;
        }
        if(currentNbCtrlPt>highestNbCtrlPt){
            highestNbCtrlPt = currentNbCtrlPt;
        }

        if(currentKilometrage > highestKilometrage){
            highestKilometrage = currentKilometrage;
        }
        inPartie = false;
    }

    /**
     * Initialise une nouvelle partie
     */
    public static void newPartie(){
        nbParties ++;
        partie = new Chrono();
        partie.start();
        currentScore = 0;
        currentNbCtrlPt = 0;
        inPartie = true;
    }

    /**
     * Initialise le jeu. N est utilise qu une seule fois lors du lancement du jeu
     */
    public static void initGame(){
        game = new Chrono();
        game.start();
        highestDurationPartie = Duration.between(Instant.now(),Instant.now());
        nbParties = 0;
        inPartie = false;
    }

    /***
     * Renvoie le meilleur score
     * @return
     */
    public static int getHighestScore(){
        if(currentScore>highestScore){
            highestScore = currentScore;
        }
        return highestScore;
    }

    /**
     * Renvoie le score actuel
     * @return
     */
    public static int getCurrentScore() {
        return currentScore;
    }

    /**
     * Ajoute au score actuel
     * @param score la valeur a ajouter au score
     */
    public static void addScore(int score){
        currentScore += score;
    }

    /**
     * Renvoie le nombre de parties jouees
     * @return
     */
    public static int getNbParties() {
        return nbParties;
    }

    /**
     * Renvoie le plus grand nombre de points de controles passes
     * @return
     */
    public static int getHighestNbCtrlPt() {
        return highestNbCtrlPt;
    }

    /**
     * Renvoie le nombre de points de controles passes dans la partie actuelle
     * @return
     */
    public static int getCurrentNbCtrlPt() {
        return currentNbCtrlPt;
    }

    /**
     * Ajoute 1 au decompte du nombre de points de controles passes
     */
    public static void addCtrlPt(){
        currentNbCtrlPt++;
    }

    /**
     * Defini le kilometrage de la partie actuelle
     * @param metrage
     */
    public static void setCurrentKilometrage(int metrage){
        currentKilometrage = metrage;
    }

    /**
     * Renvoie le kilometrage de la partie actuelle
     * @return
     */
    public static int getCurrentKilometrage() {
        return currentKilometrage;
    }

    /**
     * Renvoie le kilometrage maximal atteint lors d une partie
     * @return
     */
    public static int getHighestKilometrage() {
        return highestKilometrage;
    }

    /**
     * Met en pause le calcul de temps de la partie
     */
    public static void pausePartie(){
        partie.pause();
    }

    /**
     * Reprends le calcul de temps de la partie
     */
    public static void resumePartie(){
        partie.resume();
    }


    /**
     * Renvoie plus grand temps qu a dure une partie
     * @return
     */
    public static Duration getHighestDurationPartie() {
        return highestDurationPartie;
    }

    /**
     * Renvoie le temps qu a dure la partie en cours
     * @return
     */
    public static Duration getCurrentDurationPartie() {
        return partie.getElapsedTime();
    }

    /**
     * Renvoie la duree jouee
     * @return
     */
    public static Duration getDurationGame() {
        return game.getElapsedTime();
    }
}
