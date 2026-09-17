package template;

import java.util.HashMap;

/**
 * Data structure representing a shop item with purchase limits (DTO).
 */
public class ShopLimitItemTemplate {
    public short id;
    public byte type;
    public int point;
    public String info;
    public int limit;
    public HashMap<String, Integer> limit_data;
}
