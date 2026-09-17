package bot.botplayer.ai.nextgen;

/**
 * IGoal — Interface định nghĩa cho mọi Goal của Bot.
 */
public interface IGoal {
    
    String getName();

    /**
     * Tính điểm Utility score cho Goal.
     * Utility càng cao thì xác suất chọn Goal càng lớn.
     */
    double evaluateUtility(GoalContext ctx, BotPersonality personality);

    /**
     * Tạo kế hoạch hành động ActionPlan cho Goal này.
     */
    ActionPlan planActions(GoalContext ctx);

    /**
     * Thời gian chờ giữa 2 lần chọn lại Goal này (ms).
     */
    long getCooldownMs();

    /**
     * Mức độ rủi ro (0.0 -> 10.0).
     */
    double getRisk();

    /**
     * Chi phí thực hiện (HP, MP, Vàng, Thời gian).
     */
    double getCost();
}
