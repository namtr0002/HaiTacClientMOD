package template;

public class TichLuyEntry {
    public byte[] cat;
    public short[] id;
    public short[] quant;
    public int num;

    public TichLuyEntry() {}

    public TichLuyEntry(int num, byte[] cat, short[] id, short[] quant) {
        this.num = num;
        this.cat = cat;
        this.id = id;
        this.quant = quant;
    }
}
