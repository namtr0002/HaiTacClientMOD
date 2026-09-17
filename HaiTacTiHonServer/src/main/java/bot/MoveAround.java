package bot;

import model.Player;
import core.ZUtil;
import map.Zone;

public class MoveAround implements IMove {

    private long delay;
    private short targetX = -1;
    private short targetY = -1;

    public MoveAround(long delayMs) {
        this.delay = delayMs;
    }

    @Override
    public void move(Bot owner) {
        Zone map = owner.map;
        if (map == null || owner.isdie) {
            return;
        }

        long now = System.currentTimeMillis();
        if (owner.timeMoveBot < now) {
            owner.timeMoveBot = now + delay;
            
            // Chọn đích di chuyển ngẫu nhiên an toàn trên mặt đất của map
            short[] pt = bot.SmartMovement.getRandomGroundPoint(map);
            targetX = pt[0];
            targetY = pt[1];
        }

        if (targetX == -1 || targetY == -1) {
            short[] pt = bot.SmartMovement.getRandomGroundPoint(map);
            targetX = pt[0];
            targetY = pt[1];
        }

        // Di chuyển từng bước mượt mà đến mục tiêu trong vùng an toàn
        double dist = Math.hypot(targetX - owner.x, targetY - owner.y);
        if (dist > 15) {
            bot.SmartMovement.moveTowards(owner, targetX, targetY, 30);
        } else {
            bot.SmartMovement.clampToSafeBounds(owner);
        }
    }
}
