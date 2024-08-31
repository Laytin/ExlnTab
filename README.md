# EXLN Tab - Scoretab UI mod

# [CurseForge](https://legacy.curseforge.com/minecraft/mc-mods/exln-tab)
This is one of my many "EXLN" mods, that aims to decorate the appearance of the player list and inject permission prefixes.

On my project I came across a situation where I wanted to refresh the appearance of the players table.
As it turned out, there are very few such mods, and most importantly, they do not support privileges!
They will not show the player's donation level or his status.

This mod is designed to correct this nuance. It supports the following plugins:
- LuckPerms (for roles)
- PermissionEX (for roles)
- Also supports Spark (for current tps)

Tab is scrollable, and when players won't fit into the main screen, all you have to do is scroll your mouse wheel!

The modification also has its own, non-Minecraft font (Roboto). 

For greater performance I recommend using anti-aliasing

U can setup a lot:
- Should we inject in plugin and take group prefix?
- Should we inject in plugin and take group name instead of prefix?
- If displayed role has some "&2" (colorcodes), should we apply it to text?
- Should we write current TPS?
- Should we paint coloured rounded rectangle for role display?

U can also setup all colors for role display!

Please, If this is what you've been looking for and I was able to help you, you can always thank me on Patreon:
[Clickable](https://patreon.com/Laytin) 

# How to install
- Download both versions of file, ***-server.jar and ***-client.jar
- Put ***-client.jar version in ur launcher minecraft/mods path (or minecraft/mods/1.7.10)
- Do the same for the server, but with ***-server.jar file
- Download and install following mods (or extract from "dependences" zip):
     - CodeChickenLib
     - CodeChickenCore
     - ElegantNetworking
- **Optional**: install permission plugin (PermissionsEX or LuckPerms) and Spark (for tps)
- Start it up!

# How to setup
U can setup a lot things in config/exlntab.cfg

This option mean that we should inject in Spark plugin (if exist) and write current tps in tab. If Spark not installed, but option is *true* -  always returns 20.0:
```
B:"Draw TPS"=true
```

Should we draw role under player name in tab:
```
B:"Draw role"=true
```
Should we use colored rounded rectangle in the background of role text
```
B:"Use colored bg"=true
```
Should we use colorcodes from prefix (ex: "&2Admin&4") to paint text. If else, all text will be white
```
B:"Use colour codes"=false
```
Should we use groupname instead of group prefix ( "superadm" instead of "Super Admin")
```
B:"Use group name"=false
```
#
Photo hierarchy is Avatar -> Skin face -> Default Steve Face

This url will be used to load profile photo from forum or some else source
```
S:"Avatar URL"=https://someproject/launch/Skins/%player%.png
```
This url will be used to load subimage from player skin (it cut part of original skin image)
```
S:"Skins URL"=https://someproject/launch/Skins/%player%.png
```
**If none of the links are available, the default Steve skin face will be used**

**%player% will be replaced with playername**

Tab header, in most cases write here project name
```
S:"Tab Header"=Project name | Project server
```
#
To manage colored bg, follow this:
```
tabGroupColor {
	S:"adm6"=#d96879
    S:"groupname"=#hexcolor
}
```
U can add ur colors here in format "groupname":#hexcolor. Use groupname, not a group prefix! Hexcolor u can take [here](https://g.co/kgs/Drv68qV)

# ScreenShots
Enabled colored bg with color setup, enabled draw tps. Disabled color codes, avatar url in empty.
![123](https://github.com/user-attachments/assets/6da1f65b-4448-4e33-aecd-59bf56f449b6)
Disabled colored bg, disabled draw tps. Enabled color codes, installed avatar url.
![222](https://github.com/user-attachments/assets/e617530f-ebd7-44e9-b75a-26b2fc87c36b)


# PermissionsEX Setup 
```
groups:
  default:
    options:
      prefix: 'Player'
      default: 'true'
      rank: '10'
    permissions:
    - modifyworld.*
  adm:
    options:
      prefix: 'Admin'
      rank: '100'
    permissions:
    - modifyworld.*
```
Rank affects the score of the player with the privilege. This is done to ensure that administrators are at the top of the list. Accordingly, the tab displays players as their rank decreases

The prefix is ​​the main thing you need, this is what will be displayed by default and can be formatted `/pex group <group> prefix <prefix>`

Also u should setup default group 
`/pex default group <group> true` (otherwise new player joined with null group, it throw new NullPointer ex)

# LuckPerms Setup

Group or player should have permission like that in format `prefix.<rank>.<GroupNameOrPrefix>`:
```
prefix.100.Admin 
```
U can simply do it in `/lp editor`

![image](https://github.com/user-attachments/assets/9724a12e-67ce-4d4e-8dc1-2bf9989af3ac)
