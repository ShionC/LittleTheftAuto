package vue;

import controleur.Accueil;
import game.Tools;
import model.Data;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.util.ArrayList;

public class OutsideScreen  extends JPanel {

    private Affichage aff;
    private Accueil acc;

    ImageIcon iconSelect = null;

    JPanel Jstats, Jregles, JfirstScreen, JendGame;

    /**La liste des strings contenant les stats**/
    ArrayList<JLabel> stats = new ArrayList<>();
    private ArrayList<Integer> idxChoiceStats = new ArrayList<>();
    /**La liste des stings de la premiere fenetre**/
    ArrayList<JLabel> firstScreen = new ArrayList<>();
    private ArrayList<Integer> idxChoiceFirstScreen = new ArrayList<>();
    /**ArrayList des regles du jeu**/
    ArrayList<JLabel> reglesDuJeu = new ArrayList<>();
    private ArrayList<Integer> idxChoiceRegles = new ArrayList<>();
    /**ArrayList de l ecran de fin**/
    ArrayList<JLabel> endGame = new ArrayList<>();
    private ArrayList<Integer> idxChoiceEndGame = new ArrayList<>();



    public OutsideScreen(Affichage aff){
        this.aff = aff;
        this.setPreferredSize(new Dimension(Affichage.LARGEUR, Affichage.HAUTEUR));
        this.setLayout(new CardLayout());
        setFocusable(true);
        this.setBackground(new Color(131, 166, 151));

        this.iconSelect = new ImageIcon(((new ImageIcon("src/Sprites/curseur.png")).getImage()).getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

        this.initFirstScreen();
        this.initRegles();
        this.initStats();
        this.initEndGame();


    }


    /**
     * Lie le controleur correspondant au JPannel
     * @param acc le controleur
     */
    public void setAccueil(Accueil acc){
        this.acc = acc;
        update();
    }


    private void initStats(){
        Jstats = new JPanel(new GridBagLayout());
        Jstats.setBackground(this.getBackground());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 3; //Il prends 3 cases
        c.gridx = 1;
        int i = 0;
        c.gridy = i;
        JLabel l1 = new JLabel("<html><body><u>"+"Stats"+"</u></body></html>", JLabel.CENTER);
        l1.setFont(new Font("Arial",Font.BOLD,30));
        l1.setLocation((Affichage.LARGEUR/2)-20, 200);
        this.stats.add(l1);
        Jstats.add(l1, c);
        l1.setVisible(true);
        i++;
        c.gridwidth= 1;
        Font fontText = new Font("Arial",Font.PLAIN,15);
        c.gridx = 1;
        c.gridy = i;
        JLabel l2 = new JLabel("Score actuel : ", JLabel.CENTER);
        l2.setFont(fontText);
        this.stats.add(l2);
        Jstats.add(l2, c);
        i++;
        c.gridx = 3;
        c.gridy = i-1;
        JLabel l3 = new JLabel("Meilleur Score : ", JLabel.CENTER);
        l3.setFont(fontText);
        this.stats.add(l3);
        Jstats.add(l3, c);
        i++;
        c.gridx = 1;
        c.gridy = i;
        JLabel l4 = new JLabel("Nombre de points de controle dépassés : ", JLabel.CENTER);
        l4.setFont(fontText);
        this.stats.add(l4);
        Jstats.add(l4, c);
        i++;
        c.gridx = 3;
        c.gridy = i-1;
        JLabel l5 = new JLabel("Nombre de points de controle maximal : ", JLabel.CENTER);
        l5.setFont(fontText);
        this.stats.add(l5);
        Jstats.add(l5, c);
        i++;
        c.gridx = 2;
        c.gridy = i;
        JLabel l6 = new JLabel("Nombre de parties jouees : ", JLabel.CENTER);
        l6.setFont(fontText);
        this.stats.add(l6);
        Jstats.add(l6, c);
        i++;
        c.gridx = 1;
        c.gridy = i;
        JLabel l7 = new JLabel("Duree de la partie : ", JLabel.CENTER);
        l7.setFont(fontText);
        this.stats.add(l7);
        Jstats.add(l7, c);
        i++;
        c.gridx = 3;
        c.gridy = i-1;
        JLabel l8 = new JLabel("Duree maximale d'une partie : ", JLabel.CENTER);
        l8.setFont(fontText);
        this.stats.add(l8);
        Jstats.add(l8, c);
        i++;
        c.gridx = 2;
        c.gridy = i;
        JLabel l9 = new JLabel("Temps de jeu : ", JLabel.CENTER);
        l9.setFont(fontText);
        this.stats.add(l9);
        Jstats.add(l9, c);
        i++;
        c.gridx = 2;
        c.gridy = i;
        JLabel c1 = new JLabel("Retour", JLabel.CENTER);
        c1.setFont(new Font("Arial",Font.BOLD,22));
        this.stats.add(c1);
        this.idxChoiceStats.add(i);
        Jstats.add(c1, c);
        c1.setVisible(true);
        i++;
        this.add(Jstats, "Stats");


    }

    private void drawStats(CardLayout cl){
        boolean draw = this.acc.stats;
        if(draw){
            cl.show(this, "Stats");
        }
        /*
        for(JLabel l : this.stats){
            l.setVisible(draw);
        }

         */

    }

    private void updateStats(){
        if(this.acc.stats){
            for(int i = 0; i<this.idxChoiceStats.size(); i++){
                if(i == this.acc.getCurrentChoice()-1){
                    this.stats.get(this.idxChoiceStats.get(i)).setIcon(iconSelect);
                } else {
                    this.stats.get(this.idxChoiceStats.get(i)).setIcon(null);
                }
            }

            int i = 1;
            this.stats.get(i).setText("Score actuel : "+ Data.getCurrentScore());
            i++;
            this.stats.get(i).setText("Meilleur Score : "+Data.getHighestScore());
            i++;
            this.stats.get(i).setText("Nombre de points de contrôle dépassés : "+Data.getCurrentNbCtrlPt());
            i++;
            this.stats.get(i).setText("Nombre de points de contrôle max : "+Data.getHighestNbCtrlPt());
            i++;
            this.stats.get(i).setText("Nombre de parties jouées : "+Data.getNbParties());
            i++;
            this.stats.get(i).setText("Duree de la partie : "+Tools.toStringDuration(Data.getCurrentDurationPartie()));
            i++;
            this.stats.get(i).setText("Duree maximale d'une partie : "+Tools.toStringDuration(Data.getHighestDurationPartie()));
            i++;
            this.stats.get(i).setText("Temps de jeu : "+ Tools.toStringDuration(Data.getDurationGame()));
            i++;
        }
    }

    private void initRegles(){

        Jregles = new JPanel(new GridBagLayout());
        Jregles.setBackground(this.getBackground());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 3; //Il prends 3 cases
        c.gridx = 2;
        int i = 0;
        c.gridy = i;
        JLabel l1 = new JLabel("<html><body><u>"+"Regles du jeu"+"</u></body></html>", JLabel.CENTER);
        l1.setFont(new Font("Arial",Font.BOLD,30));
        l1.setLocation((Affichage.LARGEUR/2)-20, 200);
        this.reglesDuJeu.add(l1);
        Jregles.add(l1, c);
        l1.setVisible(true);
        i++;
        //Choix
        c.gridy = i;
        JLabel c1 = new JLabel("Retour", JLabel.CENTER);
        c1.setFont(new Font("Arial",Font.BOLD,20));
        c1.setLocation((Affichage.LARGEUR/2)-20, 400);
        c1.setIcon(null); //Pour laffichage de la selection
        this.reglesDuJeu.add(c1);
        this.idxChoiceRegles.add(i);
        Jregles.add(c1, c);
        c1.setVisible(true);
        i++;
        this.add(Jregles, "Regles");


    }

    private void drawRegles(CardLayout cl){
        boolean draw = this.acc.regles;
        if(draw){
            cl.show(this, "Regles");
        }
    }

    private void updateRegles(){
        if(this.acc.regles){
            for(int i = 0; i<this.idxChoiceRegles.size(); i++){
                if(i == this.acc.getCurrentChoice()-1){
                    this.reglesDuJeu.get(this.idxChoiceRegles.get(i)).setIcon(iconSelect);
                } else {
                    this.reglesDuJeu.get(this.idxChoiceRegles.get(i)).setIcon(null);
                }
            }
        }
    }

    private void initFirstScreen(){
        JfirstScreen = new JPanel(new GridBagLayout());
        JfirstScreen.setBackground(this.getBackground());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 3; //Il prends 3 cases
        c.gridx = 2;
        int i = 0;
        c.gridy = i;
        JLabel l1 = new JLabel("<html><body><u>"+"Bienvenue dans Little Theft Auto !"+"</u></body></html>", JLabel.CENTER);
        l1.setFont(new Font("Arial",Font.BOLD,30));
        this.firstScreen.add(l1);
        JfirstScreen.add(l1, c);
        i++;
        //Choix
        c.gridy = i+3;
        JLabel c1 = new JLabel("Commencer une partie", JLabel.CENTER);
        c1.setFont(new Font("Arial",Font.BOLD,20));
        this.firstScreen.add(c1);
        this.idxChoiceFirstScreen.add(i);
        JfirstScreen.add(c1, c);
        i++;
        c.gridy = i+3;
        JLabel c2 = new JLabel("Regles du jeu", JLabel.CENTER);
        c2.setFont(new Font("Arial",Font.BOLD,20));
        this.firstScreen.add(c2);
        this.idxChoiceFirstScreen.add(i);
        JfirstScreen.add(c2, c);
        i++;
        c.gridy = i+3;
        JLabel c3 = new JLabel("Quitter", JLabel.CENTER);
        c3.setFont(new Font("Arial",Font.BOLD,20));
        this.firstScreen.add(c3);
        this.idxChoiceFirstScreen.add(i);
        JfirstScreen.add(c3, c);
        i++;
        this.add(JfirstScreen, "FirstScreen");
    }

    private void drawFirstScreen(CardLayout cl){
        boolean draw = this.acc.premierEcran && !this.acc.regles;
        if(draw){
            cl.show(this, "FirstScreen");
        }
    }

    /**
     * Met a jour la selection du choix
     */
    private void updateFirstScreen(){
        if(this.acc.premierEcran && !this.acc.regles){
            for(int i = 0; i<this.idxChoiceFirstScreen.size(); i++){
                if(i == this.acc.getCurrentChoice()-1){
                    this.firstScreen.get(this.idxChoiceFirstScreen.get(i)).setIcon(iconSelect);
                } else {
                    this.firstScreen.get(this.idxChoiceFirstScreen.get(i)).setIcon(null);
                }
            }
        }
    }

    private void initEndGame(){
        JendGame = new JPanel(new GridBagLayout());
        JendGame.setBackground(this.getBackground());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 3; //Il prends 3 cases
        c.gridx = 2;
        int i = 0;
        c.gridy = i;
        JLabel l1 = new JLabel("<html><body><u>"+"Partie terminee !"+"</u></body></html>", JLabel.CENTER);
        l1.setFont(new Font("Arial",Font.BOLD,30));
        l1.setLocation((Affichage.LARGEUR/2)-20, 200);
        this.endGame.add(l1);
        JendGame.add(l1, c);
        i++;
        //Choix
        c.gridy = i;
        JLabel c1 = new JLabel("Nouvelle partie", JLabel.CENTER);
        c1.setFont(new Font("Arial",Font.BOLD,20));
        this.endGame.add(c1);
        this.idxChoiceEndGame.add(i);
        JendGame.add(c1, c);
        i++;
        c.gridy = i;
        JLabel c2 = new JLabel("Regles du jeu", JLabel.CENTER);
        c2.setFont(new Font("Arial",Font.BOLD,20));
        this.endGame.add(c2);
        this.idxChoiceEndGame.add(i);
        JendGame.add(c2, c);
        i++;
        c.gridy = i;
        JLabel c3 = new JLabel("Stats", JLabel.CENTER);
        c3.setFont(new Font("Arial",Font.BOLD,20));
        this.endGame.add(c3);
        this.idxChoiceEndGame.add(i);
        JendGame.add(c3, c);
        i++;
        c.gridy = i;
        JLabel c4 = new JLabel("Quitter", JLabel.CENTER);
        c4.setFont(new Font("Arial",Font.BOLD,20));
        this.endGame.add(c4);
        this.idxChoiceEndGame.add(i);
        JendGame.add(c4, c);
        i++;
        this.add(JendGame, "EndGame");
    }

    private void drawEndGame(CardLayout cl){
        boolean draw = this.acc.endGame;
        if(draw){
            cl.show(this, "EndGame");
        }
    }

    private void updateEndGame(){
        if(this.acc.endGame){
            for(int i = 0; i<this.idxChoiceEndGame.size(); i++){
                if(i == this.acc.getCurrentChoice()-1){
                    this.endGame.get(this.idxChoiceEndGame.get(i)).setIcon(iconSelect);
                } else {
                    this.endGame.get(this.idxChoiceEndGame.get(i)).setIcon(null);
                }
            }
        }
    }

    /**
     * Dessine le Jpannel selon les conditions de Accueil
     */
    private void draw(){

        CardLayout cl = (CardLayout) this.getLayout();
        this.drawFirstScreen(cl);
        this.drawRegles(cl);
        this.drawStats(cl);
        this.drawEndGame(cl);
    }

    public void update(){
        this.updateStats();
        this.updateRegles();
        this.updateEndGame();
        this.updateFirstScreen();
        this.draw();
    }


}
