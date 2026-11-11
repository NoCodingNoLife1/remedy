from PIL import Image

# Open the JPEG image
img = Image.open('src/main/resources/com/mlms/images/logo.jpg')

# Resize to icon size if needed (e.g., 256x256 for a nice Windows icon)
# img = img.resize((256, 256), Image.Resampling.LANCZOS)
# Windows .ico files can contain multiple sizes, but a single size like 256x256 works perfectly.

# Save it as an .ico file
img.save('logo.ico', format='ICO', sizes=[(256, 256)])
print("Successfully created logo.ico")
