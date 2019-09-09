# Authme

Packet authentication system for minecraft server 1.7.10, You also have an email verification system

# How to install ?

* EnumConnectionState

In the EnumConnectionState class you must add in PLAY
```java
this.func_150756_b(110, PacketClientAuth.class);
this.func_150751_a(110, PacketServerAuth.class);
```