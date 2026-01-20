package server;

import utils.Index2D;

public class GameMap {
    private int[][] map;
    private int w,h;

    public static final int EMPTY = 0;// free space
    public static final int BLUE = 1;  // wall
    public static final int PINK = 2;  // pink point
    public static final int GREEN = 3; // green point (power-up)

    /**
     * Constructs a new GameMap from a 2D array of integers.
     *
     * @param board 2D array representing the map layout
     */
    public GameMap(int[][] board) {
        map = board;
        w = board.length;
        h = board[0].length;
    }

    /**
     * Returns the width of the map.
     *
     * @return number of columns
     */
    public int getWidth() {
        return w;
    }

    /**
     * Returns the height of the map.
     *
     * @return number of rows
     */
    public int getHeight() {
        return h;
    }

    /**
     * Checks if a position is inside the map boundaries.
     *
     * @param p position to check
     * @return true if inside, false otherwise
     */
    public boolean isInside(Index2D p) {
        return p.getX() >= 0 && p.getX() < w && p.getY() >= 0 && p.getY() < h;
    }

    /**
     * Checks if a position is free to move (not a wall and inside the map).
     *
     * @param p position to check
     * @return true if the position is free, false if wall or outside
     */
    public boolean isFree(Index2D p) {
        return isInside(p) && map[p.getX()][p.getY()] != BLUE;
    }

    /**
     * Returns the value of a cell at a given position.
     *
     * @param p the position
     * @return cell value (EMPTY, BLUE, PINK, GREEN)
     */
    public int get(Index2D p) {
        return map[p.getX()][p.getY()];
    }

    /**
     * Sets a cell at a given position to a specific value.
     *
     * @param p the position
     * @param v the value to set (EMPTY, BLUE, PINK, GREEN)
     */
    public void set(Index2D p, int v) {
        map[p.getX()][p.getY()] = v;
    }

    /**
     * Computes the next position when stepping from a given position in a direction.
     *
     * @param p starting position
     * @param dir direction (Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT)
     * @return new Index2D after moving one step in the given direction
     */
    public Index2D step(Index2D p, int dir) {
        int x = p.getX(), y = p.getY();
        if(dir==Game.UP) y++;
        if(dir==Game.LEFT) x--;
        if(dir==Game.DOWN) y--;
        if(dir==Game.RIGHT) x++;
        // wrap around if outside boundaries
        return new Index2D((x+w)%w, (y+h)%h);
    }

    /**
     * Returns the internal 2D array representing the board.
     *
     * @return 2D array of cell values
     */
    public int[][] getBoard() {
        return map;
    }
}

