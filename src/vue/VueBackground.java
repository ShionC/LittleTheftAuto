package vue;

import controleur.MyTimer;
import game.Tools;
import model.Data;
import model.Obstacle;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.util.concurrent.locks.ReentrantLock;

public class VueBackground {

    // ********************************** 1) Attributs **********************************

    //public static final int horizon = (int) (((float)Affichage.HAUTEUR) * (1/4))+1;
    public static final int horizon = 200;
    ///**La forme des montagnes, ne change pas. Initialisee dans le constructeur**/
    //private Shape montagnes = null;
    /**
     * On initialise la forme de la route en un rectangle
     */
    private Area route = new Area(new Rectangle(Affichage.LARGEUR/4, VueBackground.horizon, Affichage.LARGEUR/2, Affichage.HAUTEUR-VueBackground.horizon)) ;
    /**La liste des coord des nuages**/
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Point> clouds = new ArrayList<>();
    /**Le type de nuage**/
    private ArrayList<Integer> cloudTypes = new ArrayList<>();

    /**La liste de la largeur de la route pour chaque point. <br/>Permet un effet de profondeur**/
    private ArrayList<Integer> rangeroute = new ArrayList<>();
    /**Mutex de rangeRoute**/
    private final ReentrantLock rangeMutex = new ReentrantLock();

    /**
     * La liste des obstacles
     */
    private ArrayList<Obstacle> listObstacles = new ArrayList<>();

    //Images nuages :
    BufferedImage cloud1;
    BufferedImage cloud2;
    BufferedImage cloud3;
    BufferedImage cloud4;
    //Images montagnes
    BufferedImage mountain;

    //Coord montagnes
    /**La position x initiale des montagnes**/
    private final int initXMontagnes = -500;

    /**Le modificateur de deplacement des montagnes**/
    private int modMontagnes = 0;

    private final Affichage aff;

    // ********************************** 2) Constructeur **********************************

    public VueBackground(Affichage aff){
        this.aff = aff;


        //Initialiser montagnes
        // Les creer apartir de l horizon. Le 1er et dernier point de la liste doit etre le meme
        /*int nbPoints = 0;
        int[] tabX = new int[0];
        int[] tabY = new int[0];*/
        /*
        int nbPoints = 10;
        int[] tabX = new int[nbPoints+1];
        int[] tabY = new int[nbPoints+1];
        tabX[0] = 0;
        tabY[0] = horizon;

        int marge = Affichage.LARGEUR / nbPoints+1; //60
        for (int i=1; i<nbPoints; i++) { // Redefinition de tab[0]
            // Tous les 3 points, on revient à la ligne d'horizon.
            if (i%3 == 0) {
                tabY[i] = this.horizon;
            } else {
                tabY[i] = Tools.rangedRandomInt(this.horizon - 150, this.horizon - 10); //A revoir, on ne touche ni le ciel, ni le sol !
            }
            tabX[i] = marge;
            marge += aff.LARGEUR / nbPoints+1;
        }
        tabX[nbPoints] = tabX[0];
        tabY[nbPoints] = tabY[0];

        this.montagnes = new Polygon(tabX, tabY, nbPoints);

        */

        //Initialiser les images des nuages et de la montagne :
            try{
                //Nuages
                File c1 = new File("src/Sprites/cloud1.png");
                File c2 = new File("src/Sprites/cloud2.png");
                File c3 = new File("src/Sprites/cloud3.png");
                File c4 = new File("src/Sprites/cloud4.png");
                cloud1 = ImageIO.read(c1);
                cloud2 = ImageIO.read(c2);
                cloud3 = ImageIO.read(c3);
                cloud4 = ImageIO.read(c4);
                //Scale, changer la taille des images
                double scaleX = 0.5;
                double scaleY = 0.5;
                cloud1 = Tools.scaleBI(cloud1, scaleX, scaleY); //Au cas ou l image soit trop grande.
                cloud2 = Tools.scaleBI(cloud2, scaleX, scaleY);
                cloud3 = Tools.scaleBI(cloud3, scaleX, scaleY);
                cloud4 = Tools.scaleBI(cloud4, scaleX, scaleY);

                //Montagne
                File m = new File("src/Sprites/mountains.png");
                this.mountain = ImageIO.read(m);
                this.mountain = Tools.scaleBI(this.mountain, 0.5, 0.5);
                if(this.mountain == null){
                    System.out.println("        *Image nulle !!!!*");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("l'image n'a pas pu etre lue");
            } catch (Exception e){
                e.printStackTrace();
            }

        //Initialiser nuages
        this.initClouds();
        this.setShapeRoute();


        //Initialiser les obstacles
        //TODO les images
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Cree la forme de la route et met a jour rangeRoute
     * <br/>Met aussi a jour this.route qui donne la forme de la route
     * <br/>A initialiser lors d un changement de la Route, pour eviter de conserver l ancienne
     */
    void setShapeRoute(){

        float rangeInit = 100; //Lorsque y=Affichage.HAUTEUR alors de chaque cote de la route il y a cette valeur
        ArrayList<Point> list = this.aff.route.getRoute();
        synchronized (this.rangeroute){
            try{
                this.rangeMutex.lock();
                //Initialisation de rangeRoute
                ArrayList<Integer> newRange = new ArrayList<>();
                //this.rangeroute.clear();
                for(int i = 0; i<list.size(); i++){
                    Point p = list.get(i);
                    if(p.y < horizon){
                        if(i==0){
                            p.move((Tools.findX(horizon, list.get(i+1), p)), horizon);
                        } else {
                            p.move(Tools.findX(horizon,p,list.get(i-1)), horizon); //N affiche que les points sous l horizon !
                        }

                    } else if(p.y>Affichage.HAUTEUR){
                        if(i == list.size()-1){
                            p.move(Tools.findX(Affichage.HAUTEUR, p, list.get(i-1)), Affichage.HAUTEUR);
                        } else {
                            p.move(Tools.findX(Affichage.HAUTEUR,list.get(i+1),p), Affichage.HAUTEUR); //N affiche que les points dans l ecran !
                        }

                    }
                    //int range = Math.round((rangeInit*(float) p.y)/(float) Affichage.HAUTEUR); //Produit en croix
                    int range = Math.round((rangeInit*(float) (p.y-horizon+50))/(float) (Affichage.HAUTEUR-horizon+50)); //Produit en croix
                    if(range <1){
                        range = 1; //Mettre une largeur minimale de 1 pour pouvoir dessiner un beau polygone
                    }
                    newRange.add(range);
                }
                this.rangeroute = newRange;


            } finally {
                this.rangeMutex.unlock();
            }

        }

        synchronized (this.route){
            //Creation de la route

                    int sizeTab = (list.size()*2)+1;
                    int[] tabX = new int[sizeTab];//+1 pour fermer le polygon
                    int[] tabY = new int[sizeTab];
                    //Initialisation du 1er point
                    Point oldP = list.get(0); //On a toujours olp > p
                    if(oldP.y>Affichage.HAUTEUR){
                        oldP.move(Tools.findX(Affichage.HAUTEUR,list.get(1),oldP), Affichage.HAUTEUR); //N affiche que les points dans l ecran !
                    }
                    tabX[0] = oldP.x-this.rangeroute.get(0);
                    tabX[sizeTab-2] = oldP.x+this.rangeroute.get(0); //Remplir en partant de la fin
                    tabY[0] = oldP.y;
                    tabY[sizeTab-2] = oldP.y;

                    for(int i = 1; i<list.size(); i++){

                        Point p = list.get(i);
                        if(p.y < horizon){
                            p.move(Tools.findX(horizon,p,oldP), horizon); //N affiche que les points sous l horizon !
                        }
                        //Le point 0 est le point en bas a
                        tabX[i] = p.x-this.rangeroute.get(i);
                        tabX[sizeTab-2-i] = p.x+this.rangeroute.get(i); //Remplir en partant de la fin
                        tabY[i] = p.y;
                        tabY[sizeTab-2-i] = p.y;

                        oldP = p;
                    }
                    tabX[sizeTab-1] = tabX[0];//Fermer le polygon
                    tabY[sizeTab-1] = tabY[0];

                    this.route = new Area(new Polygon(tabX,tabY, sizeTab)); //Pour ne pas avoir a calculer plusieurs fois
        }



    }

    /**
     * Renvoie la forme de la route, sans la recalculer
     * @return area
     */
    public Area getShapeRoute(){
        synchronized (this.route){return this.route;}
    }

    /**
     * Renvoie un clone de la distance entre le bord de la route et le milieu de la route pour chaque point correspondant a la route
     * <br/>Protege par un mutex
     * @return range list
     */
    public ArrayList<Integer> getRangeRoute(){
        synchronized (this.rangeroute){
            try{
                this.rangeMutex.lock();
                return (ArrayList<Integer>) this.rangeroute.clone();
            } finally {
                this.rangeMutex.unlock();
            }

        }
    }

    /**
     * Renvoie range au point p sur la route
     * @param p Le point p sur la route (p.x n est pas oblige d etre sur la route)
     * @return -1 si p.y est sortit de la route
     */
    public int getRange(Point p){
        ArrayList<Point> listRoute = this.aff.route.getRoute();
        //Touver le bon point sur le segment de route
        /*
        boolean inRoute = true; //Si l obstacle est toujours au niveau de la route
        Point p1 = listRoute.get(0); //p1>p2
        Point p2 = listRoute.get(1);
        int i = 1;
        while (p.y<p2.y && i<listRoute.size()-1){
            if(p.y>=p1.y){ //Sortit par le bas
                inRoute = false;
                break;
            }
            if(i+1 >= listRoute.size()){  //Pour le cas des objets au dessus de l horizon
                inRoute = false;
                break;
            } else if (inRoute) {
                p1 = listRoute.get(i);
                if(i+1 >= listRoute.size()){  //Juste au cas ou
                    System.out.println("Sortie de array");
                }
                p2 = listRoute.get(i+1);
            }

            i++;
        }

         */
        int i = Tools.findIdxFirstInfByY(p, listRoute);
        i--; //Car on veux dernier superieur


        if(i>=0){ //Si l obstacle est toujours au niveau de la route
            Point p1 = listRoute.get(i);
            return (this.getRangeRoute().get(i)*(p.y- VueBackground.horizon +50))/(p1.y- VueBackground.horizon +50);//Produit en croix
        } else {
            return -1;
        }

    }

    /**
     * Initialise la position et le nombre de nuages de facon aleatoire enregiste dans clouds
     * <br/>Initialise egalement le type de nuage par un nombre de 0 a n (n le nb de dessins) enregistre dans cloudType
     */
    void initClouds(){
        //Nombre de nuages
        int nb_clouds = Tools.rangedRandomInt(5,10);
        //Nombre de types de nuages
        int nb_cloudTypes = 4;
        // Dans l'espace entre la ligne d'abscisse 0 et la ligne d'horizon, initialiser des positions random
        for (int i = 0; i < nb_clouds; i++) {
            int marge = 0;
            int random_x = Tools.rangedRandomInt(0+marge, Affichage.LARGEUR);
            int random_y = Tools.rangedRandomInt(0, horizon-70);
            marge += 100;
            Point random_point = new Point(random_x, random_y);
            this.clouds.add(random_point);
            this.cloudTypes.add(Tools.rangedRandomInt(0, nb_cloudTypes));
        }
    }

    /**
     * Reinitialise la liste des obstacles
     */
    void initObstacles(){
        this.listObstacles.clear();
    }

    /**
     * Renvoie la forme de l obstacle
     * <br/>Utilise pour les formules de collision
     * @param obs
     * @return
     */
    public Area getShapeObstacle(Obstacle obs){
        //TODO
        return null;
    }

    /**
     * Met a jour la liste des obstacles : En ajoute si il reste de la place, en enleve si ils sont sortis de la fenetre
     */
    public void updateObstacles(){
        //TODO update listObstacles
    }

    /**
     * Dessine tous les obstacles en fonction de leur type
     * @param g2
     */
    private void drawObstacles(Graphics2D g2){
        //TODO ne pas oublier de scale l obstacle lui meme !
    }

    /**
     * Renvoie la liste des obstacles
     * @return
     */
    public ArrayList<Obstacle> getListObstacles() {
        return listObstacles;
    }

    /**
     * Deplace la position des nuages et de la montagne en fonction des actions de l utilisateur
     * <br/>Cette methode est appellee par le controleur, lors des actions de l utilisateur pour deplacer user
     * @param right true si deplace a droite, false sinon
     */
    public void moveDecors(boolean right){
        int sautClouds = 1;
        int sautMontagnes = 2;
        for (int i = 0; i < this.clouds.size(); i++) {
            if (right) {
                this.clouds.get(i).move(this.clouds.get(i).x + sautClouds, this.clouds.get(i).y);
                //this.modMontagnes += sautMontagnes;
            } else {
                this.clouds.get(i).move(this.clouds.get(i).x - sautClouds, this.clouds.get(i).y);
                //this.modMontagnes -= sautMontagnes;
            }
        }
        if(right){
            this.modMontagnes += sautMontagnes;
        } else {
            this.modMontagnes -= sautMontagnes;
        }
        //Pour eviter les depassements
        if(this.initXMontagnes+this.modMontagnes>0){
            this.modMontagnes = -this.initXMontagnes; //Le stabilise a 0
        }
    }

    /**
     * Dessine la pelouse, l'horizon, les nuages et les montagnes de fond
     * @param g2 le graphism
     */
    private void drawFond(Graphics2D g2){
        //Pelouse
        g2.setColor(new Color(58, 137, 35));
        g2.fillRect(0, horizon, Affichage.LARGEUR, Affichage.HAUTEUR - horizon);
        //Ciel
        g2.setColor(new Color(116, 208, 241));
        g2.fillRect(0,0, Affichage.LARGEUR, horizon);

        //Nuages
        for (int i = 0; i < this.clouds.size(); i++) {
            BufferedImage modeleImage;
            if (this.cloudTypes.get(i) == 0) {
                modeleImage = cloud1;
            } else if (this.cloudTypes.get(i) == 1) {
                modeleImage = cloud2;
            } else if (this.cloudTypes.get(i) == 2) {
                modeleImage = cloud3;
            } else {
                modeleImage = cloud4;
            }
            BufferedImage img = Tools.deepCopy(modeleImage); //quoi que l on fasse sur la copie, l original ne sera pas impacte

            AffineTransform at = new AffineTransform();
            at.translate(this.clouds.get(i).getX(), this.clouds.get(i).getY()); //Les coord du nuage
            g2.drawImage(img, at, null);


            //g2.setColor(Color.WHITE);
            //g2.fillOval(this.clouds.get(i).x, this.clouds.get(i).y, 30,20);
        }

        //Montagnes
        /*
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(0.5f));
        g2.fill(this.montagnes);
        g2.setColor(Color.BLACK);
        g2.draw(this.montagnes);//Ligne noire autour des montagnes, donne une sensation de dessin

         */

        BufferedImage montagnes = Tools.deepCopy(this.mountain);
        AffineTransform at = new AffineTransform();
        at.translate(this.initXMontagnes+this.modMontagnes, horizon-(this.mountain.getHeight()/(double)2)); //Les coord du nuage
        g2.drawImage(montagnes, at, null);

    }

    /**
     * Dessine l arriere plan
     * @param g2 le graphisme
     */
    public void drawBackground(Graphics2D g2){
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setColor(Color.GREEN);
        g2.fillRect(0, horizon, Affichage.LARGEUR, Affichage.HAUTEUR - horizon);
        drawFond(g2);

        //Dessine la route

        setShapeRoute();
        g2.setColor(Color.gray);
        g2.fill(this.route);

        //Ligne au milieu
        g2.setColor(new Color(250, 240, 230)); //Blanc

        ArrayList<Point> listRoute = this.aff.route.getRoute();
        for(int i = 1; i<listRoute.size(); i++){

            Point p1 = listRoute.get(i-1);
            Point p2 = listRoute.get(i);
            if(p2.y < horizon){
                p2.move(Tools.findX(horizon,p2,p1), horizon); //N affiche que les points sous l horizon !
            }
            g2.drawLine(p1.x,p1.y,p2.x,p2.y);
        }

        //Point de controle
        int yPtCtrl = this.aff.route.getCtrl();
        if(yPtCtrl>horizon && yPtCtrl<Affichage.HAUTEUR){
            int idxRouteCtrl = Tools.findIdxFirstInfByY(new Point(0, yPtCtrl), listRoute);
            int xOnRoute = Tools.findX(yPtCtrl, listRoute.get(idxRouteCtrl-1), listRoute.get(idxRouteCtrl));
            int rangePtCtrl = this.getRange(new Point(0, yPtCtrl)) + 30; //depasse de la route sur l axe X
            g2.drawLine(xOnRoute-rangePtCtrl, yPtCtrl, xOnRoute+rangePtCtrl, yPtCtrl);
        }




        //Obstacles
        this.drawObstacles(g2);

        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
    }

    /**
     * Ecrit a l ecran les differentes donnees comme la vitesse et le kilometrage
     * @param g2 le graphisme
     */
    public void drawData(Graphics2D g2){
        Font oldFont = g2.getFont();

        String str1 = "Vitesse m/s : "+ Math.round((long)this.aff.user.getVitesse());
        String str2 = "Kilometrage : "+Tools.toStringInt(Data.getCurrentKilometrage());
        String temps = "Temps restant";
        String timer;
        MyTimer timerPtCtrl = this.aff.ctrl.getTimerPtCtrl();
        if(timerPtCtrl.getLeftoverTime().isNegative()){
            timer = "00";
        } else {
            timer = timerPtCtrl.toString();
        }
        String score = "Score : ";
        String scoreVal = Tools.toStringInt(Data.getCurrentScore());
        Color c1 = Color.BLACK;
        Color c2 = Color.WHITE;
        Font font = new Font("Arial", Font.BOLD, 20);

        if(Data.getCurrentScore()>=Data.getHighestScore() && Data.getCurrentScore() != 0){
            Color gold = new Color(255, 215, 0);
            Tools.drawDoubleString(score, Affichage.LARGEUR-90, 40, g2, font, gold, Color.BLACK);
            Tools.drawDoubleString(scoreVal, Affichage.LARGEUR-90, 80, g2, font, gold, Color.BLACK);
        } else {
            Tools.drawDoubleString(score, Affichage.LARGEUR-90, 40, g2, font, c1, c2);
            Tools.drawDoubleString(scoreVal, Affichage.LARGEUR-90, 80, g2, font, c1, c2);
        }


        Tools.drawDoubleString(temps, Affichage.LARGEUR - 154, Affichage.HAUTEUR-159, g2, font, c1, c2);
        Tools.drawDoubleString(timer, Affichage.LARGEUR - 99, Affichage.HAUTEUR-139, g2, font, c1, c2);
        Tools.drawDoubleString(str1, Affichage.LARGEUR - 204, Affichage.HAUTEUR-99, g2, font, c1, c2);
        Tools.drawDoubleString(str2, Affichage.LARGEUR - 189, Affichage.HAUTEUR-59, g2, font, c1, c2);
/*
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));

        g2.drawString(score, Affichage.LARGEUR-90, 40);
        g2.drawString(scoreVal, Affichage.LARGEUR-90, 80);

        g2.drawString(temps, Affichage.LARGEUR - 154, Affichage.HAUTEUR-159);
        g2.drawString(timer, Affichage.LARGEUR - 99, Affichage.HAUTEUR-139);
        g2.drawString(str1, Affichage.LARGEUR - 204, Affichage.HAUTEUR-99);
        g2.drawString(str2, Affichage.LARGEUR - 189, Affichage.HAUTEUR-59);

        //Reecrit tout avec x et y +1, pour un effet de decalage pour mieux voir
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));

        g2.drawString(score, Affichage.LARGEUR-91, 41);
        g2.drawString(scoreVal, Affichage.LARGEUR-91, 81);

        g2.drawString(temps, Affichage.LARGEUR - 155, Affichage.HAUTEUR-160);
        g2.drawString(timer, Affichage.LARGEUR - 100, Affichage.HAUTEUR-140);
        g2.drawString(str1, Affichage.LARGEUR - 205, Affichage.HAUTEUR-100);
        g2.drawString(str2, Affichage.LARGEUR - 190, Affichage.HAUTEUR-60);

 */

        g2.setFont(oldFont);
    }

}
