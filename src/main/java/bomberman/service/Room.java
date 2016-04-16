package bomberman.service;

import bomberman.mechanics.World;
import bomberman.mechanics.interfaces.WorldType;
import main.websocketconnection.MessageSendable;
import org.javatuples.Pair;
import org.jetbrains.annotations.Nullable;
import rest.UserProfile;

import java.util.HashMap;
import java.util.Map;

public class Room {

    public Room(){}

    public Room(int overrideCapacity) {
        capacity = overrideCapacity;
    }

    public void createNewWorld(WorldType type)
    {
        world = new World(type, playerMap.size());
    }

    @Deprecated
    public void createNewWorld(String type) {
        //world = new World(type, playerMap.size());
    }

    public void assignBombermenToPlayers() {
        world.spawnBombermen(websocketMap.size());
        final int[] bombermen = world.getBombermenIDs();
        int i = 0;
        for (Map.Entry<UserProfile, MessageSendable> player : websocketMap.entrySet()) {
            playerMap.put(bombermen[i], player.getKey());
            reversePlayerMap.put(player.getKey(), bombermen[i]);
            ++i;
        }
    }

    public int getMaximalCapacity() {
        return capacity;
    }

    public int getCurrentCapacity() {
        return websocketMap.size();
    }

    public boolean isFilled()
    {
        return websocketMap.size() >= capacity;
    }

    public boolean isEmpty() {
        return websocketMap.isEmpty();
    }

    public void insertPlayer(UserProfile user, MessageSendable socket) {
        for (Map.Entry<UserProfile, MessageSendable> entry : websocketMap.entrySet())
            socket.sendMessage(MessageCreator.createUserJoinedMessage(entry.getKey()));

        websocketMap.put(user, socket);
        readinessMap.put(user, new Pair<>(false, false));
        broadcast(MessageCreator.createUserJoinedMessage(user));
    }

    public boolean hasPlayer(UserProfile user) {
        return websocketMap.containsKey(user);
    }

    public void removePlayer(UserProfile user) {
        if (websocketMap.containsKey(user)) {
            websocketMap.remove(user);
            readinessMap.remove(user);
            broadcast(MessageCreator.createUserLeftMessage(user));
        }
    }

    public void updatePlayerState(UserProfile user, boolean isReady, boolean contentLoaded)
    {
        readinessMap.remove(user);
        readinessMap.put(user, new Pair<>(isReady, contentLoaded));
    }

    public void broadcast(String message) {
        for (Map.Entry<UserProfile, MessageSendable> entry: websocketMap.entrySet())
            entry.getValue().sendMessage(message);
    }

    // I can't determine hashCode and equals methods. :(

    private int capacity = DEFAULT_CAPACITY;
    private boolean isEveryoneReady = false;
    private boolean hasEveryoneLoadedContetnt = false;

    private final Map<Integer, UserProfile> playerMap = new HashMap<>(4);
    private final Map<UserProfile, Integer> reversePlayerMap = new HashMap<>(4);
    private final Map<UserProfile, MessageSendable> websocketMap = new HashMap<>(4);
    private final Map<UserProfile, Pair<Boolean, Boolean>> readinessMap = new HashMap<>(4);

    World world;

    public static final int DEFAULT_CAPACITY = 4;
}
