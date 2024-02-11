# AntiHealthIndicator
Plugin that fakes entities health, so the client doesn't know exact health to prevent getting unfair advantage

### Overview
<b>This plugin requires ProtocolLib to work.</b>
<br/>
The plugin was created and designed for minecraft versions 1.9+. Be careful it was tested for 1.9.4, 1.19.4, 1.20.2 and 1.20.4 versions and it may not work for other versions! If you encoutered a problem make sure to open issue in github or contact me via discord (@snuiper228) and I'll fix it.
<br/>
This plugin is setting every living entity to 1 hp (half of heart) except for ender dragon, wither, iron golem (see below) and if player is riding on vehicle (see below). As bonus it also hides entity max health and setting it to 20

### Iron golem

It has 4 texture designed depending on health. If iron golem has >74 hp then fake hp will be 75, if >49 then 50, if >24 then 25, if below then 1. See https://minecraft.fandom.com/wiki/Iron_Golem#Cracking for more.

### Vehicle entity

When a player doesn't ride on the vehicle it hides vehicle hp, but when a player starts riding on it shows as normal.
