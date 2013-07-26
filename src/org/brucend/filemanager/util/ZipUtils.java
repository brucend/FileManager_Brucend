package org.brucend.filemanager.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * ���ܣ� 1 ��ʵ�ְ�ָ���ļ����µ������ļ�ѹ��Ϊָ���ļ�����ָ�� zip �ļ� 2 ��ʵ�ְ�ָ���ļ����µ� zip �ļ���ѹ��ָ��Ŀ¼��
 */

public class ZipUtils {

	public boolean zipTerminal = false;
	public ZipUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public void terminal() { zipTerminal = true;}
	public boolean isTerminal() { return zipTerminal;}
	/**
	 * ���ܣ��� sourceDir Ŀ¼�µ������ļ����� zip ��ʽ��ѹ��������Ϊָ�� zip �ļ�
	 * 
	 * @param sourceDir
	 * @param zipFile
	 */
	public void zip(String sourceDir, String zipFile) {

		OutputStream os;
		zipTerminal = false;
		try {

			os = new FileOutputStream(zipFile);

			BufferedOutputStream bos = new BufferedOutputStream(os);

			ZipOutputStream zos = new ZipOutputStream(bos);

			File file = new File(sourceDir);

			String basePath = null;

			if (file.isDirectory()) {

				basePath = file.getPath();

			} else {// ֱ��ѹ�������ļ�ʱ��ȡ��Ŀ¼

				basePath = file.getParent();

			}

			//zos.setEncoding("gbk");

			zipFile(file, basePath, zos);
			if (zipTerminal) {
				file = new File(zipFile);
				if (file.exists())
					file.delete();
			}
				
			zos.closeEntry();

			zos.close();
			bos.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public void zipMult(String basePath, ArrayList<String> sourceDir,
			String zipFile) {

		OutputStream os;
		zipTerminal = false;
		try {

			os = new FileOutputStream(zipFile);

			BufferedOutputStream bos = new BufferedOutputStream(os);

			ZipOutputStream zos = new ZipOutputStream(bos);

			// File file = new File(sourceDir);

			int length = sourceDir.size();
			File file = null;
			String pathName = null;
			for (int i = 0; i < length; i++) {
				file = new File(sourceDir.get(i));
				if (file.isDirectory()) {
					pathName = file.getPath().substring(basePath.length() + 1)
							+ "/";
					zos.putNextEntry(new ZipEntry(pathName));
				}
				zipFile(file, basePath, zos);
				if (zipTerminal)
					break;
			}

			zos.closeEntry();

			zos.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		if (zipTerminal) {
			File file = new File(zipFile);
			if (file.exists())
				file.delete();
		}
	}

	/**
	 * ���ܣ�ִ���ļ�ѹ����zip�ļ�
	 * 
	 * @param source
	 * @param basePath
	 *            ��ѹ���ļ���Ŀ¼
	 * @param zos
	 */

	private void zipFile(File source, String basePath,

						ZipOutputStream zos) {

		File[] files = new File[0];

		if (source.isDirectory()) {

			files = source.listFiles();

		} else {

			files = new File[1];

			files[0] = source;

		}

		String pathName;// �����·��(����ڴ�ѹ���ĸ�Ŀ¼)

		byte[] buf = new byte[1024];

		int length = 0;

		try {

			for (File file : files) {

				if (file.isDirectory()) {

					pathName = file.getPath().substring(basePath.length() + 1)

					+ "/";
					ZipEntry ze = new ZipEntry(pathName);
					zos.putNextEntry(ze);

					zipFile(file, basePath, zos);

				} else {

					pathName = file.getPath().substring(basePath.length() + 1);

					InputStream is = new FileInputStream(file);

					BufferedInputStream bis = new BufferedInputStream(is);

					zos.putNextEntry(new ZipEntry(pathName));

					while ((length = bis.read(buf)) > 0 && (!zipTerminal)) {

						zos.write(buf, 0, length);

					}

					is.close();

				}
				if (zipTerminal)
					return;

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	/**
	 * ���ܣ���ѹ zip �ļ���ֻ�ܽ�ѹ zip �ļ�
	 * 
	 * @param zipfile
	 * @param destDir
	 */

	public void unZip(String zipfile, String destDir) {

		destDir = destDir.endsWith("\\") ? destDir : destDir + "\\";

		byte b[] = new byte[1024];

		int length;

		ZipFile zipFile;
		zipTerminal = false;
		try {

			zipFile = new ZipFile(new File(zipfile));

			@SuppressWarnings("rawtypes")
			Enumeration enumeration = zipFile.getEntries();

			ZipEntry zipEntry = null;

			while (enumeration.hasMoreElements()) {

				zipEntry = (ZipEntry) enumeration.nextElement();
				//String name = destDir + new String(zipEntry.getName().getBytes("gbk"), "utf-8");
				//File loadFile = new File(name);
				File loadFile = new File(destDir + zipEntry.getName());
				if (zipEntry.isDirectory()) {

					loadFile.mkdirs();

				} else {

					if (!loadFile.getParentFile().exists()) {

						loadFile.getParentFile().mkdirs();

					}

					OutputStream outputStream = new FileOutputStream(loadFile);

					InputStream inputStream = zipFile.getInputStream(zipEntry);

					while ((length = inputStream.read(b)) > 0 && (!zipTerminal))
						outputStream.write(b, 0, length);

				}
				if (zipTerminal)
					break;

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
//yy1221000415
}
