package com.alex.sample;

import com.alex.bee.Bee;
import com.alex.bee.BeeMessage;
import com.alex.bee.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "BeeSample";
    private PlaceholderFragment placeholderFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            placeholderFragment = new PlaceholderFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, placeholderFragment)
                    .commit();
        }

        Bee bee = new Bee(this);
        bee.init();
        bee.setOnReceiveMessageListener(new Bee.OnMessageReceiver() {
            @Override
            public void onMessage(Object object) {
                ChatMessage chatMessage = (ChatMessage) object;
                Log.i(TAG,"Receive message = " + chatMessage.getUser());
                placeholderFragment.updateContent(chatMessage.getUser()+ " " + chatMessage.getMessage());
            }

            @Override
            public void onError() {
                Log.i(TAG,"Receive Error");
            }
        },new BeeMessage(ChatMessage.class));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private TextView messageList;
        private String messageHistory;

        public PlaceholderFragment() {
        }
        public void updateContent(String msg) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
            messageHistory += sDateFormat.format(new Date()) + ": ";
            messageHistory += msg;
            messageHistory += "\n";

            messageList.setText(messageHistory);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            messageList = (TextView) rootView.findViewById(R.id.msg);
            messageHistory = this.getResources().getString(R.string.bee_msg);
            return rootView;
        }
    }

}
