import os
import subprocess

real_dir = r"c:\DepLor\HTTH\TeamReal\ProjectUnity129"
real_csharp = os.path.join(real_dir, "Assets", "Scripts", "Assembly-CSharp")

files = []
for r, d, fs in os.walk(real_csharp):
    for f in fs:
        if f.endswith(".cs"):
            files.append(os.path.join(r, f))

print("Total files in TeamReal:", len(files))
rsp_path = os.path.join(real_dir, "sources.rsp")
with open(rsp_path, "w", encoding="utf-8") as f:
    for fp in files:
        f.write(f'"{fp}"\n')

build_rsp = os.path.join(real_dir, "build_args.rsp")
with open(r"c:\DepLor\HTTH\Team\ProjectUnity129\build_args.rsp", "r", encoding="utf-8") as f:
    args = f.read().replace(r"c:\DepLor\HTTH\Team\ProjectUnity129", real_dir)
with open(build_rsp, "w", encoding="utf-8") as f:
    f.write(args)

cmd = ["dotnet", r"C:\Program Files\Unity\Hub\Editor\2022.3.62f3-x86_64\Editor\Data\DotNetSdkRoslyn\csc.dll", f"@{build_rsp}"]
res = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8", errors="replace")
print("TeamReal return code:", res.returncode)
errors = [l for l in res.stdout.splitlines() if "error CS" in l]
print("TeamReal error count:", len(errors))
for e in errors[:20]:
    print("  ", e)
