package com.alex.bee;

import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;

public class BeePush {

    private final static String TAG = "BeePush";
    private final static String mUrl = "http://channel.api.duapp.com/rest/2.0/channel/channel";
    private final static String MSGKEY = "msgkey";
	
	public static final String SEND_MSG_ERROR = "send_msg_error";

    private String mSecretKey;
    private String mApiKey;

    public BeePush(Context context){
        this.mApiKey = Utils.getMetaValue(context,"api_key");
        this.mSecretKey = Utils.getMetaValue(context,"secret_key");
    }

	public BeePush(String secret_key, String api_key) {
		this.mSecretKey = secret_key;
		this.mApiKey = api_key;
	}

	private String urlencode(String str) throws UnsupportedEncodingException {
		String rc = URLEncoder.encode(str, "utf-8");
		return rc.replace("*", "%2A");
	}

	public String PostHttpRequest(RestApi data) {

		StringBuilder sb = new StringBuilder();

		try {
			data.put(RestApi._TIMESTAMP,
					Long.toString(System.currentTimeMillis() / 1000));
			data.remove(RestApi._SIGN);

			sb.append("POST");
			sb.append(mUrl);
			for (Map.Entry<String, String> i : data.entrySet()) {
				sb.append(i.getKey());
				sb.append('=');
				sb.append(i.getValue());
			}
			sb.append(mSecretKey);

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(urlencode(sb.toString()).getBytes());
			byte[] md5 = md.digest();

			sb.setLength(0);
			for (byte b : md5)
				sb.append(String.format("%02x", b & 0xff));
			data.put(RestApi._SIGN, sb.toString());

			sb.setLength(0);
			for (Map.Entry<String, String> i : data.entrySet()) {
				sb.append(i.getKey());
				sb.append('=');
				sb.append(urlencode(i.getValue()));
				sb.append('&');
			}
			sb.setLength(sb.length() - 1);

		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG,"PostHttpRequest Exception:" + e.getMessage());
			return SEND_MSG_ERROR;
		}

        Log.i(TAG,sb.toString());

        String response = null;
        try {
            response = HttpRequest.post(mUrl + "?" + sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
	}


	public String PushMessage(Object message,String channelid,String userid) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);
		RestApi ra = new RestApi(RestApi.METHOD_PUSH_MESSAGE,mApiKey);
		ra.put(RestApi._MESSAGE_TYPE, RestApi.MESSAGE_TYPE_MESSAGE);
		ra.put(RestApi._MESSAGES, jsonMessage);
		ra.put(RestApi._MESSAGE_KEYS, MSGKEY);
//		ra.put(RestApi._MESSAGE_EXPIRES, "86400");
		ra.put(RestApi._CHANNEL_ID, channelid);
		ra.put(RestApi._PUSH_TYPE, RestApi.PUSH_TYPE_USER);
		ra.put(RestApi._DEVICE_TYPE, RestApi.DEVICE_TYPE_ANDROID);
		ra.put(RestApi._USER_ID, userid);
		return PostHttpRequest(ra);
	}

}