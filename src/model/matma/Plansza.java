package model.matma;

import java.util.ArrayList;
import java.util.Random;

import model.Ul;
import model.zwierzeta.ZolteZwierzeta;
import model.zwierzeta.Zwierze;
import model.zwierzeta.pajaki.Pajaczek;
import model.zwierzeta.pajaki.Pajak;
import model.zwierzeta.zolte.Osa;
import model.zwierzeta.zolte.Pszczola;
import model.zwierzeta.zolte.Szerszen;

public class Plansza {
    private int row;
    private int col;
    private Pole[][] board;
    private ArrayList<Zwierze> listOfAnimals;
    private Ul hive;

    private Random random = new Random();

    public Plansza(int row, int col) {
        this.row = row;
        this.col = col;
        this.board = new Pole[row][col];
        listOfAnimals = new ArrayList<>();
        init();
    }

    public void init() {
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                board[i][j] = new Pole(i, j);


        int numBees = random.nextInt(1,8);
        int numEnemies = random.nextInt(1,3);

        for (int i = 0; i < numBees; i++) {
            /*
             * 0 - pszczola
             * 1 - osa
             * 2 - szerszen
             */
            int rodzaj = random.nextInt(3);
            switch (rodzaj) {
                case 0:
                    listOfAnimals.add(new Pszczola());
                    break;
                case 1:
                    listOfAnimals.add(new Osa());
                    break;
                case 2:
                    listOfAnimals.add(new Szerszen());
                    break;
                default:
                    listOfAnimals.add(new Pszczola());
                    break;
            }
        }
        for (int i = 0; i < numEnemies; i++) {
            /*
             * 0 - pajak
             * 1 - pajaczek
             */
            int rodzaj = random.nextInt(2);
            switch (rodzaj) {
                case 0:
                    listOfAnimals.add(new Pajak());
                    break;
                case 1:
                    listOfAnimals.add(new Pajaczek());
                    break;
                default:
                    listOfAnimals.add(new Pajak());
                    break;
            }
        }
    }

    public void placeHive(int x, int y) {
        this.hive = new Ul(x, y);
        board[y][x].setCurrentOwner(hive);
        // Wybieramy promien wokol ula
        placeHelperHive(2);
    }

    private void placeHelperHive(int r) {
        int x = hive.getX();
        int y = hive.getY();

        for (int k = 0; k < listOfAnimals.size(); k++) {
            if (!(listOfAnimals.get(k) instanceof ZolteZwierzeta))
                continue;

            int rLocal = r;
            while (true) {
                // Bufor na przegladane pole w zasiegu kola (zaokraglilem 3,14 do 4)
                Pole[] buffor = new Pole[4*(r*r)];
                int bufforSize = 0;
                int x0 = adjust_point(x-r, 0, col-1);
                int x1 = adjust_point(x+r, 0, col-1);
                int y0 = adjust_point(y-r, 0, row-1);
                int y1 = adjust_point(y+r, 0, row-1);
    
                for (int i = y0; i <= y1; i++) {
                    for (int j = x0; j <= x1; j++) {
                        int dx = x-j;
                        int dy = y-i;
                        if (dx*dx + dy*dy <= rLocal*rLocal) {
                            if (!board[i][j].isOwned()) {
                                buffor[bufforSize] = board[i][j];
                                bufforSize++;
                            }
                        }
                    }
                }
    
                // Jezeli nie 0 to sa wolne pola
                if (bufforSize != 0) {
                    int index = random.nextInt(bufforSize);
                    int xx = buffor[index].getX();
                    int yy = buffor[index].getY();
                    board[yy][xx].setCurrentOwner(listOfAnimals.get(k));
                    listOfAnimals.get(k).setX(xx);
                    listOfAnimals.get(k).setY(yy);
                    break;
                }
    
                rLocal++;
            }
        }

        for (int k = 0; k < listOfAnimals.size(); k++) {
            if (listOfAnimals.get(k) instanceof ZolteZwierzeta)
                continue;

            int y0 = random.nextInt(row);
            int x0 = random.nextInt(col);

            while (board[y0][x0].isOwned()) {
                y0 = random.nextInt(row);
                x0 = random.nextInt(col);
            }

            board[y0][x0].setCurrentOwner(listOfAnimals.get(k));
        }
    }

    public void moveAnimal(Zwierze zwierze, Wektor wektor) {
        int x = adjust_point(zwierze.getX() + wektor.getX(), 0, col-1);
        int y = adjust_point(zwierze.getY() + wektor.getY(), 0, row-1);
        int indexOfAnimal = listOfAnimals.indexOf(zwierze);
        listOfAnimals.get(indexOfAnimal).setX(x);
        listOfAnimals.get(indexOfAnimal).setY(y);
        zwierze.setX(x);
        zwierze.setY(y);
        placeHelperAnimal(zwierze);
    }

    public void addAnimal(Zwierze zwierze) {
        listOfAnimals.add(zwierze);
        moveAnimal(zwierze, new Wektor(0, 0));
    }

    private void placeHelperAnimal(Zwierze zwierze) {
        int x = zwierze.getX();
        int y = zwierze.getY();

        if (!board[y][x].isOwned()) {
            board[y][x].setCurrentOwner(zwierze);
            return;
        }

        // Jezeli miecjse bylo staramy sie umiescic jak najblizej (w obwodzie r)
        int r = 1;
        while (true) {
            // Bufor na przegladane pole w zasiegu kola (zaokraglilem 3,14 do 4)
            Pole[] buffor = new Pole[4*(r*r)];
            int bufforSize = 0;
            int x0 = adjust_point(x-r, 0, col-1);
            int x1 = adjust_point(x+r, 0, col-1);
            int y0 = adjust_point(y-r, 0, row-1);
            int y1 = adjust_point(y+r, 0, row-1);

            for (int i = y0; i <= y1; i++) {
                for (int j = x0; j <= x1; j++) {
                    int dx = x-j;
                    int dy = y-i;
                    if (dx*dx + dy*dy <= r*r) {
                        if (!board[i][j].isOwned()) {
                            buffor[bufforSize] = board[i][j];
                            bufforSize++;
                        }
                    }
                }
            }

            // Jezeli nie 0 to sa wolne pola
            if (bufforSize != 0) {
                int index = random.nextInt(bufforSize);
                int xx = buffor[index].getX();
                int yy = buffor[index].getY();
                board[yy][xx].setCurrentOwner(zwierze);
                int indexOfAnimal = listOfAnimals.indexOf(zwierze);
                listOfAnimals.get(indexOfAnimal).setX(xx);
                listOfAnimals.get(indexOfAnimal).setY(yy);
                break;
            }

            r++;
        }
    }

    // Metoda zeby kolo nie wychodzilo poza plansze
    private int adjust_point(int x, int low, int high) {
        if (x < low)
            x = low;
        if (x > high)
            x = high;
        return x;
    }

    public Pole[][] getBoard() {
        return board;
    }
    
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
