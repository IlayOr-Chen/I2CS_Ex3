package server;

import utils.Index2D;
import java.awt.*;

public class GameGui {

    private final Game game;
    private boolean automatic = true;

    /**
     * Constructs a new Game GUI and sets up the canvas.
     *
     * @param game the game instance to visualize
     */
    public GameGui(Game game) {
        this.game = game;

        StdDraw.setCanvasSize(620, 620);
        StdDraw.setXscale(-1, game.getWidth());
        StdDraw.setYscale(-1, game.getHeight() + 2);
        StdDraw.enableDoubleBuffering();
    }

    /**
     * Shows the starting screen and waits for player mode input (Automatic or Manual).
     */
    public void showStartScreen() {
        StdDraw.clear(Color.BLACK);

        // Game title
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(game.getWidth()/2.0, game.getHeight()/2.0 + 4, "PACMAN GAME");

        // Instructions for mode selection
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(game.getWidth()/2.0, game.getHeight()/2.0 + 1, "Press A - Automatic");
        StdDraw.text(game.getWidth()/2.0, game.getHeight()/2.0 - 1, "Press M - Manual");

        StdDraw.show();

        waitForMode();
    }

    /**
     * Waits for the player to press 'A' or 'M' to select game mode.
     * This is a blocking loop until a valid key is pressed.
     */
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

    /**
     * Returns whether the player chose automatic mode.
     *
     * @return true if automatic, false if manual
     */
    public boolean isAutomatic() {
        return automatic;
    }

    /**
     * Draws the current game state on the canvas.
     * This includes:
     * - Walls, floor, pink and green points
     * - Pac-Man
     * - Ghosts (normal or eatable)
     * - Score and player info
     * - "WINNER" or "GAME OVER" message if game ended
     */
    public void drawMap() {
        StdDraw.clear();

        double cellSize = 0.9;
        int[][] b = game.getGame(0);

        // Draw map cells (walls, points, floor)
        for(int x=0;x<b.length;x++) {
            for(int y=0;y<b[0].length;y++) {
                if(b[x][y] == GameMap.BLUE)
                    StdDraw.picture(x, y, "resources/wall.png", cellSize, cellSize);
                else if(b[x][y] == GameMap.PINK)
                    StdDraw.picture(x, y, "resources/pink_circle.jpg", cellSize, cellSize);
                else if(b[x][y] == GameMap.GREEN)
                    StdDraw.picture(x, y, "resources/green_circle.jpg", cellSize, cellSize);
                else if(b[x][y] == GameMap.EMPTY)
                    StdDraw.picture(x, y, "resources/floor.jpg", cellSize, cellSize);
            }
        }

        // Draw Pac-Man
        StdDraw.picture(game.getPacman().getX(), game.getPacman().getY(), "resources/pacman.jpg", cellSize, cellSize);

        // Draw Ghosts
        for(Ghost g : game.getGhosts()) {
            Index2D gPos = g.getPos2D();
            if(g.isEatable())
                StdDraw.picture(gPos.getX(), gPos.getY(), "resources/white_ghost.jpg", cellSize, cellSize);
            else
                StdDraw.picture(gPos.getX(), gPos.getY(), "resources/ghost.jpg", cellSize, cellSize);
        }

        // Draw score
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.textLeft(0, b[0].length + 0.5, "Score: " + game.getScore());

        // Draw player info
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 12)); // קטן יותר
        StdDraw.setPenColor(Color.BLUE);
        StdDraw.textLeft(0, b[0].length + 1, "Player: " + game.getPlayerId());

        // Draw end-game messages
        if(game.getStatus() == Game.DONE) {
            StdDraw.setFont(new Font("Arial", Font.BOLD, 40));
            StdDraw.setPenColor(Color.YELLOW);
            StdDraw.text(game.getWidth()/2.0, game.getHeight()/2.0 + 1, "WINNER");
        }
        else if(game.getStatus() == Game.LOSE) {
            StdDraw.setFont(new Font("Arial", Font.BOLD, 40));
            StdDraw.setPenColor(Color.RED);
            StdDraw.text(game.getWidth()/2.0, game.getHeight()/2.0 + 1, "GAME OVER");
        }

        StdDraw.show();
    }
}
