package server;

public interface PacmanGame {
    int INIT = 0;
    int PLAY = 1;
    int PAUSE = 2;
    int DONE = 3;
    int ERR = -1;
    int STAY = 0;
    int LEFT = 2;
    int RIGHT = 4;
    int UP = 1;
    int DOWN = 3;

    public String getPos(int id);

    public GhostCL[] getGhosts(int id);

    public int[][] getGame(int id);

    public void move(int dir);

    public void play();

    public void end(int id);

    public String getData(int id);

    public int getStatus();

    public boolean isCyclic();

    public void init(int scenario, String playerId, boolean cyclicMode, long randomSeed, double resolution, int dt, int level);
}
