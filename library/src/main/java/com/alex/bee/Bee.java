package com.alex.bee;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
    private BeeMessage message;
    private Gson mGson;
    private Object mObject;

    public interface OnMessageReceiver{
        public void onMessage(Object object);
        public void onError();
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

    public void setOnReceiveMessageListener(OnMessageReceiver onMessageReveiver,BeeMessage message){
        this.onMessageReveiver = onMessageReveiver;
        this.message = message;
        mObject = message.newInstance();
        mGson = new Gson();
        // We use LocalBoardcast forward message
        IntentFilter localFilter = new IntentFilter();
        localFilter.addAction(MSG);
        LocalBroadcastManager.getInstance(context).registerReceiver(localReceiver, localFilter);
    }

    private final BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(MSG)){
                if (onMessageReveiver != null) {
                    // TODO because string length limit 140 character.can be fast parse it on mainUI thread ?
                    // or make a separate thread ??
                    try {
                        mObject = mGson.fromJson(intent.getStringExtra(EXTRA),message.getType());
                        onMessageReveiver.onMessage(mObject);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        onMessageReveiver.onError();
                    }
                }
            }
        }
    };
}
