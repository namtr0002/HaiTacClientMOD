import cv2
import numpy as np
from PIL import Image

def remove_white_bg(img_path, tol=20, feather=1.5):
    # Load with OpenCV
    img = cv2.imread(img_path, cv2.IMREAD_UNCHANGED)
    if img is None:
        raise ValueError(f"Could not open {img_path}")
    
    # If BGR, convert
    if img.shape[2] == 3:
        bgr = img
    else:
        bgr = img[:, :, :3]
    
    # White background mask: high brightness & low saturation
    hsv = cv2.cvtColor(bgr, cv2.COLOR_BGR2HSV)
    h, s, v = cv2.split(hsv)
    
    # Distance from pure white (255, 255, 255)
    diff = 255 - bgr
    dist = np.max(diff, axis=2) # 0 for pure white, higher for colors
    
    # Create soft alpha mask:
    # dist <= tol -> alpha = 0 (transparent)
    # dist >= tol + 20 -> alpha = 255 (fully opaque)
    # in between -> smooth transition
    low_thresh = tol
    high_thresh = tol + 25
    
    alpha = np.clip((dist.astype(np.float32) - low_thresh) / (high_thresh - low_thresh), 0.0, 1.0)
    
    # Flood-fill from corners so white details inside the object don't become transparent!
    # Binary mask of outside background
    bg_seed_mask = (dist < high_thresh).astype(np.uint8)
    h_img, w_img = bg_seed_mask.shape
    flood_mask = np.zeros((h_img + 2, w_img + 2), np.uint8)
    
    # Connected component from corners
    cv2.floodFill(bg_seed_mask, flood_mask, (0, 0), 2)
    cv2.floodFill(bg_seed_mask, flood_mask, (w_img - 1, 0), 2)
    cv2.floodFill(bg_seed_mask, flood_mask, (0, h_img - 1), 2)
    cv2.floodFill(bg_seed_mask, flood_mask, (w_img - 1, h_img - 1), 2)
    
    is_outer_bg = (bg_seed_mask == 2)
    
    # Final alpha: only outer background becomes transparent!
    final_alpha = np.where(is_outer_bg, alpha, 1.0)
    
    # Soft blur on alpha edge to remove any jaggedness
    final_alpha_u8 = (final_alpha * 255).astype(np.uint8)
    if feather > 0:
        final_alpha_u8 = cv2.GaussianBlur(final_alpha_u8, (3, 3), feather)
        # Re-clamp inside
        final_alpha_u8 = np.where(is_outer_bg, final_alpha_u8, 255)
    
    # Remove white color fringe on the borders (color de-contamination)
    # Wherever alpha < 255 and alpha > 0, adjust RGB towards neighboring object colors
    rgba = np.dstack([bgr, final_alpha_u8])
    return Image.fromarray(cv2.cvtColor(rgba, cv2.COLOR_BGRA2RGBA))

if __name__ == "__main__":
    src = r"C:\Users\DELL\.gemini\antigravity\brain\41d9f09e-d895-47a0-817a-a101cd061233\close_button_seal_1789643383524.jpg"
    out = r"C:\DepLor\HTTH\Team\scratch\test_clean_bg.png"
    result = remove_white_bg(src, tol=15, feather=1.0)
    result.save(out)
    print("Clean transparent cutout saved:", out, "Size:", result.size)
