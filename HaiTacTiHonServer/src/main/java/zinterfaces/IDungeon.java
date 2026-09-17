package zinterfaces;

import model.Player;
import java.io.IOException;

/**
 * Interface declaring lifecycle hooks for dungeons.
 */
public interface IDungeon {
    void initialize() throws IOException;
    void join(Player p) throws IOException;
    void leave(Player p) throws IOException;
    void tick() throws IOException;
    void spawn() throws IOException;
    void finish(Player p) throws IOException;
    void destroy() throws IOException;
    void reset() throws IOException;
    void timeout() throws IOException;
    void reward(Player p) throws IOException;
    void cleanup() throws IOException;
}
