package vue;

import controleur.Accueil;
import Tools.Tools;
import model.Data;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OutsideScreen  extends JPanel {

    private Affichage aff;
    private Accueil acc;

    ImageIcon iconSelect = null;

    JPanel Jstats, Jregles, JfirstScreen, JendGame, Jcredits;

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
    /**ArrayList des credits**/
    ArrayList<JLabel> credits = new ArrayList<>();
    private ArrayList<Integer> idxChoiceCredits = new ArrayList<>();

    private Insets title = new Insets(10,0,100,0);
    private Insets text = new Insets(5,5,10,5);
    private Insets choices = new Insets(20,0,5,0);



    public OutsideScreen(Affichage aff){
        this.aff = aff;
        this.setPreferredSize(new Dimension(Affichage.LARGEUR, Affichage.HAUTEUR));
        this.setLayout(new CardLayout());
        setFocusable(true);
        this.setBackground(new Color(131, 166, 151));

        this.iconSelect = new ImageIcon(((new ImageIcon("src/Sprites/curseur.png")).getImage()).getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));

    }

    /**
     * Initialise outsideScreen.
     * A appeler apres la construction de Accueil
     */
    private void initOutsideScreen(){
        this.initFirstScreen();
        this.initRegles();
        this.initStats();
        this.initEndGame();
        this.initCredits();
    }


    /**
     * Lie le controleur correspondant au JPannel et initialise l affichage des menus
     * @param acc le controleur
     */
    public void setAccueil(Accueil acc){
        this.acc = acc;
        initOutsideScreen();
        update();
    }


    private void initStats(){
        Jstats = new JPanel(new GridBagLayout());
        Jstats.setBackground(this.getBackground());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = title;
        c.gridwidth = 3; //Il prends 3 cases
        c.gridx = 1;
        int i = 0;
        c.gridy = i;
        JLabel l1 = new JLabel("<html><body><u>"+"Stats"+"</u></body></html>", JLabel.CENTER);
        ImageIcon statsReport = new ImageIcon(((new ImageIcon("src/Sprites/statistics-report.png")).getImage()).getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
        l1.setFont(new Font("Arial",Font.BOLD,30));
        l1.setLocation((Affichage.LARGEUR/2)-20, 200);
        l1.setIcon(statsReport);
        this.stats.add(l1);
        Jstats.add(l1, c);
        l1.setVisible(true);
        i++;
        c.insets = text;
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
        //Choix
        c.insets = choices;
        c.gridx = 2;
        c.gridy = i;
        JLabel c1 = new JLabel("Retour", JLabel.CENTER);
        c1.setFont(new Font("Arial",Font.BOLD,22));
        this.stats.add(c1);
        this.idxChoiceStats.add(i);
        Jstats.add(c1, c);
        c1.setVisible(true);
        i++;
        this.add(Jstats, this.acc.keyStats);


    }

    private void drawStats(CardLayout cl){
        if(this.acc.menus.get(this.acc.keyStats)){
            cl.show(this, this.acc.keyStats);
        }

    }

    private void updateStats(){
        if(this.acc.menus.get(this.acc.keyStats)){
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
            this.stats.get(i).setText("Durée de la partie : "+Tools.toStringDuration(Data.getCurrentDurationPartie()));
            i++;
            this.stats.get(i).setText("Durée maximale d'une partie : "+Tools.toStringDuration(Data.getHighestDurationPartie()));
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
        c.gridx = 1;
        int i = 0;
        c.gridy = i;
        c.insets = (Insets) title.clone();
        c.insets.bottom = 50;
        JLabel l1 = new JLabel("<html><body><u>"+"Règles du jeu"+"</u></body></html>", JLabel.CENTER);
        ImageIcon reglesJeu = new ImageIcon(((new ImageIcon("src/Sprites/regles_jeu.png")).getImage()).getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
        l1.setFont(new Font("Arial",Font.BOLD,30));
        l1.setLocation((Affichage.LARGEUR/2)-20, 200);
        l1.setIcon(reglesJeu);
        this.reglesDuJeu.add(l1);
        Jregles.add(l1, c);
        l1.setVisible(true);
        i++;
        c.insets = text;
        Font fontText = new Font("Arial",Font.PLAIN,15);
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = i;
        JLabel l2 = new JLabel("Bienvenue dans le circuit de Little Thief Auto ! ", JLabel.CENTER);
        l2.setFont(fontText);
        this.reglesDuJeu.add(l2);
        Jregles.add(l2, c);
        i++;
        c.gridy = i;
        JLabel l3 = new JLabel("Votre but, si vous l'acceptez, est d'atteindre le maximum de points de contrôle, chacun dans un temps limité.", JLabel.CENTER);
        l3.setFont(fontText);
        this.reglesDuJeu.add(l3);
        Jregles.add(l3, c);
        i++;
        //ImageIcon arrows = new ImageIcon(((new ImageIcon("src/Sprites/left_right_keys.png")).getImage()).getScaledInstance(80, 50, java.awt.Image.SCALE_SMOOTH));
        ImageIcon arrows2 = new ImageIcon(((new ImageIcon("src/Sprites/ArrowLeftRight.png")).getImage()).getScaledInstance(85, 60, java.awt.Image.SCALE_SMOOTH));
        c.gridy = i;
        JLabel l4 = new JLabel("Pour déplacer votre véhicule sur le côté, utilisez les touches du clavier", JLabel.CENTER);
        l4.setFont(fontText);
        l4.setIcon(arrows2);
        this.reglesDuJeu.add(l4);
        Jregles.add(l4, c);
        i++;
        c.gridy = i;
        ImageIcon speedControl = new ImageIcon(((new ImageIcon("src/Sprites/speed_control.png")).getImage()).getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
        JLabel l5 = new JLabel("Restez sur la route pour accélérer !", JLabel.CENTER);
        l5.setFont(fontText);
        l5.setIcon(speedControl);
        this.reglesDuJeu.add(l5);
        Jregles.add(l5, c);
        i++;
        ImageIcon escap = new ImageIcon(((new ImageIcon("src/Sprites/esc_key.png")).getImage()).getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
        c.gridy = i;
        JLabel l6 = new JLabel("N'oubliez pas, vous pouvez mettre le jeu en pause à tout moment", JLabel.CENTER);
        l6.setFont(fontText);
        l6.setIcon(escap);
        this.reglesDuJeu.add(l6);
        Jregles.add(l6, c);
        i++;
        ImageIcon skull = new ImageIcon(((new ImageIcon("src/Sprites/game_over.png")).getImage()).getScaledInstance(65, 45, java.awt.Image.SCALE_SMOOTH));
        c.gridy = i;
        JLabel l7 = new JLabel("Ne pas atteindre un point de contrôle à temps", JLabel.CENTER);
        l7.setFont(fontText);
        l7.setIcon(skull);
        this.reglesDuJeu.add(l7);
        Jregles.add(l7, c);
        i++;
        c.gridy = i;
        JLabel l8 = new JLabel("S'arrêter (Vous êtes dans une course bon sang !)", JLabel.CENTER);
        l8.setFont(fontText);
        l8.setIcon(skull);
        this.reglesDuJeu.add(l8);
        Jregles.add(l8, c);
        i++;
        //Choix
        c.insets = choices;
        c.gridwidth = 1;
        c.gridx = 2;
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
        this.add(Jregles, this.acc.keyRegles);


    }

    private void drawRegles(CardLayout cl){
        if(this.acc.menus.get(this.acc.keyRegles)){
            cl.show(this, this.acc.keyRegles);
        }
    }

    private void updateRegles(){
        if(this.acc.menus.get(this.acc.keyRegles)){
            for(int i = 0; i<this.idxChoiceRegles.size(); i++){
                if(i == this.acc.getCurrentChoice()-1){
                    this.reglesDuJeu.get(this.idxChoiceRegles.get(i)).setIcon(iconSelect);
                } else {
                    this.reglesDuJeu.get(this.idxChoiceRegles.get(i)).setIcon(null);
                }
            }
        }
    }

    private void initCredits(){
        Jcredits = new JPanel(new GridBagLayout());
        Jcredits.setBackground(this.getBackground());
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = 3; //Il prends 3 cases
        c.gridx = 1;
        int i = 0;
        c.gridy = i;
        c.insets = title;
        c.insets.bottom += 50;
        ImageIcon imgCredit = new ImageIcon(((new ImageIcon("src/Sprites/credits.png")).getImage()).getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
        JLabel l1 = new JLabel("<html><body><u>"+"Crédits : "+"</u></body></html>", JLabel.CENTER);
        l1.setFont(new Font("Arial",Font.BOLD,30));
        l1.setIcon(imgCredit);
        this.credits.add(l1);
        Jcredits.add(l1, c);
        i++;

        c.insets = text;
        c.gridwidth = 3;
        Font fontText = new Font("Arial",Font.PLAIN,20);
        c.gridx = 1;
        c.gridy = i;
        ImageIcon matDoll = new ImageIcon(((new ImageIcon("src/Sprites/Mathilde_doll.png")).getImage()).getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
        JLabel mathilde = new JLabel("Mathilde LASSEIGNE", JLabel.CENTER);
        mathilde.setFont(fontText);
        mathilde.setIcon(matDoll);
        this.credits.add(mathilde);
        Jcredits.add(mathilde, c);
        i++;
        c.gridx = 1;
        c.gridy = i;
        JLabel celine = new JLabel("Céline YAN", JLabel.CENTER);
        celine.setFont(fontText);
        celine.setIcon(imgCredit);
        this.credits.add(celine);
        Jcredits.add(celine, c);
        i++;

        //Choix
        c.insets = choices;
        c.insets.top = 50;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = i;
        JLabel c1 = new JLabel("Retour", JLabel.CENTER);
        c1.setFont(new Font("Arial",Font.BOLD,20));
        c1.setIcon(this.iconSelect); //Pour laffichage de la selection
        this.credits.add(c1);
        this.idxChoiceCredits.add(i);
        Jcredits.add(c1, c);
        i++;
        this.add(Jcredits, this.acc.keyCredits);

    }

    private void drawCredits(CardLayout cl){
        if(this.acc.menus.get(this.acc.keyCredits)){
            cl.show(this, this.acc.keyCredits);
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
        c.insets = title;
        JLabel l1 = new JLabel("<html><body><u>"+"Bienvenue dans Little Theft Auto !"+"</u></body></html>", JLabel.CENTER);
        l1.setFont(new Font("Arial",Font.BOLD,30));
        this.firstScreen.add(l1);
        JfirstScreen.add(l1, c);
        i++;
        //Choix
        c.insets = choices;
        c.gridy = i+3;
        JLabel c1 = new JLabel("Commencer une partie", JLabel.CENTER);
        c1.setFont(new Font("Arial",Font.BOLD,20));
        this.firstScreen.add(c1);
        this.idxChoiceFirstScreen.add(i);
        JfirstScreen.add(c1, c);
        i++;
        c.gridy = i+3;
        JLabel c2 = new JLabel("Règles du jeu", JLabel.CENTER);
        c2.setFont(new Font("Arial",Font.BOLD,20));
        this.firstScreen.add(c2);
        this.idxChoiceFirstScreen.add(i);
        JfirstScreen.add(c2, c);
        i++;
        c.gridy = i+3;
        JLabel cCredits = new JLabel("Crédits", JLabel.CENTER);
        cCredits.setFont(new Font("Arial",Font.BOLD,20));
        this.firstScreen.add(cCredits);
        this.idxChoiceFirstScreen.add(i);
        JfirstScreen.add(cCredits, c);
        i++;
        c.gridy = i+3;
        JLabel c3 = new JLabel("Quitter", JLabel.CENTER);
        c3.setFont(new Font("Arial",Font.BOLD,20));
        this.firstScreen.add(c3);
        this.idxChoiceFirstScreen.add(i);
        JfirstScreen.add(c3, c);
        i++;
        this.add(JfirstScreen, this.acc.keyFirstScreen);
    }

    private void drawFirstScreen(CardLayout cl){
        if(this.acc.menus.get(this.acc.keyFirstScreen) && this.acc.premierEcan){
            cl.show(this, this.acc.keyFirstScreen);
        }
    }

    /**
     * Met a jour la selection du choix
     */
    private void updateFirstScreen(){
        if(this.acc.menus.get(this.acc.keyFirstScreen) && this.acc.premierEcan){
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
        c.insets = title;
        c.insets.bottom = 70;
        JLabel l1 = new JLabel("<html><body><u>"+"Partie terminée !"+"</u></body></html>", JLabel.CENTER);
        l1.setFont(new Font("Arial",Font.BOLD,30));
        l1.setLocation((Affichage.LARGEUR/2)-20, 200);
        this.endGame.add(l1);
        JendGame.add(l1, c);
        i++;
        c.insets = text;
        c.insets.bottom = 20;
        Font fontText = new Font("Arial",Font.PLAIN,15);
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = i;
        JLabel l2 = new JLabel();
        l2.setHorizontalAlignment(JLabel.CENTER);
        l2.setFont(fontText);
        this.endGame.add(l2);
        JendGame.add(l2, c);
        i++;
        //Choix
        c.gridx = 2;
        c.gridwidth = 3;
        c.insets = choices;
        c.gridy = i;
        JLabel c1 = new JLabel("Nouvelle partie", JLabel.CENTER);
        c1.setFont(new Font("Arial",Font.BOLD,20));
        this.endGame.add(c1);
        this.idxChoiceEndGame.add(i);
        JendGame.add(c1, c);
        i++;
        c.gridy = i;
        JLabel c2 = new JLabel("Règles du jeu", JLabel.CENTER);
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
        JLabel cCrédits = new JLabel("Crédits", JLabel.CENTER);
        cCrédits.setFont(new Font("Arial",Font.BOLD,20));
        this.endGame.add(cCrédits);
        this.idxChoiceEndGame.add(i);
        JendGame.add(cCrédits, c);
        i++;
        c.gridy = i;
        JLabel c4 = new JLabel("Quitter", JLabel.CENTER);
        c4.setFont(new Font("Arial",Font.BOLD,20));
        this.endGame.add(c4);
        this.idxChoiceEndGame.add(i);
        JendGame.add(c4, c);
        i++;
        this.add(JendGame, this.acc.keyEndGame);
    }

    private void drawEndGame(CardLayout cl){
        if(this.acc.menus.get(this.acc.keyEndGame)){
            cl.show(this, this.acc.keyEndGame);
        }
    }

    private void updateEndGame(){
        if(this.acc.menus.get(this.acc.keyEndGame)){
            for(int i = 0; i<this.idxChoiceEndGame.size(); i++){
                if(i == this.acc.getCurrentChoice()-1){
                    this.endGame.get(this.idxChoiceEndGame.get(i)).setIcon(iconSelect);
                } else {
                    this.endGame.get(this.idxChoiceEndGame.get(i)).setIcon(null);
                }
            }
            if(this.acc.typeGameOver != 0){
                ImageIcon skull = new ImageIcon(((new ImageIcon("src/Sprites/game_over.png")).getImage()).getScaledInstance(65, 45, java.awt.Image.SCALE_SMOOTH));
                this.endGame.get(1).setIcon(skull);
                if(this.acc.typeGameOver == 1){
                    this.endGame.get(1).setText("Vous vous êtes arrêté. Vous avez un pneu crevé ?");
                } else if(this.acc.typeGameOver == 2){
                    this.endGame.get(1).setText("Votre temps est épuisé, il faudra être plus rapide la prochaine fois !");
                }
            } else {
                this.endGame.get(1).setIcon(null);
                this.endGame.get(1).setText(null);
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
        this.drawCredits(cl);
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
