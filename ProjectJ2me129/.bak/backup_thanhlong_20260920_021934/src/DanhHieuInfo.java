public class DanhHieuInfo {
    public int id;
    public String name;
    public short idEff;
    public int coin;
    public byte state; // 0 = Chưa có, 1 = Đã có, 2 = Đang dùng
    public String optionsStr;
    public mVector actionButtons = new mVector();

    public DanhHieuInfo() {
    }

    public DanhHieuInfo(int id, String name, short idEff, int coin, byte state, String optionsStr) {
        this.id = id;
        this.name = name;
        this.idEff = idEff;
        this.coin = coin;
        this.state = state;
        this.optionsStr = optionsStr;
    }
}