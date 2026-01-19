package server;

public interface GhostCL {
    int INIT = 0;
    int PLAY = 1;
    int PAUSE = 2;
    int RANDOM_WALK0 = 10;
    int RANDOM_WALK1 = 11;
    int GREEDY_SP = 12;

    public int getType();

    public String getPos(int code);

    public double remainTimeAsEatable(int code);

    public int getStatus();
}
