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

    public static int getIntColor(Color c, int code) {
        if(c.equals(Color.BLUE)) return 1;
        else if(c.equals(Color.PINK)) return 2;
        else if(c.equals(Color.GREEN)) return 3;
        else if(c.equals(Color.BLACK)) return 4;
        else if(c.equals(Color.WHITE)) return 5;
        return 0;
    }

    @Override
    public void move(int dir) {
        if(status != PLAY) return;

        Index2D next = map.step(pacman, dir);
        if(map.isFree(next)) pacman = next;

        int cell = map.get(pacman);

        if(cell == GameMap.PINK) {
            score++;
            map.set(pacman, GameMap.EMPTY);
        }

        if(cell == GameMap.GREEN) {
            score += 5;
            long until = System.currentTimeMillis() + 7000;
            // set each ghost eatable for 7 seconds
            for(Ghost g : ghosts) g.setEatable(until);
            map.set(pacman, GameMap.EMPTY);
        }

        for(Ghost g : ghosts) {
            g.moveRandom(map);

            if(g.getPos2D().equals(pacman)) {
                if(g.isEatable()) {
                    score += 10;
                    g.reset(new Index2D(10,10));
                } else {
                    status = DONE;
                }
            }
        }

        checkPinksLeft();
    }

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

    @Override
    public void play() { status = PLAY; }
    @Override
    public int getStatus() { return status; }
    @Override
    public int[][] getGame(int id) { return map.getBoard(); }
    @Override
    public GhostCL[] getGhosts(int id) { return ghosts; }
    @Override
    public String getPos(int id) { return pacman.getX()+","+pacman.getY(); }
    @Override
    public String getData(int id) { return "Score=" + score; }
    @Override
    public boolean isCyclic() { return cyclic; }
    @Override
    public void end(int id) { status = DONE;}
    @Override
    public void init(int scenario, String playerId, boolean cyclicMode, long randomSeed, double resolution, int dt, int level){
        this.playerId = playerId;
        this.cyclic = cyclicMode;
    }

    public String getPlayerId() {
        return playerId;
    }
    public int getWidth() {
        return map.getWidth();
    }

    public int getHeight() {
        return map.getHeight();
    }

    public Index2D getPacman() {
        return pacman;
    }

    public Ghost[] getGhosts() {
        return ghosts;
    }

    public int getScore() {
        return score;
    }

}
