package server;

import server.controller.utils.Paths;
import server.controller.utils.PropertyParser;
import server.controller.utils.portgenerator.PortGenerator;
import server.exceptions.PropertyParserInitException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerFacade {
    private static ServerFacade instance;

    public static synchronized ServerFacade getInstance() {
        if (instance == null) {
            instance = new ServerFacade();
        }
        return instance;
    }

    private Map<Integer, MonoClientThread> clients;

    public Map<Integer, MonoClientThread> getClients() {
        return clients;
    }

    public void setClients(Map<Integer, MonoClientThread> clients) {
        this.clients = clients;
    }

    private static ExecutorService executeIt = Executors.newFixedThreadPool(5);


    private ServerFacade() {
        clients = new HashMap<Integer, MonoClientThread>();
    }

    public void connect() throws IOException, PropertyParserInitException {
        PropertyParser parser = new PropertyParser(Paths.SERVER);
        try (ServerSocket server = new ServerSocket(Integer.parseInt(parser.getProperty("port")))) {
            while (!server.isClosed()) {
                Socket client = server.accept();
                int port = PortGenerator.getInstance().getPort();
                MonoClientThread thread = new MonoClientThread(client, port);
                clients.put(port, thread);
                executeIt.execute(thread);
                System.out.println("Connection accepted");
            }
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
