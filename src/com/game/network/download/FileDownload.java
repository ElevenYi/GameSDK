/**
 * 
 */
package com.game.network.download;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.URLUtil;

/**
 * @author 王亭
 * @since 2015-7-16 todo 文件下载
 */
public class FileDownload {
	public interface FileDownloadListener {
		void onProgress(int status, ResourceInfo resourceInfo);
	}

	private static DownloadManager downloadManager;
	private final static int REFRESH = 200;
	private final static int NOTIFYTARGET = 201;
	
	public final static int DOWNLOAD_STATUS_PENDING = 1;
	public final static int DOWNLOAD_STATUS_RUNNING = 2;
	public final static int DOWNLOAD_STATUS_SUCCESSFUL = 3;
	public final static int DOWNLOAD_STATUS_PAUSED = 4;
	public final static int DOWNLOAD_STATUS_FAILED = 5;
	
	private static Map<String, ResourceInfo> downloadList = new HashMap<String, ResourceInfo>();
	private static List<SoftReference<FileDownloadListener>> downloadListener = new ArrayList<SoftReference<FileDownloadListener>>();

	private static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH:
				if(downloadManager != null){
					queryDownloadStatus(downloadManager);
				}
				break;
			case NOTIFYTARGET:
				Bundle bundle = msg.getData();
				int status = bundle.getInt("status", 0);
				ResourceInfo resourceInfo = (ResourceInfo) bundle.getSerializable("resourceInfo");
				for (SoftReference<FileDownloadListener> softReference : downloadListener) {
					FileDownloadListener listener = softReference.get();
					if(listener != null){
						listener.onProgress(status, resourceInfo);
					}
				}
				break;
			default:
				break;
			}
			
		};
	};
	
	/**
	 * 添加 下载状态的监听
	 * 
	 * @param listener
	 */
	public static void addDownloadListener(FileDownloadListener listener) {
		if (listener == null) {
			return;
		}

		boolean isExist = false;
		for (SoftReference<FileDownloadListener> softReference : downloadListener) {
			if (listener.equals(softReference.get())) {
				isExist = true;
				break;
			}
		}

		if (!isExist) {
			downloadListener.add(new SoftReference<FileDownloadListener>(
					listener));
		}
		
		if(downloadListener.size() == 1){
			handler.sendEmptyMessageDelayed(REFRESH, 1000);
		}
	}

	/**
	 * 删除 下载状态的监听
	 * 
	 * @param listener
	 */
	public static void removeDownloadListener(FileDownloadListener listener) {
		if (listener == null) {
			return;
		}

		for (SoftReference<FileDownloadListener> softReference : downloadListener) {
			if (listener.equals(softReference.get())) {
				downloadListener.remove(softReference);
				break;
			}
		}
		
		if(downloadListener.size() == 0){
			handler.removeMessages(REFRESH);
		}
	}

	/**
	 * 文件下载
	 * 
	 * @param context
	 * @param fileUrl
	 *            文件下载链接
	 * @param fileName
	 *            文件保存名称(需要添加后缀，如.apk)
	 * @param savePath
	 *            文件保存目录（相对于SD卡的相对目录）
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static void download(Context context, ResourceInfo resourceInfo) {
		if(downloadList.containsKey(resourceInfo.getId())){
			return ;
		}
		if (!URLUtil.isNetworkUrl(resourceInfo.getFileUrl())) {
			return;
		}
		File file = Environment.getExternalStoragePublicDirectory(resourceInfo.getSavePath());
		if(!file.exists()){
			file.mkdirs();
		}else{
			file = new File(file.getPath() + File.separator + resourceInfo.getFileName());
			if(file.exists()){
				file.delete();
			}
		}
		
		if(downloadManager == null){
			downloadManager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
		}
		Uri uri = Uri.parse(resourceInfo.getFileUrl());
		Request request = new Request(uri);
		request.setTitle("download");
		request.setDescription("download apk");
		if (VERSION.SDK_INT > 10) {
			request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		} else {
			request.setShowRunningNotification(true);
		}
		request.setVisibleInDownloadsUi(true);
		request.setDestinationInExternalPublicDir(resourceInfo.getSavePath(), resourceInfo.getFileName());

		long reference = downloadManager.enqueue(request);
		resourceInfo.setRefrence(reference);
		downloadList.put(resourceInfo.getId(), resourceInfo);
	}

	/**
	 * 停止文件下载
	 * DownloadManager的remove方法可以用来取消一个准备进行的下载，中止一个正在进行的下载，或者删除一个已经完成的
	 * @param context
	 * @param fileUrl
	 */
	public static void stopDownload(Context context, ResourceInfo resourceInfo) {
		if(!downloadList.containsKey(resourceInfo.getId())){
			return ;
		}
		long reference = downloadList.get(resourceInfo.getId()).getRefrence();
		if(downloadManager == null){
			downloadManager = (DownloadManager) context
					.getSystemService(Context.DOWNLOAD_SERVICE);
		}
		downloadManager.remove(reference);
	}

	/**
	 * 查询下载状态
	 * @param context
	 */
	private static void queryDownloadStatus(DownloadManager downloadManager) {
		Set<String> keys = downloadList.keySet();
		DownloadManager.Query query = new DownloadManager.Query();
		for (String key : keys) {
			ResourceInfo resourceInfo = downloadList.get(key);
			long reference = resourceInfo.getRefrence();
			query.setFilterById(reference);
			Cursor c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				int status = c.getInt(c
						.getColumnIndex(DownloadManager.COLUMN_STATUS));

				int reasonIdx = c.getColumnIndex(DownloadManager.COLUMN_REASON);
				int titleIdx = c.getColumnIndex(DownloadManager.COLUMN_TITLE);
				int fileSizeIdx = c
						.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
				int bytesDLIdx = c
						.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
				String title = c.getString(titleIdx);
				int fileSize = c.getInt(fileSizeIdx);
				int bytesDL = c.getInt(bytesDLIdx);
				int progress = bytesDL * 100 / fileSize;

				// Translate the pause reason to friendly text.
				int reason = c.getInt(reasonIdx);
				StringBuilder sb = new StringBuilder();
				sb.append(title).append("\n");
				sb.append("Downloaded ").append(bytesDL).append(" / ")
						.append(fileSize);

				switch (reason) {
				case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
					//暂停 等待连接wifi环境
					break;
				case DownloadManager.PAUSED_UNKNOWN:
					//暂停 未知原因
					break;
				case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
					//暂停 等待网络连接
					break;
				case DownloadManager.PAUSED_WAITING_TO_RETRY:
					//暂停 等待重试
					break;
				default:
					break;
				}
				// Display the status
				Log.d("PPSSDKPlatfrom", sb.toString());
				resourceInfo.setCompleteSize(bytesDL);
				resourceInfo.setFileSize(fileSize);
				resourceInfo.setSpeed(bytesDL - resourceInfo.getCompleteSize());
				resourceInfo.setProgress(progress);
				Bundle bundle = new Bundle();
				bundle.putSerializable("resourceInfo", downloadList.get(key));
				switch (status) {
				case DownloadManager.STATUS_PAUSED:
					//暂停下载
					Log.v("tag", "STATUS_PAUSED");
					bundle.putInt("status", DOWNLOAD_STATUS_PAUSED);
				case DownloadManager.STATUS_PENDING:
					//等待下载
					Log.v("tag", "STATUS_PENDING");
					bundle.putInt("status", DOWNLOAD_STATUS_PENDING);
				case DownloadManager.STATUS_RUNNING:
					// 正在下载，通知调用者 目前的下载进度
					Log.v("tag", "STATUS_RUNNING");
					bundle.putInt("status", DOWNLOAD_STATUS_RUNNING);
					break;
				case DownloadManager.STATUS_SUCCESSFUL:
					// 下载完成
					Log.v("tag", "STATUS_SUCCESSFUL");
					bundle.putInt("status", DOWNLOAD_STATUS_SUCCESSFUL);
					// dowanloadmanager.remove(lastDownloadId);
					downloadList.remove(key);
					break;
				case DownloadManager.STATUS_FAILED:
					// 下载失败 清除已下载的内容，重新下载
					Log.v("tag", "STATUS_FAILED");
					bundle.putInt("status", DOWNLOAD_STATUS_FAILED);
					downloadManager.remove(reference);
					downloadList.remove(key);
					break;
				}
				Message msg = new Message();
				msg.what = NOTIFYTARGET;
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}
		
		handler.sendEmptyMessageDelayed(REFRESH, 1000);
	}
}
