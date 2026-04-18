from PIL import Image
import sys
import os

def process_sprite(input_path, output_path):
    img = Image.open(input_path).convert("RGBA")
    
    # Simple background removal (assuming white background)
    data = img.getdata()
    new_data = []
    for item in data:
        # Change whiteish to transparent
        if item[0] > 240 and item[1] > 240 and item[2] > 240:
            new_data.append((255, 255, 255, 0))
        else:
            new_data.append(item)
    img.putdata(new_data)
    
    # Resize and crop to 128x64 if necessary
    # (The AI prompt asked for 128x64 aspect ratio, but let's force it)
    final_img = img.resize((128, 64), Image.Resampling.LANCZOS)
    
    final_img.save(output_path)
    print(f"Processed image saved to {output_path}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python process_workbench.py <input> <output>")
    else:
        process_sprite(sys.argv[1], sys.argv[2])
