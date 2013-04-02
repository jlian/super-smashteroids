Team 3 - Super Smashteroids
===========================

An Asteroids-inspired (1979 game, by Atari) game written in Java.

Implementers
------------

This game is developed by Omar Abdelkader, John Lian, Henry Lu, Charlie Marokhovsky, Nicolas Martin, and Andrew Penhale. 

How to Run
----------

## If you would like to compile yourself:

	git clone https://github.com/winter2013-ecse321-mcgill/team3.git
	cd team3/src
	javac astr_pkg/MainMenu.java
	java astr_pkg/MainMenu

## Or you could run directly from the our binaries:

	git clone https://github.com/winter2013-ecse321-mcgill/team3.git
	cd team3/bin
	java astr_pkg/MainMenu
	
It would be preferable to have `javac -version` of 1.7.0 or above.

How to Play 
-----------

### Moving

## By default:

- W: accelerate
- A: turn left
- D: turn right
- Space: shoot

## Alternatively, if changed in options:

- Up: accelerate
- Left: turn left
- Right: turn right
- Space: shoot

### Quitting

- ESC: pause the game

Known Issues
------------

- Currently, if the program is compiled outside of Eclipse (our chosen IDE), anything resources (fonts, images, music) cannot be used by the game due to directory issues. 
- Once all the lives run out, the game crashes. We are working on a fix.

Dependencies
------------

Super Smasteroids requires the latest version of Java installed to compile and run successfully. 
