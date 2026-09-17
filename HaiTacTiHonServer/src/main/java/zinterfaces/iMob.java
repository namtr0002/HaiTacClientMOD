package zinterfaces;

import model.Player;
import mob.Mob;
import java.io.IOException;

/**
 * iMob — Interface định nghĩa hành vi tùy biến cho quái vật (Mob).
 *
 * Mọi quái vật hoặc Boss sự kiện có thể implement interface này để tùy chỉnh:
 * - onDeath: Phần thưởng hoặc hiệu ứng khi quái chết.
 * - update: Logic AI chạy định kỳ trong game loop.
 * - attack: Tùy biến sát thương, kỹ năng hoặc hành vi tấn công.
 */
public interface iMob {
    
    /** Gọi khi quái chết */
    void onDeath(Player pKill, Mob mob);
    
    /** Logic game loop định kỳ của quái */
    default void update(Mob mob) {
        // Mặc định không làm gì
    }
    
    /** Logic tấn công của quái */
    default void attack(Mob mob, Player target) throws IOException {
        // Mặc định gọi hàm tấn công gốc của Mob
        mob.baseAttack(target);
    }
}
