using System;
using System.Threading;
using UnityEngine;
using hqx;

public class Image
{
	private const int INTERVAL = 5;

	private const int MAXTIME = 500;

	public Texture2D texture = new Texture2D(1, 1);

	public static Image imgTemp;

	public static string filenametemp;

	public static byte[] datatemp;

	public static Image imgSrcTemp;

	public static int xtemp;

	public static int ytemp;

	public static int wtemp;

	public static int htemp;

	public static int transformtemp;

	public int w;

	public int h;

	public int assetZoom = 1;

	public static int status;

	public Color colorBlend = Color.black;

	public static int iA;

	public static Image createEmptyImage()
	{
		return __createEmptyImage();
	}

	public static Image createImage(string path)
	{
		path = Main.res + path;
		path = cutPng(path);
		Image result = null;
		try
		{
			result = createImageUni(path);
		}
		catch (Exception)
		{
		}
		return result;
	}

	public static string cutPng(string str)
	{
		string result = str;
		if (str.Contains(".png"))
		{
			result = str.Replace(".png", "");
		}
		return result;
	}

	public static Image createImageUni(string filename)
	{
		return __createImage(filename);
	}

	public static Image createImageX(string filename)
	{
		return __createImageX(filename);
	}

	public static Image createImage(byte[] imageData)
	{
		return __createImage(imageData);
	}

	public static Image createImage(Image src, int x, int y, int w, int h, int transform)
	{
		return __createImage(src, x, y, w, h, transform);
	}

	public static Image createImage(int w, int h)
	{
		return __createImage(w, h);
	}

	public static Image createImage(Image img)
	{
		Image image = createImage(img.w, img.h);
		image.texture = img.texture;
		image.texture.Apply();
		return image;
	}

	public static Image createImage(sbyte[] imageData, int offset, int lenght)
	{
		if (imageData == null || offset + lenght > imageData.Length)
		{
			return null;
		}
		byte[] array = new byte[lenght];
		Buffer.BlockCopy(imageData, offset, array, 0, lenght);
		return createImage(array);
	}

	public static byte convertSbyteToByte(sbyte var)
	{
		return (byte)var;
	}

	public static byte[] convertArrSbyteToArrByte(sbyte[] var)
	{
		if (var == null) return null;
		byte[] array = new byte[var.Length];
		Buffer.BlockCopy(var, 0, array, 0, var.Length);
		return array;
	}

	public static Image createRGBImage(int[] rbg, int w, int h, bool bl)
	{
		Image image = createImage(w, h);
		Color[] array = new Color[rbg.Length];
		for (int i = 0; i < array.Length; i++)
		{
			array[i] = setColorFromRBG(rbg[i]);
		}
		image.texture.SetPixels(0, 0, w, h, array);
		image.texture.Apply();
		return image;
	}

	public static Color setColorFromRBG(int rgb)
	{
		int num = rgb & 0xFF;
		int num2 = (rgb >> 8) & 0xFF;
		int num3 = (rgb >> 16) & 0xFF;
		float b = (float)num / 256f;
		float g = (float)num2 / 256f;
		float r = (float)num3 / 256f;
		return new Color(r, g, b);
	}

	public static void update()
	{
		if (status == 2)
		{
			status = 1;
			imgTemp = __createEmptyImage();
			status = 0;
		}
		else if (status == 3)
		{
			status = 1;
			imgTemp = __createImage(filenametemp);
			status = 0;
		}
		else if (status == 4)
		{
			status = 1;
			imgTemp = __createImage(datatemp);
			status = 0;
		}
		else if (status == 5)
		{
			status = 1;
			imgTemp = __createImage(imgSrcTemp, xtemp, ytemp, wtemp, htemp, transformtemp);
			status = 0;
		}
		else if (status == 6)
		{
			status = 1;
			imgTemp = __createImage(wtemp, htemp);
			status = 0;
		}
		else if (status == 7)
		{
			status = 1;
			imgTemp = __createImage(filenametemp);
			status = 0;
		}
	}

	private static Image _createEmptyImage()
	{
		if (status != 0)
		{
			Cout.LogError("CANNOT CREATE EMPTY IMAGE WHEN CREATING OTHER IMAGE");
			return null;
		}
		imgTemp = null;
		status = 2;
		int i;
		for (i = 0; i < 500; i++)
		{
			Thread.Sleep(5);
			if (status == 0)
			{
				break;
			}
		}
		if (i == 500)
		{
			Cout.LogError("TOO LONG FOR CREATE EMPTY IMAGE");
			status = 0;
		}
		return imgTemp;
	}

	private static Image _createImage(string filename)
	{
		if (status != 0)
		{
			Cout.LogError("CANNOT CREATE IMAGE " + filename + " WHEN CREATING OTHER IMAGE");
			return null;
		}
		imgTemp = null;
		filenametemp = filename;
		status = 3;
		int i;
		for (i = 0; i < 500; i++)
		{
			Thread.Sleep(5);
			if (status == 0)
			{
				break;
			}
		}
		if (i == 500)
		{
			Cout.LogError("TOO LONG FOR CREATE IMAGE " + filename);
			status = 0;
		}
		return imgTemp;
	}

	private static Image _createImageX(string filename)
	{
		if (status != 0)
		{
			Cout.LogError("CANNOT CREATE IMAGE " + filename + " WHEN CREATING OTHER IMAGE");
			return null;
		}
		imgTemp = null;
		filenametemp = filename;
		status = 7;
		int i;
		for (i = 0; i < 500; i++)
		{
			Thread.Sleep(5);
			if (status == 0)
			{
				break;
			}
		}
		if (i == 500)
		{
			Cout.LogError("TOO LONG FOR CREATE IMAGE " + filename);
			status = 0;
		}
		return imgTemp;
	}

	private static Image _createImage(byte[] imageData)
	{
		if (status != 0)
		{
			Cout.LogError("CANNOT CREATE IMAGE(FromArray) WHEN CREATING OTHER IMAGE");
			return null;
		}
		imgTemp = null;
		datatemp = imageData;
		status = 4;
		int i;
		for (i = 0; i < 500; i++)
		{
			Thread.Sleep(5);
			if (status == 0)
			{
				break;
			}
		}
		if (i == 500)
		{
			Cout.LogError("TOO LONG FOR CREATE IMAGE(FromArray)");
			status = 0;
		}
		return imgTemp;
	}

	private static Image _createImage(Image src, int x, int y, int w, int h, int transform)
	{
		if (status != 0)
		{
			Cout.LogError("CANNOT CREATE IMAGE(FromSrcPart) WHEN CREATING OTHER IMAGE");
			return null;
		}
		imgTemp = null;
		imgSrcTemp = src;
		xtemp = x;
		ytemp = y;
		wtemp = w;
		htemp = h;
		transformtemp = transform;
		status = 5;
		int i;
		for (i = 0; i < 500; i++)
		{
			Thread.Sleep(5);
			if (status == 0)
			{
				break;
			}
		}
		if (i == 500)
		{
			Cout.LogError("TOO LONG FOR CREATE IMAGE(FromSrcPart)");
			status = 0;
		}
		return imgTemp;
	}

	private static Image _createImage(int w, int h)
	{
		if (status != 0)
		{
			Cout.LogError("CANNOT CREATE IMAGE(w,h) WHEN CREATING OTHER IMAGE");
			return null;
		}
		imgTemp = null;
		wtemp = w;
		htemp = h;
		status = 6;
		int i;
		for (i = 0; i < 500; i++)
		{
			Thread.Sleep(5);
			if (status == 0)
			{
				break;
			}
		}
		if (i == 500)
		{
			Cout.LogError("TOO LONG FOR CREATE IMAGE(w,h)");
			status = 0;
		}
		return imgTemp;
	}

	public static byte[] loadData(string filename)
	{
		new Image();
		TextAsset textAsset = (TextAsset)Resources.Load(filename, typeof(TextAsset));
		if (textAsset == null || textAsset.bytes == null || textAsset.bytes.Length == 0)
		{
			throw new Exception("NULL POINTER EXCEPTION AT Image __createImage " + filename);
		}
		sbyte[] array = ArrayCast.cast(textAsset.bytes);
		Debug.LogError("CHIEU DAI MANG BYTE IMAGE CREAT = " + array.Length);
		return textAsset.bytes;
	}

	private static Texture2D loadTextureFromFile(string filename)
	{
		try
		{
			string fn = filename;
			if (fn.StartsWith("/"))
			{
				fn = fn.Substring(1);
			}
			string[] array = new string[4]
			{
				Application.dataPath + "/Resources/" + fn + ".png",
				Application.dataPath + "/Resources/res/" + fn + ".png",
				Application.dataPath + "/Resources/" + fn,
				Application.dataPath + "/Resources/res/" + fn
			};
			for (int i = 0; i < array.Length; i++)
			{
				if (System.IO.File.Exists(array[i]))
				{
					byte[] data = System.IO.File.ReadAllBytes(array[i]);
					Texture2D texture2D = new Texture2D(2, 2, TextureFormat.RGBA32, mipChain: false);
					texture2D.LoadImage(data);
					return texture2D;
				}
			}
		}
		catch (Exception)
		{
		}
		return null;
	}

	private static Texture2D loadTexture(string filename)
	{
		if (string.IsNullOrEmpty(filename)) return null;
		string fn = filename.Trim();
		while (fn.StartsWith("/"))
		{
			fn = fn.Substring(1);
		}
		// 1. Try file from disk first (Editor / loose files)
		// Preserves exact raw pixel dimensions without POT resampling or mipmap blur
		Texture2D tex = loadTextureFromFile(fn);
		if (tex != null)
		{
			return tex;
		}

		// 2. Fallback to Resources.Load for packaged builds
		string fnNoExt = fn;
		if (fnNoExt.EndsWith(".png", StringComparison.OrdinalIgnoreCase) || fnNoExt.EndsWith(".jpg", StringComparison.OrdinalIgnoreCase))
		{
			fnNoExt = fnNoExt.Substring(0, fnNoExt.Length - 4);
		}
		tex = Resources.Load(fnNoExt) as Texture2D;
		if (tex == null && !fnNoExt.StartsWith("res/"))
		{
			tex = Resources.Load("res/" + fnNoExt) as Texture2D;
		}
		return tex;
	}

	private static Image __createImage(string filename)
	{
		Image image = new Image();
		Texture2D texture2D = loadTexture(filename);
		if (texture2D == null)
		{
			throw new Exception("NULL POINTER EXCEPTION AT Image __createImage " + filename);
		}
		image.texture = texture2D;
		image.w = image.texture.width;
		image.h = image.texture.height;
		if (filename.Contains("/x4/") || filename.StartsWith("x4/") || filename.Contains("_x4_")) image.assetZoom = 4;
		else if (filename.Contains("/x3/") || filename.StartsWith("x3/") || filename.Contains("_x3_")) image.assetZoom = 3;
		else if (filename.Contains("/x2/") || filename.StartsWith("x2/") || filename.Contains("_x2_")) image.assetZoom = 2;
		else if (filename.Contains("/x1/") || filename.StartsWith("x1/") || filename.Contains("_x1_")) image.assetZoom = 1;
		else image.assetZoom = (mGraphics.zoomLevel > 0) ? mGraphics.zoomLevel : 1;
		setTextureQuality(image);
		return image;
	}

	private static Image __createImageX(string filename)
	{
		Image image = new Image();
		Texture2D texture2D = loadTexture(filename);
		if (texture2D == null)
		{
			throw new Exception("NULL POINTER EXCEPTION AT Image __createImage " + filename);
		}
		try
		{
			image.texture = texture2D;
			image.w = image.texture.width;
			image.h = image.texture.height;
			int[] array = new int[image.w * image.h];
			Color[] pixels = image.texture.GetPixels(0, 0, image.w, image.h);
			for (int i = 0; i < array.Length; i++)
			{
				array[i] = getIntByColor(pixels[i]);
			}
			int[] array2 = Hqx.HqxZoom(mGraphics.zoomLevel, array, image.w, image.h);
			int num = image.w * mGraphics.zoomLevel;
			int num2 = image.h * mGraphics.zoomLevel;
			image.texture = new Texture2D(num, num2);
			Color[] array3 = new Color[num * num2];
			for (int j = 0; j < array3.Length; j++)
			{
				array3[j] = getColor(array2[j]);
			}
			image.texture.SetPixels(array3);
			image.texture.Apply(updateMipmaps: false, makeNoLongerReadable: false);
			image.w = num;
			image.h = num2;
			image.assetZoom = (mGraphics.zoomLevel > 0) ? mGraphics.zoomLevel : 1;
			setTextureQuality(image);
		}
		catch (Exception e)
		{
			Out.printError(e);
		}
		return image;
	}

	private static Image __createImage(byte[] imageData)
	{
		if (imageData == null || imageData.Length == 0)
		{
			Cout.LogError("Create Image from byte array fail");
			return null;
		}
		Image image = new Image();
		try
		{
			image.texture.LoadImage(imageData);
			image.w = image.texture.width;
			image.h = image.texture.height;
			image.assetZoom = (Rms.lastLoadedZoom > 0) ? Rms.lastLoadedZoom : ((mGraphics.zoomLevel > 0) ? mGraphics.zoomLevel : 1);
			setTextureQuality(image);
		}
		catch (Exception)
		{
			Cout.LogError("CREAT IMAGE FROM ARRAY FAIL \n" + Environment.StackTrace);
		}
		return image;
	}

	public static Color getColor(int rgb)
	{
		int num = rgb & 0xFF;
		int num2 = (rgb >> 8) & 0xFF;
		int num3 = (rgb >> 16) & 0xFF;
		int num4 = (rgb >> 24) & 0xFF;
		float b = (float)num / 256f;
		float g = (float)num2 / 256f;
		float r = (float)num3 / 256f;
		float a = (float)num4 / 256f;
		return new Color(r, g, b, a);
	}

	public static int getIntByColor(Color cl)
	{
		float num = cl.a * 255f;
		float num2 = cl.r * 255f;
		float num3 = cl.b * 255f;
		float num4 = cl.g * 255f;
		return (((int)num & 0xFF) << 24) | (((int)num2 & 0xFF) << 16) | (((int)num4 & 0xFF) << 8) | ((int)num3 & 0xFF);
	}

	private static Image __createImage(Image src, int x, int y, int w, int h, int transform)
	{
		Image image = new Image();
		image.texture = new Texture2D(w, h);
		y = src.texture.height - y - h;
		for (int i = 0; i < w; i++)
		{
			for (int j = 0; j < h; j++)
			{
				int num = i;
				if (transform == 2)
				{
					num = w - i;
				}
				int num2 = j;
				image.texture.SetPixel(i, j, src.texture.GetPixel(x + num, y + num2));
			}
		}
		image.texture.Apply();
		image.w = image.texture.width;
		image.h = image.texture.height;
		setTextureQuality(image);
		return image;
	}

	private static Image __createEmptyImage()
	{
		return new Image();
	}

	public static Image __createImage(int w, int h)
	{
		Image obj = new Image
		{
			texture = new Texture2D(w, h, TextureFormat.RGBA32, mipChain: false)
		};
		setTextureQuality(obj);
		obj.w = w;
		obj.h = h;
		obj.assetZoom = (mGraphics.zoomLevel > 0) ? mGraphics.zoomLevel : 1;
		obj.texture.Apply();
		return obj;
	}

	public static int getImageWidth(Image image)
	{
		return image.getWidth();
	}

	public static int getImageHeight(Image image)
	{
		return image.getHeight();
	}

	public int getWidth()
	{
		return w;
	}

	public int getHeight()
	{
		return h;
	}

	private static void setTextureQuality(Image img)
	{
		setTextureQuality(img.texture);
	}

	public static void setTextureQuality(Texture2D texture)
	{
		texture.anisoLevel = 0;
		texture.filterMode = FilterMode.Point;
		texture.mipMapBias = 0f;
		texture.wrapMode = TextureWrapMode.Clamp;
	}

	public Color[] getColor()
	{
		return texture.GetPixels();
	}

	public int getRealImageWidth()
	{
		return w;
	}

	public int getRealImageHeight()
	{
		return h;
	}

	public void getRGB(ref int[] data, int x1, int x2, int x, int y, int w, int h)
	{
		Color[] pixels = texture.GetPixels(x, this.h - 1 - y, w, h);
		for (int i = 0; i < pixels.Length; i++)
		{
			data[i] = mGraphics.getIntByColor(pixels[i]);
		}
	}
}
