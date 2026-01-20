package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Game;
import utils.Index2D;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Game game;

    /**
     * Set up the object before each test
     */
    @BeforeEach
    void setup() {
        game = new Game();
        game.init("playerId", false);
    }

    /**
     * Test getIntColor method
     */
    @Test
    void testGetIntColor() {
        assertEquals(1, Game.getIntColor(Color.BLUE, 0));
        assertEquals(2, Game.getIntColor(Color.PINK, 0));
        assertEquals(3, Game.getIntColor(Color.GREEN, 0));
        assertEquals(4, Game.getIntColor(Color.BLACK, 0));
        assertEquals(5, Game.getIntColor(Color.WHITE, 0));
        assertEquals(0, Game.getIntColor(Color.ORANGE, 0));
    }

    /**
     * Test getters methods
     */
    @Test
    void testGetters() {
        assertNotNull(game.getPacman());
        assertNotNull(game.getGhosts());
        assertTrue(game.getWidth() > 0);
        assertTrue(game.getHeight() > 0);
    }

    /**
     * Test move method
     */
    @Test
    void testMove() {
        // נתחיל את המשחק כדי שמצב PLAY יאפשר תזוזה
        game.play();
        int initScore = game.getScore();

        game.move(Game.LEFT);

        Index2D pacmanPos = game.getPacman();
        assertNotNull(pacmanPos);

        // check if the pacman is in the map range after move
        assertTrue(pacmanPos.getX() >= 0 && pacmanPos.getX() < game.getWidth());
        assertTrue(pacmanPos.getY() >= 0 && pacmanPos.getY() < game.getHeight());
        // check if the score isn't lower
        assertTrue(game.getScore() >= initScore);
    }

    /**
     * Test checkPinksLeft method
     */
    @Test
    void testCheckPinksLeft() {
        game.play();
        // empty the board - remove all the pinks dots
        int[][] board = game.getGame(0);
        for (int x = 0; x < board.length; x++)
            for (int y = 0; y < board[0].length; y++)
                board[x][y] = 0;

        game.move(0);
        // the game should be ended
        assertEquals(Game.DONE, game.getStatus());
    }

}
