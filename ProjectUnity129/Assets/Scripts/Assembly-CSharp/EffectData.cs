public class SkillEffTemplate
{
	public mVector listFrame = new mVector();
	public SmallImage[] smallImage;
	public sbyte[][] frameChar = new sbyte[4][];
	public sbyte[] sequence;
	public int fw;
	public int fh;
	public int min;
}

public class EffectData
{
	public sbyte[] data;

	public long count = -1L;

	public mImage img;

	public SkillEffTemplate templateNormal;

	public SkillEffTemplate templateFlip;

	public EffectData(sbyte[] data, sbyte[] dataImg)
	{
		this.data = data;
		img = mImage.createImage(dataImg, 0, dataImg.Length);
		if (img != null && img.image != null)
		{
			img.image.assetZoom = 1;
		}
	}

	public void setData(sbyte[] data, sbyte[] dataImg)
	{
		this.data = data;
		img = mImage.createImage(dataImg, 0, dataImg.Length);
		if (img != null && img.image != null)
		{
			img.image.assetZoom = 1;
		}
		templateNormal = null;
		templateFlip = null;
	}

	public EffectData()
	{
	}
}

