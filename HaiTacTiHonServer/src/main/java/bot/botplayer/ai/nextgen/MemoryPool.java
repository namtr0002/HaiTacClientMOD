package bot.botplayer.ai.nextgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * MemoryPool — Quản lý vùng nhớ dùng lại (Object Cache / Memory Pool).
 * Giảm bớt áp lực khởi tạo object liên tục (zero-GC allocation during tick updates).
 */
public class MemoryPool {

    public static class PathBuffer {
        public final List<Integer> nodes = new ArrayList<>(64);
        public void clear() { nodes.clear(); }
    }

    private static final Queue<PathBuffer> PATH_BUFFERS = new ConcurrentLinkedQueue<>();
    
    public static PathBuffer obtainPathBuffer() {
        PathBuffer buf = PATH_BUFFERS.poll();
        if (buf == null) {
            buf = new PathBuffer();
        }
        buf.clear();
        return buf;
    }

    public static void recyclePathBuffer(PathBuffer buf) {
        if (buf != null && PATH_BUFFERS.size() < 1000) {
            buf.clear();
            PATH_BUFFERS.offer(buf);
        }
    }
}
