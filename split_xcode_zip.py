import os
import sys

def split_file(input_file, output_dir, chunk_size=45 * 1024 * 1024):
    if not os.path.exists(input_file):
        print(f"Error: {input_file} does not exist!")
        return False
    
    os.makedirs(output_dir, exist_ok=True)
    # Clear old parts in output_dir
    for f in os.listdir(output_dir):
        if f.startswith(os.path.basename(input_file) + ".part") or f.endswith(".part") or "part" in f:
            try:
                os.remove(os.path.join(output_dir, f))
            except Exception as e:
                print(f"Warning: could not remove old part {f}: {e}")
                
    base_name = os.path.basename(input_file)
    total_size = os.path.getsize(input_file)
    print(f"Splitting {input_file} ({total_size / (1024*1024):.2f} MB) into chunks of {chunk_size / (1024*1024):.2f} MB...")
    
    part_num = 1
    with open(input_file, 'rb') as f:
        while True:
            chunk = f.read(chunk_size)
            if not chunk:
                break
            part_name = f"{base_name}.part{part_num:02d}"
            part_path = os.path.join(output_dir, part_name)
            with open(part_path, 'wb') as pf:
                pf.write(chunk)
            print(f"  Created: {part_name} ({len(chunk) / (1024*1024):.2f} MB)")
            part_num += 1
            
    print(f"Successfully split into {part_num - 1} parts in {output_dir}!")
    return True

if __name__ == "__main__":
    src = sys.argv[1] if len(sys.argv) > 1 else "ProjectUnity129/Builds/XCodeHaiTacZ.zip"
    dst = sys.argv[2] if len(sys.argv) > 2 else "ios_parts"
    split_file(src, dst)
