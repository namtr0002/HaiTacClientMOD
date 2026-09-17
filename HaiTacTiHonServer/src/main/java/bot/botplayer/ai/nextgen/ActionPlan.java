package bot.botplayer.ai.nextgen;

import java.util.ArrayList;
import java.util.List;

/**
 * ActionPlan — Kế hoạch gồm chuỗi các BotAction nối tiếp nhau để đạt mục tiêu.
 */
public class ActionPlan {

    private final String goalName;
    private final List<BotAction> actions = new ArrayList<>();

    public ActionPlan(String goalName) {
        this.goalName = goalName;
    }

    public ActionPlan addAction(BotAction action) {
        actions.add(action);
        return this;
    }

    public String getGoalName() { return goalName; }

    public List<BotAction> getActions() { return actions; }

    public boolean isEmpty() { return actions.isEmpty(); }
}
