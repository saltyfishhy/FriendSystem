# FriendSystem
 A customizable plugin that lets you friend and message other people!

# Permissions

The plugin has two relevant permissions:

 - friend.admin
 - a permission settable in the config, defaults to "friend.use"
 
 friend.admin allows anyone with the permission to reload the config file. The second permission allows anyone with the permission to use the main commands of the plugin such as /friend, /msg, /unfriend, and more. 
 
 # Commands
 
 The plugin comes with a few commands:
 
   - /friend (or /f) - Allows somebody to view friends / process a friend request / send a friend request. Sending just /friend shows your friends list
   - /unfriend - Allows somebody to remove someone from their friends list 
   - /msg (or /tell) - Allows somebody to send a message to one of their friends
   - /reply (or /r) - Allows a user to message the last person they messaged / the last person that messaged them 
   - /freload (or /friendreload) - Allows anyone with the permission "friend.admin" to reload the config file
   
 # Config
 
 The plugin also comes with three configurable properties:
 
   - requestTimeout - The amount of time (in seconds) that passes before requests time out 
   - maxFriends - The max amount of friends a player can have 
   - friendPermission - The default permission required to use the basic commands
   
The config file is located at (where your server is)\plugins\FriendSystem\config.yml. Make sure that if you edit it while the server is running you use /freload to apply your changes. 

# Installation

To install this plugin, download the [jar file](https://github.com/saltyfishhy/FriendSystem/raw/master/out/artifacts/FriendSystem_jar/FriendSystem.jar) and place it into your plugins folder. Then, restart your server. The config should auto generate, and as players join they'll be added to a file called "friends.yml" which contains all of the players and their current friends, requests, and more. 

# Notes

Currently, this project does not support MySQL. That being said, all of the storage is done in a config file wherever your server is hosted. In the future, I am planning to change this, however it will remain just a config file for the time being. 

This started as a project for fun on my server, and grew to what it is now. I'm planning on developing a GUI to display friends, however, at this time it will remain a list in chat.

Hope you all enjoy!
