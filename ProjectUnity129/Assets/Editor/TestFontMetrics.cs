using UnityEditor;
using UnityEngine;

public class TestFontMetrics
{
    [MenuItem("Test/FontMetrics")]
    public static void Run()
    {
        Debug.Log("=== TEST FONT METRICS START ===");
        for (int zoom = 1; zoom <= 4; zoom++)
        {
            mGraphics.zoomLevel = zoom;
            mFont.loadmFont();
            GUIStyle style = mFont.tahoma_7_white.getCachedStyle(0);
            Vector2 size = style.CalcSize(new GUIContent("Adg,y%"));
            int fontH = mFont.tahoma_7_white.getHeight();
            Debug.Log(string.Format("ZOOM {0}: style.fontSize={1}, style.padding=({2},{3},{4},{5}), CalcSize.y={6}, getHeight()={7}, yAddFont={8}",
                zoom, style.fontSize, style.padding.left, style.padding.right, style.padding.top, style.padding.bottom, size.y, fontH, mFont.yAddFont));
        }
        Debug.Log("=== TEST FONT METRICS END ===");
    }
}
