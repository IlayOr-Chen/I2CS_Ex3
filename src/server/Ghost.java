package server;

import utils.Index2D;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Ghost implements GhostCL {
    private int type;
    private Index2D pos;
    private boolean eatable;
    private long eatableUntil;
    private int status;

    public Ghost(int type, Index2D start) {
        this.type = type;
        this.pos = start;
        this.eatable = false;
        this.status = PLAY;
    }

    public void setEatable(long until) {
        this.eatable = true;
        this.eatableUntil = until;
    }

    public void clearEatable() {
        this.eatable = false;
    }

    public boolean isEatable() {
        return eatable && System.currentTimeMillis() < eatableUntil;
    }

    public void reset(Index2D start) {
        pos = start;
        eatable = false;
    }

    public void moveRandom(GameMap map) {
        int[] dirs = {Game.UP,Game.LEFT,Game.DOWN,Game.RIGHT};
        Random r = new Random();

        for (int i = 0; i < dirs.length; i++) {
            int d = dirs[r.nextInt(dirs.length)];
            Index2D nextPixel = map.step(pos, d);
            if(map.isFree(nextPixel)) {
                pos = nextPixel;
                break;
            }
        }
    }

    @Override
    public int getType() { return type; }

    @Override
    public String getPos(int code) {
        return pos.getX() + "," + pos.getY();
    }

    @Override
    public double remainTimeAsEatable(int code) {
        if(!isEatable())
            return 0;
        return (eatableUntil - System.currentTimeMillis()) / 1000.0;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public Index2D getPos2D() {
        return pos;
    }
}