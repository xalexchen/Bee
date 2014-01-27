Bee
===

Bee is a small library which use Baidu cloud message to implement real time chat

Principle and Goal:
  Baidu cloud platform support pushing message directly to a single end user if we know the userid + channelid.
  it's already provide sendding transparent message mostly will be a string format.it also support custom content
  which can add json inside.
  
  We simply use the string channel as a container.first we put our message into a JSON object,you can encrypt it or
  whatever you like. send it over Baidu cloud platform.the receiver use the same technologies to decode it back.
  (JSON for instance)
  
