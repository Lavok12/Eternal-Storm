from PIL import Image
import sys
import os

def process_image(input_path, output_path, size_x, size_y):
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
    
    # Force resize
    final_img = img.resize((size_x, size_y), Image.Resampling.LANCZOS)
    
    final_img.save(output_path)
    print(f"Processed image saved to {output_path}")

if __name__ == "__main__":
    if len(sys.argv) < 5:
        print("Usage: python process_image.py <input> <output> <size_x> <size_y>")
    else:
        process_image(sys.argv[1], sys.argv[2], int(sys.argv[3]), int(sys.argv[4]))
