package com.prng.androidimageuploadcomponent.util;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;

import okhttp3.MediaType;

public final class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static File saveAttachment(Context context, byte[] response, String url) throws IOException {
        File file = null;
        if (response != null) {
            String fileName = FileUtils.getFileNameWithExtension(StringUtils.decodeSpace(url));
            file = FileUtils.getFileDirectory(context, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(response);
            fos.close();
        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static File getFileDirectory(Context context, String fileName) {
        // Get the directory for the app's private pictures directory.
        File file = null;
        if (isExternalStorageReadable() && isExternalStorageWritable()) {
            File rootDir = getRootDir();
            file = new File(rootDir.getAbsolutePath(), fileName);
        } else {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
        }
        return file;
    }

    @NonNull
    public static File getRootDir() {
        File rootDir = new File(Environment.getExternalStorageDirectory(), ".AscenteqStarRate");
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }
        return rootDir;
    }

    public static String getFileNameWithExtension(String url) {
        String fileName = "";
        if (!TextUtils.isEmpty(url)) {
            String mediaUrl[] = url.split("/");
            fileName = mediaUrl[mediaUrl.length - 1];
        }
        return fileName;
    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static void purgeRootDirectory(Context context) {
        if (checkWriteExternalPermission(context)) {
            File rootDir = new File(Environment.getExternalStorageDirectory(), ".AscenteqStarRate");
            purgeDirectory(rootDir);
        }
    }

    private static void purgeDirectory(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
    }

    private static boolean checkWriteExternalPermission(Context context) {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private static void purgeDirectoryButKeepSubDirectories(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }

        for (File file : dir.listFiles()) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        if (context == null || uri == null) {
            return null;
        }
        if (true)
            Log.d(TAG + " File -",
                    "Authority: " + uri.getAuthority() +
                            ", Fragment: " + uri.getFragment() +
                            ", Port: " + uri.getPort() +
                            ", Query: " + uri.getQuery() +
                            ", Scheme: " + uri.getScheme() +
                            ", Host: " + uri.getHost() +
                            ", Segments: " + uri.getPathSegments().toString()
            );

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                return DocumentsContract.getDocumentId(uri);
            }
            // ExternalStorageProvider
            else if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isLocalStorageDocument(Uri uri) {
        return ".AscenteqStarRate".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isImageFile(Context context, Uri uri) {
        String mimeType = URLConnection.guessContentTypeFromName(getPathFromUri(context, uri));
        return mimeType != null && mimeType.startsWith("image");
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                if (true)
                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static long getFileSize(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            return 0L;
        }
    }

    public static MediaType getContentType(String imageUrl) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(imageUrl);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                extension);
        if (mimeType == null) {
            return null;
        }
        return MediaType.parse(mimeType);
    }

    public static File saveBitmap(Context context, Bitmap bitmap) {
        OutputStream outStream = null;

        String filename = "" + System.currentTimeMillis() + ".jpg";
        File file = new File(context.getExternalCacheDir(), filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            // byteStream = new ByteArrayOutputStream();
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outStream);
            // bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);

            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String mimeType = context.getContentResolver().getType(Uri.fromFile(file));

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), opt);

        return file;
    }
}
