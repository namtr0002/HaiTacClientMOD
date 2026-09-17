package model;

import model.Player;
import java.util.function.Consumer;
import zinterfaces.iMenuAction;

public class Menu {
    private int id = -1;
    private String name;
    private short icon = -1;
    private Runnable runnable;
    private Consumer<Player> actionConsumer;

    public Menu(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Menu(int id, String name, short icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public Menu(int id, String name, Runnable runnable) {
        this.id = id;
        this.name = name;
        this.runnable = runnable;
    }

    public Menu(int id, String name, short icon, Runnable runnable) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.runnable = runnable;
    }

    public Menu(int id, String name, Consumer<Player> actionConsumer) {
        this.id = id;
        this.name = name;
        this.actionConsumer = actionConsumer;
    }

    public Menu(int id, String name, short icon, Consumer<Player> actionConsumer) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.actionConsumer = actionConsumer;
    }

    public Menu(String name, Runnable runnable) {
        this.name = name;
        this.runnable = runnable;
    }

    public Menu(String name, short icon, Runnable runnable) {
        this.name = name;
        this.icon = icon;
        this.runnable = runnable;
    }

    public Menu(String name, Consumer<Player> actionConsumer) {
        this.name = name;
        this.actionConsumer = actionConsumer;
    }

    public Menu(String name, short icon, Consumer<Player> actionConsumer) {
        this.name = name;
        this.icon = icon;
        this.actionConsumer = actionConsumer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getIcon() {
        return icon;
    }

    public void setIcon(short icon) {
        this.icon = icon;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Consumer<Player> getActionConsumer() {
        return actionConsumer;
    }

    public void setActionConsumer(Consumer<Player> actionConsumer) {
        this.actionConsumer = actionConsumer;
    }

    public iMenuAction getMenuAction() {
        return (runnable instanceof iMenuAction) ? (iMenuAction) runnable : (runnable != null ? runnable::run : null);
    }

    public void setMenuAction(iMenuAction menuAction) {
        this.runnable = menuAction;
    }

    public void confirm() {
        if (runnable != null) {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void confirm(Player p) {
        if (runnable != null) {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (actionConsumer != null) {
            actionConsumer.accept(p);
        } else if (id != -1) {
            executeById(p, id);
        }
    }

    public void execute(Player p) {
        confirm(p);
    }

    public void execute(Player p, int index) {
        if (runnable != null || actionConsumer != null) {
            confirm(p);
        } else {
            int targetId = (id != -1) ? id : index;
            executeById(p, targetId);
        }
    }

    private void executeById(Player p, int targetId) {
        try {
            short npcId = p.currentNpcId;
            if (npcId != 0) {
                zinterfaces.iNpc npcHandler = zinterfaces.iNpc.get(npcId);
                if (npcHandler != null) {
                    npcHandler.handleMenu(p, targetId);
                    return;
                }
                zinterfaces.iMenu menuHandler = zinterfaces.iMenu.get(npcId);
                if (menuHandler != null) {
                    menuHandler.handleMenu(p, npcId, targetId);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
