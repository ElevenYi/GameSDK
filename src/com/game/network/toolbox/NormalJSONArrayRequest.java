package com.game.network.toolbox;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.game.network.AuthFailureError;
import com.game.network.NetworkResponse;
import com.game.network.ParseError;
import com.game.network.Request;
import com.game.network.Response;
import com.game.network.Response.ErrorListener;
import com.game.network.Response.Listener;

/**
 * 正常的网络请求   返回格式为json
 * @author 王亭
 * 2014-11-27
 * TODO
 */
public class NormalJSONArrayRequest extends Request<JSONArray> {
	private Map<String, String> params;
    private Listener<JSONArray> mListener;
     
    public NormalJSONArrayRequest(int method,String url, Map<String, String> params,Listener<JSONArray> listener,ErrorListener errorListener) {
    	super(method, url,params, errorListener);
        
        mListener = listener;
        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
    
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//    	// TODO Auto-generated method stub
//    	 VolleyLog.d("method : %s", "getHeaders");
//    	 Map<String, String> headers = new HashMap<String, String>();
//    	 headers.put("Charset", "UTF-8");
//    	 headers.put("Content-Type", "application/x-javascript");
//    	 headers.put("Accept-Encoding", "gzip");
//    	return headers;
//    }
     
    //此处因为response返回值需要json数据,和JsonObjectRequest类一样即可
    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONArray(jsonString),HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
    @Override
    protected void deliverResponse(JSONArray response) {
    	if(mListener != null){
    		mListener.onResponse(response);
    	}
    }
}
