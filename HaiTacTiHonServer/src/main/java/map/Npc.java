package map;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Npc {
    public static HashMap<Integer, List<Npc>> ENTRYS = new HashMap<>();
    public short idmenu;
    public String name = "";
    public String namegt = "";
    public String chat = "";
    public short x;
    public short y;
    public byte isPerson = 1;
    public byte typeIcon = -1;
    public byte wBlock = 20;
    public byte hBlock = 20;
    public byte b3 = 0;
    public byte[] dataFrame = new byte[]{1, 2};
    public short head = 0;
    public short hair = 0;
    public short[] wearing = new short[]{-1, -1, -1, -1};
    public volatile long lastChatTime = System.currentTimeMillis() - core.ZUtil.random(10000);
    public String[] chats;

    public zinterfaces.iNpc getHandler() {
        return zinterfaces.iNpc.get(this.idmenu);
    }

    public String getChat() {
        if (getHandler() != null && getHandler().getChatText() != null) {
            String text = getHandler().getChatText();
            return text != null ? text : "";
        }
        String[] chats = getChatTexts();
        if (chats != null && chats.length > 0 && chats[0] != null) {
            return chats[0];
        }
        return this.chat == null ? "" : this.chat;
    }

    public String[] getChatTexts() {
        if (getHandler() != null && getHandler().getChatTexts() != null) {
            return getHandler().getChatTexts();
        }
        String[] cfg = zinterfaces.iNpc.getChatConfig(this.idmenu);
        if (cfg != null) {
            return cfg;
        }
        if (this.chats != null) {
            return this.chats;
        }
        if (this.chat != null && !this.chat.isEmpty()) {
            this.chats = this.chat.split("\\|");
            return this.chats;
        }
        return new String[]{""};
    }

    @Override
    public String toString() {
        return "Npc{" +
                "iditem=" + idmenu +
                ", name='" + name + '\'' +
                ", namegt='" + namegt + '\'' +
                ", chat='" + chat + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", isPerson=" + isPerson +
                ", typeIcon=" + typeIcon +
                ", wBlock=" + wBlock +
                ", hBlock=" + hBlock +
                ", b3=" + b3 +
                ", dataFrame=" + (dataFrame == null ? "null" : Arrays.toString(dataFrame)) +
                ", head=" + head +
                ", hair=" + hair +
                ", wearing=" + (wearing == null ? "null" : Arrays.toString(wearing)) +
                '}';
    }
}