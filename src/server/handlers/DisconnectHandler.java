package server.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import server.MonoClientThread;
import server.ServerFacade;
import shared.Command;
import shared.CommandCreator;
import shared.Handler;
import shared.JsonBuilder;

import java.io.IOException;
import java.util.HashMap;

public class DisconnectHandler implements Handler {
    private int port;

    @Override
    public void handle(Command command) throws IOException {
        ServerFacade facade = ServerFacade.getInstance();
        port = (int) command.getContent();
        HashMap<Integer, MonoClientThread> map = (HashMap<Integer, MonoClientThread>) facade.getClients();
        map.get(port).setExit(false);
        map.get(port).sendCommand(createStringCommand());
        map.remove(port);
        ServerFacade.getInstance().setClients(map);
    }

    private String createStringCommand() throws JsonProcessingException {
        Command newCommand = CommandCreator.getInstance().createCommand(71, port);
        JsonBuilder.getInstance().createJsonString(newCommand);
        return JsonBuilder.getInstance().createJsonString(newCommand);
    }
}
