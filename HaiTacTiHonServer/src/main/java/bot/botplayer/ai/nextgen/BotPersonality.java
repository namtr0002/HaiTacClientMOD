package bot.botplayer.ai.nextgen;

import core.ZUtil;

/**
 * BotPersonality — Đóng vai trò là đặc tính cá nhân của từng bot.
 * Giúp mỗi bot có hành vi, thói quen và mục tiêu khác nhau giống người chơi thật.
 */
public class BotPersonality {

    public enum Archetype {
        HARDCORE_FARMER,  // Chuyên cày level, farm nguyên liệu (40%)
        PVP_WARRIOR,      // Thích PK, đi truy nã, săn boss (20%)
        DUNGEON_RAIDER,   // Thích đi phó bản, săn đồ hiếm, đi party (15%)
        CLAN_LEADER,      // Thích xây dựng bang, tuyển thành viên, đi clan war (10%)
        MERCHANT,         // Thích mua bán, giao dịch, tích trữ vàng (10%)
        CASUAL_PLAYER     // Thích thong dong, chat chit, dạo chơi (5%)
    }

    public final Archetype archetype;
    public final double riskTolerance;    // 0.0 (an toàn) -> 1.0 (liều lĩnh)
    public final double diligenceRate;    // 0.0 (lười) -> 1.0 (chăm chỉ)
    public final double pkInclination;    // Độ thích PK
    public final double farmPreference;   // Độ thích farm
    public final int preferredOnlineHourStart;
    public final int preferredOnlineHourEnd;
    public final int afkIntervalMinutes;
    public int wealthTier;               // 1: Nghèo, 2: Khá, 3: Đại gia bot

    public BotPersonality(String botName) {
        // Deterministic hash based on bot name so personality stays consistent
        int hash = Math.abs(botName.hashCode());
        
        int archIdx = hash % 100;
        if (archIdx < 40) {
            this.archetype = Archetype.HARDCORE_FARMER;
        } else if (archIdx < 60) {
            this.archetype = Archetype.PVP_WARRIOR;
        } else if (archIdx < 75) {
            this.archetype = Archetype.DUNGEON_RAIDER;
        } else if (archIdx < 85) {
            this.archetype = Archetype.CLAN_LEADER;
        } else if (archIdx < 95) {
            this.archetype = Archetype.MERCHANT;
        } else {
            this.archetype = Archetype.CASUAL_PLAYER;
        }

        this.riskTolerance = 0.2 + ((hash % 80) / 100.0);
        this.diligenceRate = 0.3 + (((hash / 10) % 70) / 100.0);
        this.pkInclination = (archetype == Archetype.PVP_WARRIOR) ? 0.9 : 0.1 + (((hash / 100) % 50) / 100.0);
        this.farmPreference = (archetype == Archetype.HARDCORE_FARMER) ? 0.95 : 0.4 + (((hash / 1000) % 50) / 100.0);
        
        this.preferredOnlineHourStart = (hash % 12); // e.g., 8am
        this.preferredOnlineHourEnd = (this.preferredOnlineHourStart + 8 + (hash % 8)) % 24;
        this.afkIntervalMinutes = 15 + (hash % 45);
        this.wealthTier = 1 + (hash % 3);
    }

    public double getGoalWeightMultiplier(String goalType) {
        switch (archetype) {
            case HARDCORE_FARMER:
                if ("TRAIN_LEVEL".equals(goalType) || "FARM_GOLD".equals(goalType) || "QUEST".equals(goalType)) return 1.8;
                if ("PVP".equals(goalType)) return 0.4;
                break;
            case PVP_WARRIOR:
                if ("PVP".equals(goalType) || "WORLD_BOSS".equals(goalType)) return 2.2;
                if ("REST".equals(goalType)) return 0.3;
                break;
            case DUNGEON_RAIDER:
                if ("DUNGEON".equals(goalType) || "PARTY".equals(goalType)) return 2.0;
                break;
            case CLAN_LEADER:
                if ("CLAN".equals(goalType) || "PARTY".equals(goalType)) return 2.2;
                break;
            case MERCHANT:
                if ("SHOP".equals(goalType) || "TRADE".equals(goalType)) return 2.0;
                break;
            case CASUAL_PLAYER:
                if ("SOCIAL".equals(goalType) || "REST".equals(goalType)) return 1.8;
                break;
        }
        return 1.0;
    }
}
