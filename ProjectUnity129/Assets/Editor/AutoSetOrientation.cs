using UnityEditor;
using UnityEditor.Build;
using UnityEditor.Build.Reporting;
using UnityEngine;

[InitializeOnLoad]
public class AutoSetOrientation : IPreprocessBuildWithReport
{
    static AutoSetOrientation()
    {
        SetOrientationSettings();
    }

    public int callbackOrder => 0;

    public void OnPreprocessBuild(BuildReport report)
    {
        SetOrientationSettings();
    }

    private static void SetOrientationSettings()
    {

        // Áp dụng cho cả Android và iOS
        PlayerSettings.defaultInterfaceOrientation = UIOrientation.LandscapeLeft;
        PlayerSettings.allowedAutorotateToPortrait = true;
        PlayerSettings.allowedAutorotateToPortraitUpsideDown = false;
        PlayerSettings.allowedAutorotateToLandscapeLeft = true;
        PlayerSettings.allowedAutorotateToLandscapeRight = true;

#if UNITY_IOS
        // Một số version Unity cần chỉnh thêm ở iOS
        PlayerSettings.iOS.showActivityIndicatorOnLoading = iOSShowActivityIndicatorOnLoading.DontShow;
#endif
        Debug.Log("🔄 Setting default orientation to Landscape (Android & iOS)");

    }
}
