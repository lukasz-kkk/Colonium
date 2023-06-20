package core;

import com.badlogic.gdx.graphics.Color;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Utils.Definitions.*;

public class Client extends Thread {
    private final static int PORT = 2137;
    private final static String SERVER_IP = "20.117.176.229";
    private Socket socket;
    private OutputStream output;
    public static FileWriter fw;
    public static FileWriter rw;
    private InputStream input;
    private BufferedReader reader;
    String timeStamp;

    public static String message;
    private String response = null;
    public static List<String> lobbies = new ArrayList<>();
    public static List<String> players = new ArrayList<>();
    public static HashMap<String, Color> playersColors = new HashMap<>();
    public static String currentLobby = null;
    public static Color clientColor = Color.GRAY;
    public static int getLobbiesInfo = 0;
    public static int startMapFlag = 0;

    public static Long gold;

    static JSONObject jsonResponse = null;

    public static String clientName;

    MessageReceiver messageReceiver = new MessageReceiver();

    public void run() {
        try {
            socket = new Socket(SERVER_IP, PORT);
            output = socket.getOutputStream();
            input = socket.getInputStream();

            Thread senderThread = new Thread(this::sendMessage);
            senderThread.setName("Sender");
            Thread receiverThread = new Thread(this::receiveMessage);
            receiverThread.setName("Reciver");

            senderThread.start();
            receiverThread.start();

            senderThread.join();
            receiverThread.join();
        } catch (IOException | InterruptedException e) {
            System.out.println(ANSI_RED + "Failed to connect to server" + ANSI_RESET);
        } finally {
            try {
                if (output != null) output.close();
                if (input != null) input.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void receiveMessage() {
        try {
            fw = new FileWriter("log.txt",true);
            reader = new BufferedReader(new InputStreamReader(input));
            while ((response = reader.readLine()) != null) {
                timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                fw.write("[SERVER] "+timeStamp+ ' ' + response + '\n');
                jsonResponse = MessageUtility.reciveJSON(response);
                //System.out.println(jsonResponse);
                int error = messageReceiver.handleResponse();
                if (error != SUCCESS) {
                    System.out.println(ANSI_RED + "Error code: " + error + ANSI_RESET);
                    System.out.println(ANSI_RED + "Error Caused by response: " + response + ANSI_RESET);
                    return;
                }

                Thread.sleep(100);
                jsonResponse = null;
            }
        } catch (IOException | ParseException | InterruptedException e) {
            System.err.println(e.getMessage());
            System.err.println(jsonResponse);
        }
    }

    public void sendMessage() {
        try {
            fw = new FileWriter("log.txt",true);
            Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            while (true) {
                if (message != null) {
                    System.out.println(ANSI_GREEN + "Message written: " + message + ANSI_RESET);
                    writer.write(message);
                    timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
                    fw.write("[CLIENT] "+timeStamp+ ' ' + message + '\n');
                    Client.message = null;
                    writer.flush();
                }
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}