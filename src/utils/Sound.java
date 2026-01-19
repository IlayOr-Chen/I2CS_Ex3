package utils;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {
    private static Clip clip;

    public static void playLoop(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(Sound.class.getResource(path));
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("Sound error");
        }
    }

    public static void stop() {
        if (clip != null) clip.stop();
    }
}

