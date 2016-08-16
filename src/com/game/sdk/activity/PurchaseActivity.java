package com.game.sdk.activity;

import android.os.Bundle;

import com.game.sdk.activity.fragment.PurchaseChannelFragment;
import com.game.sdk.bean.Purchase;

/**
 * Created by echowang on 16/4/25.
 */
public class PurchaseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Purchase purchase = (Purchase) getIntent().getSerializableExtra("purchase");
        PurchaseChannelFragment purchaseChannelFragment = PurchaseChannelFragment.getFragmentByName(this,PurchaseChannelFragment.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("purchase",purchase);
        purchaseChannelFragment.setArguments(bundle);
        initFragment(purchaseChannelFragment);
    }
}
