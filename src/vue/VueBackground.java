package vue;

import Tools.MyTimer;
import Tools.Tools;
import model.ConcreteObject;
import model.Data;
import model.Images;
import model.Obstacle;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;
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

    /*---------Obstacles----------*/

    private ArrayList<Obstacle> listObstacles = new ArrayList<>();
    /**Le nombre max de concurrents a la fois**/
    private final int maxObstacles = 2;
    /**Pourcentage d apparition des obstacles**/
    private final double percChanceApparition = 3;

    public final ReentrantLock obstacleMutex = new ReentrantLock();

    //Coord montagnes
    /**La position x initiale des montagnes**/
    private final int initXMontagnes = -500;

    /**Le modificateur de deplacement des montagnes**/
    private int modMontagnes = 0;

    private final Affichage aff;

    // ********************************** 2) Constructeur **********************************

    public VueBackground(Affichage aff){
        this.aff = aff;

        //Initialiser nuages
        this.initClouds();

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
     * @return la range au point de la route le plus pres si p est sortit de la route
     */
    public int getRange(Point p){
        ArrayList<Point> listRoute = this.aff.route.getRoute();
        ArrayList<Integer> rangeRoute = this.aff.route.getRangeRoute();
        //Touver le bon point sur le segment de route
        int i = Tools.findIdxFirstInfByY(p, listRoute);
        i--; //Car on veux dernier superieur


        if(i>=0){ //Si l objet est toujours au niveau de la route
            Point p1 = listRoute.get(i);
            return (rangeRoute.get(i)*(p.y- VueBackground.horizon +50))/(p1.y- VueBackground.horizon +50);//Produit en croix
        } else if(i == -1) {
            return rangeRoute.get(0);
        } else if(i == -2){
            return rangeRoute.get(rangeRoute.size()-1);
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
        double largeurMontagne = Images.getMountain().getWidth()/(double)2;
        if(this.initXMontagnes+largeurMontagne+this.modMontagnes<Affichage.LARGEUR){ //On voit le vide dans l ecran
            this.modMontagnes = (int) (Affichage.LARGEUR-this.initXMontagnes-largeurMontagne); //Stabilise a Affichage.LARGEUR
        }
    }



    /*-------------------- Obstacles -----------*/



    /**
     * Reinitialise la liste des obstacles
     */
    void initObstacles(){
        this.listObstacles.clear();
    }

    /**
     * Renvoie la liste des obstacles actuellement sur le jeu
     * @return
     */
    public ArrayList<Obstacle> getObstacles(){
        synchronized(this.listObstacles){
            try {
                this.obstacleMutex.lock();
                return this.listObstacles;
            } finally {
                this.obstacleMutex.unlock();
            }
        }
    }

    /**
     * Ajoute un obstacle a la liste avec une certaine probabilité
     */
    private void addObstacle(){
        synchronized(this.listObstacles){
            try {
                this.obstacleMutex.lock();

                if(this.listObstacles.size() < this.maxObstacles){
                    double rand = Tools.rangedRandomDouble(0,100);
                    if(rand <= this.percChanceApparition){
                        Point pInitObs = new Point(0, (int) Obstacle.initY);
                        float posXCenterInit = (float) this.aff.route.getPointRoutePara(pInitObs).getX();
                        float rangeInit = this.getRange(pInitObs);
                        Obstacle newObs = new Obstacle(posXCenterInit, rangeInit);
                        this.listObstacles.add(newObs);
                    }
                }
            } finally {
                this.obstacleMutex.unlock();
            }
        }
    }

    /**
     * Enleve les concurrents de la liste lorsqu ils sortent de l ecran
     */
    private void deleteObstacle(){
        synchronized(this.listObstacles){
            try {
                this.obstacleMutex.lock();

                if(this.listObstacles.size()>0){
                    for(int i = 0; i<this.listObstacles.size(); i++){
                        Obstacle obs = this.listObstacles.get(i);
                        // Si l'obstacle est à droite de la route
                        if (obs.isRightRoute()) {
                            // On l'enlève s'il est sorti de la fenetre
                            if ((obs.getPosX() > Affichage.LARGEUR) || (obs.getPosY() > Affichage.HAUTEUR)) {
                                this.listObstacles.remove(obs);
                            }
                        } else {
                            // Si l'obstacle est à gauche de la route
                            if ((obs.getPosX() + obs.getLARGEUR() < 0) || (obs.getPosY() > Affichage.HAUTEUR)) {
                                this.listObstacles.remove(obs);
                            }
                        }
                    }

                }

            } finally {
                this.obstacleMutex.unlock();
            }
        }

    }

    /**
     * Met a jour la liste des obstacles : En ajoute si il reste de la place, en enleve si ils sont sortis de la fenetre
     */
    public void updateObstacles(){
        this.deleteObstacle();
        this.addObstacle();
    }

    /**
     * Renvoie la liste des obstacles
     * @return
     */
    public ArrayList<Obstacle> getListObstacles() {
        synchronized(this.listObstacles){
            try {
                this.obstacleMutex.lock();
                return this.listObstacles;
            } finally {
                this.obstacleMutex.unlock();
            }
        }
    }







    /*------------------------------------Dessin---------------------------------------------------*/


    /**
     * Dessine tous les obstacles en fonction de leur type
     * @param g2
     */
    private void drawObstacles(Graphics2D g2){
        synchronized (this.listObstacles){
            try {
                this.obstacleMutex.lock();
                for(Obstacle obs : this.listObstacles){
                    this.drawSingleObstacle(g2, obs);
                }
            } finally {
                this.obstacleMutex.unlock();
            }
        }
    }

    /**
     * Dessine un obstacle
     * @param g2
     * @param obs
     */
    private void drawSingleObstacle(Graphics2D g2, Obstacle obs) {
        //Image
        Shape collisionBox = obs.getHitBox();
        if(this.aff.showHitbox){
            this.drawHitBox(g2, obs);
        }
        //Image, centre l image sur le centre de la boite de collision
        BufferedImage img = Tools.deepCopy(obs.getImg());
        img = Tools.scaleBI(img, obs.getScale(), obs.getScale());
        //Le centre est le meme que la boite de collision
        Point2D.Double centerObs = new Point2D.Double(collisionBox.getBounds2D().getCenterX(), collisionBox.getBounds2D().getCenterY());
        double x = centerObs.x-((img.getWidth()*obs.getScale())/2); //Cherche le point en haut a droite par rapport au centre
        double y = centerObs.y-((img.getWidth()*obs.getScale())/2);
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        g2.drawImage(img, at, null);
    }

    /**
     * Dessine la boite de collision
     * @param g2 le contexte graphique
     * @param obj l objet possedant une hitbox
     */
    private void drawHitBox(Graphics2D g2, ConcreteObject obj){
        g2.setColor(new Color(188, 32, 1));
        Shape collisionBox = obj.getHitBox();
        g2.draw(collisionBox);
    }



    /**
     * Dessine l'horizon, les nuages et les montagnes de fond
     * @param g2 le graphism
     */
    void drawFond(Graphics2D g2){
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();

        //Ciel
        g2.setColor(new Color(116, 208, 241));
        g2.fillRect(0,0, Affichage.LARGEUR, horizon);

        //Nuages
        for (int i = 0; i < this.clouds.size(); i++) {
            BufferedImage modeleImage = Images.getCloudImg(this.cloudTypes.get(i));
            BufferedImage img = Tools.deepCopy(modeleImage); //quoi que l on fasse sur la copie, l original ne sera pas impacte

            AffineTransform at = new AffineTransform();
            at.translate(this.clouds.get(i).getX(), this.clouds.get(i).getY()); //Les coord du nuage
            g2.drawImage(img, at, null);


        }

        //Montagnes

        BufferedImage montagnes = Tools.deepCopy(Images.getMountain());
        AffineTransform at = new AffineTransform();
        at.translate(this.initXMontagnes+this.modMontagnes, horizon-(montagnes.getHeight()/(double)2)); //Les coord de la montagne
        g2.drawImage(montagnes, at, null);

        g2.setColor(oldColor);
        g2.setStroke(oldStroke);
    }

    /**
     * Dessine l arriere plan, contenant la pelouse, la route
     * @param g2 le graphisme
     */
    void drawBackground(Graphics2D g2){
        Color oldColor = g2.getColor();
        Stroke oldStroke = g2.getStroke();

        //drawFond(g2);

        //Pelouse
        /*
        BufferedImage pelouse = Tools.deepCopy(Images.getGrass());
        AffineTransform atGrass = new AffineTransform();
        atGrass.translate(0, horizon);
        g2.drawImage(pelouse, atGrass, null);
         */
        g2.setColor(new Color(58, 137, 35));
        g2.fillRect(0, horizon, Affichage.LARGEUR, Affichage.HAUTEUR - horizon);

        //Dessine la route
        g2.setColor(Color.gray);
        g2.fill(this.aff.route.getHitBox());

        //Ligne au milieu
        g2.setColor(new Color(250, 240, 230)); //Blanc
        float[] dashingPatternLine = {100, 10};
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 1.0f, dashingPatternLine, 0f));

        ArrayList<Point> listRoute = this.aff.route.getRoute();
        for(int i = 1; i<listRoute.size(); i++){

            Point p1 = listRoute.get(i-1);
            Point p2 = listRoute.get(i);
            if(p2.y < horizon){
                p2.move((int) Tools.findX(horizon,p2,p1), horizon); //N affiche que les points sous l horizon !
            }
            g2.drawLine(p1.x,p1.y,p2.x,p2.y);
        }
        g2.setStroke(oldStroke);

        //Point de controle
        int yPtCtrl = this.aff.route.getCtrl();
        if(yPtCtrl>horizon && yPtCtrl<Affichage.HAUTEUR+20){
            int idxRouteCtrl = Tools.findIdxFirstInfByY(new Point(0, yPtCtrl), listRoute);
            int xOnRoute = (int) Tools.findX(yPtCtrl, listRoute.get(idxRouteCtrl-1), listRoute.get(idxRouteCtrl));
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
        Color black = Color.BLACK;
        Color white = Color.WHITE;
        Color red = new Color(150, 0, 24);
        Font font = new Font("Arial", Font.BOLD, 20);

        if(Data.getCurrentScore()>=Data.getHighestScore() && Data.getCurrentScore() != 0){
            Color gold = new Color(255, 215, 0);
            Tools.drawDoubleString(score, Affichage.LARGEUR-90, 40, g2, font, gold, Color.BLACK);
            Tools.drawDoubleString(scoreVal, Affichage.LARGEUR-90, 80, g2, font, gold, Color.BLACK);
        } else {
            Tools.drawDoubleString(score, Affichage.LARGEUR-90, 40, g2, font, black, white);
            Tools.drawDoubleString(scoreVal, Affichage.LARGEUR-90, 80, g2, font, black, white);
        }

        int vitesseUser = Math.round((long)this.aff.user.getVitesse());
        String vitesse = "Vitesse m/s : "+ vitesseUser;
        String kilometrage = "Kilometrage : "+Tools.toStringInt(Data.getCurrentKilometrage());
        String temps = "Temps restant";
        String timer;
        MyTimer timerPtCtrl = this.aff.ctrl.getTimerPtCtrl();
        if(timerPtCtrl.getLeftoverTime().isNegative()){
            timer = "00";
        } else {
            timer = timerPtCtrl.toString();
        }

        boolean warningVitesse = vitesseUser <= 10;
        boolean warningTimer = timerPtCtrl.getLeftoverTime().getSeconds()<=10;

        Tools.drawDoubleString(temps, Affichage.LARGEUR - 154, Affichage.HAUTEUR-159, g2, font, black, white);
        if(warningTimer){
            Tools.drawDoubleString(timer, Affichage.LARGEUR - 99, Affichage.HAUTEUR-139, g2, font, red, white);
        } else {
            Tools.drawDoubleString(timer, Affichage.LARGEUR - 99, Affichage.HAUTEUR-139, g2, font, black, white);
        }
        if(warningVitesse){
            Tools.drawDoubleString(vitesse, Affichage.LARGEUR - 204, Affichage.HAUTEUR-99, g2, font, red, white);
        } else {
            Tools.drawDoubleString(vitesse, Affichage.LARGEUR - 204, Affichage.HAUTEUR-99, g2, font, black, white);
        }

        Tools.drawDoubleString(kilometrage, Affichage.LARGEUR - 189, Affichage.HAUTEUR-59, g2, font, black, white);


        g2.setFont(oldFont);
    }

}
