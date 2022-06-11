package controller;

import model.matma.Plansza;
import model.matma.Wektor;
import view.Panel;

public class Symulacja {
    public Symulacja(Plansza plansza, Panel panel) {
        Wektor wektorUl = panel.saveCoords();
        plansza.placeHive(wektorUl.getX(), wektorUl.getY());
        panel.draw(plansza.getBoard());
    }
}
