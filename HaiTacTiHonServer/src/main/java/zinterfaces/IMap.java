package zinterfaces;

import model.Player;
import network.Message;
import java.io.IOException;

/**
 * Interface representing common Map behaviors.
 */
public interface IMap {
    void tick() throws IOException;
    void join(Player p) throws IOException;
    void leave(Player p, int type) throws IOException;
    void broadcast(Message m, Player p, boolean includeMe) throws IOException;
    void spawn() throws IOException;
    void updateNpc() throws IOException;
    void updateMob() throws IOException;
    void updateItem() throws IOException;
    void cleanup() throws IOException;
}
