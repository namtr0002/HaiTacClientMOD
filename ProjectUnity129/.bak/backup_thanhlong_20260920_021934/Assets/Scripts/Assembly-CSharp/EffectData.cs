using System;

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
		updateAssetZoom();
	}

	public void setData(sbyte[] data, sbyte[] dataImg)
	{
		this.data = data;
		img = mImage.createImage(dataImg, 0, dataImg.Length);
		updateAssetZoom();
		templateNormal = null;
		templateFlip = null;
	}

	private void updateAssetZoom()
	{
		if (img != null && img.image != null && img.image.texture != null)
		{
			int calculatedZoom = 0;
			if (data != null && data.Length > 0)
			{
				try
				{
					DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
					int numSmall = dis.readByte();
					int maxCoordW = 0;
					int maxCoordH = 0;
					for (int i = 0; i < numSmall; i++)
					{
						dis.readUnsignedByte();
						int x = dis.readUnsignedByte();
						int y = dis.readUnsignedByte();
						int w = dis.readUnsignedByte();
						int h = dis.readUnsignedByte();
						if (x + w > maxCoordW) maxCoordW = x + w;
						if (y + h > maxCoordH) maxCoordH = y + h;
					}
					dis.close();
					if (maxCoordW > 0)
					{
						calculatedZoom = (int)System.Math.Round((float)img.image.texture.width / (float)maxCoordW);
					}
				}
				catch (Exception)
				{
				}
			}
			if (calculatedZoom <= 0)
			{
				calculatedZoom = (mGraphics.zoomLevel > 0) ? mGraphics.zoomLevel : 1;
			}
			img.image.assetZoom = calculatedZoom;
		}
	}

	public EffectData()
	{
	}
}

