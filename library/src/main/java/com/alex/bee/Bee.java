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
    public  static final String BIND = "com.alex.bee.bind";

    public  static final String EXTRA_MSG = "msg";
    public  static final String EXTRA_USER_ID = "user_id";
    public  static final String EXTRA_CHANNEL_ID = "channel_id";

    private Context context;
    private BeeMessage message;
    private Gson mGson;
    private Object mObject;
    private String userId;
    private String channelId;
    private BeePush mPush;

    public interface OnMessageReceiver{
        public void onMessage(Object object);
        public void onError();
    }
    public interface RegisterCallback {
        public void bind(String userId,String channelId);
    }

    private OnMessageReceiver onMessageReveiver;
    private RegisterCallback registerCallback;

    public Bee(Context context){
        this.context = context;
        mPush = new BeePush(context);
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
        } else {
            userId = Utils.getUserId(context);
            channelId = Utils.getChannelId(context);
            if (registerCallback != null) {
                registerCallback.bind(userId,channelId);
            }
        }
    }

    public void sendMessage(Object message,String channelid,String userid){
        mPush.PushMessage(message,channelid,userid);
    }
    public String getChannelId() {
        return channelId;
    }
    public void setRegisterCallback (RegisterCallback registerCallback) {
        this.registerCallback = registerCallback;
    }
    public void setOnReceiveMessageListener(OnMessageReceiver onMessageReveiver,BeeMessage message){
        this.onMessageReveiver = onMessageReveiver;
        this.message = message;
        mObject = message.newInstance();
        mGson = new Gson();
        // We use LocalBoardcast forward message
        IntentFilter localFilter = new IntentFilter();
        localFilter.addAction(MSG);
        localFilter.addAction(BIND);
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
                    Log.i(TAG,"msg = " + intent.getStringExtra(EXTRA_MSG));
                    try {
                        mObject = mGson.fromJson(intent.getStringExtra(EXTRA_MSG),message.getType());
                        onMessageReveiver.onMessage(mObject);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        onMessageReveiver.onError();
                    }
                }
            } else if (action.equals(BIND)) {
                userId = intent.getStringExtra(EXTRA_USER_ID);
                channelId = intent.getStringExtra(EXTRA_CHANNEL_ID);
                Utils.setUserId(context,userId);
                Utils.setChannelId(context,channelId);
                if (registerCallback != null) {
                    registerCallback.bind(userId,channelId);
                }
            }
        }
    };
}
