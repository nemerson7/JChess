# JChess
Swing Chess Application

# About this program
This is a 1 or 2 player chess application built with Java and the Swing libraries. I used stockfish for the computer player

# Future ideas / Todo
- ~~Implement computer player. Will likely use stockfish and the UCI protocol~~
- ~~Add button to flip view perspective~~
- Fix weird inconsistency where sometimes two clicks is needed to select something

# Building
The project can be compiled with Maven by running `mvn package` from within the project folder. 
The executable jar can be found in the `target` folder created by building.

# Note
In order for the computer player (the "Move" button, screenshots below show an older version) to work, make sure to run the jar from the commandline with the ```java -jar``` command. Also make sure that stockfish is installed on the PATH. To install on MacOS, do ```brew install stockfish``` or on Debian family do ```sudo apt-get install stockfish```

# JDK Version
OpenJDK 16

# Screenshots

<img src="https://github.com/nemerson7/JChess/blob/main/screenshots/img1.png" width="400" height="447">
<img src="https://github.com/nemerson7/JChess/blob/main/screenshots/img2.png" width="400" height="447">

# References
- https://www.andreinc.net/2021/04/22/writing-a-universal-chess-interface-client-in-java
    - This blog post was helpful for getting stockfish to work

