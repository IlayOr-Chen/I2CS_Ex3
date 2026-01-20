package tests;

import org.junit.jupiter.api.Test;
import utils.Sound;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SoundTest {

    /**
     * Test that calling playLoop with invalid path
     * does not throw an exception.
     */
    @Test
    void testPlayLoop() {
        assertDoesNotThrow(() -> Sound.playLoop("not exist"));
    }

    /**
     * Test that stop can be called safely
     * even if no sound was played.
     */
    @Test
    void testStop() {
        assertDoesNotThrow(Sound::stop);
    }

}
