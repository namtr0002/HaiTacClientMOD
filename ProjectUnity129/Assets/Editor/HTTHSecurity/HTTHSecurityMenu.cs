using System.IO;
using UnityEditor;
using UnityEngine;

namespace HTTHSecurity
{
    public class HTTHSecurityMenu : EditorWindow
    {
        private bool _hideAllLogic = true;
        private bool _injectFakeLogic = true;
        private bool _encryptStrings = true;
        private string _targetDll = "Temp/bin/Release/Assembly-CSharp.dll";
        private string _outputDll = "Temp/bin/Release/Assembly-CSharp-Protected.dll";

        [MenuItem("Tools/HTTH Security/Ultimate Logic Hider & Obfuscator")]
        public static void ShowWindow()
        {
            GetWindow<HTTHSecurityMenu>("HTTH Security Ultimate");
        }

        private void OnGUI()
        {
            GUILayout.Label("HTTH ULTIMATE ANTI-REVERSE & LOGIC HIDER", EditorStyles.boldLabel);
            EditorGUILayout.Space();

            _hideAllLogic = EditorGUILayout.Toggle("Ẩn Sạch Logic (Empty Stubs)", _hideAllLogic);
            _injectFakeLogic = EditorGUILayout.Toggle("Bơm Logic Giả / Fake Code", _injectFakeLogic);
            _encryptStrings = EditorGUILayout.Toggle("Mã Hóa String Byte/Hex XOR", _encryptStrings);

            EditorGUILayout.Space();
            _targetDll = EditorGUILayout.TextField("Input Assembly DLL:", _targetDll);
            _outputDll = EditorGUILayout.TextField("Output Protected DLL:", _outputDll);

            EditorGUILayout.Space();
            if (GUILayout.Button("Thực Hiện Bảo Vệ & Ẩn Logic Ngay", GUILayout.Height(35)))
            {
                if (File.Exists(_targetDll))
                {
                    HTTHLogicHider.ProcessAssembly(_targetDll, _outputDll, _hideAllLogic, _injectFakeLogic, _encryptStrings);
                    EditorUtility.DisplayDialog("HTTH Security", "Đã bảo vệ và ẩn toàn bộ logic thành công!\nFile lưu tại: " + _outputDll, "OK");
                }
                else
                {
                    EditorUtility.DisplayDialog("Lỗi", "Không tìm thấy file DLL: " + _targetDll + "\nVui lòng Build trước hoặc chọn đúng đường dẫn DLL.", "OK");
                }
            }
        }
    }
}
