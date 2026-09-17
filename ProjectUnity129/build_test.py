import os
import subprocess

project_dir = r"c:\DepLor\HTTH\Team\ProjectUnity129"
assets_csharp = os.path.join(project_dir, "Assets", "Scripts", "Assembly-CSharp")

files = []
for r, d, fs in os.walk(assets_csharp):
    for f in fs:
        if f.endswith(".cs"):
            files.append(os.path.join(r, f))

files.sort()
print(f"Total C# files found: {len(files)}")

sources_rsp = os.path.join(project_dir, "sources.rsp")
with open(sources_rsp, "w", encoding="utf-8") as f:
    for path in files:
        f.write(f'"{path}"\n')

print(f"Wrote {len(files)} files to {sources_rsp}")

# Run csc
csc_path = r"C:\Program Files\Unity\Hub\Editor\2022.3.62f3-x86_64\Editor\Data\DotNetSdkRoslyn\csc.dll"
rsp_path = os.path.join(project_dir, "build_args.rsp")

out_dir = os.path.join(project_dir, "Library", "ScriptAssemblies")
os.makedirs(out_dir, exist_ok=True)

cmd = ["dotnet", csc_path, f"@{rsp_path}"]
print("Running command:", " ".join(cmd))
res = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8", errors="replace")

print(f"Return code: {res.returncode}")
err_file = os.path.join(project_dir, "all_errors.txt")
with open(err_file, "w", encoding="utf-8") as ef:
    ef.write(res.stdout + "\n" + res.stderr)
print(f"Saved output to {err_file}")
lines = res.stdout.strip().split("\n")
err_lines = [l for l in lines if ": error CS" in l]
print(f"Total error lines: {len(err_lines)}")
for el in err_lines[:25]:
    print(el)

