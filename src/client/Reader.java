package client;

import shared.JsonParser;

import java.io.DataInputStream;

public class Reader extends Thread {
    private boolean exit;
    private DataInputStream dis;

    public Reader(DataInputStream dis) {
        this.dis = dis;
        this.exit = true;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }


    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public void run() {
        try {
            CommandProcessor processor = CommandProcessor.getInstance();
            while (this.exit) {
                Thread.sleep(2000);
                System.out.println("Client start waiting messages from server");
                String answer = dis.readUTF();
                System.out.println(answer);
                JsonParser parser = new JsonParser(answer);
                parser.parseCommand();
                processor.processCommand(parser.getCommand());
                System.out.println("Client get message from server" + answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
