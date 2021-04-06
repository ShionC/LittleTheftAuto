package game;

import audio.Audio;
import controleur.Controleur;
import model.Data;
import model.Images;
import model.Route;
import model.User;
import vue.Affichage;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class Main {


    //static int nb=0; -> utiliser une classe statique Best score, a recuperer ici.
    //Definir une methode statique permet de ne pas avoir a instancier pour utiliser la methode

    /**
     * Methode implementant un fonctionnalite permetant de recommencer la partie.
     * <br/>Cree une nouvelle fenetre
     * </br><b>Utilisation : </b>A tout moment pendant la partie, appuyer sur [ENTRER] au clavier pour la recommencer
     * @param affichage La fenetre principale
     */
    private static void enableReload(JFrame fenetre, Affichage affichage) {
        //Appuyer sur [Entrer] pour recommencer !!! Utilisation de variables statiques pour garder les scores !!

        affichage.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");

        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.print("	*Reload* \n");
                //affichage.applyScore();
                fenetre.dispose();
                Main newMain = new Main();
                //newMain.createParty();
                newMain.main(null);
            }
        };
        affichage.getActionMap().put("Enter", action);
        //affichage.addActionListener(action);
    }

    /**
     * Principale fonction du main.
     * </br>Cree tout les Swing Componment necessaires au lancement du jeu et lance les threads
     * </br>Reinitialise la partie si la touche [ENTRER] est appuyee.
     */
    private static void createParty() {


        JFrame fenetre = new JFrame("Little Thief Auto");

        Data.initGame();
        Images.initImages();
        Audio.initAudio();

        Route route = new Route();
        User user = new User();

        Affichage affichage = new Affichage(fenetre, user, route);
        Controleur ctrl = new Controleur(affichage, user, route);


        affichage.switchInteface(false);



        fenetre.pack();
        fenetre.setVisible(true);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        //EventQueue.invokeLater(new Runnable() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createParty();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


}
