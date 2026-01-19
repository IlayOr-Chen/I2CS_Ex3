package server;

import utils.Index2D;

public class GameMap {
    private int[][] map;
    private int w,h;

    public static final int EMPTY = 0;
    public static final int BLUE = 1;
    public static final int PINK = 2;
    public static final int GREEN = 3;

    public GameMap(int[][] board) {
        map = board;
        w = board.length;
        h = board[0].length;
    }

    public int getWidth() { return w;}

    public int getHeight() { return h;}

    public boolean isInside(Index2D p) {
        return p.getX()>=0 && p.getX()<w && p.getY()>=0 && p.getY()<h;
    }

    public boolean isFree(Index2D p) {
        return isInside(p) && map[p.getX()][p.getY()] != BLUE;
    }

    public int get(Index2D p) {
        return map[p.getX()][p.getY()];
    }

    public void set(Index2D p, int v) {
        map[p.getX()][p.getY()] = v;
    }

    public Index2D step(Index2D p, int dir) {
        int x = p.getX(), y = p.getY();
        if(dir==Game.UP) y++;
        if(dir==Game.LEFT) x--;
        if(dir==Game.DOWN) y--;
        if(dir==Game.RIGHT) x++;
        return new Index2D((x+w)%w, (y+h)%h);
    }

    public int[][] getBoard() {
        return map;
    }
}

