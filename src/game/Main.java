package game;

import controleur.Controleur;
import controleur.Deplace;
import model.Route;
import model.User;
import vue.Affichage;



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.Instant;

class Main {


    //static int nb=0; -> utiliser une classe statique Best score, a recuperer ici.
    //Definir une methode statique permet de ne pas avoir a instancier pour utiliser la methode

    /**
     * Methode implementant un fonctionnalite permetant de recommencer la partie.
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

        Route route = new Route();
        User user = new User();
        Controleur ctrl = new Controleur(user, route);
        Affichage affichage = new Affichage(ctrl, user, route);
        ctrl.setAffichage(affichage);
        ctrl.setCmds();
        Deplace deplace = new Deplace(user, route, affichage);

        deplace.start();//Voir qui le lance, en fonction de si il y a une fenetre de demarage ou pas
        user.start(); //Redresse constament user (etat = 0)

        fenetre.add(affichage);

        /*

        Voler fly = new Voler();
        Etat modele = new Etat(fly);
        Controleur ctrl = new Controleur(modele);
        Affichage affichage = new Affichage(ctrl, modele);
        VueBird bird = new VueBird(affichage);

        Instant start = Instant.now();
        affichage.setTimer(start);

        ctrl.setVue(affichage);
        Avancer avance = new Avancer(affichage, modele.getParcours());

        ctrl.enableKeyPad();
        enableReload(fenetre, affichage);

        modele.getParcours().setTime(start);
        fly.start();
        avance.start();



        fenetre.add(affichage);
*/




        fenetre.pack();
        fenetre.setVisible(true);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        //MainTest test = new MainTest();
        //test.createParty();
        //nb++;
        //Afficher les instructions ici pour ne pas avoir a les relire quand on relance la partie
        //Ou ne pas les afficher sous forme de message mais sous forme de label
        //qui s efface au bout d un certain temps dans le controleur
        //createParty();
        EventQueue.invokeLater(new Runnable() {
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
