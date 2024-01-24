SOFTWARE GROUP INTERVIEW TASK

# Image Upload Component

The Image Upload Component is a custom Android view that allows users to select an image file, preview it, display the file type, and simulate the image upload process.

## Integration

### 1. Download the Source Code
- Download the `prngimageuploadcomponent` source code from the provided location.

### 2. Copy the Component
- Copy the `ImageUploadComponent` class and the associated layout XML file (`image_upload_component.xml`) into your Android project.

### 3. Add the Component to Your Layout
```xml
<com.example.yourpackage.ImageUploadComponent
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```xml

Functionality
The Image Upload Component provides the following functionality:

Select Image:

When the user taps on the component, an option to choose an image file from the device's storage or capture from the camera is provided.
Preview Image:

After selecting an image, pressing the "Preview" button displays a preview of the selected image.
Display File Information:

The component shows the name and file type (e.g., JPEG, PNG) of the selected image.
Submit Image:

The "Submit" button simulates the image upload process. You can customize this part to perform an actual image upload to a server.

Customization
The Image Upload Component can be customized based on your requirements. You can modify the component's appearance, button texts, or behavior by adjusting the layout XML or the ImageUploadComponent class.

Notes
Ensure that you have the necessary permissions in your AndroidManifest.xml for accessing the camera and external storage, especially if you plan to capture images.

For a production-ready solution, consider additional features such as image compression, loading indicators, and proper error handling.
