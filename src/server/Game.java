package server;

import utils.Index2D;

import java.awt.*;

public class Game implements PacmanGame {

    private GameMap map;
    private Index2D pacman;
    private Ghost[] ghosts;
    private int status = INIT;
    private int score = 0;

    private boolean cyclic;
    private String playerId;


    /**
     * Constructor: initializes the board, PacMan, and ghosts.
     */
    public Game() {
        // the board of the game map:
        int[][] board = {
                {1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,2,3,1},
                {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1,2,1},
                {1,2,1,2,2,2,2,2,1,2,2,2,1,2,2,2,2,2,1,2,1,2,1},
                {1,2,1,2,1,1,1,2,1,1,1,1,1,2,1,1,1,2,1,2,1,2,1},
                {1,2,2,2,2,2,1,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,1},
                {1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,2,1,1,1,1,1,2,1},
                {2,2,2,2,1,2,2,2,2,2,1,2,2,2,2,2,1,2,2,2,1,2,2},
                {1,2,1,2,1,1,1,1,1,2,1,2,1,1,1,1,1,2,1,2,1,2,1},
                {1,2,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1,2,2,2,1},
                {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1,2,1},
                {1,2,2,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,2,2,2,2,1},
                {1,1,1,1,1,2,1,2,1,1,1,1,1,2,1,2,1,1,1,1,1,2,1},
                {2,2,2,2,1,2,2,2,2,2,1,2,2,2,2,2,1,2,2,2,1,2,2},
                {1,2,1,2,1,1,1,1,1,2,1,2,1,1,1,1,1,2,1,2,1,2,1},
                {1,2,1,2,2,3,2,2,1,2,2,2,1,2,2,2,2,2,1,2,2,2,1},
                {1,2,1,1,1,2,1,2,1,1,1,1,1,2,1,2,1,1,1,2,1,2,1},
                {1,2,2,2,2,2,1,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,1},
                {1,2,1,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1},
                {1,3,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,2,3,1},
                {1,1,1,1,1,1,1,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };

        map = new GameMap(board);
        pacman = new Index2D(11, 9);
        ghosts = new Ghost[]{
                new Ghost(GhostCL.RANDOM_WALK0, new Index2D(1, 1)),
                new Ghost(GhostCL.RANDOM_WALK0, new Index2D(1, 21)),
                new Ghost(GhostCL.RANDOM_WALK0, new Index2D(19, 1)),
                new Ghost(GhostCL.RANDOM_WALK0, new Index2D(19, 21))
        };
    }

    /**
     * Converts a Color to an internal integer code.
     * @param c the color
     * @param code unused
     * @return integer code for the color
     */
    public static int getIntColor(Color c, int code) {
        if(c.equals(Color.BLUE)) return 1;
        else if(c.equals(Color.PINK)) return 2;
        else if(c.equals(Color.GREEN)) return 3;
        else if(c.equals(Color.BLACK)) return 4;
        else if(c.equals(Color.WHITE)) return 5;
        return 0;
    }

    /**
     * Moves PacMan in the specified direction and updates ghosts.
     * Handles scoring, eatable ghosts, and collisions.
     * @param dir direction of PacMan (UP, DOWN, LEFT, RIGHT)
     */
    @Override
    public void move(int dir) {
        if(status != PLAY) return;

        // calculate next PacMan position
        Index2D next = map.step(pacman, dir);
        if(map.isFree(next)) pacman = next;

        int cell = map.get(pacman);

        // eating a pink cell
        if(cell == GameMap.PINK) {
            score++;
            map.set(pacman, GameMap.EMPTY);
        }

        // eating a green cell – all ghosts become eatable for 7 seconds
        if(cell == GameMap.GREEN) {
            score += 5;
            long until = System.currentTimeMillis() + 7000;
            // set each ghost eatable for 7 seconds
            for(Ghost g : ghosts) g.setEatable(until);
            map.set(pacman, GameMap.EMPTY);
        }

        // move ghosts and check collisions with PacMan
        for(Ghost g : ghosts) {
            g.moveRandom(map);

            if(g.getPos2D().equals(pacman)) {
                if(g.isEatable()) {
                    score += 10;
                    g.reset(new Index2D(10,10));
                } else {
                    status = LOSE;
                }
            }
        }

        // check if any pinks are left
        checkPinksLeft();
    }

    /**
     * Checks if any pink cells remain on the map.
     * If none remain, the game is marked as DONE.
     */
    public void checkPinksLeft() {
        boolean pinkLeft = false;
        for(int y=0; y<map.getHeight(); y++) {
            for(int x=0; x<map.getWidth(); x++) {
                if(map.get(new Index2D(x,y)) == GameMap.PINK) {
                    pinkLeft = true;
                    break;
                }
            }
            if(pinkLeft) break;
        }

        // if there isn't any pinks left - game over
        if(!pinkLeft)
            status = DONE;
    }

    /** Starts the game by setting status to PLAY */
    @Override
    public void play() { status = PLAY; }

    /** Returns the current game status */
    @Override
    public int getStatus() { return status; }

    /** Returns the current board as a 2D int array */
    @Override
    public int[][] getGame(int id) { return map.getBoard(); }

    /** Returns the array of ghosts */
    @Override
    public GhostCL[] getGhosts(int id) { return ghosts; }

    /** Returns PacMan's current position as a string "x,y" */
    @Override
    public String getPos(int id) { return pacman.getX()+","+pacman.getY(); }

    /** Ends the game by setting status to DONE */
    @Override
    public void end(int id) { status = DONE; }

    /**
     * Initializes the game with parameters
     * @param playerId player identifier
     * @param cyclicMode whether the map wraps around edges
     */
    @Override
    public void init(String playerId, boolean cyclicMode){
        this.playerId = playerId;
        this.cyclic = cyclicMode;
    }

    /** Returns the player ID */
    public String getPlayerId() { return playerId; }

    /** Returns the width of the map */
    public int getWidth() { return map.getWidth(); }

    /** Returns the height of the map */
    public int getHeight() { return map.getHeight(); }

    /** Returns PacMan's position as Index2D */
    public Index2D getPacman() { return pacman; }

    /** Returns the array of ghosts */
    public Ghost[] getGhosts() { return ghosts; }

    /** Returns current score */
    public int getScore() {
        return score;
    }

}
