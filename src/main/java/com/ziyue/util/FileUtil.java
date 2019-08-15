package com.ziyue.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
public class FileUtil {
	/**
	 * 文件移动
	 * @param oldFilepath  源文件及路径 如c:/a.doc
	 * @param newFilepath  要转移到的目录 如c:/test/a.doc
	 * @throws IOException
	 */
	public static void remove(String oldFilepath, String newFilepath)
			throws IOException {
		// 文件原地址
		File oldFile = new File(oldFilepath);
		File fnew = new File(newFilepath);
		oldFile.renameTo(fnew);
	}

	/**
	 * 删除文件，可以是文件或文件夹
	 * @param fileName 要删除的文件名
	 * @return 删除成功返回true，否则返回false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return false;
		} else {
			if (file.isFile())
				return deleteFile(fileName);
			else
				return deleteDirectory(fileName);
		}
	}

	/**
	 * 删除单个文件
	 * @param fileName  要删除的文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 删除目录及目录下的文件
	 * @param dir  要删除的目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			// System.out.println("删除目录失败：" + dir + "不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			// System.out.println("删除目录失败！");
			return false;
		}
		// 删除当前目录
		if (dirFile.delete()) {
			// System.out.println("删除目录" + dir + "成功！");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 清空目录下的文件，但保留目录
	 * @param String dir
	 * @return
	 */
	public static boolean emptyDirFile(String dir){
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
			// System.out.println("删除目录失败：" + dir + "不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹中的所有文件包括子目录
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
			// 删除子目录
			else if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			// System.out.println("删除目录失败！");
			return false;
		}
		return true;
	}
	
	
	/**
	 * 创建文件
	 * @throws IOException
	 */
	public static boolean creatTxtFile(String filePath) throws IOException {
		boolean flag = false;
		File filename = new File(filePath);
		if (!filename.exists()) {
			filename.createNewFile();
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 创建目录
	 * @param dir
	 */
	public static void mkdir(String dir) {
		File fl = new File(dir);
        //不存在则创建一个文件夹或者目录
        if ( ! (fl.exists())  &&   ! (fl.isDirectory()))  {
            fl.mkdirs();
        } 
	}
	
	/**
	 * 判断文件或者文件夹是否存在
	 * @param String filePath
	 * @return
	 */
	public static boolean isExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	/**
	 * 判断文件或者文件夹是否存在
	 * @param File file
	 * @return
	 */
	public static boolean isExist(File file) {
		return file.exists();
	}
	
	
	/**
	 * copy文件并赋予新的文件名
	 * @param File srcFile 原文件
	 * @param File destDir 目标文件（目录）
	 * @param String newFileName 新的文件名
	 * @return long 文件大小
	 */
	@SuppressWarnings("resource")
	public static long copyFile(File srcFile, File destDir, String newFileName) {  
        long copySizes = 0;  
        if (!srcFile.exists()) {  
            //System.out.println("源文件不存在");  
            copySizes = -1;  
        } else if (!destDir.exists()) {  
            //System.out.println("目标目录不存在");  
            copySizes = -1;  
        } else if (newFileName == null) {  
           // System.out.println("文件名为null");  
            copySizes = -1;  
        } else {  
        	FileChannel fcin = null;  
            FileChannel fcout = null;  
        	try {  
                fcin = new FileInputStream(srcFile).getChannel();  
                fcout = new FileOutputStream(new File(destDir,newFileName)).getChannel();  
                long size = fcin.size();  
                fcin.transferTo(0, fcin.size(), fcout);  
                fcin.close();  
                fcout.close();  
                copySizes = size;  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {
	            try {
	            	fcin.close();  
	                fcout.close();  
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	        }
        }  
        return copySizes;  
    }  

	/**
	 * 文件转byte
	 * @param filePath 文件
	 * @return
	 */
	public static byte[] File2byte(String filePath){
		byte[] buffer = null;
		try{
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1)
			{
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
		return buffer;
	}

	/**
	 * byte转为文件
	 * @param buf
	 * @param filePath
	 * @param fileName
	 */
	public static void byte2File(byte[] buf, String filePath, String fileName){
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try{
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()){
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			if (bos != null){
				try{
					bos.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
			if (fos != null){
				try{
					fos.close();
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @Description: 将base64编码字符串转换为图片
	 * @param imgStr  base64编码字符串
	 * @param path   图片路径-具体到文件
	 * @return
	 */
	public static void createBase64Image(String imgStr, String path) {
		if (null == imgStr || "".equals(imgStr)) {
			return;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(imgStr);

			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

}
