package com.deplor.haitactihontool.util;

import com.deplor.haitactihontool.effect.*;
import com.deplor.haitactihontool.effectauto.EffectAutoModel;
import com.deplor.haitactihontool.effectauto.EffectAutoParser;
import com.deplor.haitactihontool.part.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper toàn diện để xuất Animated GIF cho Effect, EffectAuto và Character Part Actions.
 */
public class GifExportHelper {

    public static final double LOGIC_SCALE = 4.0;
    private static final Color KEY_TRANSPARENT = new Color(0, 254, 0); // Chroma Key color for GIF transparency

    public interface ProgressListener {
        void onProgress(int current, int total, String message);
    }

    // ────────────────────────────────────────────────────────────────────────
    // 1. EFFECT & EFFECTAUTO GIF EXPORT
    // ────────────────────────────────────────────────────────────────────────

    /**
     * Xuất một EffectModel thành file Animated GIF theo chuỗi Sequence.
     */
    public static void exportEffectSequenceToGif(EffectModel model, File outputFile, int frameDelayMs,
                                                  double zoom, boolean transparentBg, Color bgColor) throws Exception {
        if (model == null) throw new IllegalArgumentException("Model không được null!");
        if (model.getSeqLen() == 0 && model.getFrameCount() == 0) {
            throw new IllegalStateException("Effect không có frame hoặc sequence nào!");
        }

        // Tạo thư mục cha nếu chưa có
        if (outputFile.getParentFile() != null) {
            outputFile.getParentFile().mkdirs();
        }

        // Lấy danh sách frame indices theo sequence
        int seqLen = model.getSeqLen();
        int[] seqFrames;
        if (seqLen > 0) {
            seqFrames = new int[seqLen];
            for (int i = 0; i < seqLen; i++) {
                seqFrames[i] = model.resolveSeqFrame(i);
            }
        } else {
            seqFrames = new int[model.getFrameCount()];
            for (int i = 0; i < model.getFrameCount(); i++) seqFrames[i] = i;
        }

        // Tính Bounding Box bao phủ toàn bộ các frames trong sequence
        Rectangle bounds = computeEffectSequenceBounds(model, seqFrames, zoom);
        int pad = (int)(24 * zoom);
        int gifW = Math.max(64, bounds.width + pad * 2);
        int gifH = Math.max(64, bounds.height + pad * 2);

        // Gốc tọa độ tương đối
        int anchorX = pad - bounds.x;
        int anchorY = pad - bounds.y;

        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outputFile);
        encoder.setDelay(frameDelayMs > 0 ? frameDelayMs : 100);
        encoder.setRepeat(0); // Vòng lặp vô tận
        encoder.setDispose(2); // Restore to background để không bị đè layer

        if (transparentBg) {
            encoder.setTransparent(KEY_TRANSPARENT);
        }

        for (int frameIdx : seqFrames) {
            BufferedImage frameImg = renderEffectFrame(model, frameIdx, gifW, gifH, anchorX, anchorY, zoom, transparentBg, bgColor);
            encoder.addFrame(frameImg);
        }

        encoder.finish();
    }

    /**
     * Xuất EffectAuto thành file Animated GIF theo chuỗi Sequence.
     */
    public static void exportEffectAutoSequenceToGif(EffectAutoModel model, File outputFile, int frameDelayMs,
                                                      double zoom, boolean transparentBg, Color bgColor) throws Exception {
        exportEffectSequenceToGif(model, outputFile, frameDelayMs, zoom, transparentBg, bgColor);
    }

    /**
     * Xuất hàng loạt toàn bộ Effect có trong thư mục Data/Effect thành các file GIF.
     */
    public static void exportAllEffectsToGif(String dataDir, String imgDir, File outputDir, int frameDelayMs,
                                             double zoom, boolean transparentBg, Color bgColor, ProgressListener listener) throws Exception {
        File dFolder = new File(dataDir);
        if (!dFolder.exists() || !dFolder.isDirectory()) {
            throw new IllegalArgumentException("Thư mục Data Effect không tồn tại: " + dataDir);
        }
        outputDir.mkdirs();

        File[] files = dFolder.listFiles((dir, name) -> name.matches("\\d+"));
        if (files == null || files.length == 0) return;

        Arrays.sort(files, (a, b) -> {
            try { return Integer.compare(Integer.parseInt(a.getName()), Integer.parseInt(b.getName())); }
            catch (Exception e) { return a.getName().compareTo(b.getName()); }
        });

        EffectBinaryParser parser = new EffectBinaryParser(dataDir, imgDir);
        int total = files.length;
        for (int i = 0; i < total; i++) {
            File f = files[i];
            try {
                int id = Integer.parseInt(f.getName());
                if (listener != null) listener.onProgress(i + 1, total, "Đang xuất Effect ID #" + id + " (" + (i + 1) + "/" + total + ")");
                EffectModel model = parser.parse(id);
                if (model != null && model.getFrameCount() > 0) {
                    File outGif = new File(outputDir, "effect_" + id + ".gif");
                    exportEffectSequenceToGif(model, outGif, frameDelayMs, zoom, transparentBg, bgColor);
                }
            } catch (Exception ex) {
                // Tiếp tục với các file khác
            }
        }
    }

    /**
     * Xuất hàng loạt toàn bộ EffectAuto có trong thư mục Data/EffectAuto thành các file GIF.
     */
    public static void exportAllEffectAutosToGif(String dataDir, String imgDir, File outputDir, int frameDelayMs,
                                                 double zoom, boolean transparentBg, Color bgColor, ProgressListener listener) throws Exception {
        File dFolder = new File(dataDir);
        if (!dFolder.exists() || !dFolder.isDirectory()) {
            throw new IllegalArgumentException("Thư mục Data EffectAuto không tồn tại: " + dataDir);
        }
        outputDir.mkdirs();

        File[] files = dFolder.listFiles((dir, name) -> name.matches("\\d+"));
        if (files == null || files.length == 0) return;

        Arrays.sort(files, (a, b) -> {
            try { return Integer.compare(Integer.parseInt(a.getName()), Integer.parseInt(b.getName())); }
            catch (Exception e) { return a.getName().compareTo(b.getName()); }
        });

        EffectAutoParser parser = new EffectAutoParser(dataDir, imgDir);
        int total = files.length;
        for (int i = 0; i < total; i++) {
            File f = files[i];
            try {
                int id = Integer.parseInt(f.getName());
                if (listener != null) listener.onProgress(i + 1, total, "Đang xuất EffectAuto ID #" + id + " (" + (i + 1) + "/" + total + ")");
                EffectAutoModel model = parser.parse(id);
                if (model != null && model.getFrameCount() > 0) {
                    File outGif = new File(outputDir, "effectauto_" + id + ".gif");
                    exportEffectAutoSequenceToGif(model, outGif, frameDelayMs, zoom, transparentBg, bgColor);
                }
            } catch (Exception ex) {
                // Bỏ qua lỗi và tiếp tục
            }
        }
    }

    private static Rectangle computeEffectSequenceBounds(EffectModel model, int[] seqFrames, double zoom) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (int frameIdx : seqFrames) {
            if (model.frames == null || frameIdx < 0 || frameIdx >= model.frames.length) continue;
            EffFrame frame = model.frames[frameIdx];
            if (frame.allParts == null) continue;

            for (EffPartFrame p : frame.allParts) {
                if (model.smallImages == null || p.idSmallImg < 0 || p.idSmallImg >= model.smallImages.length) continue;
                SmallImageDef si = model.smallImages[p.idSmallImg];

                int px = (int)(p.dx * LOGIC_SCALE * zoom);
                int py = (int)(p.dy * LOGIC_SCALE * zoom);
                int pw = (int)(si.w * LOGIC_SCALE * zoom);
                int ph = (int)(si.h * LOGIC_SCALE * zoom);

                if (p.rotate != 0) {
                    // Approximate bounding box of rotated part
                    double rad = Math.toRadians(Math.abs(p.rotate));
                    int rw = (int)(pw * Math.cos(rad) + ph * Math.sin(rad));
                    int rh = (int)(pw * Math.sin(rad) + ph * Math.cos(rad));
                    int cx = px + pw / 2;
                    int cy = py + ph / 2;
                    minX = Math.min(minX, cx - rw / 2);
                    minY = Math.min(minY, cy - rh / 2);
                    maxX = Math.max(maxX, cx + rw / 2);
                    maxY = Math.max(maxY, cy + rh / 2);
                } else {
                    minX = Math.min(minX, px);
                    minY = Math.min(minY, py);
                    maxX = Math.max(maxX, px + pw);
                    maxY = Math.max(maxY, py + ph);
                }
            }
        }

        if (minX > maxX || minY > maxY) {
            return new Rectangle(-50, -50, 100, 100);
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    private static BufferedImage renderEffectFrame(EffectModel model, int frameIdx, int width, int height,
                                                    int anchorX, int anchorY, double zoom, boolean transparentBg, Color bgColor) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        if (transparentBg) {
            g2.setColor(KEY_TRANSPARENT);
            g2.fillRect(0, 0, width, height);
        } else {
            g2.setColor(bgColor != null ? bgColor : new Color(18, 18, 24));
            g2.fillRect(0, 0, width, height);
        }

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (model != null && model.frames != null && frameIdx >= 0 && frameIdx < model.frames.length) {
            EffFrame frame = model.frames[frameIdx];
            if (frame.allParts != null && model.atlasImage != null && model.smallImages != null) {
                // Render parts
                for (EffPartFrame p : frame.allParts) {
                    if (p.idSmallImg < 0 || p.idSmallImg >= model.smallImages.length) continue;
                    SmallImageDef si = model.smallImages[p.idSmallImg];

                    int destX = anchorX + (int)(p.dx * LOGIC_SCALE * zoom);
                    int destY = anchorY + (int)(p.dy * LOGIC_SCALE * zoom);
                    int dw = (int)(si.w * LOGIC_SCALE * zoom);
                    int dh = (int)(si.h * LOGIC_SCALE * zoom);

                    BufferedImage atlas = model.atlasImage;
                    int iw = atlas.getWidth(), ih = atlas.getHeight();
                    int cx = (int)(si.x * LOGIC_SCALE), cy = (int)(si.y * LOGIC_SCALE), cw = (int)(si.w * LOGIC_SCALE), ch = (int)(si.h * LOGIC_SCALE);
                    if (cx >= iw) cx = 0; if (cy >= ih) cy = 0;
                    if (cx + cw > iw) cw = iw - cx; if (cy + ch > ih) ch = ih - cy;

                    if (cw > 0 && ch > 0) {
                        if (p.rotate != 0) {
                            AffineTransform oldTx = g2.getTransform();
                            g2.rotate(Math.toRadians(p.rotate), destX + dw / 2.0, destY + dh / 2.0);
                            if (p.flip == 1) {
                                g2.drawImage(atlas, destX + dw, destY, destX, destY + dh, cx, cy, cx + cw, cy + ch, null);
                            } else {
                                g2.drawImage(atlas, destX, destY, destX + dw, destY + dh, cx, cy, cx + cw, cy + ch, null);
                            }
                            g2.setTransform(oldTx);
                        } else {
                            if (p.flip == 1) {
                                g2.drawImage(atlas, destX + dw, destY, destX, destY + dh, cx, cy, cx + cw, cy + ch, null);
                            } else {
                                g2.drawImage(atlas, destX, destY, destX + dw, destY + dh, cx, cy, cx + cw, cy + ch, null);
                            }
                        }
                    }
                }
            }
        }

        g2.dispose();
        return img;
    }

    // ────────────────────────────────────────────────────────────────────────
    // 2. CHARACTER PART ACTIONS GIF EXPORT
    // ────────────────────────────────────────────────────────────────────────

    public static final int ACTION_STAND         = 0;
    public static final int ACTION_MOVE          = 1;
    public static final int ACTION_JUMP          = 2;
    public static final int ACTION_FALL          = 3;
    public static final int ACTION_ATTACK_MELEE  = 4;
    public static final int ACTION_ATTACK_SLASH  = 5;
    public static final int ACTION_ATTACK_RANGED = 6;
    public static final int ACTION_ATTACK_HEAVY  = 7;
    public static final int ACTION_HIT           = 8;
    public static final int ACTION_DIE           = 9;
    public static final int ACTION_BOAT          = 10;
    public static final int ACTION_ALL_POSES     = 11;

    // Backward compatibility aliases
    public static final int ACTION_ATTACK = ACTION_ATTACK_MELEE;

    public static final int[] FE_STAND  = CharInfoData.FE_STAND;
    public static final int[] FE_MOVE   = CharInfoData.FE_RUN;
    public static final int[] FE_JUMP   = CharInfoData.FE_JUMP;
    public static final int[] FE_FALL   = CharInfoData.FE_FALL;
    public static final int[] FE_ATTACK = CharInfoData.FE_ATTACK_MELEE;
    public static final int[] FE_DIE    = CharInfoData.FE_DIE;

    public static int[] getActionSequence(int action) {
        switch (action) {
            case ACTION_MOVE:          return CharInfoData.FE_RUN;
            case ACTION_JUMP:          return CharInfoData.FE_JUMP;
            case ACTION_FALL:          return CharInfoData.FE_FALL;
            case ACTION_ATTACK_MELEE:  return CharInfoData.FE_ATTACK_MELEE;
            case ACTION_ATTACK_SLASH:  return CharInfoData.FE_ATTACK_SLASH;
            case ACTION_ATTACK_RANGED: return CharInfoData.FE_ATTACK_RANGED;
            case ACTION_ATTACK_HEAVY:  return CharInfoData.FE_ATTACK_HEAVY;
            case ACTION_HIT:           return CharInfoData.FE_HIT;
            case ACTION_DIE:           return CharInfoData.FE_DIE;
            case ACTION_BOAT:          return CharInfoData.FE_BOAT;
            case ACTION_ALL_POSES: {
                int[] all = new int[CharInfoData.CharInfo.length];
                for (int i = 0; i < all.length; i++) all[i] = i;
                return all;
            }
            default:                   return CharInfoData.FE_STAND;
        }
    }

    public static String getActionName(int action) {
        switch (action) {
            case ACTION_MOVE:          return "01_move_run";
            case ACTION_JUMP:          return "02_jump";
            case ACTION_FALL:          return "03_fall";
            case ACTION_ATTACK_MELEE:  return "04_attack_melee";
            case ACTION_ATTACK_SLASH:  return "05_attack_slash";
            case ACTION_ATTACK_RANGED: return "06_attack_ranged";
            case ACTION_ATTACK_HEAVY:  return "07_attack_heavy";
            case ACTION_HIT:           return "08_hit";
            case ACTION_DIE:           return "09_die";
            case ACTION_BOAT:          return "10_sit_boat";
            case ACTION_ALL_POSES:     return "11_all_poses_loop";
            default:                   return "00_stand";
        }
    }

    public static String getActionDisplayName(int action) {
        switch (action) {
            case ACTION_MOVE:          return "Chạy bộ (Move/Run)";
            case ACTION_JUMP:          return "Nhảy (Jump)";
            case ACTION_FALL:          return "Rơi (Fall)";
            case ACTION_ATTACK_MELEE:  return "Cận chiến/Đấm đá (Attack Melee)";
            case ACTION_ATTACK_SLASH:  return "Chém liên hoàn (Attack Slash)";
            case ACTION_ATTACK_RANGED: return "Bắn/Cast phép (Attack Ranged)";
            case ACTION_ATTACK_HEAVY:  return "Đại chiêu/Gồng (Attack Heavy)";
            case ACTION_HIT:           return "Thụ thương (Hit)";
            case ACTION_DIE:           return "Gục ngã (Die)";
            case ACTION_BOAT:          return "Ngồi thuyền (Sit Boat)";
            case ACTION_ALL_POSES:     return "Tất cả 62 Poses Loop";
            default:                   return "Đứng thở (Stand)";
        }
    }

    /**
     * Tự động tính toán Bounding Box chính xác cho một chuỗi Frame của Character Part,
     * đảm bảo KHÔNG BAO GIỜ bị cắt xén đầu, mũ, nón, vũ khí hay chân trong bất kỳ tư thế nào.
     */
    public static Rectangle computePartSequenceBounds(short[] fashion, List<mPart> allParts, int[] seqFrames, int direction, double zoom) {
        if (fashion == null || allParts == null || seqFrames == null || seqFrames.length == 0) {
            int defW = (int)(300 * zoom);
            int defH = (int)(340 * zoom);
            return new Rectangle(defW / 2, (int)(defH * 0.82), defW, defH);
        }

        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        int[] sort = (direction == 2)
                ? new int[] { 7, 1, 2, 3, 6, 0, 5, 4 }
                : new int[] { 7, 1, 2, 0, 5, 4, 3, 6 };

        for (int frameIdx : seqFrames) {
            int fr = Math.abs(frameIdx) % CharInfoData.CharInfo.length;

            for (int slotIndex : sort) {
                mPart part = getWearingPart(fashion, allParts, slotIndex);
                if (part == null) continue;

                int ciSlot = slotIndex;
                if (slotIndex == 6) ciSlot = 3;
                else if (slotIndex == 7) ciSlot = 6;

                if (ciSlot < 0 || ciSlot >= CharInfoData.CharInfo[fr].length) continue;
                int ciIdx = CharInfoData.CharInfo[fr][ciSlot][0];
                if (ciIdx < 0 || ciIdx >= part.pi.length) continue;

                PartImage pi = part.pi[ciIdx];
                if (pi == null || pi.id < 0) continue;

                BufferedImage partImg = PartDataManager.getImage(pi.id);
                if (partImg == null) continue;

                int[] sc = computeScreenTopLeft(0, 0, fr, slotIndex, pi, fashion, direction, zoom);
                int imgW = (int)(partImg.getWidth() * zoom);
                int imgH = (int)(partImg.getHeight() * zoom);

                int pLeft, pRight;
                if (direction == 2) {
                    pLeft = sc[0] - imgW;
                    pRight = sc[0];
                } else {
                    pLeft = sc[0];
                    pRight = sc[0] + imgW;
                }
                int pTop = sc[1];
                int pBottom = sc[1] + imgH;

                minX = Math.min(minX, pLeft);
                maxX = Math.max(maxX, pRight);
                minY = Math.min(minY, pTop);
                maxY = Math.max(maxY, pBottom);
            }
        }

        if (minX >= maxX || minY >= maxY) {
            int defW = (int)(300 * zoom);
            int defH = (int)(340 * zoom);
            return new Rectangle(defW / 2, (int)(defH * 0.82), defW, defH);
        }

        int pad = (int)(28 * zoom);
        int neededW = (maxX - minX) + pad * 2;
        int neededH = (maxY - minY) + pad * 2;

        int finalW = Math.max(neededW, (int)(280 * zoom));
        int finalH = Math.max(neededH, (int)(320 * zoom));

        int extraW = finalW - (maxX - minX);
        int anchorX = -minX + extraW / 2;

        int extraH = finalH - (maxY - minY);
        int anchorY = -minY + extraH / 2;

        return new Rectangle(anchorX, anchorY, finalW, finalH);
    }

    /**
     * Xuất một hành động cụ thể của Character thành file GIF.
     */
    public static void exportPartActionToGif(short[] fashion, List<mPart> allParts, int action, int direction,
                                             File outputFile, int frameDelayMs, double zoom, boolean transparentBg, Color bgColor) throws Exception {
        if (fashion == null || allParts == null) {
            throw new IllegalArgumentException("Dữ liệu trang bị fashion hoặc danh sách part không hợp lệ!");
        }

        if (outputFile.getParentFile() != null) {
            outputFile.getParentFile().mkdirs();
        }

        int[] seq = getActionSequence(action);

        // Tính bounds toàn cục dựa trên toàn bộ 62 pose để kích thước frame GIF luôn đồng bộ, không bao giờ bị cắt đầu/mũ
        int[] allFrames = new int[CharInfoData.CharInfo.length];
        for (int i = 0; i < allFrames.length; i++) allFrames[i] = i;
        Rectangle bounds = computePartSequenceBounds(fashion, allParts, allFrames, direction, zoom);

        int gifW = bounds.width;
        int gifH = bounds.height;
        int ox = bounds.x;
        int oy = bounds.y;

        AnimatedGifEncoder encoder = new AnimatedGifEncoder();
        encoder.start(outputFile);
        encoder.setDelay(frameDelayMs > 0 ? frameDelayMs : 120);
        encoder.setRepeat(0);
        encoder.setDispose(2);

        if (transparentBg) {
            encoder.setTransparent(KEY_TRANSPARENT);
        }

        for (int frameIdx : seq) {
            BufferedImage frameImg = renderPartFrame(fashion, allParts, frameIdx, direction, gifW, gifH, ox, oy, zoom, transparentBg, bgColor, true);
            encoder.addFrame(frameImg);
        }

        encoder.finish();
    }

    /**
     * Xuất toàn bộ tất cả hành động của Character:
     * - Nếu separateFiles = true: Tạo từng file gif riêng (prefix_stand.gif, prefix_move.gif, etc.)
     * - Nếu separateFiles = false: Tạo 1 file gif duy nhất nối chuỗi tất cả hành động (Action Combo Reel).
     */
    public static void exportAllPartActionsToGif(short[] fashion, List<mPart> allParts, int direction,
                                                 File outputDir, String prefix, int frameDelayMs, double zoom,
                                                 boolean transparentBg, Color bgColor, boolean separateFiles,
                                                 ProgressListener listener) throws Exception {
        outputDir.mkdirs();
        int[] actions = {
            ACTION_STAND, ACTION_MOVE, ACTION_JUMP, ACTION_FALL,
            ACTION_ATTACK_MELEE, ACTION_ATTACK_SLASH, ACTION_ATTACK_RANGED, ACTION_ATTACK_HEAVY,
            ACTION_HIT, ACTION_DIE, ACTION_BOAT, ACTION_ALL_POSES
        };

        if (separateFiles) {
            int total = actions.length;
            for (int i = 0; i < total; i++) {
                int act = actions[i];
                String name = getActionName(act);
                if (listener != null) listener.onProgress(i + 1, total, "Đang xuất hành động " + getActionDisplayName(act) + " (" + (i + 1) + "/" + total + ")...");
                File outGif = new File(outputDir, prefix + "_" + name + ".gif");
                exportPartActionToGif(fashion, allParts, act, direction, outGif, frameDelayMs, zoom, transparentBg, bgColor);
            }
        } else {
            // Nối chuỗi tất cả hành động thành 1 file GIF duy nhất
            if (listener != null) listener.onProgress(1, 1, "Đang tạo GIF tổng hợp tất cả hành động...");
            File outGif = new File(outputDir, prefix + "_all_actions.gif");

            int[] allFrames = new int[CharInfoData.CharInfo.length];
            for (int i = 0; i < allFrames.length; i++) allFrames[i] = i;
            Rectangle bounds = computePartSequenceBounds(fashion, allParts, allFrames, direction, zoom);

            int gifW = bounds.width;
            int gifH = bounds.height;
            int ox = bounds.x;
            int oy = bounds.y;

            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            encoder.start(outGif);
            encoder.setDelay(frameDelayMs > 0 ? frameDelayMs : 120);
            encoder.setRepeat(0);
            encoder.setDispose(2);

            if (transparentBg) {
                encoder.setTransparent(KEY_TRANSPARENT);
            }

            for (int act : actions) {
                int[] seq = getActionSequence(act);
                for (int frameIdx : seq) {
                    BufferedImage frameImg = renderPartFrame(fashion, allParts, frameIdx, direction, gifW, gifH, ox, oy, zoom, transparentBg, bgColor, true);
                    encoder.addFrame(frameImg);
                }
            }

            encoder.finish();
        }
    }

    /**
     * Xuất tất cả 62 Pose frames thành từng ảnh PNG riêng lẻ.
     */
    public static void exportAllPoseFramesToPng(short[] fashion, List<mPart> allParts, int direction,
                                                File outputDir, String prefix, double zoom,
                                                boolean transparentBg, Color bgColor,
                                                ProgressListener listener) throws Exception {
        outputDir.mkdirs();
        int total = CharInfoData.CharInfo.length;
        int[] allFrames = new int[total];
        for (int i = 0; i < total; i++) allFrames[i] = i;

        Rectangle bounds = computePartSequenceBounds(fashion, allParts, allFrames, direction, zoom);
        int frameW = bounds.width;
        int frameH = bounds.height;
        int ox = bounds.x;
        int oy = bounds.y;

        for (int i = 0; i < total; i++) {
            if (listener != null) {
                listener.onProgress(i + 1, total, "Đang xuất Pose Frame " + i + "/62...");
            }
            BufferedImage frameImg = renderPartFrame(fashion, allParts, i, direction, frameW, frameH, ox, oy, zoom, transparentBg, bgColor, false);
            String poseName = CharInfoData.getPoseName(i)
                    .replaceAll("[^a-zA-Z0-9_\\-\\s]", "")
                    .trim()
                    .replaceAll("\\s+", "_");
            String fileName = String.format("%s_pose_%02d_%s.png", prefix, i, poseName);
            File outFile = new File(outputDir, fileName);
            ImageIO.write(frameImg, "PNG", outFile);
        }
    }

    private static BufferedImage renderPartFrame(short[] fashion, List<mPart> allParts, int frameIndex, int direction,
                                                  int width, int height, int ox, int oy, double zoom,
                                                  boolean transparentBg, Color bgColor) {
        return renderPartFrame(fashion, allParts, frameIndex, direction, width, height, ox, oy, zoom, transparentBg, bgColor, false);
    }

    private static BufferedImage renderPartFrame(short[] fashion, List<mPart> allParts, int frameIndex, int direction,
                                                  int width, int height, int ox, int oy, double zoom,
                                                  boolean transparentBg, Color bgColor, boolean isForGif) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        if (transparentBg) {
            if (isForGif) {
                g2.setColor(KEY_TRANSPARENT);
                g2.fillRect(0, 0, width, height);
            }
            // Với PNG (isForGif == false), BufferedImage TYPE_INT_ARGB mặc định đã là trong suốt 100% (alpha = 0)
        } else {
            g2.setColor(bgColor != null ? bgColor : new Color(18, 18, 24));
            g2.fillRect(0, 0, width, height);
        }

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int fr = frameIndex % CharInfoData.CharInfo.length;
        int[] sort = (direction == 2)
                ? new int[] { 7, 1, 2, 3, 6, 0, 5, 4 }
                : new int[] { 7, 1, 2, 0, 5, 4, 3, 6 };

        for (int slotIndex : sort) {
            mPart part = getWearingPart(fashion, allParts, slotIndex);
            if (part == null) continue;

            int ciSlot = slotIndex;
            if (slotIndex == 6) ciSlot = 3;
            else if (slotIndex == 7) ciSlot = 6;

            if (ciSlot < 0 || ciSlot >= CharInfoData.CharInfo[fr].length) continue;
            int ciIdx = CharInfoData.CharInfo[fr][ciSlot][0];
            if (ciIdx < 0 || ciIdx >= part.pi.length) continue;

            PartImage pi = part.pi[ciIdx];
            if (pi == null || pi.id < 0) continue;

            BufferedImage partImg = PartDataManager.getImage(pi.id);
            if (partImg == null) continue;

            int[] sc = computeScreenTopLeft(ox, oy, fr, slotIndex, pi, fashion, direction, zoom);
            AffineTransform at = new AffineTransform();
            at.translate(sc[0], sc[1]);
            if (direction == 2) {
                at.scale(-zoom, zoom);
            } else {
                at.scale(zoom, zoom);
            }
            g2.drawImage(partImg, at, null);
        }

        g2.dispose();
        return img;
    }

    private static mPart getWearingPart(short[] fashion, List<mPart> allParts, int slotIndex) {
        if (fashion == null || allParts == null) return null;
        short partId = -1;
        switch (slotIndex) {
            case 0: if (fashion.length > 6) partId = fashion[6]; break; // Head
            case 1: if (fashion.length > 5) partId = fashion[5]; break; // Leg
            case 2: if (fashion.length > 3) partId = fashion[3]; break; // Body
            case 3: if (fashion.length > 0) partId = fashion[0]; break; // Weapon
            case 4: if (fashion.length > 1) partId = fashion[1]; break; // Hat
            case 5: if (fashion.length > 7) partId = fashion[7]; break; // Hair
            case 6: if (fashion.length > 2) partId = fashion[2]; break; // WFashion
            case 7: if (fashion.length > 4) partId = fashion[4]; break; // Cloak
        }
        if (partId < 0) return null;
        for (mPart p : allParts) {
            if (p != null && p.id == partId) return p;
        }
        return null;
    }

    private static int[] computeScreenTopLeft(int ox, int oy, int frame, int slotIndex, PartImage pi,
                                              short[] fashion, int direction, double zoom) {
        int ciSlot = slotIndex;
        if (slotIndex == 6) ciSlot = 3;
        else if (slotIndex == 7) ciSlot = 6;

        int ciX = CharInfoData.CharInfo[frame][ciSlot][1];
        int ciY = CharInfoData.CharInfo[frame][ciSlot][2];

        short bodyId = fashion != null && fashion.length > 3 ? fashion[3] : -1;
        short headId = fashion != null && fashion.length > 6 ? fashion[6] : -1;

        int lechYHead = OffsetUtility.getLechYHead(bodyId);
        boolean isHeadRelated = (slotIndex == 0 || slotIndex == 4 || slotIndex == 5);
        if (isHeadRelated) {
            if (slotIndex != 0 || !OffsetUtility.isKoLechHead(headId)) {
                ciY += lechYHead;
            }
        }

        int finalCiX = (int)((ciX + pi.dx) * LOGIC_SCALE * zoom);
        int finalCiY = (int)((ciY + pi.dy) * LOGIC_SCALE * zoom);

        int screenY = oy + finalCiY;
        int screenX = (direction == 2) ? (ox - finalCiX) : (ox + finalCiX);

        return new int[] { screenX, screenY };
    }
}
