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

public class VueBackground {

    // ********************************** 1) Attributs **********************************

    //public static final int horizon = (int) (((float)Affichage.HAUTEUR) * (1/4))+1;
    public static final int horizon = 200;
    /**La forme des montagnes, ne change pas. Initialisee dans le constructeur**/
    private Shape montagnes = null;
    private Area route;
    /**La liste des coord des nuages**/
    private ArrayList<Point> clouds = new ArrayList<>();
    /**Le type de nuage**/
    private ArrayList<Integer> cloudTypes = new ArrayList<>();

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
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Cree la forme de la route
     * @return
     */
    private Area getShapeRoute(){
        return null;
    }

    /**
     * Initialise la position et le nombre de nuages de facon aleatoire enregiste dans clouds
     * Initialise egalement le type de nuage par un nombre de 0 a n (n le nb de dessins) enregistre dans cloudType
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
     * Cette methode est appellee par le controleur, lors des actions de l utilisateur pour deplacer user
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
        ArrayList<Point> list = this.aff.route.getRoute();
        for(int i = 1; i<list.size(); i++){

            Point p1 = list.get(i-1);
            Point p2 = list.get(i);
            if(p2.y < horizon){
                p2.move(Tools.findX(horizon,p2,p1), horizon); //N affiche que les points sous l horizon !
            }
            g2.drawLine(p1.x,p1.y,p2.x,p2.y);
        }
        //g2.fill(getShapeRoute());
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
        String str1 = "Vitesse : "+this.aff.user.getVitesse();
        String str2 = "Kilometrage : "+this.aff.route.getKilometrage();

        g2.drawString(str1, Affichage.LARGEUR - 149, Affichage.HAUTEUR-99);
        g2.drawString(str2, Affichage.LARGEUR - 189, Affichage.HAUTEUR-59);

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(str1, Affichage.LARGEUR - 150, Affichage.HAUTEUR-100);
        g2.drawString(str2, Affichage.LARGEUR - 190, Affichage.HAUTEUR-60);

        g2.setFont(oldFont);
    }

}
