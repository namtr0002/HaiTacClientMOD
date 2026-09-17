package model;

import java.io.IOException;

public class YesNoDialog {
    @FunctionalInterface
    public interface YesNoDialogHandler {
        void handle(byte value) throws IOException;
    }

    @FunctionalInterface
    public interface SimpleConfirmHandler {
        void onConfirm() throws IOException;
    }

    private Player player;
    public int id;
    public String title;
    public String text;
    public String[] commands;
    public byte[] icons;
    private YesNoDialogHandler handler;

    public static byte[] generateDefaultIcons(String[] commands) {
        if (commands == null || commands.length == 0) {
            return new byte[]{2, 1};
        }
        byte[] icons = new byte[commands.length];
        for (int i = 0; i < commands.length; i++) {
            if (i == 0) {
                icons[i] = 2; // Icon Đồng ý / OK
            } else if (i == commands.length - 1) {
                icons[i] = 1; // Icon Hủy / Đóng
            } else {
                icons[i] = 2;
            }
        }
        return icons;
    }

    public YesNoDialog(Player player, int id, String title, String text, String[] commands, byte[] icons, YesNoDialogHandler handler) {
        this.player = player;
        this.id = id;
        this.title = title;
        this.text = text;
        this.commands = commands;
        this.icons = icons != null ? icons : generateDefaultIcons(commands);
        this.handler = handler;
    }

    public YesNoDialog(Player player, int id, String title, String text, String[] commands, YesNoDialogHandler handler) {
        this(player, id, title, text, commands, generateDefaultIcons(commands), handler);
    }

    public YesNoDialog(int id, String title, String text, String[] commands, byte[] icons, YesNoDialogHandler handler) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.commands = commands;
        this.icons = icons;
        this.handler = handler;
    }

    public YesNoDialog(int id, String title, String text, String[] commands, byte[] icons) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.commands = commands;
        this.icons = icons;
    }

    public YesNoDialog(Player player, int id, String title, String text, String[] commands, byte[] icons) {
        this.player = player;
        this.id = id;
        this.title = title;
        this.text = text;
        this.commands = commands;
        this.icons = icons;
    }

    public YesNoDialog(Player player, int id, String title, String text, String[] commands) {
        this(player, id, title, text, commands, generateDefaultIcons(commands));
    }

    public YesNoDialog(int id) {
        this.id = id;
    }

    public YesNoDialog(Player player, int id) {
        this.player = player;
        this.id = id;
    }

    public YesNoDialog setHandler(YesNoDialogHandler handler) {
        this.handler = handler;
        return this;
    }

    public YesNoDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public void startYesNo() {
        this.player.yesNoDialog = this;
        this.player.getService().startYesNo();
    }

    public boolean hasHandler() {
        return this.handler != null;
    }

    public void handle(byte value) throws IOException {
        if (handler != null) {
            handler.handle(value);
        }
    }
}
