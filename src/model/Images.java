package model;

import Tools.Tools;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Banque d images du projet
 */
public class Images {

/*--------------------------------------Images------------------------------------------*/

    //Images nuages :
    static BufferedImage cloud1;
    static BufferedImage cloud2;
    static BufferedImage cloud3;
    static BufferedImage cloud4;


    //Images montagnes
    static BufferedImage mountain;

    //User
    static HashMap<Integer, BufferedImage> listStatesUser;


    //Menu
    static BufferedImage curseur;
    static ImageIcon iconCurseur;


    /*--------------------Scale----------------------------------------*/

    public static final double scaleUser = 0.5;

    /*--------------------------------Init-------------------------------------------------*/

    /**
     * Initialise toutes les images utilisees par le jeu
     */
    public static void initImages(){
        initClouds_Montain();
        initUsers();
        initCurseur();
    }


    /**
     * Initialise l image des nuages et de la montagne
     */
    private static void initClouds_Montain(){
        //Nuages
        cloud1 = Tools.getBIfromPath("src/Sprites/cloud1.png");
        cloud2 = Tools.getBIfromPath("src/Sprites/cloud2.png");
        cloud3 = Tools.getBIfromPath("src/Sprites/cloud3.png");
        cloud4 = Tools.getBIfromPath("src/Sprites/cloud4.png");
        //Scale, changer la taille des images
        double scaleX = 0.5;
        double scaleY = 0.5;
        cloud1 = Tools.scaleBI(cloud1, scaleX, scaleY); //Au cas ou l image soit trop grande.
        cloud2 = Tools.scaleBI(cloud2, scaleX, scaleY);
        cloud3 = Tools.scaleBI(cloud3, scaleX, scaleY);
        cloud4 = Tools.scaleBI(cloud4, scaleX, scaleY);

        //Montagne
        mountain = Tools.getBIfromPath("src/Sprites/mountains.png");
        mountain = Tools.scaleBI(mountain, 0.5, 0.5);
    }

    private static void initUsers(){
        // Dessins du user
        BufferedImage userStraight = Tools.getBIfromPath("src/Sprites/user.png");
        BufferedImage userLeft = Tools.getBIfromPath("src/Sprites/userleft.png");
        BufferedImage userRight = Tools.getBIfromPath("src/Sprites/userright.png");
        userStraight = Tools.scaleBI(userStraight, scaleUser, scaleUser);
        userLeft = Tools.scaleBI(userLeft, scaleUser, scaleUser);
        userRight = Tools.scaleBI(userRight, scaleUser, scaleUser);

        listStatesUser = new HashMap<>();
        listStatesUser.put(0, userStraight);
        listStatesUser.put(-1, userLeft);
        listStatesUser.put(1, userRight);
    }

    private static void initCurseur(){
        curseur = Tools.getBIfromPath("src/Sprites/curseur.png");
        curseur = Tools.deepCopy(curseur);
        curseur = Tools.getResizedImage(curseur, 20, 20);

        iconCurseur = new ImageIcon(((new ImageIcon("src/Sprites/curseur.png")).getImage()).getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    }

    /*--------------------------------------Getters--------------------------------------------------*/


    /**
     * Recupere l image associee au type de cloud
     * @param cloudType
     * @return
     */
    public static BufferedImage getCloudImg(int cloudType){
        BufferedImage modeleImage;
        if (cloudType == 0) {
            modeleImage = cloud1;
        } else if (cloudType == 1) {
            modeleImage = cloud2;
        } else if (cloudType == 2) {
            modeleImage = cloud3;
        } else {
            modeleImage = cloud4;
        }
        return Tools.deepCopy(modeleImage);
    }

    /**
     * Renvoie l image des montagnes
     * @return
     */
    public static BufferedImage getMountain(){
        return Tools.deepCopy(mountain);
    }

    /**
     * Renvoie la liste des etats et des images pour User
     * @return
     */
    public static HashMap<Integer, BufferedImage> getListStateUser(){
        return (HashMap<Integer, BufferedImage>) listStatesUser.clone();
    }

    /**
     * Renvoie le bufferedImage du curseur
     * @return
     */
    public static BufferedImage getCurseur(){
        return Tools.deepCopy(curseur);
    }

    /**
     * Renvoie la version ImageIcon du curseur
     * @return
     */
    public static ImageIcon getIconCurseur() {
        return iconCurseur;
    }
}
