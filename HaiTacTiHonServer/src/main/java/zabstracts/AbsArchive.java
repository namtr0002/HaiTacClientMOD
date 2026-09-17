package zabstracts;

import model.Player;
import network.Message;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import template.GiftBox;

public abstract class AbsArchive {
    public int id;
    public String title;
    public String info;
    public int num;
    public short icon;
    public byte type; // 0 for normal, -1 for special
    protected String key;

    public String getSeasonKey() {
        return key != null ? key : "Archive_" + id;
    }

    public void setSeasonKey(String key) {
        this.key = key;
    }

    protected final List<GiftBox> rewards = new ArrayList<>();

    private static final Map<Integer, AbsArchive> instances = new HashMap<>();
    public static final List<AbsArchive> ENTRY = new ArrayList<>();

    public static void register(AbsArchive instance) {
        instances.put(instance.id, instance);
        ENTRY.add(instance);
    }

    public static void init() {
        instances.clear();
        ENTRY.clear();
        achievement.ArchiDaily.init();
        achievement.ArchiPrivatePass.init();
    }

    public static AbsArchive get(int id) {
        return instances.get(id);
    }

    public List<GiftBox> getRewards() {
        return rewards;
    }

    protected void addNormalRewards() {
        template.ItemTemplate4 coin = template.ItemTemplate4.get_it_by_id(0);
        if (coin != null) {
            this.rewards.add(new GiftBox(coin, 1_000_000));
        }
        template.ItemTemplate4 ruby = template.ItemTemplate4.get_it_by_id(1);
        if (ruby != null) {
            this.rewards.add(new GiftBox(ruby, 100));
        }
    }

    protected void addSpecialRewards() {
        template.ItemTemplate4 coin = template.ItemTemplate4.get_it_by_id(0);
        if (coin != null) {
            this.rewards.add(new GiftBox(coin, 2_500_000));
        }
        template.ItemTemplate4 ruby = template.ItemTemplate4.get_it_by_id(1);
        if (ruby != null) {
            this.rewards.add(new GiftBox(ruby, 300));
        }
    }
}
