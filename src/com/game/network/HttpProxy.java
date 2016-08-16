/**
 * 
 */
package com.game.network;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.webkit.URLUtil;

import com.game.network.Response.ErrorListener;
import com.game.network.Response.Listener;
import com.game.network.download.FileDownload;
import com.game.network.download.FileDownload.FileDownloadListener;
import com.game.network.download.ResourceInfo;
import com.game.network.toolbox.ImageRequest;
import com.game.network.toolbox.NormalJSONArrayRequest;
import com.game.network.toolbox.NormalJSONObjectRequest;
import com.game.network.toolbox.StringRequest;
import com.game.network.toolbox.Volley;

/** 
 * @author 王亭 
 * @since 2015-7-15 
 * todo   主要用来代理所以的网络请求
 */
public class HttpProxy {

	private static RequestQueue mRequestQueue;   //网络请求队列
	
	/**
	 * 获取网络请求的队列
	 * @param context
	 * @return
	 */
	private static RequestQueue getRequestQueue(Context context){
		if(mRequestQueue == null){
			mRequestQueue =  Volley.newRequestQueue(context.getApplicationContext());
		}
		return mRequestQueue;
	}
	
	/**
	 * 中断网络请求
	 * @param request  网络请求的request对对象
	 */
	public static void cancelRequest(Request<?> request){
		if(mRequestQueue == null){
			return ;
		}

		mRequestQueue.cancelAll(request.getTag());
	}
	
	/**
	 * get 请求
	 * @param context		Android环境上下文
	 * @param apiAlias		接口别名    此别名已经在Config中配置，可以读取接口地址和接口参数
	 * @param cache			是否使用缓存 true:使用缓存 false:不使用缓存
	 * @param httpListener   网络请求状态监听  其中泛型支持String,JSONObject,JSONArray
	 *                       如果httpListener为null时 默认内部按照String方式解析
	 */
	public static <T> Request<?> get(Context context,String apiAlias,boolean cache,final HttpListener<T> httpListener){
		return null;
	}
	
	/**
	 * get 请求
	 * @param url		请求url 
	 * @param params	请求参数
	 * @param cache			是否使用缓存 true:使用缓存 false:不使用缓存
	 * @param httpListener   网络请求状态监听  其中泛型支持String,JSONObject,JSONArray
	 *                       如果httpListener为null时 默认内部按照String方式解析
	 */
	public static <T> Request<?> get(Context context,String url,Map<String,String> params,boolean cache,final HttpListener<T> httpListener){
		if(!URLUtil.isNetworkUrl(url)){
			httpListener.onFail("url is error");
			return null;
		}
		
		Class<?> clazz = String.class;
		if(httpListener != null){
			clazz = httpListener.getGenericType();
		}
		
		Request<?> request = getRequestByClazz(clazz,url,params,Request.Method.GET,new Listener<T>() {

			@Override
			public void onResponse(T response) {
				// TODO Auto-generated method stub
				if(httpListener != null){
					httpListener.onSuccess(response);
				}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.i("HttpProxy","error : " + error.getLocalizedMessage());
				if(httpListener != null){
					httpListener.onFail("network request failure : " + error.getLocalizedMessage());
				}
			}
		});
		
		if(request != null){
			request.setTag(url);
			request.setShouldCache(cache);
			getRequestQueue(context).add(request);
		}
		
		return request;
	}
	
//	public static <T> Request<?> noCache(Context context,String url,Map<String,String> params,final HttpListener<T> httpListener){
//		if(!URLUtil.isNetworkUrl(url)){
//			httpListener.onFail("url is error");
//			return null;
//		}
//
//		Class<?> clazz = String.class;
//		if(httpListener != null){
//			clazz = httpListener.getGenericType();
//		}
//
//		Request<?> request = getRequestByClazz(clazz,url,params,Request.Method.GET,new Listener<T>() {
//
//			@Override
//			public void onResponse(T response) {
//				// TODO Auto-generated method stub
//				if(httpListener != null){
//					httpListener.onSuccess(response);
//				}
//			}
//		},new ErrorListener() {
//
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				// TODO Auto-generated method stub
//				if(httpListener != null){
//					httpListener.onFail("network request failure");
//				}
//			}
//		});
//
//		if(request != null){
//			request.setTag(url);
//			request.setShouldCache(false);
//			getRequestQueue(context).add(request);
//		}
//
//		return request;
//	}
	
	/**
	 * post 请求
	 * @param context		Android环境上下文
	 * @param apiAlias		接口别名    此别名已经在Config中配置，可以读取接口地址和接口参数
	 * @param cache			是否使用缓存 true:使用缓存 false:不使用缓存
	 * @param httpListener   网络请求状态监听  其中T泛型支持String,JSONObject,JSONArray
	 *                       如果httpListener为null时 默认内部按照String方式解析
	 */
	public static <T> Request<?> post(Context context,String apiAlias,boolean cache,final HttpListener<T> httpListener){
		return null;
	}
	
	/**
	 * post 请求
	 * @param context		Android环境上下文
	 * @param url		请求url 
	 * @param params	请求参数
	 * @param cache			是否使用缓存 true:使用缓存 false:不使用缓存
	 * @param httpListener   网络请求状态监听  其中T泛型支持String,JSONObject,JSONArray
	 * 						 如果httpListener为null时 默认内部按照String方式解析
	 */
	public static <T> Request<?> post(Context context,String url,Map<String,String> params,boolean cache,final HttpListener<T> httpListener){
		if(!URLUtil.isNetworkUrl(url)){
			httpListener.onFail("url is error");
			return null;
		}
		
		Class<?> clazz = String.class;
		if(httpListener != null){
			clazz = httpListener.getGenericType();
		}
		
		Request<?> request = getRequestByClazz(clazz,url,params,Request.Method.POST,new Listener<T>() {

			@Override
			public void onResponse(T response) {
				// TODO Auto-generated method stub
				if(httpListener != null){
					httpListener.onSuccess(response);
				}
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.i("HttpProxy","error : " + error.getLocalizedMessage());
				if(httpListener != null){
					httpListener.onFail("network request failure : " + error.getLocalizedMessage());
				}
			}
		});
		
		if(request != null){
			request.setTag(url);
			request.setShouldCache(cache);
			getRequestQueue(context).add(request);
		}
		
		return request;
	}
	
	/**
	 * 下载网络图片
	 * @param context		Android环境上下文
	 * @param imageUrl		图片地址
	 * @param maxWidth		图片显示的最大宽度
	 * @param maxHeight		图片显示的最大高度
	 * @param decodeConfig	图片解码配置信息    android.graphics.Bitmap.Config
	 * @param httpListener	图片下载状态监听
	 * @return
	 */
	public static Request<?> downloadImage(Context context,String imageUrl,int maxWidth,int maxHeight,Config decodeConfig,final HttpListener<Bitmap> httpListener){
		if(!URLUtil.isNetworkUrl(imageUrl)){
			httpListener.onFail("imageUrl is error");
			return null;
		}
		
		Request<?> request = new ImageRequest(imageUrl, new Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap response) {
				// TODO Auto-generated method stub
				if(httpListener != null){
					httpListener.onSuccess(response);
				}
			}
		}, maxWidth, maxHeight, decodeConfig, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				if(httpListener != null){
					httpListener.onFail("imageUrl download failure");
				}
			}
		});
		
		request.setTag(imageUrl);
		getRequestQueue(context).add(request);
		return request;
	}
	
	/**
	 * 大文件下载（如：apk、zip等）
	 * @param context
	 * @param fileUrl		文件下载地址
	 * @param fileName		文件保存名称（带后缀，如.apk）
	 * @param savePath		文件保存路径（文件保存路径，相对于SD卡的相对目录）
	 */
	public static ResourceInfo download(Context context,String fileUrl,String fileName,String savePath){
		ResourceInfo resourceInfo = new ResourceInfo(fileUrl,fileName,savePath);
		FileDownload.download(context, resourceInfo);
		return resourceInfo;
	}
	
	/**
	 * 暂停下载任务
	 * @param context
	 * @param resourceInfo	文件下载地址
	 */
	public static void stopDownload(Context context,ResourceInfo resourceInfo){
		FileDownload.stopDownload(context, resourceInfo);
	}
	
	/**
	 * 添加文件下载状态 监听
	 * @param listener
	 */
	public static void addDownloadListener(FileDownloadListener listener){
		FileDownload.addDownloadListener(listener);
	}
	
	/**
	 * 删除文件下载状态 监听
	 * @param listener
	 */
	public static void removeDownloadListener(FileDownloadListener listener){
		FileDownload.removeDownloadListener(listener);
	}
	
	/**
	 * 处理 请求的url与请求参数的拼接
	 * @param url				请求url
	 * @param params			请求参数
	 * @param paramsEncoding	字符编码  默认是 UTF-8
	 * @return
	 */
	public static String getUrlWithQueryString(String url,
			Map<String, String> params, String paramsEncoding){
		if(paramsEncoding == null){
			paramsEncoding = Request.DEFAULT_PARAMS_ENCODING;
		}
		return Request.getUrlWithQueryString(url, params, paramsEncoding);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Request<?> getRequestByClazz(Class<?> clazz,String url,Map<String,String> params,int method,Listener listener,ErrorListener errorListener){
		Request<?> request = null;
		if(clazz == String.class){
			request = new StringRequest(method, url, params, listener, errorListener);
		}
		
		if(clazz == JSONObject.class){
			request = new NormalJSONObjectRequest(method, url, params, listener, errorListener);
		}
		
		if(clazz == JSONArray.class){
			request = new NormalJSONArrayRequest(method, url, params, listener, errorListener);
		}
		
		return request;
	}
}
