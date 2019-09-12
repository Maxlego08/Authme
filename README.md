# Authme

Packet authentication system for minecraft server 1.7.10, You also have an email verification system

# How to install ?

* EnumConnectionState

In the EnumConnectionState class you must add in PLAY
```java
this.func_150756_b(110, PacketClientAuth.class);
this.func_150751_a(110, PacketServerAuth.class);
```

# Features

* Password hashed in SHA512 with a different salt per user
* Custom inventory to register, login, confirm your email
* Verification and notification by mail
* User limitation system by ip and mac address
* Only works for servers modded in 1.7.10

# Commandes

* /authme - See all available commands
* /authme reload - Reload configuration
* /authme setmail \<mail\> - Check your email
* /authme verifmail \<code\> - Check the code to confirm your email
* /authme setlocation - Put the player connection area
* /authme notif - Enable or disable email notifications
* /authme login - Enable or disable email connection
* /authme forcelogin \<player\> - Force a player's connection
* /authme register \<player\> \<password\> - Force a player's registration
* /authme unregister - Delete his account
* /authme stats - See the number of users
* /authme version - See plugin version
* /authme show \<player\> - View player information
* /authme bls - View blacklist players
* /authme bladd \<player\> - add player in blacklist
* /authme blremove \<player\> - remove player from blacklist
