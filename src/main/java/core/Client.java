package core;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client extends Thread {
    private final static int PORT = 2137;
    private final static String SERVER_IP = "20.117.176.229";
    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private BufferedReader reader;

    public static void setMessage(String message) {
        Client.message = message;
    }

    public static String message;
    private String messageDisc = "disconunu";
    private String response;

    public void run() {
        try {
            socket = new Socket(SERVER_IP, PORT);
            output = socket.getOutputStream();
            input = socket.getInputStream();

            Thread senderThread = new Thread(this::sendMessage);
            Thread receiverThread = new Thread(this::receiveMessage);

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
                System.out.println("Response from server: " + response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendMessage() {
        try {
            Writer writer = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            while (true) {
                System.out.println("Sending request to Socket Server");
                if (message != null) {
                    writer.write(message);
                    Client.message = null;
                    writer.flush();
                }
                Thread.sleep(1000);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}