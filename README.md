Bee
===

Bee is a small library which use Baidu cloud message to implement real time chat

Principle and Goal
===
Baidu cloud platform support pushing message directly to a single end user if we know the userid and channelid.
it's already provide sending transparent message (140 characters limit)
  
We simply use this channel as a container.we put our message into a `JSON` object,you can encrypt it or
not. send it over Baidu cloud platform.the receiver use the same technologies to decode it back.

Usage
===
1. Add `Bee` library modules as dependency to your existing project.
2. If your project already in inheritance `Application` class.use `BeeApplication` instead.
3. Add `secret_key` and `api_key` into your `Androidmainfest.xml` file.
```
    <meta-data android:name="api_key" android:value="Your api_key" />
    <meta-data android:name="secret_key" android:value="Your secret_key" />
```

Simple Example
==
```
 public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bee bee = new Bee(this);
        bee.setRegisterCallback(new Bee.RegisterCallback() {

            @Override
            public void bind(String userId, String channelId) {
               ChatMessage message = new ChatMessage();
               message.setMessage("Hello forks,this message send from myself");
               message.setUser(id);
               bee.sendMessage(message, channelId, id);
            }
        });
        bee.init();
        bee.setOnReceiveMessageListener(new Bee.OnMessageReceiver() {
            @Override
            public void onMessage(Object object) {
                ChatMessage chatMessage = (ChatMessage) object;
                Log.i(TAG,"Receive message = " + chatMessage.getUser()+ " " + chatMessage.getMessage());
            }

            @Override
            public void onError() {
                Log.i(TAG,"Receive Error");
            }
        },new BeeMessage(ChatMessage.class));
 }
```
```
  public class ChatMessage {
      private String user;
      private String message;
  
      public String getUser() {
          return user;
      }
  
      public void setUser(String user) {
          this.user = user;
      }
  
      public String getMessage() {
          return message;
      }
  
      public void setMessage(String message) {
          this.message = message;
      }
  }
```

Pull Requests
===
  i still working on progress to fix the bug and make it easy to use.also gladly accept pull requests

License
===

   Copyright 2014 Alex Chen

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
