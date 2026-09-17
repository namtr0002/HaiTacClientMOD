using System;

public class mImage
{
	public Image image;

	public int w;

	public int h;

	public static string getLink(string str)
	{
		return str;
	}

	public static string getUrlImage(string str)
	{
		if (!string.IsNullOrEmpty(str) && !str.StartsWith("/"))
		{
			str = "/" + str;
		}
		return str.Replace("/", "_");
	}

	public void setWH()
	{
		if (image != null)
		{
			w = getImageWidth(image);
			h = getImageHeight(image);
		}
	}

	public static mImage createImage(string url)
	{
		mImage mImage2 = new mImage();
		try
		{
			if (GameMidlet.DEVICE != 0)
			{
				sbyte[] array = CRes.loadRMS("Main_Image" + getUrlImage(url));
				if (array == null && url.StartsWith("/w_interface/"))
				{
					array = CRes.loadRMS("Main_Image" + getUrlImage(url.Replace("/w_interface/", "/interface/")));
				}
				if (array != null)
				{
					mImage2 = createImage(array, 0, array.Length);
					if (mImage2 != null && mImage2.image != null)
					{
						mImage2.image.assetZoom = (mGraphics.zoomLevel > 0) ? mGraphics.zoomLevel : 1;
					}
					return mImage2;
				}
			}
			try
			{
				int resZoom = (mGraphics.zoomResource > 0) ? mGraphics.zoomResource : mGraphics.zoomLevel;
				mImage2.image = Image.createImage("/x" + resZoom + url);
				if (mImage2.image == null && resZoom != mGraphics.zoomLevel)
				{
					mImage2.image = Image.createImage("/x" + mGraphics.zoomLevel + url);
				}
				if (mImage2.image == null && url.StartsWith("/w_interface/"))
				{
					mImage2.image = Image.createImage("/x" + resZoom + url.Replace("/w_interface/", "/interface/"));
					if (mImage2.image == null && resZoom != mGraphics.zoomLevel)
					{
						mImage2.image = Image.createImage("/x" + mGraphics.zoomLevel + url.Replace("/w_interface/", "/interface/"));
					}
				}
				if (mImage2.image == null && resZoom != 1)
				{
					mImage2.image = Image.createImage("/x1" + url);
				}
				if (url.Contains("noeff_mui_ten"))
				{
					mImage2.image = Image.createImage("/x" + 1 + url);
				}
			}
			catch (Exception)
			{
			}
			if (mImage2.image == null)
			{
				return null;
			}
		}
		catch (Exception)
		{
		}
		return mImage2;
	}

	public static mImage createImageAll(string url)
	{
		mImage mImage2 = new mImage();
		try
		{
			mImage2.image = Image.createImage(url);
		}
		catch (Exception)
		{
		}
		if (mImage2.image == null)
		{
			return null;
		}
		return mImage2;
	}

	public static mImage createImage(int w, int h)
	{
		return new mImage
		{
			image = Image.createImage(w * mGraphics.zoomLevel, h * mGraphics.zoomLevel)
		};
	}

	public static mImage createImage(sbyte[] data, int w, int h)
	{
		return new mImage
		{
			image = Image.createImage(data, 0, data.Length)
		};
	}

	public TemGraphics getGraphics()
	{
		return new TemGraphics();
	}

	public static int getImageWidth(Image image)
	{
		if (image == null) return 0;
		int az = (image.assetZoom > 0) ? image.assetZoom : 1;
		int w = image.getWidth() / az;
		return (w > 0) ? w : image.getWidth();
	}

	public static int getImageHeight(Image image)
	{
		if (image == null) return 0;
		int az = (image.assetZoom > 0) ? image.assetZoom : 1;
		int h = image.getHeight() / az;
		return (h > 0) ? h : image.getHeight();
	}

	public static int getImageWidth(mImage image)
	{
		if (image == null || image.image == null) return 0;
		return getImageWidth(image.image);
	}

	public static int getImageHeight(mImage image)
	{
		if (image == null || image.image == null) return 0;
		return getImageHeight(image.image);
	}

	public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height)
	{
	}

	public static mImage createRGBImage(int[] rgb, int width, int height, bool processAlpha)
	{
		return new mImage
		{
			image = Image.createRGBImage(rgb, width, height, processAlpha)
		};
	}

	public static mImage scaleImage(mImage img, int w, int h)
	{
		return img;
	}
}
