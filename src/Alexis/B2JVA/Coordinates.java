package Alexis.B2JVA;

import java.io.Serializable;

/**
 * @User: CHEVALIER Alexis <Alexis.Chevalier@supinfo.com>
 * @Date: 09/02/13
 */

public class Coordinates implements Serializable {
    private int x;
    private int y;

    //Constructeur
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Getters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
