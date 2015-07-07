
package com.hinj.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.hinj.app.bean.FileInfo;

public class FileUtils {
    private static final String TAG = "FileUtils";
    public static final int COPY_FILE_RESULT = 0;
    public static final int MOVE_FILE_RESULT = 1;

    /*
     * public static final int COPY_FILE_EXCEPTION = 2; public static final int
     * MOVE_FILE_EXCEPTION = 3;
     */

    public static ArrayList<FileInfo> GetPathFiles(String path) {
        File[] files = new File(path).listFiles(new SolarexFilter());
        return sortAndGenerate(files);
    }

    public static ArrayList<FileInfo> sortAndGenerate(File[] files) {
        assert (files != null);
        Log.v(TAG, "files = " + files);
        List<File> fileList = null;
        try {
            fileList = Arrays.asList(files);
        } catch (Exception e) {
            Log.v(TAG, "Exception happened, ex = " + e.getMessage());
        }

        Collections.sort(fileList, new FileComparator());
        ArrayList<FileInfo> allFileInfos = new ArrayList<FileInfo>();
        for (File file : fileList) {
            FileInfo info = new FileInfo(file, false);
            allFileInfos.add(info);
        }
        return allFileInfos;
    }

    public static void PrintFileInfos(ArrayList<FileInfo> fileInfos) {
        for (FileInfo fileInfo : fileInfos) {
            Log.v(TAG, "file = " + fileInfo);
        }
    }

    public static String makePath(String path, String name) {
        if (path.endsWith("/")) {
            return path + name;
        }
        return path + "/" + name;
    }

    public static boolean CreateFolder(String dest, String name) {
        String newFolderName = makePath(dest, name);
        File file = new File(newFolderName);
        if (file.exists()) {
            return false;
        }
        return file.mkdir();
    }

    public static boolean MoveFile(FileInfo fileInfo, String dest) {
        if (fileInfo == null || dest == null) {
            Log.v(TAG, "MoveFile: null parameter");
            return false;
        }
        File file = fileInfo.getFile();
        String newPath = makePath(dest, file.getName());
        try {
            boolean isSuccess = file.renameTo(new File(newPath));
            Log.v(TAG, "file = " + file.getAbsolutePath() + " newFile = " + newPath
                    + " isSuccess = " + isSuccess);
            return isSuccess;
        } catch (Exception e) {
            Log.v(TAG,
                    "MoveFile: failed to move " + file.getAbsolutePath() + " exception = "
                            + e.getMessage());
        }
        return false;
    }

    /*
     * public static void StartCopyFile(HashSet<FileInfo> fileInfos, String
     * dest, Handler handler){ new CopyFileThread(fileInfos, dest,
     * handler).start(); }
     */
    public static void CopyFile(FileInfo fileInfo, String dest) {
        if (fileInfo == null || dest == null) {
            Log.e(TAG, "CopyFile: null parameter");
        }
        File file = fileInfo.getFile();
        Log.v(TAG, "CopyFile file = " + file.getName() + " isDir = " + file.isDirectory());
        if (file.isDirectory()) {
            String destPath = makePath(dest, file.getName());
            File destFile = new File(destPath);
            int i = 0;
            while (destFile.exists()) {
                destPath = makePath(dest, file.getName() + " " + i);
                destFile = new File(destPath);
                i++;
            }
            for (File fileInDirectory : file.listFiles(new SolarexFilter())) {
                FileInfo tmpFileInfo = new FileInfo(fileInDirectory, false);
                CopyFile(tmpFileInfo, destPath);
            }
        } else {
            String destFilePath = copyRawFile(file, dest);
            /*
             * Message msg = Message.obtain(); msg.what = COPY_FILE_RESULT; if
             * (null == destFilePath) { msg.obj = "Copy raw file " +
             * file.getAbsolutePath() + " failed"; } else { msg.obj =
             * "Copy raw file success, new file is " + destFilePath; }
             * handler.sendMessage(msg);
             */
        }
    }

    public static String copyRawFile(File file, String dest) {
        if (!file.exists() || file.isDirectory()) {
            Log.v(TAG, "copyRawFile: file dont exists or file is directory");
            return null;
        }

        File destDir = new File(dest);
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                return null;
            }
        }

        String destFileString = makePath(dest, file.getName());
        File destFile = new File(destFileString);
        int i = 0;
        while (destFile.exists()) {
            destFileString = makePath(dest, getFileNameFromFile(file) + " " + i
                    + getFileExtFromFile(file));
            destFile = new File(destFileString);
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            if (!destFile.createNewFile()) {
                return null;
            }

            fis = new FileInputStream(file);
            fos = new FileOutputStream(destFile);

            int count = 10240;
            byte[] buffer = new byte[count];
            int len = 0;

            while ((len = fis.read(buffer, 0, count)) != -1) {
                fos.write(buffer, 0, len);
            }

            return destFileString;
        } catch (FileNotFoundException e) {
            /*
             * Message msg = Message.obtain(); msg.what = COPY_FILE_EXCEPTION;
             * msg.obj = e.getMessage(); handler.sendMessage(msg);
             */
            Log.e(TAG, "copyRawFile exception happened, ex = " + e.getMessage());
        } catch (IOException e) {
            /*
             * Message msg = Message.obtain(); msg.what = COPY_FILE_EXCEPTION;
             * msg.obj = e.getMessage(); handler.sendMessage(msg);
             */
            Log.e(TAG, "copyRawFile exception happened, ex = " + e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "copyRawFile exception happened, ex = " + e.getMessage());
            }
        }

        return null;
    }

    public static String getFileNameFromFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        int index = file.getName().lastIndexOf(".");
        if (index != -1) {
            return file.getName().substring(0, index);
        } else {
            return file.getName();
        }
    }

    public static String getFileExtFromFile(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        int index = file.getName().lastIndexOf(".");
        if (index != -1) {
            return file.getName().substring(index, file.getName().length());
        } else {
            return "";
        }
    }
    
    public static boolean DeleteFiles(FileInfo fileInfo){
        if (fileInfo == null) {
            return false;
        }
        File file = fileInfo.getFile();
        if (!file.exists()) {
            return false;
        }
        
        if (file.isDirectory()) {
            for (File file2 : file.listFiles()) {
                DeleteFiles(new FileInfo(file2, false));
            }
        }
        
        boolean isSuccess = file.delete();
        Log.v(TAG, "file = " + file.getAbsolutePath() + " delete isSuccess = " + isSuccess);
        return isSuccess;
    }

    /*
     * public static class CopyFileThread extends Thread{ private
     * HashSet<FileInfo> fileInfos; private String dest; private Handler
     * handler; public CopyFileThread(HashSet<FileInfo> fileInfos, String dest,
     * Handler handler){ this.fileInfos = fileInfos; this.dest = dest;
     * this.handler = handler; }
     * @Override public void run() { Log.v(TAG, "copyfilethread = " +
     * this.getId() + " dest = " + dest); for (FileInfo fileInfo : fileInfos) {
     * CopyFile(fileInfo, dest, handler); } } }
     */
}
