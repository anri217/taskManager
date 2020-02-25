package server.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import server.MonoClientThread;
import server.ServerFacade;
import server.controller.Controller;
import server.view.RefreshHelper;
import shared.Command;
import shared.CommandCreator;
import shared.JsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CancelTaskHandler implements Handler {

    @Override
    public void handle(Command command) throws IOException {
        Controller controller = Controller.getInstance();
        ArrayList<Integer> ids = (ArrayList<Integer>) command.getContent();
        for (int i = 0; i < ids.size(); i++) {
            controller.cancelTask(ids.get(i));
        }
        RefreshHelper.getInstance().getMainWindowController().refresh();
        HashMap<Integer, MonoClientThread> clients = (HashMap<Integer, MonoClientThread>) ServerFacade.getInstance().getClients();
        String entry = CommandCreator.getInstance().createStringCommand(0, controller.getAll());
        for (int port : clients.keySet()) {
            clients.get(port).sendCommand(entry);
        }
    }

}
