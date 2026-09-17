package itemz;

import itemz.MainItem;
import core.ZUtil;
import java.util.List;

public class RandomMainItem {
    public MainItem item;
    public int rate;
    
    public RandomMainItem(MainItem item, int rate) {
        this.item = item;
        this.rate = rate;
    }
    
    public static MainItem random(List<RandomMainItem> list) {
        int totalRate = 0;
        for (RandomMainItem randomItem : list) {
            totalRate += randomItem.rate;
        }
        int randomNumber = ZUtil.random(totalRate);
        int cumulativeRate = 0;
        for (RandomMainItem randomItem : list) {
            cumulativeRate += randomItem.rate;
            if (randomNumber < cumulativeRate) {
                return randomItem.item;
            }
        }
        return null;
    }
}
