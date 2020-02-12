package server;

import shared.Command;
import shared.JsonParser;
import shared.model.Task;

import java.io.*;
import java.net.Socket;

public class MonoThreadClientHandler implements Runnable {

    private static Socket clientDialog;

    public MonoThreadClientHandler(Socket client) {
        MonoThreadClientHandler.clientDialog = client;
    }

    @Override
    public void run() {
        try (DataInputStream br = new DataInputStream(clientDialog.getInputStream());
                DataOutputStream dos = new DataOutputStream(clientDialog.getOutputStream())) {
            while (!clientDialog.isClosed()) {
                System.out.println("Server start waiting message from client");
                String entry = br.readUTF();
                JsonParser parser = new JsonParser(entry);
                parser.parseCommand();
                Command command = parser.getCommand();
               /* CommandProcessor processor = new CommandProcessor(command);
                processor.chooseActivity();*/
                System.out.println("READ from clientDialog message - " + entry);

                if (entry.equalsIgnoreCase("quit")) {
                    System.out.println("Client initialize connections suicide ...");
                    dos.writeUTF("Server reply - " + entry + " - OK");
                    break;
                }
            }
            clientDialog.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
