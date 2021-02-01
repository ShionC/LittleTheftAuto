package vue;

import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.Area;

public class VueBackground {

    // ********************************** 1) Attributs **********************************

    private int horizon;
    private ArrayList<Point> montagnes;
    private Area route;

    private Affichage aff;

    public VueBackground(Affichage aff){
        this.aff = aff;
        //Initialiser montagnes
    }

    /**
     * Cree la forme de la route
     * @return
     */
    private Area getShapeRoute(){
        return null;
    }

    /**
     * Dessine la pelouse, l'horizon et les montagnes de fond
     * @param g
     */
    private void drawFond(Graphics2D g2){

    }

    /**
     * Dessine l arriere plan
     * @param g2
     */
    public void drawBackground(Graphics2D g2){

        drawFond(g2);

        //Dessine la route
        ArrayList<Point> list = this.aff.route.getRoute();
        for(int i = 1; i<list.size(); i++){

            Point p = list.get(i-1);
            Point x = list.get(i);
            g2.drawLine(p.x,p.y,x.x,x.y);
        }
    }



}
