import controller.Symulacja;
import model.matma.Plansza;
import view.Panel;

public class App {
    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        Plansza plansza = new Plansza(10,10);
        Panel panel = new Panel(10, 10);
        Symulacja symulacja = new Symulacja(plansza, panel);
    }
}
