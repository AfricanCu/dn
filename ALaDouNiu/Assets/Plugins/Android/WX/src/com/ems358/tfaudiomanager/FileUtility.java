package com.ems358.tfaudiomanager;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by jenuce on 15/11/18.
 */
public class FileUtility {
    public static boolean existSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String getAppDataDir() {
        if (existSdcard()) {
            String dir = Environment.getExternalStorageDirectory().getPath();
            if (dir == null) {
                return null;
            }

            return new String(dir + "/TFAudioManager");
        } else {
            return null;
        }
    }
    public static String getLogCachePath(Context context) {
        if (existSdcard()) {
            return getAppDataDir() + "/log";
        } else {
            return context.getCacheDir() + "/log";
        }
    }
    public static String getVoiceCachePaht(Context context){
        if (existSdcard()) {
            return getAppDataDir() + "/voice";
        } else {
            return context.getCacheDir() + "/voice";
        }
    }
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0;files!=null && i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        return dirFile.delete();
    }

    public static boolean deleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(filePath);
            } else {
                return deleteDirectory(filePath);
            }
        }
    }
    public static boolean createFolders(String strFolder) {
        File file = new File(strFolder);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;

            }
        }
        return true;

    }
}
