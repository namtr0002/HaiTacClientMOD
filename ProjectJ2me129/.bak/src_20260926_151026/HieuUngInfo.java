public class HieuUngInfo {
    public int id;
    public String name;
    public String category;
    public short idEff;
    public byte type; // 0 = DataSkillEff ID, 1..5 = Set +11..+15, 6 = Title, 7 = Fashion, 8 = Mastery/Other
    public byte state; // 1 = Đang Bật, 0 = Đang Tắt
    public String optionsStr;
    public mVector actionButtons = new mVector();

    public HieuUngInfo() {
    }

    public HieuUngInfo(int id, String name, String category, short idEff, byte type, byte state, String optionsStr) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.idEff = idEff;
        this.type = type;
        this.state = state;
        this.optionsStr = optionsStr;
    }
}
