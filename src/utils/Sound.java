package utils;

import javax.sound.sampled.*;

public class Sound {
    private static Clip clip;

    /**
     * Plays a sound file in an infinite loop.
     * The sound file must be located in the project's resources directory.
     *
     * @param path relative path to the sound file
     * @throws RuntimeException if the sound file cannot be loaded or played
     */
    public static void playLoop(String path) {
        try {
            // Load the audio file from the classpath (resources folder)
            AudioInputStream audio = AudioSystem.getAudioInputStream(Sound.class.getResource(path));
            // Create a clip for playing the sound
            clip = AudioSystem.getClip();
            clip.open(audio);
            // Play the sound continuously in a loop
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("Sound error");
        }
    }

    /**
     * Stops the currently playing sound.
     * If no sound is playing, this method does nothing.
     */
    public static void stop() {
        if (clip != null) clip.stop();
    }
}

