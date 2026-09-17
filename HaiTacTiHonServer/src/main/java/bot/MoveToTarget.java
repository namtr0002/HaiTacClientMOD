package bot;

import model.Player;
import core.ZUtil;
import map.Zone;
import java.util.ArrayList;
import java.util.List;

public class MoveToTarget implements IMove {

    private Player target;
    private long delay;

    public MoveToTarget(Player target, long delayMs) {
        this.target = target;
        this.delay = delayMs;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    private Player detect(Bot owner) {
        Zone map = owner.map;
        if (map == null) return null;
        List<Player> targets = new ArrayList<>();
        for (int i = 0; i < map.players.size(); i++) {
            Player p = map.players.get(i);
            if (p != null && p.IDPlayer != owner.IDPlayer && !p.isdie && !p.isSpectator && p.map == map) {
                if (owner.canAttackTargetPlayer(p)) {
                    targets.add(p);
                }
            }
        }
        if (targets.isEmpty()) return null;
        
        // 70% tìm đối thủ gần nhất để bám đuổi, 30% chọn ngẫu nhiên để tạo loạn chiến
        if (core.ZUtil.random(100) < 70) {
            Player closest = null;
            double minDist = Double.MAX_VALUE;
            for (Player p : targets) {
                double dist = Math.hypot(p.x - owner.x, p.y - owner.y);
                if (dist < minDist) {
                    minDist = dist;
                    closest = p;
                }
            }
            return closest;
        } else {
            return targets.get(core.ZUtil.random(targets.size()));
        }
    }

    @Override
    public void move(Bot owner) {
        Zone map = owner.map;
        if (map == null || owner.isdie || Zone.isWaitingOrUnstartedMatch(map)) {
            return;
        }

        if (target == null || target.isdie || target.map != map) {
            target = detect(owner);
        }

        if (target == null) {
            if (map.IsMapBaoVePhaoDai() && map.baoVePhaoDai != null) {
                short targetX = (short) (owner.type_pk == 5 ? 250 : 1350);
                if (Math.abs(owner.x - targetX) > 40) {
                    bot.SmartMovement.moveTowards(owner, targetX, owner.y, 25);
                }
            } else {
                bot.SmartMovement.clampToSafeBounds(owner);
            }
            return;
        }

        // Bám đuổi mục tiêu mượt mà trong vùng an toàn
        double dist = Math.hypot(target.x - owner.x, target.y - owner.y);
        boolean isWorldWar = (map.template != null && map.template.id >= 272 && map.template.id <= 275);
        if (dist > 450 && !isWorldWar) {
            // Dịch chuyển lại gần mục tiêu trong vùng an toàn
            bot.SmartMovement.moveTowards(owner, target.x, target.y, 9999);
        } else if (dist > 40) {
            // Di chuyển từng bước đến gần mục tiêu
            bot.SmartMovement.moveTowards(owner, target.x, target.y, 35);
        } else {
            bot.SmartMovement.clampToSafeBounds(owner);
        }
    }
}
