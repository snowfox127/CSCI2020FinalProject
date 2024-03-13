import java.io.*;
import java.net.*;

// client side of the SocketIO-based leaderboard system, requires an open LeaderBoardServer on the same port to work
public class LeaderBoardClient {

    static DataInputStream isFromServer;
    static DataOutputStream osToServer;
    static int[] scores = new int[]{0,0,0,0,0,0,0,0};
    static String[] names = new String[]{"","","","","","","",""};
    static PrintWriter writer;
    static BufferedReader reader;

    public LeaderBoardClient() {
        try {
            //seek out LeaderBoardServer
            Socket connectToServer = new Socket("localhost", 8000);
            if(connectToServer==null) {
                System.exit(0);
            }

            //connect socketIO streams
            isFromServer = new DataInputStream(connectToServer.getInputStream());
            osToServer = new DataOutputStream(connectToServer.getOutputStream());

            writer = new PrintWriter(osToServer, true);
            reader = new BufferedReader(new InputStreamReader(isFromServer));
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getScores(int i) {
        return scores[i];
    }

    public String getNames(int i) {
        return names[i];
    }

    //update data between Client and Server side
    public void connectToServer() {

        try {
            for(int i=0; i<8; i++) {
                scores[i]=Integer.parseInt(reader.readLine());
                names[i]=reader.readLine();
            }
            if(Main.newScoreReady) {
                writer.println(Main.score);
                writer.println(Main.username);
                Main.newScoreReady=false;
            }
            else {
                writer.println(-1);
                writer.println("Anonymous");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}