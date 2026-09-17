using System;
using System.Collections;
using UnityEngine;

public class mGraphics
{
	public static int HCENTER = 1;

	public static int VCENTER = 2;

	public static int LEFT = 4;

	public static int RIGHT = 8;

	public static int TOP = 16;

	public static int BOTTOM = 32;

	private float r;

	private float g;

	private float b;

	private float a;

	public int clipX;

	public int clipY;

	public int clipW;

	public int clipH;

	private bool isClip;

	private bool isTranslate = true;

	private int translateX;

	private int translateY;

	private float translateXf;

	private float translateYf;

	public static int zoomLevel = 1;

	public const int BASELINE = 64;

	public const int SOLID = 0;

	public const int DOTTED = 1;

	public const int TRANS_MIRROR = 2;

	public const int TRANS_MIRROR_ROT180 = 1;

	public const int TRANS_MIRROR_ROT270 = 4;

	public const int TRANS_MIRROR_ROT90 = 7;

	public const int TRANS_NONE = 0;

	public const int TRANS_ROT180 = 3;

	public const int TRANS_ROT270 = 6;

	public const int TRANS_ROT90 = 5;

	public static Hashtable cachedTextures = new Hashtable();

	public static int addYWhenOpenKeyBoard;

	public static int zoomResource = 1;

	public struct ClipState
	{
		public bool isClip;
		public int clipX;
		public int clipY;
		public int clipW;
		public int clipH;
		public int clipTX;
		public int clipTY;
	}

	private int clipTX;

	private int clipTY;

	private int currentBGColor;

	private Vector2 pos = new Vector2(0f, 0f);

	private Rect rect;

	private Matrix4x4 matrixBackup;

	private Vector2 pivot;

	public Vector2 size = new Vector2(128f, 128f);

	public Vector2 relativePosition = new Vector2(0f, 0f);

	public static Color transParentColor = new Color(1f, 1f, 1f, 0f);

	private Material lineMaterial;

	private void cache(string key, Texture value)
	{
		if (cachedTextures.Count > 400)
		{
			cachedTextures.Clear();
		}
		if (value.width * value.height < MotherCanvas.w * MotherCanvas.h)
		{
			cachedTextures.Add(key, value);
		}
	}

	public void translate(int tx, int ty)
	{
		tx *= zoomLevel;
		ty *= zoomLevel;
		translateX += tx;
		translateY += ty;
		isTranslate = true;
		if (translateX == 0 && translateY == 0)
		{
			isTranslate = false;
		}
	}

	public void translate(float x, float y)
	{
		translateXf += x;
		translateYf += y;
		isTranslate = true;
		if (translateXf == 0f && translateYf == 0f)
		{
			isTranslate = false;
		}
	}

	public int getTranslateX()
	{
		return translateX / zoomLevel;
	}

	public int getTranslateY()
	{
		return translateY / zoomLevel + addYWhenOpenKeyBoard;
	}

	public void setClip(int x, int y, int w, int h)
	{
		w = System.Math.Max(0, w);
		h = System.Math.Max(0, h);
		x *= zoomLevel;
		y *= zoomLevel;
		w *= zoomLevel;
		h *= zoomLevel;
		clipTX = translateX;
		clipTY = translateY;
		clipX = x;
		clipY = y;
		clipW = w;
		clipH = h;
		isClip = true;
	}

	public ClipState getClipState()
	{
		return new ClipState
		{
			isClip = isClip,
			clipX = clipX,
			clipY = clipY,
			clipW = clipW,
			clipH = clipH,
			clipTX = clipTX,
			clipTY = clipTY
		};
	}

	public void setClipState(ClipState state)
	{
		isClip = state.isClip;
		clipX = state.clipX;
		clipY = state.clipY;
		clipW = state.clipW;
		clipH = state.clipH;
		clipTX = state.clipTX;
		clipTY = state.clipTY;
	}

	public void clearClip()
	{
		isClip = false;
	}

	private struct ClipEntry
	{
		public bool isClip;
		public int clipX;
		public int clipY;
		public int clipW;
		public int clipH;
		public int clipTX;
		public int clipTY;
	}

	private ClipEntry[] clipStack = new ClipEntry[128];
	private int clipStackTop = 0;

	public void pushClip()
	{
		if (clipStackTop < clipStack.Length)
		{
			clipStack[clipStackTop++] = new ClipEntry
			{
				isClip = isClip,
				clipX = clipX,
				clipY = clipY,
				clipW = clipW,
				clipH = clipH,
				clipTX = clipTX,
				clipTY = clipTY
			};
		}
	}

	public void popClip()
	{
		if (clipStackTop > 0)
		{
			ClipEntry e = clipStack[--clipStackTop];
			isClip = e.isClip;
			clipX = e.clipX;
			clipY = e.clipY;
			clipW = e.clipW;
			clipH = e.clipH;
			clipTX = e.clipTX;
			clipTY = e.clipTY;
		}
	}

	public void clipRectIntersect(int x, int y, int w, int h)
	{
		w = System.Math.Max(0, w);
		h = System.Math.Max(0, h);
		int zx = x * zoomLevel;
		int zy = y * zoomLevel;
		int zw = w * zoomLevel;
		int zh = h * zoomLevel;

		if (!isClip)
		{
			setClip(x, y, w, h);
			return;
		}

		int curX = clipX;
		int curY = clipY;
		int curW = clipW;
		int curH = clipH;

		int newX = System.Math.Max(zx, curX);
		int newY = System.Math.Max(zy, curY);
		int newRight = System.Math.Min(zx + zw, curX + curW);
		int newBottom = System.Math.Min(zy + zh, curY + curH);
		int newW = newRight - newX;
		int newH = newBottom - newY;

		if (newW > 0 && newH > 0)
		{
			clipX = newX;
			clipY = newY;
			clipW = newW;
			clipH = newH;
			clipTX = translateX;
			clipTY = translateY;
			isClip = true;
		}
		else
		{
			clipW = 0;
			clipH = 0;
			isClip = true;
		}
	}

	public void fillRect(int x, int y, int w, int h, int color, int alpha)
	{
		float alpha2 = 0.5f;
		setColor(color, alpha2);
		fillRect(x, y, w, h, isA: false);
	}

	public void fillRect(int x, int y, int w, int h)
	{
		fillRect(x, y, w, h, isA: false);
	}

	public void drawLine(int x1, int y1, int x2, int y2, bool isA)
	{
		x1 *= zoomLevel;
		y1 *= zoomLevel;
		x2 *= zoomLevel;
		y2 *= zoomLevel;
		if (y1 == y2)
		{
			if (x1 > x2)
			{
				int num = x2;
				x2 = x1;
				x1 = num;
			}
			fillRect(x1, y1, x2 - x1, 1, isA);
			return;
		}
		if (x1 == x2)
		{
			if (y1 > y2)
			{
				int num2 = y2;
				y2 = y1;
				y1 = num2;
			}
			fillRect(x1, y1, 1, y2 - y1, isA);
			return;
		}
		if (isTranslate)
		{
			x1 += translateX;
			y1 += translateY;
			x2 += translateX;
			y2 += translateY;
		}
		string key = "dl" + r + g + b;
		Texture2D texture2D = (Texture2D)cachedTextures[key];
		if (texture2D == null)
		{
			texture2D = new Texture2D(1, 1);
			Color color = new Color(r, g, b);
			texture2D.SetPixel(0, 0, color);
			texture2D.Apply();
			cache(key, texture2D);
		}
		Vector2 vector = new Vector2(x1, y1);
		Vector2 vector2 = new Vector2(x2, y2) - vector;
		float num3 = 57.29578f * Mathf.Atan(vector2.y / vector2.x);
		if (vector2.x < 0f)
		{
			num3 += 180f;
		}
		int num4 = (int)Mathf.Ceil(0f);
		GUIUtility.RotateAroundPivot(num3, vector);
		int num5 = 0;
		int num6 = 0;
		int num7 = 0;
		int num8 = 0;
		if (isClip)
		{
			num5 = clipX;
			num6 = clipY;
			num7 = clipW;
			num8 = clipH;
			if (isTranslate)
			{
				num5 += clipTX;
				num6 += clipTY;
			}
		}
		if (isClip)
		{
			GUI.BeginGroup(new Rect(num5, num6, num7, num8));
		}
		Graphics.DrawTexture(new Rect(vector.x - (float)num5, vector.y - (float)num4 - (float)num6, vector2.magnitude, 1f), texture2D);
		if (isClip)
		{
			GUI.EndGroup();
		}
		GUIUtility.RotateAroundPivot(0f - num3, vector);
	}

	public void drawLine(int x1, int y1, int x2, int y2)
	{
		drawLine(x1, y1, x2, y2, isA: false);
	}

	public Color setColorMiniMap(int rgb)
	{
		int num = rgb & 0xFF;
		int num2 = (rgb >> 8) & 0xFF;
		int num3 = (rgb >> 16) & 0xFF;
		float num4 = (float)num / 256f;
		float num5 = (float)num2 / 256f;
		float num6 = (float)num3 / 256f;
		return new Color(num6, num5, num4);
	}

	public float[] getRGB(Color cl)
	{
		float num = 256f * cl.r;
		float num2 = 256f * cl.g;
		float num3 = 256f * cl.b;
		return new float[3] { num, num2, num3 };
	}

	public void drawRect(int x, int y, int w, int h, bool isA)
	{
		int num = 1;
		fillRect(x, y, w, num, isA);
		fillRect(x, y, num, h, isA);
		fillRect(x + w, y, num, h + 1, isA);
		fillRect(x, y + h, w + 1, num, isA);
	}

	public void drawRect(int x, int y, int w, int h)
	{
		int num = 1;
		fillRect(x, y, w, num);
		fillRect(x, y, num, h);
		fillRect(x + w, y, num, h + 1);
		fillRect(x, y + h, w + 1, num);
	}

	public void drawArc(int x, int y, int w, int h, int a, int b, bool isA)
	{
		int num = 1;
		fillRect(x, y, w, num, isA);
		fillRect(x, y, num, h, isA);
		fillRect(x + w, y, num, h + 1, isA);
		fillRect(x, y + h, w + 1, num, isA);
	}

	public void fillRect(int x, int y, int w, int h, bool isA)
	{
		x *= zoomLevel;
		y *= zoomLevel;
		w *= zoomLevel;
		h *= zoomLevel;
		if (w < 0 || h < 0)
		{
			return;
		}
		if (isTranslate)
		{
			x += translateX;
			y += translateY;
		}
		int width = 1;
		int height = 1;
		string key = "fr" + width + height + r + g + b + a;
		Texture2D texture2D = (Texture2D)cachedTextures[key];
		if (texture2D == null)
		{
			texture2D = new Texture2D(width, height);
			Color color = new Color(r, g, b, a);
			texture2D.SetPixel(0, 0, color);
			texture2D.Apply();
			cache(key, texture2D);
		}
		int num = 0;
		int num2 = 0;
		int num3 = 0;
		int num4 = 0;
		if (isClip)
		{
			num = clipX;
			num2 = clipY;
			num3 = clipW;
			num4 = clipH;
			if (isTranslate)
			{
				num += clipTX;
				num2 += clipTY;
			}
		}
		if (isClip)
		{
			GUI.BeginGroup(new Rect(num, num2, num3, num4));
		}
		GUI.DrawTexture(new Rect(x - num, y - num2, w, h), texture2D);
		if (isClip)
		{
			GUI.EndGroup();
		}
	}

	public void fillArc(int x, int y, int w, int h, int a, int b, bool isSetClip)
	{
		fillRect(x, y, w, h, isA: false);
	}

	public void setColor(int rgb)
	{
		int num = rgb & 0xFF;
		int num2 = (rgb >> 8) & 0xFF;
		int num3 = (rgb >> 16) & 0xFF;
		b = (float)num / 256f;
		g = (float)num2 / 256f;
		r = (float)num3 / 256f;
		a = 255f;
	}

	public void setColor(Color color)
	{
		b = color.b;
		g = color.g;
		r = color.r;
	}

	public void setBgColor(int rgb)
	{
		if (rgb != currentBGColor)
		{
			currentBGColor = rgb;
			int num = rgb & 0xFF;
			int num2 = (rgb >> 8) & 0xFF;
			int num3 = (rgb >> 16) & 0xFF;
			b = (float)num / 256f;
			g = (float)num2 / 256f;
			r = (float)num3 / 256f;
			Main.main.GetComponent<UnityEngine.Camera>().backgroundColor = new Color(r, g, b);
		}
	}

	public void drawString(string s, int x, int y, GUIStyle style)
	{
		x *= zoomLevel;
		y *= zoomLevel;
		if (isTranslate)
		{
			x += translateX;
			y += translateY;
		}
		int num = 0;
		int num2 = 0;
		int num3 = 0;
		int num4 = 0;
		if (isClip)
		{
			num = clipX;
			num2 = clipY;
			num3 = clipW;
			num4 = clipH;
			if (isTranslate)
			{
				num += clipTX;
				num2 += clipTY;
			}
		}
		if (isClip)
		{
			GUI.BeginGroup(new Rect(num, num2, num3, num4));
		}
		GUI.Label(new Rect(x - num, y - num2, ScaleGUI.WIDTH, 100f), s, style);
		if (isClip)
		{
			GUI.EndGroup();
		}
	}

	public void drawString(string s, int x, int y, int archor)
	{
		int num = -8;
		mFont.tahoma_7_white.drawString(this, s, x, y + num, archor);
	}

	public void setColor(int rgb, float alpha)
	{
		int num = rgb & 0xFF;
		int num2 = (rgb >> 8) & 0xFF;
		int num3 = (rgb >> 16) & 0xFF;
		b = (float)num / 256f;
		g = (float)num2 / 256f;
		r = (float)num3 / 256f;
		a = alpha;
	}

	public void drawString(string s, int x, int y, GUIStyle style, int w)
	{
		x *= zoomLevel;
		y *= zoomLevel;
		if (isTranslate)
		{
			x += translateX;
			y += translateY;
		}
		int num = 0;
		int num2 = 0;
		int num3 = 0;
		int num4 = 0;
		if (isClip)
		{
			num = clipX;
			num2 = clipY;
			num3 = clipW;
			num4 = clipH;
			if (isTranslate)
			{
				num += clipTX;
				num2 += clipTY;
			}
		}
		if (isClip)
		{
			GUI.BeginGroup(new Rect(num, num2, num3, num4));
		}
		GUI.Label(new Rect(x - num, y - num2 - 4, w, 100f), s, style);
		if (isClip)
		{
			GUI.EndGroup();
		}
	}

	private void UpdatePos(int anchor)
	{
		Vector2 vector = new Vector2(0f, 0f);
		switch (anchor)
		{
		case 3:
			vector = new Vector2(size.x / 2f, size.y / 2f);
			break;
		case 20:
			vector = new Vector2(0f, 0f);
			break;
		case 17:
			vector = new Vector2(Screen.width / 2, 0f);
			break;
		case 24:
			vector = new Vector2(Screen.width, 0f);
			break;
		case 6:
			vector = new Vector2(0f, Screen.height / 2);
			break;
		case 10:
			vector = new Vector2(Screen.width, Screen.height / 2);
			break;
		case 36:
			vector = new Vector2(0f, Screen.height);
			break;
		case 33:
			vector = new Vector2(Screen.width / 2, Screen.height);
			break;
		case 40:
			vector = new Vector2(Screen.width, Screen.height);
			break;
		}
		pos = vector + relativePosition;
		rect = new Rect(pos.x - size.x * 0.5f, pos.y - size.y * 0.5f, size.x, size.y);
		pivot = new Vector2(rect.xMin + rect.width * 0.5f, rect.yMin + rect.height * 0.5f);
	}

	public void drawRegion(Image arg0, int x0, int y0, int w0, int h0, int arg5, int x, int y, int arg8, bool isA)
	{
		_drawRegion(arg0, x0, y0, w0, h0, arg5, x, y, arg8);
	}

	public void drawRegion(Image arg0, int x0, int y0, int w0, int h0, int arg5, int x, int y, int arg8)
	{
		_drawRegion(arg0, x0, y0, w0, h0, arg5, x, y, arg8);
	}

	public void drawRegion(mImage arg0, int x0, int y0, int w0, int h0, int arg5, float x, float y, int arg8)
	{
		if (arg0 != null && arg0.image != null)
		{
			_drawRegion(arg0.image, x0, y0, w0, h0, arg5, (int)x, (int)y, arg8);
		}
	}

	public void __drawRegion(Image image, int x0, int y0, int w, int h, int transform, float x, float y, int anchor)
	{
		_drawRegion(image, (float)x0, (float)y0, w, h, transform, (int)x, (int)y, anchor);
	}

	public void _drawRegion(Image image, float x0, float y0, int w, int h, int transform, int x, int y, int anchor)
	{
		if (image == null || image.texture == null || w <= 0 || h <= 0)
		{
			return;
		}

		float logicW = (float)mImage.getImageWidth(image);
		float logicH = (float)mImage.getImageHeight(image);
		if (logicW <= 0f) logicW = (float)image.texture.width;
		if (logicH <= 0f) logicH = (float)image.texture.height;

		float uv_x = x0 / logicW;
		float uv_w = (float)w / logicW;
		float uv_h = (float)h / logicH;
		float uv_y = (logicH - y0 - (float)h) / logicH;

		if (uv_x < 0f) { uv_w += uv_x; uv_x = 0f; }
		if (uv_y < 0f) { uv_h += uv_y; uv_y = 0f; }
		if (uv_x + uv_w > 1f) uv_w = 1f - uv_x;
		if (uv_y + uv_h > 1f) uv_h = 1f - uv_y;
		if (uv_w <= 0f || uv_h <= 0f) return;

		float sw = (float)w * (float)zoomLevel;
		float sh = (float)h * (float)zoomLevel;
		float sx = (float)x * (float)zoomLevel;
		float sy = (float)y * (float)zoomLevel;
		if (isTranslate)
		{
			sx += (float)translateX;
			sy += (float)translateY;
		}

		float dispW = ((uint)(transform - 4) <= 3u) ? sh : sw;
		float dispH = ((uint)(transform - 4) <= 3u) ? sw : sh;

		if ((anchor & HCENTER) != 0) sx -= dispW / 2f;
		else if ((anchor & RIGHT) != 0) sx -= dispW;

		if ((anchor & VCENTER) != 0) sy -= dispH / 2f;
		else if ((anchor & BOTTOM) != 0) sy -= dispH;

		if (isClip)
		{
			float cx = (float)clipX + (float)(isTranslate ? clipTX : 0);
			float cy = (float)clipY + (float)(isTranslate ? clipTY : 0);
			Rect clipR = new Rect(cx, cy, (float)clipW, (float)clipH);
			Rect targetR = new Rect(sx, sy, dispW, dispH);
			Rect visibleR = intersectRect(clipR, targetR);
			if (visibleR.width <= 0f || visibleR.height <= 0f)
			{
				return;
			}
			float cutLeft = (visibleR.x - targetR.x) / dispW;
			float cutTop = (visibleR.y - targetR.y) / dispH;

			uv_x += cutLeft * uv_w;
			uv_w = (visibleR.width / dispW) * uv_w;
			uv_y += ((targetR.y + dispH) - (visibleR.y + visibleR.height)) / dispH * uv_h;
			uv_h = (visibleR.height / dispH) * uv_h;

			sx = visibleR.x;
			sy = visibleR.y;
			dispW = visibleR.width;
			dispH = visibleR.height;
		}

		uv_x = Mathf.Clamp01(uv_x);
		uv_y = Mathf.Clamp01(uv_y);
		if (uv_x + uv_w > 1f) uv_w = 1f - uv_x;
		if (uv_y + uv_h > 1f) uv_h = 1f - uv_y;
		if (uv_w <= 0f || uv_h <= 0f) return;

		float drawUvX = uv_x;
		float drawUvY = uv_y;
		float drawUvW = uv_w;
		float drawUvH = uv_h;

		if (transform == 2)
		{
			drawUvX = uv_x + uv_w;
			drawUvW = -uv_w;
		}
		else if (transform == 1)
		{
			drawUvY = uv_y + uv_h;
			drawUvH = -uv_h;
		}
		else if (transform == 3)
		{
			drawUvX = uv_x + uv_w;
			drawUvW = -uv_w;
			drawUvY = uv_y + uv_h;
			drawUvH = -uv_h;
		}
		else if (transform == 4 || transform == 5 || transform == 6 || transform == 7)
		{
			matrixBackup = GUI.matrix;
			Vector2 pivotPoint = new Vector2(sx + dispW * 0.5f, sy + dispH * 0.5f);
			float angle = 0f;
			if (transform == 5) angle = 90f;
			else if (transform == 6) angle = 270f;
			else if (transform == 4) { angle = 270f; drawUvX = uv_x + uv_w; drawUvW = -uv_w; }
			else if (transform == 7) { angle = 270f; drawUvY = uv_y + uv_h; drawUvH = -uv_h; }
			GUIUtility.RotateAroundPivot(angle, pivotPoint);
		}

		Graphics.DrawTexture(new Rect(sx, sy, dispW, dispH), image.texture, new Rect(drawUvX, drawUvY, drawUvW, drawUvH), 0, 0, 0, 0);

		if (transform == 4 || transform == 5 || transform == 6 || transform == 7)
		{
			GUI.matrix = matrixBackup;
		}
	}

	public void drawRegion2(Image image, float x0, float y0, int w, int h, int transform, int x, int y, int anchor)
	{
		GUI.color = image.colorBlend;
		if (isTranslate)
		{
			x += translateX;
			y += translateY;
		}
		string key = "dg" + x0 + y0 + w + h + transform + image.GetHashCode();
		Texture2D texture2D = (Texture2D)cachedTextures[key];
		if (texture2D == null)
		{
			texture2D = Image.createImage(image, (int)x0, (int)y0, w, h, transform).texture;
			cache(key, texture2D);
		}
		int num = 0;
		int num2 = 0;
		int num3 = 0;
		int num4 = 0;
		float num5 = w;
		float num6 = h;
		float num7 = 0f;
		float num8 = 0f;
		if ((anchor & HCENTER) == HCENTER)
		{
			num7 -= num5 / 2f;
		}
		if ((anchor & VCENTER) == VCENTER)
		{
			num8 -= num6 / 2f;
		}
		if ((anchor & RIGHT) == RIGHT)
		{
			num7 -= num5;
		}
		if ((anchor & BOTTOM) == BOTTOM)
		{
			num8 -= num6;
		}
		x += (int)num7;
		y += (int)num8;
		if (isClip)
		{
			num = clipX;
			num2 = clipY;
			num3 = clipW;
			num4 = clipH;
			if (isTranslate)
			{
				num += clipTX;
				num2 += clipTY;
			}
		}
		if (isClip)
		{
			GUI.BeginGroup(new Rect(num, num2, num3, num4));
		}
		GUI.DrawTexture(new Rect(x - num, y - num2, w, h), texture2D);
		if (isClip)
		{
			GUI.EndGroup();
		}
		GUI.color = new Color(1f, 1f, 1f, 1f);
	}

	public void drawImagaByDrawTexture(Image image, float x, float y)
	{
		x *= (float)zoomLevel;
		y *= (float)zoomLevel;
		GUI.DrawTexture(new Rect(x + (float)translateX, y + (float)translateY, image.getRealImageWidth(), image.getRealImageHeight()), image.texture);
	}

	public void drawImage(mImage image, int x, int y, int anchor)
	{
		if (image != null && image.image != null)
		{
			drawImage(image.image, x, y, anchor);
		}
	}

	public void drawImage(Image image, int x, int y, int anchor)
	{
		if (image == null || image.texture == null)
		{
			return;
		}
		int w = mImage.getImageWidth(image);
		int h = mImage.getImageHeight(image);
		if (w <= 0) w = image.texture.width;
		if (h <= 0) h = image.texture.height;
		_drawRegion(image, 0, 0, w, h, 0, x, y, anchor);
	}

	public void drawRoundRect(int x, int y, int w, int h, int arcWidth, int arcHeight)
	{
		drawRect(x, y, w, h, isA: false);
	}

	public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
	{
		fillRect(x, y, width, height, isA: false);
	}

	public void reset()
	{
		isClip = false;
		isTranslate = false;
		translateX = 0;
		translateY = 0;
	}

	public Rect intersectRect(Rect r1, Rect r2)
	{
		float num = r1.x;
		float num2 = r1.y;
		float x = r2.x;
		float y = r2.y;
		float num3 = num;
		num3 += r1.width;
		float num4 = num2;
		num4 += r1.height;
		float num5 = x;
		num5 += r2.width;
		float num6 = y;
		num6 += r2.height;
		if (num < x)
		{
			num = x;
		}
		if (num2 < y)
		{
			num2 = y;
		}
		if (num3 > num5)
		{
			num3 = num5;
		}
		if (num4 > num6)
		{
			num4 = num6;
		}
		num3 -= num;
		num4 -= num2;
		if (num3 < -30000f)
		{
			num3 = -30000f;
		}
		if (num4 < -30000f)
		{
			num4 = -30000f;
		}
		return new Rect(num, num2, (int)num3, (int)num4);
	}

	public void drawImageScale(Image image, int x, int y, int w, int h, int tranform)
	{
		GUI.color = Color.red;
		x *= zoomLevel;
		y *= zoomLevel;
		w *= zoomLevel;
		h *= zoomLevel;
		if (image != null)
		{
			Graphics.DrawTexture(new Rect(x + translateX, y + translateY, (tranform == 0) ? w : (-w), h), image.texture);
		}
	}

	public void drawImageSimple(Image image, int x, int y)
	{
		x *= zoomLevel;
		y *= zoomLevel;
		if (image != null)
		{
			Graphics.DrawTexture(new Rect(x, y, image.w, image.h), image.texture);
		}
	}

	public static int getImageWidth(Image image)
	{
		return image.getWidth();
	}

	public static int getImageHeight(Image image)
	{
		return image.getHeight();
	}

	public static bool isNotTranColor(Color color)
	{
		if (color == Color.clear || color == transParentColor)
		{
			return false;
		}
		return true;
	}

	public static Image blend(Image img0, float level, int rgb)
	{
		int num = rgb & 0xFF;
		int num2 = (rgb >> 8) & 0xFF;
		int num3 = (rgb >> 16) & 0xFF;
		float num4 = (float)num / 256f;
		float num5 = (float)num2 / 256f;
		float num6 = (float)num3 / 256f;
		Color color = new Color(num6, num5, num4);
		Color[] pixels = img0.texture.GetPixels();
		float num7 = color.r;
		float num8 = color.g;
		float num9 = color.b;
		for (int i = 0; i < pixels.Length; i++)
		{
			Color color2 = pixels[i];
			if (isNotTranColor(color2))
			{
				float num10 = (num7 - color2.r) * level + color2.r;
				float num11 = (num8 - color2.g) * level + color2.g;
				float num12 = (num9 - color2.b) * level + color2.b;
				if (num10 > 255f)
				{
					num10 = 255f;
				}
				if (num10 < 0f)
				{
					num10 = 0f;
				}
				if (num11 > 255f)
				{
					num11 = 255f;
				}
				if (num11 < 0f)
				{
					num11 = 0f;
				}
				if (num12 < 0f)
				{
					num12 = 0f;
				}
				if (num12 > 255f)
				{
					num12 = 255f;
				}
				pixels[i].r = num10;
				pixels[i].g = num11;
				pixels[i].b = num12;
			}
		}
		Image image = Image.createImage(img0.getRealImageWidth(), img0.getRealImageHeight());
		image.texture.SetPixels(pixels);
		Image.setTextureQuality(image.texture);
		image.texture.Apply();
		Cout.LogError2("BLEND ----------------------------------------------------");
		return image;
	}

	public static Color setColorObj(int rgb)
	{
		int num = rgb & 0xFF;
		int num2 = (rgb >> 8) & 0xFF;
		int num3 = (rgb >> 16) & 0xFF;
		float num4 = (float)num / 256f;
		float num5 = (float)num2 / 256f;
		float num6 = (float)num3 / 256f;
		return new Color(num6, num5, num4);
	}

	public void fillTrans(Image imgTrans, int x, int y, int w, int h)
	{
		setColor(0, 0.5f);
		fillRect(x * zoomLevel, y * zoomLevel, w * zoomLevel, h * zoomLevel, isA: false);
	}

	public static int blendColor(float level, int color, int colorBlend)
	{
		Color color2 = setColorObj(colorBlend);
		float num = color2.r * 255f;
		float num2 = color2.g * 255f;
		float num3 = color2.b * 255f;
		Color color3 = setColorObj(color);
		float num4 = (num + color3.r) * level + color3.r;
		float num5 = (num2 + color3.g) * level + color3.g;
		float num6 = (num3 + color3.b) * level + color3.b;
		if (num4 > 255f)
		{
			num4 = 255f;
		}
		if (num4 < 0f)
		{
			num4 = 0f;
		}
		if (num5 > 255f)
		{
			num5 = 255f;
		}
		if (num5 < 0f)
		{
			num5 = 0f;
		}
		if (num6 < 0f)
		{
			num6 = 0f;
		}
		if (num6 > 255f)
		{
			num6 = 255f;
		}
		return (int)num6 & (255 + ((int)num5 << 8)) & (255 + ((int)num4 << 16)) & 0xFF;
	}

	public static int getIntByColor(Color cl)
	{
		float num = cl.r * 255f;
		float num2 = cl.b * 255f;
		float num3 = cl.g * 255f;
		return (((int)num & 0xFF) << 16) | (((int)num3 & 0xFF) << 8) | ((int)num2 & 0xFF);
	}

	public static int getRealImageWidth(Image img)
	{
		return img.w;
	}

	public static int getRealImageHeight(Image img)
	{
		return img.h;
	}

	public void fillArg(int i, int j, int k, int l, int m, int n)
	{
		fillRect(i * zoomLevel, j * zoomLevel, k * zoomLevel, l * zoomLevel, isA: false);
	}

	[Obsolete]
	public void CreateLineMaterial()
	{
		if (!lineMaterial)
		{
			try
			{
				lineMaterial = new Material(Shader.Find("Lines/Colored Blended"));
				lineMaterial.hideFlags = HideFlags.HideAndDontSave;
				lineMaterial.shader.hideFlags = HideFlags.HideAndDontSave;
			}
			catch (Exception)
			{
			}
		}
	}

	public void drawlineGL(mVector totalLine)
	{
		lineMaterial.SetPass(0);
		GL.PushMatrix();
		GL.Begin(1);
		for (int i = 0; i < totalLine.size(); i++)
		{
			mLine mLine2 = (mLine)totalLine.elementAt(i);
			GL.Color(new Color(mLine2.r, mLine2.g, mLine2.b, mLine2.a));
			int num = mLine2.x1 * zoomLevel;
			int num2 = mLine2.y1 * zoomLevel;
			int num3 = mLine2.x2 * zoomLevel;
			int num4 = mLine2.y2 * zoomLevel;
			if (isTranslate)
			{
				num += translateX;
				num2 += translateY;
				num3 += translateX;
				num4 += translateY;
			}
			for (int j = 0; j < zoomLevel; j++)
			{
				GL.Vertex(new Vector2(num + j, num2 + j));
				GL.Vertex(new Vector2(num3 + j, num4 + j));
				if (j > 0)
				{
					GL.Vertex(new Vector2(num + j, num2));
					GL.Vertex(new Vector2(num3 + j, num4));
					GL.Vertex(new Vector2(num, num2 + j));
					GL.Vertex(new Vector2(num3, num4 + j));
				}
			}
		}
		GL.End();
		GL.PopMatrix();
		totalLine.removeAllElements();
	}

	public void drawLine(mGraphics g, int x, int y, int xTo, int yTo, int nLine, int color)
	{
		mVector mVector2 = new mVector();
		for (int i = 0; i < nLine; i++)
		{
			mVector2.addElement(new mLine(x, y, xTo + i, yTo + i, color));
		}
		g.drawlineGL(mVector2);
	}

	public void saveCanvas()
	{
	}

	public void ClipRec(int x, int i, int win, int hcur)
	{
	}

	public static void resetTransAndroid(mGraphics g2)
	{
	}

	public void restoreCanvas()
	{
	}

	public void fillRecAlpla(int x, int y, int w, int h, int color)
	{
		drawRecAlpa(0, 0, GameCanvas.loadmap.mapW * 24, y, color);
		drawRecAlpa(0, y, x, GameCanvas.loadmap.mapH * 24 - y, color);
		drawRecAlpa(x, y + h, GameCanvas.loadmap.mapW * 24 - x, GameCanvas.loadmap.mapH * 24 - (y + h), color);
		drawRecAlpa(x + w, y, GameCanvas.loadmap.mapW * 24 - (x + w), h, color);
		int num = 100;
		drawRecAlpa(0, -num, GameCanvas.loadmap.mapW * 24, num, color);
	}

	public void drawRecAlpa(int x, int y, int w, int h, int color)
	{
		float alpha = 0.5f;
		setColor(color, alpha);
		fillRect(x, y, w, h, isA: false);
	}
}
