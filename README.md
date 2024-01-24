![ic_launcher_foreground](https://github.com/GopikrishnanN/prng_imageupload/assets/58000918/96a852b3-7f2b-4542-ba7a-2c447d52ed73)SOFTWARE GROUP INTERVIEW TASK

# Image Upload Component

The Image Upload Component is a custom Android view that allows users to select an image file, preview it, display the file type, and simulate the image upload process.

![screenshot_1706115612424](https://github.com/GopikrishnanN/prng_imageupload/assets/58000918/80e537c3-e6cf-4af4-b87b-79f1bd8d5252) ![screenshot_1706115624542](https://github.com/GopikrishnanN/prng_imageupload/assets/58000918/b2daf0be-401e-4981-8fe1-d1330cc38302)
![screenshot_1706115639514](https://github.com/GopikrishnanN/prng_imageupload/assets/58000918/9e22d11d-e85e-4566-b267-4d23f85199fa) ![screenshot_1706115875337](https://github.com/GopikrishnanN/prng_imageupload/assets/58000918/0ab0e9c6-a318-415c-973b-5587f4f94004)
![screenshot_1706115886926](https://github.com/GopikrishnanN/prng_imageupload/assets/58000918/d87575ee-9d64-4cf4-b56d-e5d81623635d)


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
```

### Functionality
The Image Upload Component provides the following functionality:

### Select Image:
When the user taps on the component, an option to choose an image file from the device's storage or capture from the camera is provided.

### Preview Image:
After selecting an image, pressing the "Zoom in" button displays a preview of the selected image.

### Display File Information:
The component shows the name and file type (e.g., JPEG, PNG) of the selected image.

### Submit Image:
The `Submit` button simulates the image upload process. You can get uri `onClickListener` this part to perform an actual image upload to a server or your wish.

```kt
binding.imageUploadComponent.onClickListener(object : OnClickListener {
   override fun onClickListener(uri: Uri) {
       uri.path?.let { File(it) }?.let {
           // Do Your action
       }
   }
})
```

### How to Plugin?

The `prngimageuploadcomponent` module import your project

use Gradle
```kts
implementation(project(":prngimageuploadcomponent"))
```

use setting gradle
```kts
include(":prngimageuploadcomponent")
```

### Customization
The Image Upload Component can be customized based on your requirements. You can modify the component's appearance, button texts, or behavior by adjusting the layout XML or the `ImageUploadComponent` class.

### Notes
Ensure that you have the necessary permissions in your AndroidManifest.xml for accessing the camera and external storage, especially if you plan to capture images.

For a production-ready solution, consider additional features such as image compression, loading indicators, and proper error handling.
