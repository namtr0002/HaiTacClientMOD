public class TitleActionBtn {
    public byte actionId;
    public String name;
    public byte style; // 0: Vàng/Gold, 1: Xanh lá/Green, 2: Đỏ/Red

    public TitleActionBtn(byte actionId, String name, byte style) {
        this.actionId = actionId;
        this.name = name;
        this.style = style;
    }
}
