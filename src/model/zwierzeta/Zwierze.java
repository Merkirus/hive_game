package model.zwierzeta;

public abstract class Zwierze {
    protected int speed;
    protected int size;
    protected String name;
    protected boolean alive = true;

    /*
     * Wspolrzedne zwierzecie na planszy
     */
    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
