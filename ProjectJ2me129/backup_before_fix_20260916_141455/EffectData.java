class SkillEffTemplate {
   public mVector listFrame = new mVector();
   public SmallImage[] smallImage;
   public byte[][] frameChar = new byte[4][];
   public byte[] sequence;
   public int fw;
   public int fh;
   public int min;
}

public final class EffectData {
   public byte[] data;
   public long count = -1L;
   public mImage image;
   public SkillEffTemplate templateNormal;
   public SkillEffTemplate templateFlip;
}

