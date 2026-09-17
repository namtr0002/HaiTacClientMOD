import org.junit.Test;
import static org.junit.Assert.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GenerateThanTrangIcons {

    public static class SetTheme {
        public String name;
        public Color primaryColor;
        public Color secondaryColor;
        public Color glowColor;
        public Color darkBg;
        public String emblemType;

        public SetTheme(String name, Color primary, Color secondary, Color glow, Color dark, String emblem) {
            this.name = name;
            this.primaryColor = primary;
            this.secondaryColor = secondary;
            this.glowColor = glow;
            this.darkBg = dark;
            this.emblemType = emblem;
        }
    }

    private static final SetTheme[] THEMES = new SetTheme[]{
        new SetTheme("Khởi Nguyên", new Color(255, 215, 0), new Color(255, 255, 240), new Color(255, 235, 100), new Color(30, 20, 0), "DIVINE"),
        new SetTheme("Akainu Magma", new Color(255, 60, 0), new Color(255, 170, 0), new Color(255, 100, 0), new Color(40, 5, 0), "MAGMA"),
        new SetTheme("Aokiji Ice", new Color(0, 191, 255), new Color(224, 255, 255), new Color(135, 206, 250), new Color(0, 25, 45), "ICE"),
        new SetTheme("Kizaru Light", new Color(255, 240, 0), new Color(255, 140, 0), new Color(255, 255, 180), new Color(35, 25, 0), "LIGHT"),
        new SetTheme("Blackbeard Void", new Color(147, 50, 230), new Color(40, 0, 70), new Color(180, 80, 255), new Color(15, 0, 25), "DARK"),
        new SetTheme("Enel Thunder", new Color(0, 240, 255), new Color(255, 230, 0), new Color(180, 255, 255), new Color(0, 20, 40), "THUNDER"),
        new SetTheme("Whitebeard Quake", new Color(64, 224, 208), new Color(255, 99, 71), new Color(175, 238, 238), new Color(10, 25, 30), "QUAKE"),
        new SetTheme("Law Room", new Color(0, 206, 209), new Color(0, 250, 154), new Color(127, 255, 212), new Color(0, 25, 25), "ROOM"),
        new SetTheme("Kid Magnet", new Color(220, 20, 60), new Color(192, 192, 192), new Color(255, 69, 0), new Color(35, 5, 10), "MAGNET"),
        new SetTheme("Magellan Venom", new Color(199, 21, 133), new Color(128, 0, 128), new Color(255, 105, 180), new Color(30, 0, 25), "VENOM"),
        new SetTheme("Hancock Mero", new Color(255, 105, 180), new Color(255, 20, 147), new Color(255, 192, 203), new Color(35, 0, 20), "LOVE"),
        new SetTheme("Marco Phoenix", new Color(0, 238, 255), new Color(255, 215, 0), new Color(100, 255, 255), new Color(0, 25, 35), "PHOENIX"),
        new SetTheme("Sengoku Buddha", new Color(255, 215, 0), new Color(255, 165, 0), new Color(255, 245, 150), new Color(35, 20, 0), "BUDDHA"),
        new SetTheme("Kaido Beast", new Color(0, 150, 180), new Color(255, 50, 50), new Color(100, 220, 255), new Color(5, 20, 25), "DRAGON"),
        new SetTheme("Who's Who Saber", new Color(245, 222, 179), new Color(178, 34, 34), new Color(255, 160, 122), new Color(30, 10, 10), "SABER"),
        new SetTheme("Queen Dino", new Color(50, 205, 50), new Color(255, 215, 0), new Color(152, 251, 152), new Color(10, 30, 10), "MECH")
    };

    @Test
    public void generateAllIcons() throws Exception {
        File baseDir = new File("data/icon");
        File dir1 = new File(baseDir, "1");
        File dir2 = new File(baseDir, "2");
        File dir3 = new File(baseDir, "3");
        File dir4 = new File(baseDir, "4");

        dir1.mkdirs();
        dir2.mkdirs();
        dir3.mkdirs();
        dir4.mkdirs();

        for (int setIdx = 0; setIdx < 16; setIdx++) {
            SetTheme theme = THEMES[setIdx];
            int baseSkillId = 4000 + setIdx * 5;

            // Generate 5 Skills for this set
            for (int skIdx = 1; skIdx <= 5; skIdx++) {
                int skillId = baseSkillId + skIdx;
                int buffIconId = skillId + 500; // 4501..4580

                // 1. Generate Main Skill Icon (88x88 for Zoom 4)
                BufferedImage imgSkillZ4 = renderSkillIcon(88, theme, skIdx, setIdx + 1);
                BufferedImage imgSkillZ3 = resizeImage(imgSkillZ4, 66, 66);
                BufferedImage imgSkillZ2 = resizeImage(imgSkillZ4, 44, 44);
                BufferedImage imgSkillZ1 = resizeImage(imgSkillZ4, 22, 22);

                ImageIO.write(imgSkillZ4, "png", new File(dir4, skillId + ".png"));
                ImageIO.write(imgSkillZ3, "png", new File(dir3, skillId + ".png"));
                ImageIO.write(imgSkillZ2, "png", new File(dir2, skillId + ".png"));
                ImageIO.write(imgSkillZ1, "png", new File(dir1, skillId + ".png"));

                // 2. Generate Buff Icon (44x44 for Zoom 4, idIcon + 500)
                BufferedImage imgBuffZ4 = renderBuffIcon(44, theme, skIdx, setIdx + 1);
                BufferedImage imgBuffZ3 = resizeImage(imgBuffZ4, 33, 33);
                BufferedImage imgBuffZ2 = resizeImage(imgBuffZ4, 22, 22);
                BufferedImage imgBuffZ1 = resizeImage(imgBuffZ4, 11, 11);

                ImageIO.write(imgBuffZ4, "png", new File(dir4, buffIconId + ".png"));
                ImageIO.write(imgBuffZ3, "png", new File(dir3, buffIconId + ".png"));
                ImageIO.write(imgBuffZ2, "png", new File(dir2, buffIconId + ".png"));
                ImageIO.write(imgBuffZ1, "png", new File(dir1, buffIconId + ".png"));
            }
        }

        // Verify count
        for (int z = 1; z <= 4; z++) {
            File zDir = new File(baseDir, String.valueOf(z));
            for (int id = 4001; id <= 4080; id++) {
                assertTrue("Missing skill icon " + id + " in zoom " + z, new File(zDir, id + ".png").exists());
                assertTrue("Missing buff icon " + (id + 500) + " in zoom " + z, new File(zDir, (id + 500) + ".png").exists());
            }
        }
        System.out.println("SUCCESS: Generated all 160 Than Trang icons (80 skills + 80 buff icons) for Zoom 1, 2, 3, 4!");
    }

    private BufferedImage renderSkillIcon(int size, SetTheme theme, int skillType, int setNum) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int pad = 3;
        int innerSize = size - pad * 2;

        // 1. Dark Theme Gradient Background
        Paint bgPaint = new RadialGradientPaint(
            new Point2D.Float(size / 2f, size / 2f),
            size * 0.6f,
            new float[]{0.0f, 0.7f, 1.0f},
            new Color[]{theme.glowColor.darker(), theme.darkBg, new Color(5, 5, 8)}
        );
        g.setPaint(bgPaint);
        g.fillRoundRect(pad, pad, innerSize, innerSize, 16, 16);

        // 2. Multi-layer Glowing Ornate Border
        g.setColor(new Color(theme.glowColor.getRed(), theme.glowColor.getGreen(), theme.glowColor.getBlue(), 120));
        g.setStroke(new BasicStroke(3.5f));
        g.drawRoundRect(pad + 1, pad + 1, innerSize - 2, innerSize - 2, 15, 15);

        g.setColor(theme.primaryColor);
        g.setStroke(new BasicStroke(2.0f));
        g.drawRoundRect(pad + 2, pad + 2, innerSize - 4, innerSize - 4, 13, 13);

        // Inner border highlight
        g.setColor(new Color(255, 255, 255, 100));
        g.setStroke(new BasicStroke(1.0f));
        g.drawRoundRect(pad + 4, pad + 4, innerSize - 8, innerSize - 8, 10, 10);

        // 3. Central Graphic Sigil based on Skill Type (1: Active1, 2: Active2, 3: Transform, 4: Stat Buff, 5: Passive)
        int cx = size / 2;
        int cy = size / 2;

        drawSkillSigil(g, cx, cy, size, theme, skillType);

        // 4. Badge / Tag Indicator on Corners
        drawSkillBadge(g, size, skillType, theme);

        g.dispose();
        return img;
    }

    private BufferedImage renderBuffIcon(int size, SetTheme theme, int skillType, int setNum) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int pad = 2;
        int innerSize = size - pad * 2;

        // Circular / Octagonal Dark Gradient Badge
        Paint bgPaint = new RadialGradientPaint(
            new Point2D.Float(size / 2f, size / 2f),
            size * 0.55f,
            new float[]{0.0f, 0.75f, 1.0f},
            new Color[]{theme.primaryColor.darker(), theme.darkBg, new Color(5, 5, 8)}
        );
        g.setPaint(bgPaint);
        g.fillOval(pad, pad, innerSize, innerSize);

        // Glowing Ring Border
        g.setColor(theme.glowColor);
        g.setStroke(new BasicStroke(2.5f));
        g.drawOval(pad + 1, pad + 1, innerSize - 2, innerSize - 2);

        g.setColor(new Color(255, 255, 255, 180));
        g.setStroke(new BasicStroke(1.2f));
        g.drawOval(pad + 2, pad + 2, innerSize - 4, innerSize - 4);

        // Central Icon Emblem
        int cx = size / 2;
        int cy = size / 2;
        drawBuffSigil(g, cx, cy, size, theme, skillType);

        g.dispose();
        return img;
    }

    private void drawSkillSigil(Graphics2D g, int cx, int cy, int size, SetTheme theme, int type) {
        switch (type) {
            case 1: { // Active Skill 1 (Burst Attack / Strike)
                // Diagonal Energy Blade / Fist Flash
                g.setColor(theme.secondaryColor);
                g.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawLine(cx - 20, cy + 20, cx + 20, cy - 20);

                g.setColor(theme.primaryColor);
                g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawLine(cx - 20, cy + 20, cx + 20, cy - 20);

                // Cross energy slashes
                g.setColor(Color.WHITE);
                g.fillOval(cx - 7, cy - 7, 14, 14);
                g.setColor(theme.glowColor);
                g.drawOval(cx - 12, cy - 12, 24, 24);
                break;
            }
            case 2: { // Active Skill 2 (Ultimate Area Blast / Nova)
                // 8-Pointed Starburst / Nova Explosion
                g.setColor(new Color(theme.glowColor.getRed(), theme.glowColor.getGreen(), theme.glowColor.getBlue(), 160));
                for (int i = 0; i < 8; i++) {
                    double angle = i * Math.PI / 4.0;
                    int x2 = cx + (int) (Math.cos(angle) * 24);
                    int y2 = cy + (int) (Math.sin(angle) * 24);
                    g.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.drawLine(cx, cy, x2, y2);
                }
                g.setColor(theme.primaryColor);
                for (int i = 0; i < 8; i++) {
                    double angle = i * Math.PI / 4.0;
                    int x2 = cx + (int) (Math.cos(angle) * 22);
                    int y2 = cy + (int) (Math.sin(angle) * 22);
                    g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.drawLine(cx, cy, x2, y2);
                }
                g.setColor(Color.WHITE);
                g.fillOval(cx - 8, cy - 8, 16, 16);
                break;
            }
            case 3: { // Transformation Buff (Avatar / Crown / Wing)
                // Crown / Wings of Awakening
                g.setColor(theme.primaryColor);
                g.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                // Wing Left
                g.drawArc(cx - 24, cy - 14, 24, 30, 45, 180);
                // Wing Right
                g.drawArc(cx, cy - 14, 24, 30, -45, 180);

                // Center Awakening Core
                g.setColor(theme.secondaryColor);
                Polygon poly = new Polygon();
                poly.addPoint(cx, cy - 18);
                poly.addPoint(cx + 12, cy);
                poly.addPoint(cx, cy + 18);
                poly.addPoint(cx - 12, cy);
                g.fill(poly);

                g.setColor(Color.WHITE);
                g.fillOval(cx - 4, cy - 4, 8, 8);
                break;
            }
            case 4: { // Stat Buff (Shield / Power Crest)
                // Ornate Power Shield
                Polygon shield = new Polygon();
                shield.addPoint(cx - 16, cy - 16);
                shield.addPoint(cx + 16, cy - 16);
                shield.addPoint(cx + 16, cy + 4);
                shield.addPoint(cx, cy + 20);
                shield.addPoint(cx - 16, cy + 4);

                g.setColor(new Color(theme.primaryColor.getRed(), theme.primaryColor.getGreen(), theme.primaryColor.getBlue(), 180));
                g.fill(shield);
                g.setColor(theme.glowColor);
                g.setStroke(new BasicStroke(2.5f));
                g.draw(shield);

                // Upward Stat Arrow inside Shield
                g.setColor(Color.WHITE);
                Polygon arrow = new Polygon();
                arrow.addPoint(cx, cy - 12);
                arrow.addPoint(cx + 8, cy - 2);
                arrow.addPoint(cx + 3, cy - 2);
                arrow.addPoint(cx + 3, cy + 10);
                arrow.addPoint(cx - 3, cy + 10);
                arrow.addPoint(cx - 3, cy - 2);
                arrow.addPoint(cx - 8, cy - 2);
                g.fill(arrow);
                break;
            }
            case 5: { // Passive (Rune Eye / Hexagon Crest)
                // Mystic Hexagon
                Polygon hex = new Polygon();
                for (int i = 0; i < 6; i++) {
                    double a = i * Math.PI / 3.0;
                    hex.addPoint(cx + (int) (Math.cos(a) * 20), cy + (int) (Math.sin(a) * 20));
                }
                g.setColor(new Color(theme.primaryColor.getRed(), theme.primaryColor.getGreen(), theme.primaryColor.getBlue(), 120));
                g.fill(hex);
                g.setColor(theme.glowColor);
                g.setStroke(new BasicStroke(2.5f));
                g.draw(hex);

                // Infinity / Dragon Eye Symbol
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(3f));
                g.drawOval(cx - 12, cy - 6, 12, 12);
                g.drawOval(cx, cy - 6, 12, 12);
                break;
            }
        }
    }

    private void drawSkillBadge(Graphics2D g, int size, int type, SetTheme theme) {
        String label = switch (type) {
            case 1 -> "A1";
            case 2 -> "A2";
            case 3 -> "TF";
            case 4 -> "BF";
            case 5 -> "PS";
            default -> "";
        };

        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        int tw = fm.stringWidth(label);
        int th = fm.getAscent();

        int bx = size - tw - 10;
        int by = size - 8;

        // Badge Pill Background
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRoundRect(bx - 3, by - th - 1, tw + 6, th + 4, 6, 6);
        g.setColor(theme.glowColor);
        g.setStroke(new BasicStroke(1.2f));
        g.drawRoundRect(bx - 3, by - th - 1, tw + 6, th + 4, 6, 6);

        g.setColor(Color.WHITE);
        g.drawString(label, bx, by);
    }

    private void drawBuffSigil(Graphics2D g, int cx, int cy, int size, SetTheme theme, int type) {
        switch (type) {
            case 3: { // Transform Buff (Awakening Wings/Crown)
                g.setColor(theme.glowColor);
                g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawArc(cx - 12, cy - 8, 12, 16, 45, 180);
                g.drawArc(cx, cy - 8, 12, 16, -45, 180);
                g.setColor(Color.WHITE);
                g.fillOval(cx - 3, cy - 3, 6, 6);
                break;
            }
            case 4: { // Stat Buff (Power Shield & Arrow)
                Polygon shield = new Polygon();
                shield.addPoint(cx - 9, cy - 9);
                shield.addPoint(cx + 9, cy - 9);
                shield.addPoint(cx + 9, cy + 2);
                shield.addPoint(cx, cy + 11);
                shield.addPoint(cx - 9, cy + 2);

                g.setColor(new Color(theme.primaryColor.getRed(), theme.primaryColor.getGreen(), theme.primaryColor.getBlue(), 200));
                g.fill(shield);
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(1.2f));
                g.draw(shield);

                // Small arrow
                Polygon arrow = new Polygon();
                arrow.addPoint(cx, cy - 6);
                arrow.addPoint(cx + 4, cy - 1);
                arrow.addPoint(cx + 1, cy - 1);
                arrow.addPoint(cx + 1, cy + 5);
                arrow.addPoint(cx - 1, cy + 5);
                arrow.addPoint(cx - 1, cy - 1);
                arrow.addPoint(cx - 4, cy - 1);
                g.setColor(Color.YELLOW);
                g.fill(arrow);
                break;
            }
            default: { // General Buff Sigil
                g.setColor(theme.glowColor);
                g.setStroke(new BasicStroke(2f));
                g.drawOval(cx - 8, cy - 8, 16, 16);
                g.setColor(Color.WHITE);
                g.fillOval(cx - 4, cy - 4, 8, 8);
                break;
            }
        }
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }
}
