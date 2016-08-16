/**
 * 
 */
package com.game.network;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
* 
* @author 王亭 
* @since 2015-7-15 
* todo	 主要用来监听网络请求的消息响应
*/
public abstract class HttpListener<T>{
	public abstract void onSuccess(T response);
	public abstract void onFail(String msg);
	
	public Class<?> getGenericType(){
		Type genType = this.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
	}
}
