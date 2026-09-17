package template;

public class Log_template {
    public int accountId;
    public int playerId;
    public String type;
    public String name;
    public String data;

    public Log_template(String name, String text) {
        this.accountId = 0;
        this.playerId = 0;
        this.type = "SYSTEM";
        this.name = name;
        this.data = text;
    }

    public Log_template(int accountId, int playerId, String name, String text) {
        this.accountId = accountId;
        this.playerId = playerId;
        this.type = "GENERAL";
        this.name = name;
        this.data = text;
    }

    public Log_template(int accountId, int playerId, String type, String name, String text) {
        this.accountId = accountId;
        this.playerId = playerId;
        this.type = type;
        this.name = name;
        this.data = text;
    }
}
