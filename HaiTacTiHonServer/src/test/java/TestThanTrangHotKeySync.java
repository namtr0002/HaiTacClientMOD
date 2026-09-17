import model.Player;
import model.ThanTrangConfig;
import java.io.*;

public class TestThanTrangHotKeySync {
    public static void main(String[] args) throws Exception {
        System.out.println("=== BAT DAU KIEM TRA TU DONG DONG BO HOTKEY & ID SKILL THAN TRANG ===");

        // 1. Kiem tra mapping getSkillBySetId cho 15 Sets (1..15 -> 4002..4016)
        System.out.println("-> Kiem tra mapping Set ID -> Skill ID (4002..4016):");
        for (int setId = 1; setId <= 15; setId++) {
            int expectedSkill = 4001 + setId;
            int actualModel = model.ThanTrangConfig.getSkillBySetId(setId);
            int actualTemplate = template.ThanTrangConfig.getSkillBySetId(setId);
            assertMatch(actualModel, expectedSkill, "model.ThanTrangConfig Set " + setId);
            assertMatch(actualTemplate, expectedSkill, "template.ThanTrangConfig Set " + setId);

            int revModel = model.ThanTrangConfig.getSetIdBySkill(expectedSkill);
            int revTemplate = template.ThanTrangConfig.getSetIdBySkill(expectedSkill);
            assertMatch(revModel, setId, "model.ThanTrangConfig rev Set " + setId);
            assertMatch(revTemplate, setId, "template.ThanTrangConfig rev Set " + setId);
        }
        System.out.println("-> Kiem tra 15 Set mapping: HOAN TOAN DUNG (Set 1 -> 4002 ... Set 15 -> 4016)!");

        // 2. Khoi tao Player gia lap va tao du lieu p.rms[0] mau
        Player p = new Player();
        p.rms = new byte[11][];
        for (int i = 0; i < p.rms.length; i++) {
            p.rms[i] = new byte[0];
        }

        // Du lieu mau cho rms[0]:
        // Slot 0: Skill thuong (type 1, id 1)
        // Slot 1: Skill Than Trang Set cu (type 1, id 4001)
        // Slot 2: Potion mau (type 0, id 25)
        // Slot 3: O trong (type -1)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeByte(1); // slot 0: skill 1
        dos.writeShort(1);

        dos.writeByte(1); // slot 1: skill 4001
        dos.writeShort(4001);

        dos.writeByte(0); // slot 2: potion 25
        dos.writeShort(25);

        dos.writeByte(-1); // slot 3: o trong

        dos.flush();
        p.rms[0] = baos.toByteArray();

        System.out.println("-> Du lieu ban dau p.rms[0] length: " + p.rms[0].length + " bytes");

        // 3. Kiem tra doi sang Set 1 (Dung Nham Volcano -> Target Skill ID = 4002)
        boolean changed1 = p.updateThanTrangRmsHotKey(ThanTrangConfig.getSkillBySetId(1));
        System.out.println("-> Ket qua update sang Set 1 (4002): " + (changed1 ? "THANH CONG" : "THAT BAI"));
        if (!changed1) {
            throw new RuntimeException("Loi: updateThanTrangRmsHotKey Set 1 tra ve false!");
        }

        // Doc lai p.rms[0] de kiem tra tung o
        ByteArrayInputStream bais = new ByteArrayInputStream(p.rms[0]);
        DataInputStream dis = new DataInputStream(bais);

        // Slot 0: type 1, id 1
        byte t0 = dis.readByte();
        short s0 = dis.readShort();
        assertMatch(t0, (byte) 1, "Slot 0 type phai la 1");
        assertMatch(s0, (short) 1, "Slot 0 id phai giu nguyen la 1");

        // Slot 1: type 1, id 4002 (da duoc hoan doi!)
        byte t1 = dis.readByte();
        short s1 = dis.readShort();
        assertMatch(t1, (byte) 1, "Slot 1 type phai la 1");
        assertMatch(s1, (short) 4002, "Slot 1 id phai tu dong chuyen sang 4002");

        // Slot 2: type 0, id 25
        byte t2 = dis.readByte();
        short s2 = dis.readShort();
        assertMatch(t2, (byte) 0, "Slot 2 type phai la 0");
        assertMatch(s2, (short) 25, "Slot 2 id phai giu nguyen la 25");

        // Slot 3: type -1
        byte t3 = dis.readByte();
        assertMatch(t3, (byte) -1, "Slot 3 phai giu nguyen la o trong -1");

        System.out.println("-> Kiem tra Slot 0 (Skill 1): HOP LE!");
        System.out.println("-> Kiem tra Slot 1 (Skill Than Trang 4002): HOP LE TU DONG HOAN DOI!");
        System.out.println("-> Kiem tra Slot 2 (Potion 25): HOP LE!");
        System.out.println("-> Kiem tra Slot 3 (Trong -1): HOP LE!");

        // 4. Kiem tra doi sang Set 2 (Han Bang -> Target Skill ID = 4003)
        boolean changed2 = p.updateThanTrangRmsHotKey(ThanTrangConfig.getSkillBySetId(2));
        System.out.println("-> Ket qua update tiep sang Set 2 (4003): " + (changed2 ? "THANH CONG" : "THAT BAI"));
        if (!changed2) {
            throw new RuntimeException("Loi: update sang Set 2 tra ve false!");
        }

        ByteArrayInputStream bais2 = new ByteArrayInputStream(p.rms[0]);
        DataInputStream dis2 = new DataInputStream(bais2);
        dis2.readByte(); dis2.readShort(); // skip slot 0
        dis2.readByte();
        short s1_set2 = dis2.readShort();
        assertMatch(s1_set2, (short) 4003, "Slot 1 id phai tu dong chuyen sang 4003");
        System.out.println("-> Kiem tra Slot 1 doi tiep sang 4003: HOP LE!");

        // 5. Kiem tra doi sang Set 15 (Queen T-Rex -> Target Skill ID = 4016)
        boolean changed15 = p.updateThanTrangRmsHotKey(ThanTrangConfig.getSkillBySetId(15));
        System.out.println("-> Ket qua update sang Set 15 (4016): " + (changed15 ? "THANH CONG" : "THAT BAI"));
        if (!changed15) {
            throw new RuntimeException("Loi: update sang Set 15 tra ve false!");
        }
        ByteArrayInputStream bais15 = new ByteArrayInputStream(p.rms[0]);
        DataInputStream dis15 = new DataInputStream(bais15);
        dis15.readByte(); dis15.readShort(); // skip slot 0
        dis15.readByte();
        short s1_set15 = dis15.readShort();
        assertMatch(s1_set15, (short) 4016, "Slot 1 id phai tu dong chuyen sang 4016");
        System.out.println("-> Kiem tra Slot 1 doi sang 4016: HOP LE!");

        // 6. Kiem tra neu goi lai voi cung skill 4016 (khong can thay doi)
        boolean changedSame = p.updateThanTrangRmsHotKey(4016);
        if (changedSame) {
            throw new RuntimeException("Loi: Cung skill khong duoc bao la changed!");
        }
        System.out.println("-> Kiem tra goi voi cung skill 4016: tra ve false dung thiet ke (khong bi ghi de du thua)!");

        System.out.println("=== TAT CA KIEM TRA HOAN TOAN CHINH XAC 100% ===");
    }

    private static void assertMatch(int actual, int expected, String msg) {
        if (actual != expected) {
            throw new RuntimeException("ASSERTION FAILED: " + msg + " (Actual: " + actual + ", Expected: " + expected + ")");
        }
    }
}
