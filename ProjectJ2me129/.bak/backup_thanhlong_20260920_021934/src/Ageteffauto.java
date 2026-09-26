import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.microedition.lcdui.Image;

public class Ageteffauto {

    private static final int START_ID = 0;
    private static final int END_ID = 255;
    private static final String base = "C:\\ThMadara\\HTTH\\res\\effect_auto\\x" + Ageticon.fakezoomlv + "\\";
    private static final String dataPath = base + "data\\";
    private static final String imgPath = base + "img\\";
    private static final String sqlPath = base + "sql\\";

    private static final long TIMEOUT_MS = 1000;
    private static final long POLL_MS = 0;

    private static final Hashtable received = new Hashtable(); // id -> byte[]

    public static void run() {
        try { new File(dataPath).mkdirs(); new File(imgPath).mkdirs(); new File(sqlPath).mkdirs(); } catch (Exception ignored) {}

        final IMessageHandler prev = Session_ME.AB;
        Session_ME.setHandler(new IMessageHandler() {
            public void onMessage(Message m) {
                try {
                    if (m != null && m.command == (byte)-44) {
                        DataInputStream rd = m.reader();
                        if (rd != null) {
                            int len = rd.available();
                            if (len > 0) {
                                byte[] buf = new byte[len];
                                rd.readFully(buf);
                                DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buf));
                                int id = dis.readShort();
                                received.put(new Integer(id), buf);
                            }
                        }
                        if (prev != null) prev.onMessage(m);
                        return;
                    }
                } catch (Throwable t) { t.printStackTrace(); }
                if (prev != null) prev.onMessage(m);
            }
            public void AA() { if (prev != null) { try { prev.AA(); } catch (Throwable ignored) {} } }
            public void AB() { if (prev != null) { try { prev.AB(); } catch (Throwable ignored) {} } }
            public void GlobalLogicHandlerV() { if (prev != null) { try { prev.GlobalLogicHandlerV(); } catch (Throwable ignored) {} } }
        });

        for (int id = START_ID; id <= END_ID; id++) {
            try {
                Message m = new Message((byte)-44);
                m.writer().writeShort(id);
                m.writer().flush();
                Session_ME.getInstance().sendMessage(m);
            } catch (Throwable t) {
                System.err.println("send fail id " + id);
                continue;
            }

            // wait response
            long t0 = System.currentTimeMillis();
            byte[] dataeff = null;
            while (System.currentTimeMillis() - t0 < TIMEOUT_MS) {
                dataeff = (byte[])received.remove(new Integer(id));
                if (dataeff != null) break;
                try { Thread.sleep(POLL_MS); } catch (InterruptedException ie) { break; }
            }
            if (dataeff == null) { System.out.println("timeout id " + id); continue; }

            // save .bin
            try { FileOutputStream fos = new FileOutputStream(dataPath + id + ".bin"); fos.write(dataeff); fos.close(); } catch (Exception e) { e.printStackTrace(); }

            // parse
            EffectAuto.readData(dataeff, true);
            Object o = MainEffectAuto.hashTemEffAuto.get(String.valueOf(id));
            if (o instanceof MainEffectAuto) {
                MainEffectAuto eff = (MainEffectAuto)o;

                // save image
                if (eff.img != null && eff.img.getImageObject() != null) {
                    BufferedImage bi = toBufferedImage(eff.img.getImageObject());
                    if (bi != null) {
                        try { ImageIO.write(bi, "png", new File(imgPath + id + ".png")); } catch (Exception e) { e.printStackTrace(); }
                    }
                }
                String jsonHashImage = buildJsonHashImage(eff.hashImage);
                String jsonMFrame = buildJsonMFrame(eff.mFrame);
                String jsonMRunFrame = buildJsonArrayShort(eff.mRunFrame);
                String sql = "INSERT INTO effect_auto_data(eff_id, hashImage, mFrame, mRunFrame, typeEffect, valueEffect) VALUES("
                        + id + ", '"
                        + jsonHashImage + "', '"
                        + jsonMFrame + "', '"
                        + jsonMRunFrame + "', "
                        + 0 + ", " + 1 + ");";
                try { FileOutputStream fos = new FileOutputStream(sqlPath + id + ".sql"); fos.write(sql.getBytes()); fos.close(); } catch (Exception e) { e.printStackTrace(); }
                System.out.println("saved SQL for eff_id " + id);
            }
        }
        Session_ME.setHandler(prev);
        System.out.println("done");
    }

    private static String buildJsonHashImage(Hashtable hashImage) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        Enumeration keys = hashImage.keys();
        boolean first = true;
        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            MainPartImage part = (MainPartImage)hashImage.get(key);
            if(!first) sb.append(",");
            sb.append("\"").append(key).append("\":{")
              .append("\"x\":").append(part.x)
              .append(",\"y\":").append(part.y)
              .append(",\"w\":").append(part.w)
              .append(",\"h\":").append(part.h)
              .append(",\"ID\":").append(part.ID)
              .append("}");
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }

    private static String buildJsonMFrame(MainFrameEff[] mFrame) {
        if(mFrame == null) return "[]";
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(int i = 0; i < mFrame.length; i++) {
            if(i > 0) sb.append(",");
            sb.append("{\"mpart\":[");
            if(mFrame[i].mpart != null) {
                for(int j = 0; j < mFrame[i].mpart.length; j++) {
                    if(j > 0) sb.append(",");
                    Part p = mFrame[i].mpart[j];
                    sb.append("{\"x\":").append(p.x)
                      .append(",\"y\":").append(p.y)
                      .append(",\"idPartImage\":").append(p.idPartImage)
                      .append("}");
                }
            }
            sb.append("]}");
        }
        sb.append("]");
        return sb.toString();
    }

    private static String buildJsonArrayShort(short[] arr) {
        if(arr == null) return "[]";
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(int i = 0; i < arr.length; i++) {
            if(i > 0) sb.append(",");
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    private static BufferedImage toBufferedImage(Image img) {
        try {
            if(img == null) return null;
            int w = img.getWidth();
            int h = img.getHeight();
            BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            int[] rgb = new int[w * h];
            img.getRGB(rgb, 0, w, 0, 0, w, h);
            b.setRGB(0, 0, w, h, rgb, 0, w);
            return b;
        } catch (Throwable t) { return null; }
    }
}
