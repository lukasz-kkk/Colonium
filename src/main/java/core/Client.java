package core;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Client extends Thread {
    private final static int PORT = 2137;
    private final static String SERVER_IP = "20.117.176.229";
    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private BufferedReader reader;

    public static String message;
    private String mapRequest = MessageUtility.createMapUpdateRequest();
    private String response = null;
    public static List<String> lobbies;
    public static String currentLobby;

    public static int getLobbiesInfo = 0;

    static JSONObject jsonResponse = null;

    public static String clientName;

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
            throw new RuntimeException(e);
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
            reader = new BufferedReader(new InputStreamReader(input));
            String response;
            while ((response = reader.readLine()) != null) {
                if(response.charAt(0) == 'J'){
                    System.out.println("Error Here");
                    System.out.println(response);
                    return;
                }
                //System.out.println(response);
//                System.out.println("Response from server: \n" + response);
                jsonResponse = MessageUtility.reciveJSON(response);
                if(getLobbiesInfo == 1) {
                    lobbies = MessageUtility.jsonDecodeLobbies(jsonResponse.toString());
                    getLobbiesInfo = 0;
                }
                //System.out.println(jsonResponse);
//                if(jsonResponse.get("type").equals("lobbies")){
//                    List<String> lobbyInfoList = MessageUtility.jsonDecodeLobbies(response);
//                    for (String lobbyInfo : lobbyInfoList) {
//                        System.out.println(lobbyInfo);
//                    }
//                }
                Thread.sleep(100);
                jsonResponse = null;
            }
        } catch (IOException | ParseException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMessage() {
        try {
            Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            while (true) {
                //System.out.println("Sending request to Socket Server");
                if (message != null) {
                    //System.out.println(message);
                    writer.write(message);
                    Client.message = null;
                    writer.flush();
                }
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMapRequest(){
        try{
            Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            while(true){
                writer.write(mapRequest);
                writer.flush();
                Thread.sleep(250);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}