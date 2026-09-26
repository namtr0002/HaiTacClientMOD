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

	public EffectData(sbyte[] data, sbyte[] dataImg, int hintZoom = 0)
	{
		this.data = data;
		img = mImage.createImage(dataImg, 0, dataImg.Length);
		updateAssetZoom(hintZoom);
	}

	public void setData(sbyte[] data, sbyte[] dataImg, int hintZoom = 0)
	{
		this.data = data;
		img = mImage.createImage(dataImg, 0, dataImg.Length);
		updateAssetZoom(hintZoom);
		templateNormal = null;
		templateFlip = null;
	}

	public void updateAssetZoom(int hintZoom = 0)
	{
		if (img == null || img.image == null || img.image.texture == null)
		{
			return;
		}

		int clientZoom = (mGraphics.zoomLevel > 0) ? mGraphics.zoomLevel : 1;
		int finalZoom = 1;

		if (hintZoom >= 1 && hintZoom <= 4)
		{
			finalZoom = hintZoom;
		}
		else
		{
			int texW = img.image.texture.width;
			if (texW % 256 == 0 && texW <= 1024)
			{
				int autoZ = texW / 256;
				if (autoZ >= 1 && autoZ <= 4)
				{
					finalZoom = autoZ;
				}
			}
			else if (data != null && data.Length > 0)
			{
				try
				{
					DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
					int numSmall = dis.readByte();
					int maxCoordW = 0;
					for (int i = 0; i < numSmall; i++)
					{
						int imgId = dis.readUnsignedByte();
						int x, y, w, h;
						if (imgId == 255)
						{
							dis.readUnsignedByte();
							x = dis.readShort();
							y = dis.readShort();
							w = dis.readShort();
							h = dis.readShort();
						}
						else
						{
							x = dis.readUnsignedByte();
							y = dis.readUnsignedByte();
							w = dis.readUnsignedByte();
							h = dis.readUnsignedByte();
						}
						if (x + w > maxCoordW) maxCoordW = x + w;
					}
					dis.close();
					if (maxCoordW > 0)
					{
						if (texW >= (int)((float)maxCoordW * 3.5f)) finalZoom = 4;
						else if (texW >= (int)((float)maxCoordW * 2.5f)) finalZoom = 3;
						else if (texW >= (int)((float)maxCoordW * 1.5f)) finalZoom = 2;
						else finalZoom = 1;
					}
				}
				catch (Exception)
				{
				}
			}
		}

		img.image.assetZoom = (finalZoom > 0) ? finalZoom : 1;
	}

	public EffectData()
	{
	}
}

