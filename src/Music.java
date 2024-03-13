import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;


public class Music{
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

    public void setSoundFile(String filename) {
        soundFile = new File("src/Misc/Music/" + filename + ".wav");
    }

    public void openSoundFile() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying(){
        return clip.isRunning();
    }

    public void playLong(long time){
        float toVolume = ConstantSettings.settingsValues[0];
        play(time);
        setVolume(0);
        new Thread(() -> {
            float volume = 0;
            while(volume < toVolume){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                volume += 0.01;
                setVolume(volume/10);
            }
        }).start();
    }

    public void play() {
        setVolume((float) ConstantSettings.settingsValues[0]/10);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void play(long time) {
        clip.setMicrosecondPosition(time);
        play();

    }

    public void stop() {
        if(clip.isActive()) {
            clip.stop();
            clip.close();
        }
    }

    public long pause() {
        if(clip.isActive()) {
            long t = clip.getMicrosecondPosition();
            stop();
            return t;
        }
        return 0;
    }
}