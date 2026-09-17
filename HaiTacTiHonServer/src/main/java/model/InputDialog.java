package model;

import java.io.IOException;

public class InputDialog {
    @FunctionalInterface
    public interface InputDialogHandler {
        void handle(String[] inputs) throws IOException;
    }

    private Player player;
    public int id;
    public String title;
    public String[] options;
    private InputDialogHandler handler;

    public InputDialog(Player player, int id, String title, String[] options, InputDialogHandler handler) {
        this.player = player;
        this.id = id;
        this.title = title;
        this.options = options;
        this.handler = handler;
    }

    public InputDialog(int id, String title, String[] options, InputDialogHandler handler) {
        this.id = id;
        this.title = title;
        this.options = options;
        this.handler = handler;
    }

    public InputDialog(int id, String title, String[] options) {
        this.id = id;
        this.title = title;
        this.options = options;
    }

    public InputDialog(Player player, int id, String title, String[] options) {
        this.player = player;
        this.id = id;
        this.title = title;
        this.options = options;
    }

    public InputDialog(int id) {
        this.id = id;
    }

    public InputDialog(Player player, int id) {
        this.player = player;
        this.id = id;
    }

    public InputDialog setHandler(InputDialogHandler handler) {
        this.handler = handler;
        return this;
    }

    public InputDialog setOptions(String[] options) {
        this.options = options;
        return this;
    }

    public InputDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public void startInput() {
        this.player.inputDialog = this;
        this.player.getService().startInput();
    }

    public boolean hasHandler() {
        return this.handler != null;
    }

    public void handle(String[] inputs) throws IOException {
        if (handler != null) {
            handler.handle(inputs);
        }
    }
}
