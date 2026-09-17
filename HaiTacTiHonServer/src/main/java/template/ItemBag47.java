package template;

public class ItemBag47 {
    public short id;
    public byte category;
    public short quant;
    
    public ItemBag47() {
        
    }
    
    public ItemBag47(short id, short num) {
        this.id = id;
        this.category = 4;
        this.quant = num;
    }

    public ItemBag47(int id, int num) {
        this.id = (short) id;
        this.category = 4;
        this.quant = (short) num;
    }
    
    public ItemBag47(int id, int cat, int num) {
        this.id = (short) id;
        this.category = (byte) cat;
        this.quant = (short) num;
    }
}
