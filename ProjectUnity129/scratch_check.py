import clr
import System
from System.Reflection import Assembly
import glob

for f in glob.glob(r"C:\Program Files\Unity\Hub\Editor\2022.3.62f3\Editor\Data\Managed\UnityEngine\*.dll"):
    try:
        a = Assembly.LoadFrom(f)
        for t in a.GetTypes():
            for m in t.GetMethods():
                if m.Name == "DrawTexture":
                    print(f, t.FullName)
    except Exception as e:
        pass
