import java.io.*;
import java.util.*;

public final class Agetskill {
    private static final String BASE = "C:/ThMadara/HTTH/SQL/";
    private static PrintStream ps;
    private static boolean running;

    public static void run() {
        if (running) return;
        running = true;
        try {
            File dir = new File(BASE);
            if (!dir.exists() && !dir.mkdirs()) {
                throw new IOException("Cannot create directory: " + BASE);
            }
            String fileName = BASE + "skill_" + (System.currentTimeMillis() / 1000L) + ".sql";
            ps = new PrintStream(new FileOutputStream(fileName), true, "UTF-8");
            GlobalService.getInstance().get_DATA((byte)3);
            Message m = new Message((byte)-7);
            onServerMessage(m);
            System.out.println(">> Request skill sent, saving to: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            close();
        }
    }

    public static void onServerMessage(Message m) {
        try {
            byte type = m.reader().readByte();
            if (type != 3)return;
            short skillId = m.reader().readShort();
            if (skillId == -1) {
                close();
                return;
            }

            byte idIndex = m.reader().readByte();
            byte id2 = m.reader().readByte();
            short icon = m.reader().readShort();
            byte typeSkill = m.reader().readByte();
            byte typeBuff = m.reader().readByte();
            String name = m.reader().readUTF();
            byte typeEffSkill = m.reader().readByte();
            short range = m.reader().readShort();
            short rangeLan = m.reader().readShort();
            byte nTarget = m.reader().readByte();
            short damage = m.reader().readShort();
            short manaLost = m.reader().readShort();
            int timeDelay = m.reader().readInt();
            byte nKick = m.reader().readByte();
            String info = m.reader().readUTF();
            byte lvRQ = m.reader().readByte();
            byte percentLv = m.reader().readByte();
            byte typeDevil = m.reader().readByte();
            String option = m.reader().readUTF();
            String effSpec = m.reader().readUTF();
            byte lvDevilSkill = m.reader().readByte();
            byte phanTramDevilSkill = m.reader().readByte();

            // Handle vecAtt to match option format like '[]' or '[0,-1,-1]'
            byte numAtt = m.reader().readByte();
            List<String> attData = new ArrayList<>();
            for (int i = 0; i < numAtt; i++) {
                byte attType = m.reader().readByte();
                short attValue = m.reader().readShort();
                attData.add(attType + "," + attValue);
            }
            String optionFormatted = attData.isEmpty() ? "[]" : "[" + String.join(",", attData) + "]";

            // Format EffSpec to match '[0,-1,-1]'
            byte idEffSpec = m.reader().readByte();
            String effSpecFormatted = "[0,-1,-1]";
            if (idEffSpec > 0) {
                short perEffSpec = m.reader().readShort();
                short timeEffSpec = m.reader().readShort();
                effSpecFormatted = "[" + idEffSpec + "," + perEffSpec + "," + timeEffSpec + "]";
            }

            String sql = String.format(
                "INSERT INTO `skill` (`id`, `id_index`, `id_2`, `icon`, `typeSkill`, `typeBuff`, `name`, " +
                "`typeEffSkill`, `range`, `rangeLan`, `nTarget`, `damage`, `manaLost`, `timeDelay`, `nKick`, " +
                "`info`, `Lv_RQ`, `percentLv`, `typeDevil`, `option`, `EffSpec`, `LvDevilSkill`, `phanTramDevilSkill`) " +
                "VALUES (%d, %d, %d, %d, %d, %d, '%s', %d, %d, %d, %d, %d, %d, %d, %d, '%s', %d, %d, %d, '%s', '%s', %d, %d);",
                skillId, idIndex, id2, icon, typeSkill, typeBuff, esc(name),
                typeEffSkill, range, rangeLan, nTarget, damage, manaLost, timeDelay, nKick,
                esc(info), lvRQ, percentLv, typeDevil, esc(optionFormatted), esc(effSpecFormatted),
                lvDevilSkill, phanTramDevilSkill
            );

            if (ps != null) {
                ps.println(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("'", "''").replace("\\", "\\\\");
    }

    private static void close() {
        try {
            if (ps != null) {
                ps.close();
                ps = null;
            }
            System.out.println(">> Saved skills SQL done.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            running = false;
        }
    }
}