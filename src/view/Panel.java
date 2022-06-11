package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Ul;
import model.matma.Pole;
import model.matma.Wektor;
import model.zwierzeta.Pajeczaki;
import model.zwierzeta.ZolteZwierzeta;

public class Panel extends JFrame {
    private static final int WYS = 64;
    private static final int SZE = 64;
    private static final int WYS_ZDJ = 60;
    private static final int SZE_ZDJ = 60;
    private JPanel mainPanel;
    Image[] images = new Image[3];

    private int xUl;
    private int yUl;

    public Panel(int row, int col) {
        try {
            Path path = FileSystems.getDefault().getPath(".", "/src/view/zdjecia/pszczola.png");
            images[0] = ImageIO.read(new File(path.toString())).getScaledInstance(SZE_ZDJ, WYS_ZDJ, 0);
            path = FileSystems.getDefault().getPath(".", "/src/view/zdjecia/pajak.png");
            images[1] = ImageIO.read(new File(path.toString())).getScaledInstance(SZE_ZDJ, WYS_ZDJ, 0);
            path = FileSystems.getDefault().getPath(".", "/src/view/zdjecia/ul.png");
            images[2] = ImageIO.read(new File(path.toString())).getScaledInstance(SZE_ZDJ, WYS_ZDJ, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setTitle("Gra");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setBounds(0, 0, SZE*col, WYS*row);

        JFrame temp = new JFrame();
        String output = JOptionPane.showInputDialog(temp, "Podaj współrzędne ula (x,y)");
        String[] arr = output.split(",");

        xUl = Integer.parseInt(arr[0]);
        yUl = Integer.parseInt(arr[1]);

        this.setVisible(true);
    }

    public void draw(Pole[][] board) {
        mainPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.black);
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[0].length; j++) {
                        g.fillRect( j*SZE, i*WYS, 2, WYS);
                        g.fillRect( j*SZE, i*WYS, SZE, 2);
                    }
                }
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[0].length; j++) {
                        Image image = null;
                        if (board[i][j].isOwned()) {
                            if (board[i][j].getCurrentOwner() instanceof ZolteZwierzeta)
                                image = images[0];
                            if (board[i][j].getCurrentOwner() instanceof Pajeczaki)
                                image = images[1];
                            if (board[i][j].getCurrentOwner() instanceof Ul)
                                image = images[2];
                        }
                        g.drawImage(image, j*SZE+4, i*WYS+4, j*SZE+SZE_ZDJ, i*WYS+SZE_ZDJ,  0, 0, SZE_ZDJ, WYS_ZDJ, Color.white, null);
                    }
                }
            }
        };
        this.getContentPane().add(mainPanel);
        this.setVisible(true);
    }

    public Wektor saveCoords() {
        return new Wektor(xUl, yUl);
    }
}
