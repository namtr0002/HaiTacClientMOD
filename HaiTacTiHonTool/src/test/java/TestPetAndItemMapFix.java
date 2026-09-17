import com.deplor.haitactihontool.config.AppConfig;
import com.deplor.haitactihontool.map.*;
import com.deplor.haitactihontool.pet.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

public class TestPetAndItemMapFix {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  RUNNING VERIFICATION: PET AND ITEM MAP FIX");
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        AppConfig.load();

        // ----------------------------------------------------
        // Test 1: TemplateManager loads all 423 Item Map Templates
        // ----------------------------------------------------
        System.out.println("\n[Test 1] Verifying Item Map Templates in TemplateManager...");
        Map<Short, ItemMapTemplate> templates = TemplateManager.gI().getAllItemTemplates();
        System.out.println("Loaded template count: " + templates.size());
        if (templates.size() >= 420) {
            System.out.println("=> PASSED: Found " + templates.size() + " templates (expected >= 420).");
            passed++;
        } else {
            System.err.println("=> FAILED: Expected >= 420 templates, but got " + templates.size());
            failed++;
        }

        // Check specific templates
        short[] sampleIds = {0, 1, 2, 10, 11, 100};
        boolean samplesOk = true;
        for (short id : sampleIds) {
            ItemMapTemplate t = TemplateManager.gI().getItemTemplate(id);
            if (t == null) {
                System.err.println("=> FAILED: Template " + id + " is null!");
                samplesOk = false;
            } else {
                System.out.println("   Template " + id + ": idImage=" + t.idImage + ", layer=" + t.layer + ", dx=" + t.dx + ", dy=" + t.dy);
            }
        }
        if (samplesOk) {
            System.out.println("=> PASSED: Sample templates parsed successfully.");
            passed++;
        } else {
            failed++;
        }

        // ----------------------------------------------------
        // Test 2: ImageCache loads Item Map images
        // ----------------------------------------------------
        System.out.println("\n[Test 2] Verifying Item Map Image loading via ImageCache...");
        ItemMapTemplate t0 = TemplateManager.gI().getItemTemplate((short)0);
        if (t0 != null) {
            BufferedImage img0 = ImageCache.getServerImage(t0.idImage);
            if (img0 != null) {
                System.out.println("=> PASSED: Loaded image for template 0 (idImage " + t0.idImage + "): " + img0.getWidth() + "x" + img0.getHeight());
                passed++;
            } else {
                System.err.println("=> FAILED: Image for template 0 (idImage " + t0.idImage + ") is null!");
                failed++;
            }
        }

        // ----------------------------------------------------
        // Test 3: Pet sprite resolution (NO Item Map confusion)
        // ----------------------------------------------------
        System.out.println("\n[Test 3] Verifying Pet Sprite Resolution & Fallback Isolation...");

        // Pet 1 (White Zeus, frame 77 -> 1077.png exists)
        PetTemplate p1 = new PetTemplate(1, "White Zeus", 703, 4, 77, null);
        PetCanvas.PetImageResult r1 = PetCanvas.resolvePetImage(p1);
        if (r1 != null && r1.file != null && r1.file.getName().equals("1077.png")) {
            System.out.println("=> PASSED: Pet 1 resolved correctly to 1077.png");
            passed++;
        } else {
            System.err.println("=> FAILED: Pet 1 resolution failed: " + (r1 != null ? r1.file : "null"));
            failed++;
        }

        // Pet 4 (Tiểu Sunny, frame 80 -> 1080.png exists)
        PetTemplate p4 = new PetTemplate(4, "Tiểu Sunny", 706, 5, 80, null);
        PetCanvas.PetImageResult r4 = PetCanvas.resolvePetImage(p4);
        if (r4 != null && r4.file != null && r4.file.getName().equals("1080.png")) {
            System.out.println("=> PASSED: Pet 4 resolved correctly to 1080.png");
            passed++;
        } else {
            System.err.println("=> FAILED: Pet 4 resolution failed: " + (r4 != null ? r4.file : "null"));
            failed++;
        }

        // Pet 8 (Choper giáng sinh, frame 809 -> 1809 does NOT exist; MUST NOT fallback to 8.png!)
        PetTemplate p8 = new PetTemplate(8, "Choper giáng sinh", 712, 5, 809, null);
        PetCanvas.PetImageResult r8 = PetCanvas.resolvePetImage(p8);
        if (r8 == null) {
            System.out.println("=> PASSED: Pet 8 returned null (correctly did NOT load 8.png item map tile!)");
            passed++;
        } else {
            System.err.println("=> FAILED: Pet 8 incorrectly loaded image: " + r8.file.getAbsolutePath());
            failed++;
        }

        // Pet 9 (Rắn Quý Tỵ, frame 810 -> MUST NOT load 9.png!)
        PetTemplate p9 = new PetTemplate(9, "Rắn Quý Tỵ", 713, 5, 810, null);
        PetCanvas.PetImageResult r9 = PetCanvas.resolvePetImage(p9);
        if (r9 == null) {
            System.out.println("=> PASSED: Pet 9 returned null (correctly did NOT load 9.png item map tile!)");
            passed++;
        } else {
            System.err.println("=> FAILED: Pet 9 incorrectly loaded image: " + r9.file.getAbsolutePath());
            failed++;
        }

        // Pet 10 (Ma xà, frame 807 -> MUST NOT load 10.png!)
        PetTemplate p10 = new PetTemplate(10, "Ma xà", 712, 2, 807, null);
        PetCanvas.PetImageResult r10 = PetCanvas.resolvePetImage(p10);
        if (r10 == null) {
            System.out.println("=> PASSED: Pet 10 returned null (correctly did NOT load 10.png item map tile!)");
            passed++;
        } else {
            System.err.println("=> FAILED: Pet 10 incorrectly loaded image: " + r10.file.getAbsolutePath());
            failed++;
        }

        // Pet 11 (Thủy Quái, frame 808 -> MUST NOT load 11.png!)
        PetTemplate p11 = new PetTemplate(11, "Thủy Quái", 713, 2, 808, null);
        PetCanvas.PetImageResult r11 = PetCanvas.resolvePetImage(p11);
        if (r11 == null) {
            System.out.println("=> PASSED: Pet 11 returned null (correctly did NOT load 11.png item map tile!)");
            passed++;
        } else {
            System.err.println("=> FAILED: Pet 11 incorrectly loaded image: " + r11.file.getAbsolutePath());
            failed++;
        }

        // Pet 12 (Thần Hỏa Prometheus, frame 111 -> MUST NOT load 111.png!)
        PetTemplate p12 = new PetTemplate(12, "Thần Hỏa Prometheus", 702, 4, 111, null);
        PetCanvas.PetImageResult r12 = PetCanvas.resolvePetImage(p12);
        if (r12 == null) {
            System.out.println("=> PASSED: Pet 12 returned null (correctly did NOT load 111.png item map tile!)");
            passed++;
        } else {
            System.err.println("=> FAILED: Pet 12 incorrectly loaded image: " + r12.file.getAbsolutePath());
            failed++;
        }

        // ----------------------------------------------------
        // Test 4: GameMapCanvas item lazy resolution
        // ----------------------------------------------------
        System.out.println("\n[Test 4] Verifying GameMapCanvas Item lazy resolution...");
        GameMap map = new GameMap();
        map.id = 999;
        map.width = 10;
        map.height = 10;
        map.setTileSetId((byte)0);
        map.items.add(new ItemMapEntity((short)0, 2, 3));
        map.items.add(new ItemMapEntity((short)1, 4, 5));

        GameMapCanvas canvas = new GameMapCanvas();
        canvas.setMap(map);

        boolean itemsResolved = true;
        for (ItemMapEntity it : map.items) {
            if (it.template == null) {
                System.err.println("=> FAILED: Item entity " + it.templateId + " template was not resolved!");
                itemsResolved = false;
            } else {
                System.out.println("   Item entity " + it.templateId + " template resolved: idImage=" + it.template.idImage + ", layer=" + it.template.layer);
            }
        }

        if (itemsResolved) {
            System.out.println("=> PASSED: GameMapCanvas setMap resolved all item templates successfully.");
            passed++;
        } else {
            failed++;
        }

        System.out.println("\n==================================================");
        System.out.println("  TEST RESULTS: " + passed + " PASSED, " + failed + " FAILED");
        System.out.println("==================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }
}
