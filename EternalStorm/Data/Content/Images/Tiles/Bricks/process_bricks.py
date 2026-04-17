import os
from PIL import Image
import shutil

def process_bricks(folder_path):
    # 1. Create backup folder
    backup_path = os.path.join(folder_path, "Backup_Originals")
    if not os.path.exists(backup_path):
        os.makedirs(backup_path)
    
    # 2. Process each PNG
    for filename in os.listdir(folder_path):
        if filename.endswith(".png") and not filename.startswith("processed_"):
            file_path = os.path.join(folder_path, filename)
            
            # Backup original
            shutil.copy(file_path, os.path.join(backup_path, filename))
            
            # Open, resize and save
            img = Image.open(file_path)
            # Use LANCZOS for best high-quality downscaling
            img_resized = img.resize((64, 64), Image.Resampling.LANCZOS)
            img_resized.save(file_path)
            
            print(f"Processed and resized {filename} to 64x64.")

if __name__ == "__main__":
    folder = r"C:\Users\Alexey\Desktop\Eternal-Storm\EternalStorm\Data\Content\Images\Tiles\Bricks"
    process_bricks(folder)
