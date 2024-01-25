SOFTWARE GROUP INTERVIEW TASK
![ic_launcher_foreground](https://github.com/GopikrishnanN/prng_imageupload/assets/58000918/96a852b3-7f2b-4542-ba7a-2c447d52ed73)

# Image Upload Component

The Image Upload Component is a custom Android view that allows users to select an image file, preview it, display the file type, and simulate the image upload process.

![New Project (5)](https://github.com/GopikrishnanN/prng_imageupload/assets/58000918/cb1627ff-6164-43c8-871c-dd3f9543b40f)

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

# Manual Dependency Injection with Retrofit, LiveData, and okhttp3 (Optional)

This guide explains how to set up a manual dependency injection in an Android project using Kotlin, Retrofit for network requests, LiveData for observing data changes, and okhttp3 for HTTP client functionality. The example showcases the use of `lazy` initialization to ensure efficient resource utilization.

## Setup

### Dependencies

Ensure that the following dependencies are added to your project:

```gradle
// Retrofit
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp
implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.0"))
implementation("com.squareup.okhttp3:okhttp")
implementation("com.squareup.okhttp3:logging-interceptor")

// LiveData
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
```

### Customization
The Image Upload Component can be customized based on your requirements. You can modify the component's appearance, button texts, or behavior by adjusting the layout XML or the `ImageUploadComponent` class.

### Notes
Ensure that you have the necessary permissions in your AndroidManifest.xml for accessing the camera and external storage, especially if you plan to capture images.

For a production-ready solution, consider additional features such as image compression, loading indicators, and proper error handling.
