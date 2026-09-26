using System;

public class FrameImage
{
	public static FrameImage getFrameImage(mImage img, int width, int height)
	{
		return new FrameImage(img, width, height);
	}

	public static FrameImage getFrameImage(int ID, int width, int height)
	{
		return new FrameImage(ID, width, height);
	}

	public static FrameImage getFrameImage(int ID, int numframe)
	{
		return new FrameImage(ID, numframe);
	}

	public static FrameImage getFrameImage(int ID, int width, int height, int width2, int height2)
	{
		return new FrameImage(ID, width, height, width2, height2);
	}

	public static FrameImage getFrameImage(int ID, int width, int height, int maxNumFrame)
	{
		return new FrameImage(ID, width, height, maxNumFrame);
	}

	public static FrameImage getFrameImage(int ID, int width, int height, int width2, int height2, int maxNumFrame)
	{
		return new FrameImage(ID, width, height, width2, height2, maxNumFrame);
	}

	public int frameWidth;

	public int frameHeight;

	public int nFrame = 1;

	public int maxNumFrame = 1;

	public int indexSuper;

	public mImage imgFrame;

	private int Id = -1;

	private bool lowG;

	private bool isFormFrame;

	private bool isLoaded;

	private int lastLoadAttemptFrame = -1;

	private int normalizeId(int id)
	{
		if (id >= 25000)
		{
			lowG = true;
			return id - 25000;
		}
		if (id >= 24000)
		{
			return id - 24000;
		}
		return id;
	}

	public FrameImage(mImage img, int width, int height)
	{
		imgFrame = img;
		frameWidth = width;
		frameHeight = height;
		if (img != null && img.image != null && height > 0)
		{
			nFrame = mImage.getImageHeight(img.image) / height;
			if (nFrame < 1) nFrame = 1;
			isLoaded = (mImage.getImageHeight(img.image) > 0);
		}
		else
		{
			nFrame = 1;
			isLoaded = false;
		}
		maxNumFrame = nFrame;
	}

	public FrameImage(mImage img, int width, int height, int maxNumFrame)
	{
		imgFrame = img;
		frameWidth = width;
		frameHeight = height;
		this.maxNumFrame = (maxNumFrame > 0) ? maxNumFrame : 1;
		if (img != null && img.image != null && width > 0)
		{
			nFrame = mImage.getImageWidth(img.image) / width * this.maxNumFrame;
			if (nFrame < 1) nFrame = 1;
			isLoaded = (mImage.getImageWidth(img.image) > 0);
		}
		else
		{
			nFrame = 1;
			isLoaded = false;
		}
	}

	public FrameImage(mImage img, int width, int height, int maxNumFrame, sbyte frameSuper)
	{
		indexSuper = frameSuper;
		imgFrame = img;
		frameWidth = width;
		frameHeight = height;
		this.maxNumFrame = (maxNumFrame > 0) ? maxNumFrame : 1;
		if (img != null && img.image != null && width > 0)
		{
			nFrame = mImage.getImageWidth(img.image) / width * this.maxNumFrame;
			if (nFrame < 1) nFrame = 1;
			isLoaded = (mImage.getImageWidth(img.image) > 0);
		}
		else
		{
			nFrame = 1;
			isLoaded = false;
		}
	}

	public FrameImage(int ID, int width, int height)
	{
		Id = normalizeId(ID);
		frameWidth = width;
		frameHeight = height;
		imgFrame = getImage();
		if (imgFrame != null && imgFrame.image != null && height > 0)
		{
			nFrame = mImage.getImageHeight(imgFrame.image) / height;
			if (nFrame < 1) nFrame = 1;
			isLoaded = (mImage.getImageHeight(imgFrame.image) > 0);
		}
		else
		{
			nFrame = 1;
			isLoaded = false;
		}
		maxNumFrame = nFrame;
	}

	public FrameImage(mImage img, int numframe)
	{
		imgFrame = img;
		nFrame = (numframe > 0) ? numframe : 1;
		maxNumFrame = nFrame;
		isFormFrame = true;
		if (imgFrame != null && imgFrame.image != null && numframe > 0)
		{
			frameWidth = mImage.getImageWidth(imgFrame.image);
			frameHeight = mImage.getImageHeight(imgFrame.image) / numframe;
			isLoaded = (frameWidth > 0 && frameHeight > 0);
		}
		else
		{
			isLoaded = false;
		}
	}

	public FrameImage(int ID, int numframe)
	{
		Id = normalizeId(ID);
		nFrame = (numframe > 0) ? numframe : 1;
		maxNumFrame = nFrame;
		imgFrame = getImage();
		isFormFrame = true;
		if (imgFrame != null && imgFrame.image != null && numframe > 0)
		{
			frameWidth = mImage.getImageWidth(imgFrame.image);
			frameHeight = mImage.getImageHeight(imgFrame.image) / numframe;
			isLoaded = (frameWidth > 0 && frameHeight > 0);
		}
		else
		{
			isLoaded = false;
		}
	}

	public FrameImage(MainImage ImagePotion, int ID, int numframe)
	{
		try
		{
			Id = normalizeId(ID);
			nFrame = (numframe > 0) ? numframe : 1;
			maxNumFrame = nFrame;
			imgFrame = (ImagePotion != null) ? ImagePotion.img : null;
			isFormFrame = true;
			if (imgFrame != null && imgFrame.image != null && numframe > 0)
			{
				frameWidth = mImage.getImageWidth(imgFrame.image);
				frameHeight = mImage.getImageHeight(imgFrame.image) / numframe;
				isLoaded = (frameWidth > 0 && frameHeight > 0);
			}
			else
			{
				isLoaded = false;
			}
		}
		catch (Exception)
		{
			isLoaded = false;
		}
	}

	public FrameImage(int ID, int width, int height, int width2, int height2)
	{
		Id = normalizeId(ID);
		if (GameCanvas.lowGraphic)
		{
			frameWidth = width2;
			frameHeight = height2;
			lowG = true;
		}
		else
		{
			frameWidth = width;
			frameHeight = height;
		}
		imgFrame = getImage();
		if (imgFrame != null && imgFrame.image != null && frameHeight > 0)
		{
			nFrame = mImage.getImageHeight(imgFrame.image) / frameHeight;
			if (nFrame < 1) nFrame = 1;
			isLoaded = (mImage.getImageHeight(imgFrame.image) > 0);
		}
		else
		{
			nFrame = 1;
			isLoaded = false;
		}
		maxNumFrame = nFrame;
	}

	public FrameImage(int ID, int width, int height, int maxNumFrame)
	{
		createFrameImgNew(ID, width, height, maxNumFrame);
	}

	public FrameImage(mImage img, int ID, int width, int height, int maxNumFrame)
	{
		Id = normalizeId(ID);
		frameWidth = width;
		frameHeight = height;
		this.maxNumFrame = (maxNumFrame > 0) ? maxNumFrame : 1;
		imgFrame = img;
		if (imgFrame != null && imgFrame.image != null && width > 0)
		{
			nFrame = mImage.getImageWidth(imgFrame.image) / width * this.maxNumFrame;
			if (nFrame < 1) nFrame = 1;
			isLoaded = (mImage.getImageWidth(imgFrame.image) > 0);
		}
		else
		{
			nFrame = 1;
			isLoaded = false;
		}
	}

	public FrameImage(int ID, int width, int height, sbyte maxNumFrame, sbyte frameSuper)
	{
		indexSuper = frameSuper;
		createFrameImgNew(ID, width, height, maxNumFrame);
	}

	public void createFrameImgNew(int ID, int width, int height, int maxNumFrame)
	{
		Id = normalizeId(ID);
		frameWidth = width;
		frameHeight = height;
		this.maxNumFrame = (maxNumFrame > 0) ? maxNumFrame : 1;
		imgFrame = getImage();
		if (imgFrame != null && imgFrame.image != null && width > 0)
		{
			nFrame = mImage.getImageWidth(imgFrame.image) / width * this.maxNumFrame;
			if (nFrame < 1) nFrame = 1;
			isLoaded = (mImage.getImageWidth(imgFrame.image) > 0);
		}
		else
		{
			nFrame = 1;
			isLoaded = false;
		}
	}

	public FrameImage(int ID, int width, int height, int width2, int height2, int maxNumFrame)
	{
		Id = normalizeId(ID);
		if (GameCanvas.lowGraphic)
		{
			frameWidth = width2;
			frameHeight = height2;
			lowG = true;
		}
		else
		{
			frameWidth = width;
			frameHeight = height;
		}
		this.maxNumFrame = (maxNumFrame > 0) ? maxNumFrame : 1;
		imgFrame = getImage();
		if (imgFrame != null && imgFrame.image != null && frameWidth > 0)
		{
			nFrame = mImage.getImageWidth(imgFrame.image) / frameWidth * this.maxNumFrame;
			if (nFrame < 1) nFrame = 1;
			isLoaded = (mImage.getImageWidth(imgFrame.image) > 0);
		}
		else
		{
			nFrame = 1;
			isLoaded = false;
		}
	}

	public void updateDimensions()
	{
		if (imgFrame == null || imgFrame.image == null) return;
		int imgW = mImage.getImageWidth(imgFrame.image);
		int imgH = mImage.getImageHeight(imgFrame.image);
		if (imgW <= 0 && imgFrame.image != null) imgW = imgFrame.image.getWidth();
		if (imgH <= 0 && imgFrame.image != null) imgH = imgFrame.image.getHeight();
		if (imgW <= 0 || imgH <= 0) return;

		if (isFormFrame)
		{
			if (imgW > 0) frameWidth = imgW;
			if (nFrame > 0 && imgH > 0)
			{
				frameHeight = imgH / nFrame;
			}
			else if (imgH > 0)
			{
				frameHeight = imgH;
			}
			isLoaded = (frameWidth > 0 && frameHeight > 0);
		}
		else if (maxNumFrame > 1 && frameWidth > 0)
		{
			nFrame = (imgW / frameWidth) * maxNumFrame;
			if (nFrame < 1) nFrame = 1;
			if (frameHeight <= 0 && imgH > 0)
			{
				frameHeight = imgH / maxNumFrame;
			}
			isLoaded = (frameWidth > 0 && frameHeight > 0);
		}
		else if (frameHeight > 0)
		{
			nFrame = imgH / frameHeight;
			if (nFrame < 1) nFrame = 1;
			maxNumFrame = nFrame;
			if (frameWidth <= 0 && imgW > 0)
			{
				frameWidth = imgW;
			}
			isLoaded = (frameWidth > 0 && frameHeight > 0);
		}
		else if (frameWidth > 0)
		{
			nFrame = imgW / frameWidth;
			if (nFrame < 1) nFrame = 1;
			maxNumFrame = 1;
			if (frameHeight <= 0 && imgH > 0)
			{
				frameHeight = imgH;
			}
			isLoaded = (frameWidth > 0 && frameHeight > 0);
		}
	}

	// ─── Draw Methods — fix cho Unity: check imgFrame.image==null + try/catch getImage ─

	public void drawFrame(int idx, int x, int y, int trans, int orthor, mGraphics g)
	{
		if (g == null) return;
		if (imgFrame == null || imgFrame.image == null)
		{
			int curTick = GameCanvas.gameTick;
			if (curTick - lastLoadAttemptFrame < 30 && lastLoadAttemptFrame >= 0) return;
			lastLoadAttemptFrame = curTick;
			imgFrame = getImage();
			if (imgFrame == null || imgFrame.image == null) return;
			isLoaded = false;
		}
		if (!isLoaded || frameWidth <= 0 || frameHeight <= 0)
		{
			updateDimensions();
			if (imgFrame != null && imgFrame.image != null && imgFrame.image.texture != null && imgFrame.image.texture.filterMode != UnityEngine.FilterMode.Bilinear)
			{
				imgFrame.image.texture.filterMode = UnityEngine.FilterMode.Bilinear;
			}
		}
		if (idx >= 0 && idx < nFrame && frameHeight > 0 && frameWidth > 0)
		{
			g.drawRegion(imgFrame, 0, idx * frameHeight, frameWidth, frameHeight, trans, x, y, orthor);
		}
	}

	public mImage getImageFrame()
	{
		if (imgFrame != null && imgFrame.image != null)
		{
			return imgFrame;
		}
		if (Id >= 0)
		{
			mImage loaded = getImage();
			if (loaded != null && loaded.image != null)
			{
				imgFrame = loaded;
				isLoaded = false;
			}
		}
		return imgFrame;
	}

	public mImage getImage()
	{
		if (Id < 0)
		{
			return imgFrame;
		}
		try
		{
			MainImage mainImg = ObjectData.getImage(lowG ? IconType.EFF_CLIENT_LOW : IconType.EFF_CLIENT, Id);
			if (mainImg != null && mainImg.img != null && mainImg.img.image != null)
			{
				if (imgFrame != mainImg.img)
				{
					imgFrame = mainImg.img;
					isLoaded = false;
				}
				return imgFrame;
			}
			return (mainImg != null) ? mainImg.img : null;
		}
		catch (Exception)
		{
			return null;
		}
	}

	public void drawFrameNew_BeginSuper(int idx, int x, int y, int trans, int orthor, mGraphics g)
	{
		if (g == null) return;
		int maxN = (maxNumFrame > 0) ? maxNumFrame : 1;
		int num = idx + indexSuper * maxN;
		if (imgFrame == null || imgFrame.image == null)
		{
			int curTick = GameCanvas.gameTick;
			if (curTick - lastLoadAttemptFrame < 30 && lastLoadAttemptFrame >= 0) return;
			lastLoadAttemptFrame = curTick;
			imgFrame = getImage();
			if (imgFrame == null || imgFrame.image == null) return;
			isLoaded = false;
		}
		if (!isLoaded || frameWidth <= 0 || frameHeight <= 0)
		{
			updateDimensions();
		}
		if (num >= 0 && num < nFrame && maxN > 0 && frameWidth > 0 && frameHeight > 0)
		{
			g.drawRegion(imgFrame, num / maxN * frameWidth, num % maxN * frameHeight, frameWidth, frameHeight, trans, x, y, orthor);
		}
	}

	public void drawFrameNew(int idx, int x, int y, int trans, int orthor, mGraphics g)
	{
		if (g == null) return;
		int maxN = (maxNumFrame > 0) ? maxNumFrame : 1;
		if (imgFrame == null || imgFrame.image == null)
		{
			int curTick = GameCanvas.gameTick;
			if (curTick - lastLoadAttemptFrame < 30 && lastLoadAttemptFrame >= 0) return;
			lastLoadAttemptFrame = curTick;
			imgFrame = getImage();
			if (imgFrame == null || imgFrame.image == null) return;
			isLoaded = false;
		}
		if (!isLoaded || frameWidth <= 0 || frameHeight <= 0)
		{
			updateDimensions();
		}
		if (idx >= 0 && idx < nFrame && maxN > 0 && frameWidth > 0 && frameHeight > 0)
		{
			g.drawRegion(imgFrame, idx / maxN * frameWidth, idx % maxN * frameHeight, frameWidth, frameHeight, trans, x, y, orthor);
		}
	}
}
