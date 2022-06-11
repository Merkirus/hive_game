package model.matma;

public class Pole {
    private Object currentOwner;
    private boolean owned;
    private int x;
    private int y;

    public Pole(int x, int y) {
        this.x = x;
        this.y = y;
        this.owned = false;
    }

    public Object getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(Object currentOwner) {
        this.currentOwner = currentOwner;
        this.owned = true;
        if (currentOwner == null)
            owned = false;
    }
    
    public boolean isOwned() {
        return owned;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
