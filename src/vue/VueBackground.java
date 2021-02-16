package vue;

import game.Tools;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class VueBackground {

    // ********************************** 1) Attributs **********************************

    //public static final int horizon = (int) (((float)Affichage.HAUTEUR) * (1/4))+1;
    public static final int horizon = 200;
    /**La forme des montagnes, ne change pas. Initialisee dans le constructeur**/
    private Shape montagnes = null;
    /**
     * On initialise la forme de la route en un rectangle
     */
    private Area route = new Area(new Rectangle(Affichage.LARGEUR/4, VueBackground.horizon, Affichage.LARGEUR/2, Affichage.HAUTEUR-VueBackground.horizon)) ;
    /**La liste des coord des nuages**/
    private ArrayList<Point> clouds = new ArrayList<>();
    /**Le type de nuage**/
    private ArrayList<Integer> cloudTypes = new ArrayList<>();
    private ArrayList<Integer> rangeroute = new ArrayList<>();
    private ReentrantLock rangeMutex = new ReentrantLock();

    //Images nuages :
    BufferedImage cloud1;
    BufferedImage cloud2;
    BufferedImage cloud3;
    BufferedImage cloud4;
    //Images montagnes
    BufferedImage mountain;

    //Coord montagnes
    /**La position x initiale des montagnes**/
    private int initXMontagnes = -500;

    /**Le modificateur de deplacement des montagnes**/
    private int modMontagnes = 0;

    private Affichage aff;

    // ********************************** 2) Constructeur **********************************

    public VueBackground(Affichage aff){
        this.aff = aff;
        //Initialiser montagnes
        // Les creer apartir de l horizon. Le 1er et dernier point de la liste doit etre le meme
        /*int nbPoints = 0;
        int[] tabX = new int[0];
        int[] tabY = new int[0];*/

        int nbPoints = 10;
        int[] tabX = new int[nbPoints+1];
        int[] tabY = new int[nbPoints+1];
        tabX[0] = 0;
        tabY[0] = this.horizon;

        int marge = aff.LARGEUR / nbPoints+1; //60
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
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Cree la forme de la route et met a jour rangeRoute
     * <br/>Met aussi a jour this.route
     * @return
     */
    private void setShapeRoute(){

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
                    int range = Math.round((rangeInit*(float) p.y)/(float) Affichage.HAUTEUR); //Produit en croix
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
     * @return
     */
    public Area getShapeRoute(){
        synchronized (this.route){return this.route;}
    }

    /**
     * Renvoie la distance entre le bord de la route et le milieu de la route pour chaque point correspondant a la route
     * @return
     */
    public ArrayList<Integer> getRangeRoute(){
        synchronized (this.rangeroute){
            try{
                this.rangeMutex.lock();
                return this.rangeroute;
            } finally {
                this.rangeMutex.unlock();
            }

        }
    }

    /**
     * Initialise la position et le nombre de nuages de facon aleatoire enregiste dans clouds
     * <br/>Initialise egalement le type de nuage par un nombre de 0 a n (n le nb de dessins) enregistre dans cloudType
     */
    private void initClouds(){
        /**Nombre de nuages**/
        int nb_clouds = Tools.rangedRandomInt(5,10);
        /**Nombre de types de nuages**/
        int nb_cloudTypes = 4;
        // Dans l'espace entre la ligne d'abscisse 0 et la ligne d'horizon, initialiser des positions random
        for (int i = 0; i < nb_clouds; i++) {
            int marge = 0;
            int random_x = Tools.rangedRandomInt(0 + marge, this.aff.LARGEUR);
            int random_y = Tools.rangedRandomInt(0, this.horizon-70);
            marge += 100;
            Point random_point = new Point(random_x, random_y);
            this.clouds.add(random_point);
            this.cloudTypes.add(Tools.rangedRandomInt(0, nb_cloudTypes));
        }
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
                this.modMontagnes += sautMontagnes;
            } else {
                this.clouds.get(i).move(this.clouds.get(i).x - sautClouds, this.clouds.get(i).y);
                this.modMontagnes -= sautMontagnes;
            }
        }
    }

    /**
     * Dessine la pelouse, l'horizon, les nuages et les montagnes de fond
     * @param g2
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
            BufferedImage modeleImage = null;
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
        at.translate(this.initXMontagnes+this.modMontagnes, horizon-(this.mountain.getHeight()/2)); //Les coord du nuage
        g2.drawImage(montagnes, at, null);

    }

    /**
     * Dessine l arriere plan
     * @param g2
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
        g2.setColor(Color.WHITE);

        ArrayList<Point> list = this.aff.route.getRoute();
        for(int i = 1; i<list.size(); i++){

            Point p1 = list.get(i-1);
            Point p2 = list.get(i);
            if(p2.y < horizon){
                p2.move(Tools.findX(horizon,p2,p1), horizon); //N affiche que les points sous l horizon !
            }
            g2.drawLine(p1.x,p1.y,p2.x,p2.y);
        }



        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
    }

    /**
     * Ecrit a l ecran les differentes donnees comme la vitesse et le kilometrage
     * @param g2
     */
    public void drawData(Graphics2D g2){
        Font oldFont = g2.getFont();
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        String str1 = "Vitesse m/s : "+Math.round(this.aff.user.getVitesse());
        String str2 = "Kilometrage : "+this.aff.route.getKilometrage();

        g2.drawString(str1, Affichage.LARGEUR - 204, Affichage.HAUTEUR-99);
        g2.drawString(str2, Affichage.LARGEUR - 189, Affichage.HAUTEUR-59);

        //Reecrit tout avec x et y +1, pour un effet de decalage pour mieux voir
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(str1, Affichage.LARGEUR - 205, Affichage.HAUTEUR-100);
        g2.drawString(str2, Affichage.LARGEUR - 190, Affichage.HAUTEUR-60);

        g2.setFont(oldFont);
    }

}
