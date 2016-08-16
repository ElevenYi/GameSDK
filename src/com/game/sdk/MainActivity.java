package com.game.sdk;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.game.sdk.bean.Purchase;
import com.game.sdk.bean.User;
import com.game.sdk.callback.LoginCallback;
import com.game.sdk.callback.PurchaseCallback;

public class MainActivity extends Activity {
	private final static String TAG = "TEST";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Platform.getInstance().initPlatform(this,"17");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Platform.getInstance().floatResume(this);
	}

	@Override
    protected void onPause() {
        super.onPause();
        Platform.getInstance().floatPause();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Platform.getInstance().floatConfigurationChanged(newConfig);
	}
    
	@Override
    protected void onDestroy() {
        super.onDestroy();
        Platform.getInstance().floatDestroy();
    }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Platform.getInstance().exit();
	}

	public void userLogin(View view){
		if (Platform.getInstance().isLogin()){
			Toast.makeText(this,"您已经登录过帐号",Toast.LENGTH_SHORT).show();
			return;
		}
		Platform.getInstance().login(this, new LoginCallback() {
			@Override
			public void loginSuccess(User gameUser) {
				Log.i(TAG,"登录成功：" + gameUser.getSid());
//				Toast.makeText(getApplicationContext(),"登录成功：" + gameUser.getSid(),Toast.LENGTH_SHORT).show();
			}

			@Override
			public void loginFail(String msg) {
				Log.i(TAG,"登录失败：" + msg);
//				Toast.makeText(getApplicationContext(),"登录失败：" + msg,Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void userPurchase(View view){
		Purchase purchase = new Purchase();
		purchase.setCoins(1);
		purchase.setRoleid("roleid");
		purchase.setServerid("1");
		purchase.setDeveloperInfo("developerinfo");
		Platform.getInstance().purchase(this, purchase, new PurchaseCallback() {
			@Override
			public void onCancel() {
				Log.i(TAG,"取消支付");
//				Toast.makeText(getApplicationContext(),"取消支付",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(String ordeId, Purchase purchase1) {
				Log.i(TAG,"支付成功");
//				Toast.makeText(getApplicationContext(),"支付成功",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFail(String msg) {
				Log.i(TAG,"失败支付 : " + msg);
//				Toast.makeText(getApplicationContext(),"失败支付 : " + msg,Toast.LENGTH_SHORT).show();
			}
		});

	}

}
