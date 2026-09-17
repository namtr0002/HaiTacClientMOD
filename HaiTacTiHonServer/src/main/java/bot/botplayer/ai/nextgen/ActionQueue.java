package bot.botplayer.ai.nextgen;

import bot.botplayer.BotPlayerReal;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ActionQueue — Hàng chờ thực thi hành động thread-safe.
 */
public class ActionQueue {

    private final Queue<BotAction> queue = new ConcurrentLinkedQueue<>();
    private BotAction currentAction = null;
    private String currentGoalName = "NONE";

    public synchronized void setPlan(ActionPlan plan) {
        clear();
        if (plan != null) {
            this.currentGoalName = plan.getGoalName();
            for (BotAction action : plan.getActions()) {
                queue.offer(action);
            }
        }
    }

    public synchronized void clear() {
        if (currentAction != null) {
            currentAction.onCancel(null);
            currentAction = null;
        }
        queue.clear();
        currentGoalName = "NONE";
    }

    public synchronized boolean isFinished() {
        return currentAction == null && queue.isEmpty();
    }

    public String getCurrentGoalName() {
        return currentGoalName;
    }

    public synchronized void tick(BotPlayerReal bot) {
        if (currentAction == null) {
            currentAction = queue.poll();
            if (currentAction != null) {
                currentAction.start();
            }
        }

        if (currentAction != null) {
            if (currentAction.isTimedOut()) {
                AIDebugLogger.logStuckRecovery(bot, "ACTION_TIMEOUT", "Cancelling action " + currentAction.getName());
                currentAction.onCancel(bot);
                currentAction = null; // Skip to next action
                return;
            }

            try {
                boolean completed = currentAction.execute(bot);
                if (completed) {
                    currentAction = null; // Done, advance to next in next tick
                }
            } catch (Exception e) {
                e.printStackTrace();
                currentAction = null;
            }
        }
    }
}
