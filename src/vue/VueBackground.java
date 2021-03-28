package vue;

import Tools.MyTimer;
import Tools.Tools;
import model.Data;
import model.Obstacle;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.util.concurrent.locks.ReentrantLock;

public class VueBackground {

    // ********************************** 1) Attributs **********************************

    //public static final int horizon = (int) (((float)Affichage.HAUTEUR) * (1/4))+1;
    /**La position sur l axe Y de la ligne d horizon**/
    public static final int horizon = 200;

    /**
     * On initialise la forme de la route en un rectangle
     */
    private Area route = new Area(new Rectangle(Affichage.LARGEUR/4, VueBackground.horizon, Affichage.LARGEUR/2, Affichage.HAUTEUR-VueBackground.horizon)) ;
    /**La liste des coord des nuages**/
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Point> clouds = new ArrayList<>();
    /**Le type de nuage**/
    private ArrayList<Integer> cloudTypes = new ArrayList<>();


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

        //Initialiser les images des nuages et de la montagne :

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
        this.mountain = Tools.getBIfromPath("src/Sprites/mountains.png");
        this.mountain = Tools.scaleBI(this.mountain, 0.5, 0.5);


        //Initialiser nuages
        this.initClouds();
        //this.setShapeRoute();


        //Initialiser les obstacles
        //TODO les images
    }

    // ********************************** 3) Méthodes **********************************

    /**
     * Reinitialise VueBackground (clouds, route, obstacles, mod montagne)
     */
    public void init(){
        this.initClouds();
        this.initObstacles();
        this.modMontagnes = 0;
    }



    /**
     * Renvoie range au point p sur la route
     * @param p Le point p sur la route (p.x n est pas oblige d etre sur la route)
     * @return -1 si p.y est sortit de la route
     */
    public int getRange(Point p){
        ArrayList<Point> listRoute = this.aff.route.getRoute();
        //Touver le bon point sur le segment de route
        int i = Tools.findIdxFirstInfByY(p, listRoute);
        i--; //Car on veux dernier superieur


        if(i>=0){ //Si l objet est toujours au niveau de la route
            Point p1 = listRoute.get(i);
            return (this.aff.route.getRangeRoute().get(i)*(p.y- VueBackground.horizon +50))/(p1.y- VueBackground.horizon +50);//Produit en croix
        } else {
            return -1;
        }

    }

    /**
     * Initialise la position et le nombre de nuages de facon aleatoire enregiste dans clouds
     * <br/>Initialise egalement le type de nuage par un nombre de 0 a n (n le nb de dessins) enregistre dans cloudType
     */
    void initClouds(){
        this.clouds.clear();
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
            } else {
                this.clouds.get(i).move(this.clouds.get(i).x - sautClouds, this.clouds.get(i).y);
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
        double largeurMontagne = this.mountain.getWidth()/(double)2;
        if(this.initXMontagnes+largeurMontagne+this.modMontagnes<Affichage.LARGEUR){ //On voit le vide dans l ecran
            this.modMontagnes = (int) (Affichage.LARGEUR-this.initXMontagnes-largeurMontagne); //Stabilise a Affichage.LARGEUR
        }
    }






    /*------------------------------------Dessin---------------------------------------------------*/





    /**
     * Dessine l'horizon, les nuages et les montagnes de fond
     * @param g2 le graphism
     */
    private void drawFond(Graphics2D g2){

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


        }

        //Montagnes

        BufferedImage montagnes = Tools.deepCopy(this.mountain);
        AffineTransform at = new AffineTransform();
        at.translate(this.initXMontagnes+this.modMontagnes, horizon-(this.mountain.getHeight()/(double)2)); //Les coord de la montagne
        g2.drawImage(montagnes, at, null);

    }

    /**
     * Dessine l arriere plan, contenant la pelouse, la route
     * @param g2 le graphisme
     */
    public void drawBackground(Graphics2D g2){
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();
        g2.setColor(Color.GREEN);
        g2.fillRect(0, horizon, Affichage.LARGEUR, Affichage.HAUTEUR - horizon);
        drawFond(g2);

        //Pelouse
        g2.setColor(new Color(58, 137, 35));
        g2.fillRect(0, horizon, Affichage.LARGEUR, Affichage.HAUTEUR - horizon);

        //Dessine la route
        g2.setColor(Color.gray);
        g2.fill(this.aff.route.getHitBox());

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
        if(yPtCtrl>horizon && yPtCtrl<Affichage.HAUTEUR+20){
            int idxRouteCtrl = Tools.findIdxFirstInfByY(new Point(0, yPtCtrl), listRoute);
            int xOnRoute = Tools.findX(yPtCtrl, listRoute.get(idxRouteCtrl-1), listRoute.get(idxRouteCtrl));
            int modFromRouteMax = 50;
            int modFromRoute = Math.round((modFromRouteMax*(float) (yPtCtrl-horizon+50))/(float) (Affichage.HAUTEUR-horizon+50));
            int rangePtCtrl = this.getRange(new Point(0, yPtCtrl)) + modFromRoute; //depasse de la route sur l axe X

            //Dessine une ligne en pointilles
            int startX = xOnRoute-rangePtCtrl;
            int endX = xOnRoute+rangePtCtrl;
            Line2D.Double lineCtrl = new Line2D.Double(startX, yPtCtrl, endX, yPtCtrl);
            //Gestion profondeur
            float heightMax = 10;
            float heightCtrl = (heightMax*(float) (yPtCtrl))/(float) (Affichage.HAUTEUR);
            float dash1Max = 15;
            float dash1 = (dash1Max*(float) (yPtCtrl))/(float) (Affichage.HAUTEUR);
            float dash2Max = 10;
            float dash2 = (dash2Max*(float) (yPtCtrl))/(float) (Affichage.HAUTEUR);
            float[] dashingPattern = {dash1, dash2};
            //Dessin
            BasicStroke strokeCtrl = new BasicStroke(heightCtrl, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 1.0f, dashingPattern, 0f);
            Color c1 = new Color(243, 214, 23);
            Color c2 = Color.black;
            Tools.drawDashedLineWith2Colors(g2, lineCtrl, c1, c2, strokeCtrl);
            //Contour du pt de ctrl
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(0.5f));
            g2.draw(new Rectangle2D.Double(startX,yPtCtrl-(heightCtrl/2),endX-startX, heightCtrl));



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

        Tools.drawDoubleString(temps, Affichage.LARGEUR - 154, Affichage.HAUTEUR-159, g2, font, c1, c2);
        Tools.drawDoubleString(timer, Affichage.LARGEUR - 99, Affichage.HAUTEUR-139, g2, font, c1, c2);
        Tools.drawDoubleString(str1, Affichage.LARGEUR - 204, Affichage.HAUTEUR-99, g2, font, c1, c2);
        Tools.drawDoubleString(str2, Affichage.LARGEUR - 189, Affichage.HAUTEUR-59, g2, font, c1, c2);


        g2.setFont(oldFont);
    }

}
