package com.wfx.autorunner.common;

import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BinaryFileWriter {

    private static final String TAG = "BinaryFileManager";

    private FileOutputStream fileOutputStream;

    /**
     * BinaryFileWriter constructor <br>
     * Use "dir + fileName" as the full path, so control the separator yourself.
     * @param dir file dir
     * @param fileName file name
     */
    public BinaryFileWriter(String dir, String fileName) {
        try {
            Log.i(TAG, "File Path:" + dir + fileName);
            File dirFile = new File(dir);
            if (dirFile.exists() || dirFile.mkdirs()) {

                File file = new File(dir + fileName);
                if (file.exists()) {
                    file.delete();
                }
                if (file.exists() || file.createNewFile()) {
                    fileOutputStream = new FileOutputStream(dir + fileName);
                }

            }
        }
        catch (IOException e) {
            Log.i(TAG, e.getMessage());
        }
    }

    /**
     * Write one byte[]
     * @param bytes byte[]
     */
    public void write(byte[] bytes) {
        try {
            fileOutputStream.write(bytes);
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    }

    /**
     * Write a part of the byte[]
     * @param bytes byte[]
     * @param offset offset
     * @param count count
     */
    public void write(byte[] bytes, int offset, int count) {
        try {
            fileOutputStream.write(bytes, offset, count);
        }
        catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

    /**
     * Close resource and write complete, <br>
     * Do not use the writer after call this method.
     */
    public void complete() {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            catch (IOException e) {
                Log.i(TAG, e.getMessage());
            }
            finally {
                fileOutputStream = null;
            }
        }
    }
}
