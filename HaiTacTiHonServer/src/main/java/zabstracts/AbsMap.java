package zabstracts;

import zinterfaces.IMap;
import model.Player;
import network.Message;
import java.io.IOException;

/**
 * Abstract map base implementing IMap.
 */
public abstract class AbsMap implements IMap {
    protected String key;

    public String getSeasonKey() {
        return key != null ? key : "Map";
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    @Override
    public void tick() throws IOException {}
    @Override
    public void join(Player p) throws IOException {}
    @Override
    public void leave(Player p, int type) throws IOException {}
    @Override
    public void broadcast(Message m, Player p, boolean includeMe) throws IOException {}
    @Override
    public void spawn() throws IOException {}
    @Override
    public void updateNpc() throws IOException {}
    @Override
    public void updateMob() throws IOException {}
    @Override
    public void updateItem() throws IOException {}
    @Override
    public void cleanup() throws IOException {}
}
