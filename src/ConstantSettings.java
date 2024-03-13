import java.io.*;

public class ConstantSettings {

    public static int[] settingsValues = new int[3];
    public static int BGMSelection = 0;

    public static void init(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/Misc/settings"));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i > 2){
                    BGMSelection = Integer.parseInt(line);
                    break;
                }
                settingsValues[i] = Integer.parseInt(line);
                i++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            settingsValues = new int[]{5, 5, 15};
        }
        finally {
            if (BGMSelection == 0){
                Main.selectedMusic = "LostFuture";
            }
            else if (BGMSelection == 1){
                Main.selectedMusic = "SpaceFlight";
            }
            else{
                Main.selectedMusic = "Stardust";
            }
        }
    }

    public static void writeSettingToFile(){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/Misc/settings", false));
            for (int i : settingsValues){
                bw.write(i + "\n");
            }
            bw.write(Integer.toString(BGMSelection));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
