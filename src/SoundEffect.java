import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;


public class SoundEffect {
    private File soundFile = null;
    private Clip clip = null;
    private FloatControl gainControl = null;

    public float getVolume() {
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    public void setVolume(float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public SoundEffect(String filename) {
        soundFile = new File("src/Misc/SFX/" + filename + ".wav");
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume((float) ConstantSettings.settingsValues[1]/10);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public boolean isRunning() { return clip.isRunning(); }

    public void play() {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setMicrosecondPosition(0);
        clip.start();
    }

    public void stop() {
        if (clip.isActive()) {
            clip.stop();
            clip.close();
        }
    }
}