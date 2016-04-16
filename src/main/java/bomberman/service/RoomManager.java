package bomberman.service;

import main.websocketconnection.MessageSendable;
import org.eclipse.jetty.websocket.api.Session;
import rest.UserProfile;

public interface RoomManager {
    Room assignUserToFreeRoom(UserProfile user, MessageSendable socket);
    void removeUserFromRoom(UserProfile user);
}