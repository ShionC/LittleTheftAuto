package model;

import java.time.Duration;
import java.time.Instant;

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

    /**Le debut du jeu**/
    private static Instant startGame;

    /**Le debut de la partie**/
    private static Instant startPartie;
    /**La duree jouee**/
    private static Duration durationGame;
    /**Le plus grand temps passe sur une partie**/
    private static Duration highestDurationPartie;
    /**Le temps de la partie en cours**/
    private static Duration currentDurationPartie;

    public Data(){

    }

    /**
     * Compare les data de la partie en cours avec les meilleurs
     */
    public static void push(){
        currentDurationPartie = Duration.between(startPartie, Instant.now());
        if(currentDurationPartie.compareTo(highestDurationPartie)>0){
            highestDurationPartie = currentDurationPartie;
        }
        durationGame = Duration.between(startGame, Instant.now());

        if(currentScore>highestScore){
            highestScore = currentScore;
        }
        if(currentNbCtrlPt>highestNbCtrlPt){
            highestNbCtrlPt = currentNbCtrlPt;
        }
    }

    /**
     * Initialise une nouvelle partie
     */
    public static void newPartie(){
        nbParties ++;
        startPartie = Instant.now();
        currentScore = 0;
        currentNbCtrlPt = 0;
    }

    public static void initGame(){
        startGame = Instant.now();
        startPartie = Instant.now();
        highestDurationPartie = Duration.between(Instant.now(),Instant.now());
        currentDurationPartie = Duration.between(Instant.now(),Instant.now());
        nbParties = 1;
    }

    /***
     * Renvoie le meilleur score
     * @return
     */
    public static int getHighestScore(){
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
        return currentDurationPartie;
    }

    /**
     * Renvoie la duree jouee
     * @return
     */
    public static Duration getDurationGame() {
        return durationGame;
    }
}
