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

    //Decors

    //Images nuages :
    private static BufferedImage cloud1;
    private static BufferedImage cloud2;
    private static BufferedImage cloud3;
    private static BufferedImage cloud4;

    //Images montagnes
    private static BufferedImage mountain;

    //Image herbe
    private static BufferedImage grass;

    // Obstacles
    private static BufferedImage rock1;
    private static BufferedImage rock2;
    private static BufferedImage rock3;
    private static BufferedImage rock4;
    private static BufferedImage tree1;
    private static BufferedImage tree2;
    private static BufferedImage tree3;
    private static BufferedImage rockground1;
    private static BufferedImage rockground2;


    //User
    private static HashMap<Integer, BufferedImage> listStatesUser;

    //Concurrents
    private static HashMap<Integer, BufferedImage> concurrent1;
    private static HashMap<Integer, BufferedImage> concurrent2;
    private static HashMap<Integer, BufferedImage> concurrent3;


    //Menu
    private static BufferedImage curseur;
    private static ImageIcon iconCurseur;


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
        initObstacles();
    }


    /**
     * Initialise l image des nuages et de la montagne
     */
    private static void initClouds_Montain(){
        //Nuages
        cloud1 = Tools.getBIfromPath("src/Sprites/Decors/Nuages/cloud1.png");
        cloud2 = Tools.getBIfromPath("src/Sprites/Decors/Nuages/cloud2.png");
        cloud3 = Tools.getBIfromPath("src/Sprites/Decors/Nuages/cloud3.png");
        cloud4 = Tools.getBIfromPath("src/Sprites/Decors/Nuages/cloud4.png");
        //Scale, changer la taille des images
        double scaleX = 0.5;
        double scaleY = 0.5;
        cloud1 = Tools.scaleBI(cloud1, scaleX, scaleY); //Au cas ou l image soit trop grande.
        cloud2 = Tools.scaleBI(cloud2, scaleX, scaleY);
        cloud3 = Tools.scaleBI(cloud3, scaleX, scaleY);
        cloud4 = Tools.scaleBI(cloud4, scaleX, scaleY);

        //Montagne
        mountain = Tools.getBIfromPath("src/Sprites/Decors/mountains.png");
        mountain = Tools.scaleBI(mountain, 0.5, 0.5);

        //Herbe
        grass = Tools.getBIfromPath("src/Sprites/Decors/grass.png");
    }

    private static void initUsers(){
        // Dessins du user
        BufferedImage userStraight = Tools.getBIfromPath("src/Sprites/User/user.png");
        BufferedImage userLeft = Tools.getBIfromPath("src/Sprites/User/userleft.png");
        BufferedImage userRight = Tools.getBIfromPath("src/Sprites/User/userright.png");
        userStraight = Tools.scaleBI(userStraight, scaleUser, scaleUser);
        userLeft = Tools.scaleBI(userLeft, scaleUser, scaleUser);
        userRight = Tools.scaleBI(userRight, scaleUser, scaleUser);

        listStatesUser = new HashMap<>();
        listStatesUser.put(0, userStraight);
        listStatesUser.put(-1, userLeft);
        listStatesUser.put(1, userRight);

        //Concurrent1
        BufferedImage c1Straight = Tools.getBIfromPath("src/Sprites/Concurrents/c1.png");
        BufferedImage c1Left = Tools.getBIfromPath("src/Sprites/Concurrents/c1left.png");
        BufferedImage c1Right = Tools.getBIfromPath("src/Sprites/Concurrents/c1right.png");
        c1Straight = Tools.scaleBI(c1Straight, scaleUser, scaleUser);
        c1Left = Tools.scaleBI(c1Left, scaleUser, scaleUser);
        c1Right = Tools.scaleBI(c1Right, scaleUser, scaleUser);

        concurrent1 = new HashMap<>();
        concurrent1.put(0, c1Straight);
        concurrent1.put(-1, c1Left);
        concurrent1.put(1, c1Right);


        //Concurrent2
        BufferedImage c2Straight = Tools.getBIfromPath("src/Sprites/Concurrents/c2.png");
        BufferedImage c2Left = Tools.getBIfromPath("src/Sprites/Concurrents/c2left.png");
        BufferedImage c2Right = Tools.getBIfromPath("src/Sprites/Concurrents/c2right.png");
        c2Straight = Tools.scaleBI(c2Straight, scaleUser, scaleUser);
        c2Left = Tools.scaleBI(c2Left, scaleUser, scaleUser);
        c2Right = Tools.scaleBI(c2Right, scaleUser, scaleUser);

        concurrent2 = new HashMap<>();
        concurrent2.put(0, c2Straight);
        concurrent2.put(-1, c2Left);
        concurrent2.put(1, c2Right);

        //Concurrent3
        BufferedImage c3Straight = Tools.getBIfromPath("src/Sprites/Concurrents/c3.png");
        BufferedImage c3Left = Tools.getBIfromPath("src/Sprites/Concurrents/c3left.png");
        BufferedImage c3Right = Tools.getBIfromPath("src/Sprites/Concurrents/c3right.png");
        c3Straight = Tools.scaleBI(c3Straight, scaleUser, scaleUser);
        c3Left = Tools.scaleBI(c3Left, scaleUser, scaleUser);
        c3Right = Tools.scaleBI(c3Right, scaleUser, scaleUser);

        concurrent3 = new HashMap<>();
        concurrent3.put(0, c3Straight);
        concurrent3.put(-1, c3Left);
        concurrent3.put(1, c3Right);


    }

    private static void initCurseur(){
        curseur = Tools.getBIfromPath("src/Sprites/Bienvenue/curseur.png");
        curseur = Tools.deepCopy(curseur);
        curseur = Tools.getResizedImage(curseur, 20, 20);

        iconCurseur = new ImageIcon(((new ImageIcon("src/Sprites/Bienvenue/curseur.png")).getImage()).getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
    }

    public static void initObstacles(){
        rock1 = Tools.getBIfromPath("src/Sprites/Obstacles/rock1.png");
        rock2 = Tools.getBIfromPath("src/Sprites/Obstacles/rock2.png");
        rock3 = Tools.getBIfromPath("src/Sprites/Obstacles/rock3.png");
        rock4 = Tools.getBIfromPath("src/Sprites/Obstacles/rock4.png");
        tree1 = Tools.getBIfromPath("src/Sprites/Obstacles/tree1.png");
        tree2 = Tools.getBIfromPath("src/Sprites/Obstacles/tree2.png");
        tree3 = Tools.getBIfromPath("src/Sprites/Obstacles/tree3.png");
        rockground1 = Tools.getBIfromPath("src/Sprites/Obstacles/rockground1.png");
        rockground2 = Tools.getBIfromPath("src/Sprites/Obstacles/rockground2.png");
        double scaleXrock = 0.5;
        double scaleYrock = 0.5;
        rock1 = Tools.scaleBI(rock1, scaleXrock, scaleYrock);
        rock2 = Tools.scaleBI(rock2, scaleXrock, scaleYrock);
        rock3 = Tools.scaleBI(rock3, scaleXrock, scaleYrock);
        rock4 = Tools.scaleBI(rock4, scaleXrock, scaleYrock);
        double scaleXtree = 0.5;
        double scaleYtree = 0.5;
        tree1 = Tools.scaleBI(tree1, scaleXtree, scaleYtree);
        tree2 = Tools.scaleBI(tree2, scaleXtree, scaleYtree);
        tree3 = Tools.scaleBI(tree3, scaleXtree, scaleYtree);

        rockground1 = Tools.scaleBI(rockground1, scaleXrock, scaleYrock);
        rockground2 = Tools.scaleBI(rockground2, scaleXrock, scaleYrock);

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
     * Renvoie l image de l'herbe
     * @return
     */
    public static BufferedImage getGrass(){
        return Tools.deepCopy(grass);
    }

    /**
     * Renvoie la liste des etats et des images pour User
     * @return
     */
    public static HashMap<Integer, BufferedImage> getListStateUser(){
        return (HashMap<Integer, BufferedImage>) listStatesUser.clone();
    }

    /**
     * Renvoie la liste des stats et des images d un concurrent en fonction de son type
     * @param type
     * @return
     */
    public static HashMap<Integer, BufferedImage> getConcurrentImg(int type){
        HashMap<Integer, BufferedImage> res;
        if(type == 1){
            res = concurrent1;
        } else if(type == 2){
            res = concurrent2;
        } else {
            res = concurrent3;
        }
        return (HashMap<Integer, BufferedImage>) res.clone();
    }

    /**
     * Recupere les images d'obstacles
     * @param obstacleType
     * @return
     */
    public static BufferedImage getObstacleimg(int obstacleType){
        BufferedImage modeleImg = null;
        if (obstacleType == 0) {
            modeleImg = rock1;
        } else if (obstacleType == 1) {
            modeleImg = rock2;
        } else if (obstacleType == 2) {
            modeleImg = rock3;
        } else if (obstacleType == 3) {
            modeleImg = rock4;
        } else if (obstacleType == 4) {
            modeleImg = tree1;
        } else if (obstacleType == 5) {
            modeleImg = tree2;
        } else if (obstacleType == 6) {
            modeleImg = tree3;
        } else if (obstacleType == 7) {
            modeleImg = rockground1;
        } else {
            modeleImg = rockground2;
        }
            return Tools.deepCopy(modeleImg);
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
