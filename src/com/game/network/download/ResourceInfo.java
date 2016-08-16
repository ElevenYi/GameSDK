/**
 * 
 */
package com.game.network.download;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;

/** 
 * @author 王亭 
 * @since 2015-7-24 
 * todo   下载对象Model  可以支持下载信息存储与读取
 */
public class ResourceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2659974102004467702L;
	
	private String id;				//下载对象唯一标识 （根据fileUrl、fileName、savePath生成）
	private String fileUrl;			//文件下载地址
	private String fileName;		//文件保存名称 （需要带后缀，如：.apk等）
	private String savePath;		//文件保存目录 （SD的相对目录）
	private long completeSize;		//文件已下载的大小
	private long fileSize;			//下载文件的总大小
	private long speed;				//下载速度  b/s
	private int progress;			//下载完成的百分比
	private long refrence;			//DownloadManager 下载对列中的唯一标识
	
	public ResourceInfo(String fileUrl,String fileName,String savePath){
		this.fileUrl = fileUrl;
		this.fileName = fileName;
		this.savePath = savePath;
		this.id = toMd5(fileUrl + fileName + savePath);
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the fileUrl
	 */
	public String getFileUrl() {
		return fileUrl;
	}
	/**
	 * @param fileUrl the fileUrl to set
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the savePath
	 */
	public String getSavePath() {
		return savePath;
	}
	/**
	 * @param savePath the savePath to set
	 */
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	/**
	 * @return the completeSize
	 */
	public long getCompleteSize() {
		return completeSize;
	}
	/**
	 * @param completeSize the completeSize to set
	 */
	public void setCompleteSize(long completeSize) {
		this.completeSize = completeSize;
	}
	/**
	 * @return the fileSize
	 */
	public long getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return the speed
	 */
	public long getSpeed() {
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(long speed) {
		this.speed = speed;
	}
	/**
	 * @return the refrence
	 */
	public long getRefrence() {
		return refrence;
	}
	/**
	 * @param refrence the refrence to set
	 */
	public void setRefrence(long refrence) {
		this.refrence = refrence;
	}
	
	
	
	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * 将下载信息存储到缓存区（file或sqlite3）
	 * @param context
	 */
	public void saveToCache(Context context){
		
	}
	
	/**
	 * 从缓存区恢复下载信息
	 * @param context
	 */
	public void recoveryFromCache(Context context){
		
	}
	
	/**
	 * 将下载信息在缓存区删除
	 * @param context
	 */
	public void deleteCache(Context context){
		
	}
	
	/**
	 * 生成md5码
	 * @param string
	 * @return
	 */
	public static String toMd5(String string) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(string.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuffer sb = new StringBuffer();
			
			for (int i = 0; i < messageDigest.length; i++) {
				int temp = 0xFF & messageDigest[i];
				String s = Integer.toHexString(temp);
				if (temp <= 0x0F) {
					s = "0" + s;
				}
				sb.append(s);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
}
