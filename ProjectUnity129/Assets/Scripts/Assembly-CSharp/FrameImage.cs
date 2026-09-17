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
		}
		else
		{
			nFrame = 1;
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
		}
		else
		{
			nFrame = 1;
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
		}
		else
		{
			nFrame = 1;
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
		}
		else
		{
			nFrame = 1;
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
			}
		}
		catch (Exception)
		{
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
		}
		else
		{
			nFrame = 1;
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
		}
		else
		{
			nFrame = 1;
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
		}
		else
		{
			nFrame = 1;
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
		}
		else
		{
			nFrame = 1;
		}
	}

	// ─── Draw Methods — fix cho Unity: check imgFrame.image==null + try/catch getImage ─

	public void drawFrame(int idx, int x, int y, int trans, int orthor, mGraphics g)
	{
		if (g == null) return;
		// Retry load nếu image chưa sẵn sàng (null hoặc texture chưa load), chỉ khi có Id hợp lệ
		if (Id >= 0 && (imgFrame == null || imgFrame.image == null))
		{
			mImage loaded = getImage();
			if (loaded != null && loaded.image != null)
			{
				imgFrame = loaded;
			}
		}
		if (imgFrame == null || imgFrame.image == null) return;

		// Lazy-init dims lần đầu tiên sau khi image load xong
		if (isFormFrame)
		{
			if (frameWidth <= 0 || frameHeight <= 0)
			{
				frameWidth = mImage.getImageWidth(imgFrame.image);
				frameHeight = (nFrame > 0) ? (mImage.getImageHeight(imgFrame.image) / nFrame) : mImage.getImageHeight(imgFrame.image);
			}
		}
		else if (frameHeight > 0 && nFrame <= 1)
		{
			int h = mImage.getImageHeight(imgFrame.image);
			if (h > 0 && frameHeight > 0)
			{
				nFrame = h / frameHeight;
				if (nFrame < 1) nFrame = 1;
				maxNumFrame = nFrame;
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
		// Retry load nếu image chưa sẵn sàng
		if (Id >= 0 && (imgFrame == null || imgFrame.image == null))
		{
			mImage loaded = getImage();
			if (loaded != null && loaded.image != null)
			{
				imgFrame = loaded;
			}
		}
		if (imgFrame == null || imgFrame.image == null) return;

		// Lazy-init dims
		if (isFormFrame)
		{
			if (frameWidth <= 0 || frameHeight <= 0)
			{
				frameWidth = mImage.getImageWidth(imgFrame.image);
				frameHeight = (nFrame > 0) ? (mImage.getImageHeight(imgFrame.image) / nFrame) : mImage.getImageHeight(imgFrame.image);
			}
		}
		else if (frameWidth > 0 && nFrame <= 1)
		{
			int w = mImage.getImageWidth(imgFrame.image);
			if (w > 0 && frameWidth > 0)
			{
				nFrame = w / frameWidth * maxN;
				if (nFrame < 1) nFrame = 1;
			}
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
		// Retry load nếu image chưa sẵn sàng
		if (Id >= 0 && (imgFrame == null || imgFrame.image == null))
		{
			mImage loaded = getImage();
			if (loaded != null && loaded.image != null)
			{
				imgFrame = loaded;
			}
		}
		if (imgFrame == null || imgFrame.image == null) return;

		// Lazy-init dims
		if (isFormFrame)
		{
			if (frameWidth <= 0 || frameHeight <= 0)
			{
				frameWidth = mImage.getImageWidth(imgFrame.image);
				frameHeight = (nFrame > 0) ? (mImage.getImageHeight(imgFrame.image) / nFrame) : mImage.getImageHeight(imgFrame.image);
			}
		}
		else if (frameWidth > 0 && nFrame <= 1)
		{
			int w = mImage.getImageWidth(imgFrame.image);
			if (w > 0 && frameWidth > 0)
			{
				nFrame = w / frameWidth * maxN;
				if (nFrame < 1) nFrame = 1;
			}
		}

		if (idx >= 0 && idx < nFrame && maxN > 0 && frameWidth > 0 && frameHeight > 0)
		{
			g.drawRegion(imgFrame, idx / maxN * frameWidth, idx % maxN * frameHeight, frameWidth, frameHeight, trans, x, y, orthor);
		}
	}
}
