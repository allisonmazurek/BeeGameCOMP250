# BeeGameCOMP250

Fork of [Allison Mazurek's GUI](https://github.com/allisonmazurek/BeeGameCOMP250) for the Fall 2020 COMP 250 tower defense game.
Added features:
- Hornets that can be spawned at the nest and move toward the hive, stopping to attack bees
- Action-taking via button press
- Health indicator for bees without relying on toString() methods defined in Assignment1 source code
- Icons for bees and hornets that update anytime an action is taken, including a TankyBee in pseudo-Viking armour

Unresolved issues:
- Subsequent hornet swarms spawned after the first swarm skip tiles on the way to the hive
- Hornets stay in place when attacked by StingyBees instead of moving toward them
- Hornets sometimes display strange behaviour when TankyBees are on the board

Possible future additions:
- Attack indicators for StingyBees
- Random board generation
- Predefined gameplay rounds

![screenshot](https://github.com/croissantfriend/BeeGameCOMP250/blob/master/BeeGameGUI.png)

Note that the source code may need some tweaking if it isn't fully compatible with your Assignment1 source code out of the box. Please open an issue if you need any help doing so, and feel free to fork and pull-request if you have any improvements or ideas!


Allison's original README follows:
---
Comp250 Assignment1 Game Mcgill University Fall 2020

![screenshot](https://github.com/allisonmazurek/BeeGameCOMP250/blob/master/GUIsnapshot.jpg)

# What does it do?
GUI for a Toward a Tower Defense Game. Beginning of an operational game of the first COMP250 Assignment.
Currently only works for IntelliJ users. 

# Installation: Standard Method
Clone this repository to your directory of choice.
Add all 8 of the assignment .java files to /src of the project. 
* Insect.java
∗ Hornet.java
∗ HoneyBee.java 
∗ BusyBee.java
∗ StingyBee.java 
∗ TankyBee.java
∗ Tile.java
∗ SwarmOfHornets.java

![screenshot](https://github.com/allisonmazurek/BeeGameCOMP250/blob/master/FileLocation.jpg)

# Installation: Submodule Method
This method is useful if you are managing your own git repository for this project and do not want to fork this one. It allows you to maintain your existing workflow and still keep up to date with new versions of the visualizer.

IntelliJ Idea: Right click your project folder -> open in terminal -> enter command: git submodule add https://github.com/allisonmazurek/BeeGameCOMP250

Other IDE on Linux / Mac: Open terminal. CD your project directory, enter command: git submodule add https://github.com/allisonmazurek/BeeGameCOMP250

Other IDE on Windows: Install Git Bash Follow steps for Linux/Mac using Git Bash.

# Contributions Anyone?
Intrested in making this GUI into a game? Fork this repository (https://help.github.com/en/github/getting-started-with-github/fork-a-repo) and add whatever you like. 
Pull request and if your code is cool, I'll merge it!! 

# Questions? 
Reach me on Messenger or allison.mazurek5@gmail.com 



