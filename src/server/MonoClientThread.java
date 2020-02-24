package server;

import shared.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MonoClientThread extends Thread {

    private Socket clientSocket;
    private int port;
    private boolean exit;
    private DataOutputStream stream;


    public MonoClientThread(Socket client, int port) {
        this.clientSocket = client;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        this.exit = true;
        try (DataInputStream dis = new DataInputStream(this.clientSocket.getInputStream());
             DataOutputStream dos = new DataOutputStream(this.clientSocket.getOutputStream())) {
            dos.writeUTF(String.valueOf(this.port));
            dos.flush();
            try (Socket secondClientSocket = new Socket(this.clientSocket.getInetAddress().getHostAddress(), this.port);
                 DataOutputStream secondDos = new DataOutputStream(secondClientSocket.getOutputStream())) {
                this.stream = secondDos;
                CommandProcessor processor = CommandProcessor.getInstance();
                while (this.exit) {
                    Thread.sleep(2000);
                    System.out.println("Server start waiting message from client");
                    String answer = dis.readUTF();
                    System.out.println(answer);
                    JsonParser parser = new JsonParser(answer);
                    parser.parseCommand();
                    try {
                        processor.processCommand(parser.getCommand());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("READ from clientDialog message - " + answer);
                }
                System.out.println("Client disconnected");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            if (!this.exit) {
                System.out.println("Catch exception to correct close connections");
            } else {
                e.printStackTrace();
            }
        } finally {
            try {
                this.clientSocket.close();
                System.out.println("Connections are closed");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCommand(String entry) throws IOException {
        this.stream.writeUTF(entry);
        this.stream.flush();
    }
}
