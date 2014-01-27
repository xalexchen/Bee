package com.alex.bee;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by Alex on 1/27/14.
 */
public class Bee {

    private static final String TAG = "Bee";
    public  static final String MSG = "com.alex.bee.msg";
    public  static final String EXTRA = "extra";

    private Context context;

    public interface OnMessageReceiver{
        public void onMessage(String msg);
    }
    private OnMessageReceiver onMessageReveiver;

    public Bee(Context context){
        this.context = context;
    }
    public void init () {
        String key = Utils.getMetaValue(context, "api_key");
        init(key);
    }

    public void init (String api_key) {
        if (!Utils.hasBind(context.getApplicationContext())) {
            Log.d(TAG, "Bee init");
            PushManager.startWork(context.getApplicationContext(),
                    PushConstants.LOGIN_TYPE_API_KEY,
                    api_key);
//            PushManager.enableLbs(activity.getApplicationContext());
        }
    }

    public void setOnReceiveMessageListener(OnMessageReceiver onMessageReveiver){
        this.onMessageReveiver = onMessageReveiver;
        // We use LocalBoardcast forward message
        IntentFilter localFilter = new IntentFilter();
        localFilter.addAction(MSG);
        LocalBroadcastManager.getInstance(context).registerReceiver(localReceiver, localFilter);
    }

    private final BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO open a thread parse Bee json format.
            final String action = intent.getAction();
            if (action.equals(MSG)){
                if (onMessageReveiver != null) {
                    onMessageReveiver.onMessage(intent.getStringExtra(EXTRA));
                }
            }
        }
    };
}
