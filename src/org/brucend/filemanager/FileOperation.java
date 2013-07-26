package org.brucend.filemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作类
 */
public class FileOperation {
	
	/**
	 * 复制文件
	 */
	public static void copyFile(String oldPath, String newPath)
			throws IOException {
		int bytesum = 0;
		int byteread = 0;
		File oldfile = new File(oldPath);
		if (oldfile.exists()) {
			InputStream inStream = new FileInputStream(oldPath); // 读入原文件
			FileOutputStream fs = new FileOutputStream(newPath);
			byte[] buffer = new byte[4096];
			// int length;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread; // 字节数 文件大小
				fs.write(buffer, 0, byteread);
			}
			inStream.close();
		}
	}

	/**
	 * 递归获取文件夹里所有文件的总大小
	 * <br><b>BUG</b>: 不能分辨链接文件
	 */
	public static long getDirectorySize(File f) throws IOException {
		long size = 0;
		File flist[] = f.listFiles();
		if (flist == null)
			return f.length();
		int length = flist.length;
		for (int i = 0; i < length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getDirectorySize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static long getDirectorySize(String fp) throws IOException {
		return getDirectorySize(new File(fp));
	}
}
