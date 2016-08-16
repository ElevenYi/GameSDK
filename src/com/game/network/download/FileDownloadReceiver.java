/**
 * 
 */
package com.game.network.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/** 
 * @author 王亭 
 * @since 2015-7-17 
 * todo  主要用来接收 DownloadManager 文件下载的状态
 */
public class FileDownloadReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
			Log.i("PPSSDKPlatfrom", "DOWNLOAD_COMPLETE");
			return ;
		}
		
		if(DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.getAction())){
			Log.i("PPSSDKPlatfrom", "DOWNLOAD_COMPLETE");
			return ;
		}
		
	}

}
