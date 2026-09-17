package map;

import template.DataTemplate;

public class Map {
    public int id;
    public String keyflag;
    public MapTemplate template;
    public Zone[] zones;

    public Map(int id, MapTemplate template, Zone[] zones) {
        this.id = id;
        this.template = template;
        this.zones = zones;
    }
    
    public boolean isLang() {
        return Zone.isMapLang(template.id);
    }
    
    public boolean isMapNotPK() {
        return isLang() || template.id == 62;
    }
    
    public boolean isSea() {
        if (template.id == 7) {
            return true;
        }
        for (int[] s : DataTemplate.mSea) {
            if (s[1] == template.id) {
                return true;
            }
        }
        return false;
    }
}
