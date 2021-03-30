package vue;

import Tools.Tools;
import model.ConcreteObject;
import model.Concurrent;
import model.Images;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class VueUser {
    // ********************************** 1) Attributs **********************************

    private final Affichage aff;


    /**Le timer qui va supprimer le message a la fin de son temps**/
    private Timer messageTimer;
    /**Le message a afficher au dessus de User**/
    private String message = "";


    /*---------Concurrents----------*/

    private ArrayList<Concurrent> concurrents = new ArrayList<>();
    /**Le nombre max de concurrents a la fois**/
    private final int maxConcurrents = 1;//2;
    private final double percChanceApparition = 2;//0.2;

    public final ReentrantLock concurrentMutex = new ReentrantLock();

    // ********************************** 2) Constructeur **********************************

    public VueUser(Affichage aff) {
        this.aff = aff;


        //Init timer
        //Initialise l action de supprimer le message
        ActionListener cancelMessage = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = "";
            }
        };
        //Le timer supprime le message apres un delais de 4000 milliseconds
        this.messageTimer = new Timer(3*1000,cancelMessage);
        this.messageTimer.setRepeats(false);
        this.messageTimer.start();

    }

    // ********************************** 3) Méthodes **********************************


    /**
     * Ecrit un message qui s affiche au dessus de User pendant n millisecondes
     * @param message
     */
    public void writeMessage(String message){
        this.message = message;
        this.messageTimer.restart();
    }



    /*-------------Concurrents------------------*/


    /**
     * Renvoie la liste des concurrents actuellement sur le jeu
     * @return
     */
    public ArrayList<Concurrent> getConcurrents(){
        synchronized(this.concurrents){
            try {
                this.concurrentMutex.lock();
                return this.concurrents;
            } finally {
                this.concurrentMutex.unlock();
            }
        }
    }

    /**
     * Ajoute un concurrent a la liste avec une certaine probabilité
     */
    private void addConcurents(){
        synchronized(this.concurrents){
            try {
                this.concurrentMutex.lock();

                if(this.concurrents.size() < this.maxConcurrents){
                    double rand = Tools.rangedRandomDouble(0,100);
                    if(rand <= this.percChanceApparition){
                        Concurrent newC = new Concurrent();
                        newC.startCar();
                        this.concurrents.add(newC);

                    }
                }

            } finally {
                this.concurrentMutex.unlock();
            }
        }


    }

    /**
     * Enleve les concurrents de la liste lorsqu ils sortent de l ecran
     */
    private void deleteConcurrents(){
        synchronized(this.concurrents){
            try {
                this.concurrentMutex.lock();

                if(this.concurrents.size()>0){
                    //System.out.println("probleme, size = "+this.concurrents.size());
                    for(int i = 0; i<this.concurrents.size(); i++){
                        Concurrent c = this.concurrents.get(i);
                        if(c.getPosY()>Affichage.HAUTEUR){
                            c.stopRun();
                            this.concurrents.remove(c);
                        }
                    }

                }

            } finally {
                this.concurrentMutex.unlock();
            }
        }

    }


    /*------------Dessin---------------*/



    /**
     * Dessine l ensemble des concurrents
     * @param g2
     */
    public void drawConcurrent(Graphics2D g2){
        synchronized (this.concurrents){
            try {
                this.concurrentMutex.lock();
                for(Concurrent c : this.concurrents){
                    this.drawUser(g2, c);
                }
            } finally {
                this.concurrentMutex.unlock();
            }
        }

    }

    /**
     * Ecrit un message au dessus de user.
     * <br/>Chaque message dure uniquement n millisecondes
     * @param g2
     */
    public void drawMessage(Graphics2D g2){
        Font fontMessage = new Font("Arial", Font.BOLD, 15);
        int posX = this.aff.user.getPosX()+20;
        int posY = this.aff.user.getPosY() - 40;
        Tools.drawDoubleString(this.message, posX, posY, g2, fontMessage, Color.BLACK, Color.WHITE);
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
     * Dessine user a partir de son image
     * @param g2 le contexte graphique
     * @param obj User
     */
    private void drawUser(Graphics2D g2, User obj){
        //Image
        Shape collisionBox = obj.getHitBox();
        //Image, centre l image sur le centre de la boite de collision
        BufferedImage img = Tools.deepCopy(obj.getEtat().getCurrentImage());
        //Le centre est le meme que la boite de collision
        Point2D.Double centerUser = new Point2D.Double(collisionBox.getBounds2D().getCenterX(), collisionBox.getBounds2D().getCenterY());
        double x = centerUser.x-((img.getWidth()* Images.scaleUser)/2); //Cherche le point en haut a droite par rapport au centre
        double y = centerUser.y-((img.getHeight()*Images.scaleUser)/2);
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        g2.drawImage(img, at, null);
    }

    // Affichage du véhicule dans une sous-méthode
    public void drawCar(Graphics2D g2) {
        //Boite de collision
        this.drawHitBox(g2, this.aff.user);
        /*//Image
        Shape collisionBox = this.aff.user.getHitBox();
        //Image, centre l image sur le centre de la boite de collision
        BufferedImage img = Tools.deepCopy(this.aff.user.getEtat().getCurrentImage());
        //Le centre est le meme que la boite de collision
        Point2D.Double centerUser = new Point2D.Double(collisionBox.getBounds2D().getCenterX(), collisionBox.getBounds2D().getCenterY());
        double x = centerUser.x-((img.getWidth()* Images.scaleUser)/2); //Cherche le point en haut a droite par rapport au centre
        double y = centerUser.y-((img.getHeight()*Images.scaleUser)/2);
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        g2.drawImage(img, at, null);

         */
        this.drawUser(g2, this.aff.user);
    }



    /**
     * Met a jour tous les elements pour une nouvelle partie
     */
    public void newPartie(){
        this.concurrents.clear();
    }


    /**
     * Mise à jour les concurrents
     */
    public void update() {
        this.addConcurents();
        this.deleteConcurrents();
    }

}
