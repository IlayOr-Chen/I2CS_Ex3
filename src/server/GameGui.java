package server;

import utils.Index2D;
import java.awt.*;

public class GameGui {

    private final Game game;
    private boolean automatic = true;

    public GameGui(Game game) {
        this.game = game;

        StdDraw.setCanvasSize(620, 620);
        StdDraw.setXscale(-1, game.getWidth());
        StdDraw.setYscale(-1, game.getHeight() + 2);
        StdDraw.enableDoubleBuffering();
    }

    public void showStartScreen() {
        StdDraw.clear(Color.BLACK);

        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(game.getWidth()/2.0, game.getHeight()/2.0 + 4, "PACMAN GAME");

        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(game.getWidth()/2.0, game.getHeight()/2.0 + 1, "Press A - Automatic");
        StdDraw.text(game.getWidth()/2.0, game.getHeight()/2.0 - 1, "Press M - Manual");

        StdDraw.show();

        waitForMode();
    }

    private void waitForMode() {
        while(true) {
            if(StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if(c == 'A') {
                    automatic = true;
                    break;
                }
                else if(c == 'M') {
                    automatic = false;
                    break;
                }
            }
            StdDraw.pause(20);
        }
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public void drawMap() {
        StdDraw.clear();

        double cellSize = 0.9;
        int[][] b = game.getGame(0);
        for(int x=0;x<b.length;x++) {
            for(int y=0;y<b[0].length;y++) {
                if(b[x][y] == GameMap.BLUE)
                    StdDraw.picture(x, y, "./resources/wall.png", cellSize, cellSize);
                else if(b[x][y] == GameMap.PINK)
                    StdDraw.picture(x, y, "./resources/pink_circle.jpg", cellSize, cellSize);
                else if(b[x][y] == GameMap.GREEN)
                    StdDraw.picture(x, y, "./resources/green_circle.jpg", cellSize, cellSize);
                else if(b[x][y] == GameMap.EMPTY)
                    StdDraw.picture(x, y, "./resources/floor.jpg", cellSize, cellSize);
            }
        }

        // Pacman
        StdDraw.picture(game.getPacman().getX(), game.getPacman().getY(), "./resources/pacman.jpg", cellSize, cellSize);

        // Ghosts
        for(Ghost g : game.getGhosts()) {
            Index2D gPos = g.getPos2D();
            if(g.isEatable())
                StdDraw.picture(gPos.getX(), gPos.getY(), "./resources/white_ghost.jpg", cellSize, cellSize);
            else
                StdDraw.picture(gPos.getX(), gPos.getY(), "./resources/ghost.jpg", cellSize, cellSize);
        }

        // Score
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.textLeft(0, b[0].length + 0.5, "Score: " + game.getScore());

        // Player ID
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 12)); // קטן יותר
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.textLeft(0, b[0].length + 1, "Player: " + game.getPlayerId());

        if(game.getStatus() == Game.DONE) {
            StdDraw.setFont(new Font("Arial", Font.BOLD, 40));
            StdDraw.setPenColor(Color.RED);
            StdDraw.text(game.getWidth()/2.0, game.getHeight()/2.0 + 1, "GAME OVER");
        }

        StdDraw.show();
    }
}
