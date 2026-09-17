package mob;

/**
 * Phân loại các nhóm Quái vật (Mob) trong game Hải Tặc Tí Hon.
 * Tương thích chuẩn logic giữa Client và Server (MainMonster / MonsterHuman / MonsterWalk / Pet).
 */
public enum MobType {
    /** Quái thường di chuyển tự do */
    NORMAL("Quái Thường"),
    
    /** Quái dạng người (Humanoid) - dùng wearing, head, hair */
    HUMAN("Quái Dạng Người"),
    
    /** Quái dạng thú/bò (Animal/Walk) - dùng icon sprite, typemonster (AD 0..20) */
    WALK("Quái Dạng Thú"),
    
    /** Quái Linh Thú / Pokemon / Pet dã ngoại - typemonster == 21 / Icon 55, 56 */
    PET("Quái Linh Thú / Pet"),
    
    /** Quái Tinh Anh / Thủ Lĩnh (Elite Mob) - Chỉ số cao, có hiệu ứng hào quang (MR == 1) */
    ELITE("Quái Tinh Anh"),
    
    /** Quái Trùm / Boss Map / Event Boss - AI cao cấp, kỹ năng diện rộng (MR == 2) */
    BOSS("Trùm / Boss"),
    
    /** Quái Biển / Hải Trình - Di chuyển và chiến đấu trên môi trường biển (specMap == 4) */
    SEA("Quái Biển"),
    
    /** Quái Pháo Đài / Căn Cứ Bang Hội - Bảo vệ lãnh địa bang (BR == 19) */
    GUILD("Quái Căn Cứ Bang");

    private final String description;

    MobType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
