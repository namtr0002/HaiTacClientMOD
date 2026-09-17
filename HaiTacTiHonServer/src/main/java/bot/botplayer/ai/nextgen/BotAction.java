package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerReal;

/**
 * BotAction — Bước hành động đơn lẻ trong ActionPlan.
 */
public abstract class BotAction {

    private final String name;
    private final long timeoutMs;
    private long startTime = 0;

    public BotAction(String name, long timeoutMs) {
        this.name = name;
        this.timeoutMs = timeoutMs;
    }

    public String getName() { return name; }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean isTimedOut() {
        return startTime > 0 && (System.currentTimeMillis() - startTime > timeoutMs);
    }

    /**
     * Thực thi 1 step của hành động.
     * @return true nếu hành động đã HOÀN THÀNH.
     */
    public abstract boolean execute(BotPlayerReal bot);

    /**
     * Hàm gọi khi hành động bị hủy hoặc timed out.
     */
    public void onCancel(BotPlayerReal bot) {}
}
