package vue;

import Tools.Tools;
import model.ConcreteObject;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class VueUser {
    // ********************************** 1) Attributs **********************************

    private final Affichage aff;

    private double scaleUser = 0.5;

    //List Etat et Images pour User
    private HashMap<Integer, BufferedImage> listStatesUser;

    /**Le timer qui va supprimer le message a la fin de son temps**/
    private Timer messageTimer;
    /**Le message a afficher au dessus de User**/
    private String message = "";

    // ********************************** 2) Constructeur **********************************

    public VueUser(Affichage aff) {
        this.aff = aff;

        // Dessins du véhicule
        BufferedImage userStraight = Tools.getBIfromPath("src/Sprites/user.png");
        BufferedImage userLeft = Tools.getBIfromPath("src/Sprites/userleft.png");
        BufferedImage userRight = Tools.getBIfromPath("src/Sprites/userright.png");
        userStraight = Tools.scaleBI(userStraight, scaleUser, scaleUser);
        userLeft = Tools.scaleBI(userLeft, scaleUser, scaleUser);
        userRight = Tools.scaleBI(userRight, scaleUser, scaleUser);

        this.listStatesUser = new HashMap<>();
        this.listStatesUser.put(0, userStraight);
        this.listStatesUser.put(-1, userLeft);
        this.listStatesUser.put(1, userRight);

        //Init timer
        //Initialise l action de supprimer le message
        ActionListener cancelMessage = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = "";
            }
        };
        //Le timer supprime le message apres un delais de 4000 milliseconds
        this.messageTimer = new Timer(4*1000,cancelMessage);
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
     * @param g2
     * @param obj
     */
    private void drawHitBox(Graphics2D g2, ConcreteObject obj){
        g2.setColor(new Color(188, 32, 1));
        Shape collisionBox = obj.getHitBox();
        g2.draw(collisionBox);
    }

    // Affichage du véhicule dans une sous-méthode
    public void drawCar(Graphics2D g2) {
        //Boite de collision
        this.drawHitBox(g2, this.aff.user);
        //Image
        Shape collisionBox = this.aff.user.getHitBox();
        //Image, centre l image sur le centre de la boite de collision
        BufferedImage img = Tools.deepCopy(this.aff.user.getEtat().getCurrentImage());
        //Le centre est le meme que la boite de collision
        Point2D.Double centerUser = new Point2D.Double(collisionBox.getBounds2D().getCenterX(), collisionBox.getBounds2D().getCenterY());
        double x = centerUser.x-((img.getWidth()*scaleUser)/2); //Cherche le point en haut a droite par rapport au centre
        double y = centerUser.y-((img.getHeight()*scaleUser)/2);
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        g2.drawImage(img, at, null);

    }

    public void initUser(){
        this.aff.user.getEtat().setImages(this.listStatesUser);
    }

    /**
     * Mise à jour la forme de user
     */
    public void updateCar() {

    }

}
