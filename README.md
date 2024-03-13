# CSCI 2020 Final Project
This is the git repository of Final Project of CSCI 2020 Winter Semester. This is a video game project called "ASTEROIDS" that has the requested features.

## Getting Started
This instruction will get you a copy of the code up and running on your local machine. Follow the instruction from installing the IDE and Library to Running the Code.

### Pre-requisite
```
IntelliJ IDEA Community Version
JavaFX SDK 11.0.2
```
* JavaFX SDK: https://gluonhq.com/products/javafx/
* IntelliJ IDEA: https://www.jetbrains.com/idea/download/

### Installing

1. Download JavaFX SDK and pick the appropriate platform
2. Download IntelliJ IDEA Community Version and pick the appropriate platform 
3. Install IntelliJ IDEA on your local machine
4. Clone this repository
5. Open JavaFX and Open Project of this repository.
6. Once opened, go to File > Project Structure.
7. Go to Project > Project SDK as "`12`"
8. Go to Modules, select "`/src`" directory as Sources and "`/out`" directory as Excluded.
9. Select "`/src/Textures`", "`/src/Misc`", "`/src/Misc/SFX`" and "`/src/Misc/Music`" as Resources.
10. Go to Libraries, select "+" icon, go to your JavaFX SDK root folder and select "`lib`" folder.
11. Click OK

## Running the Code

1. Select Run > Edit Configuration...
2. Select Application > "+" symbol on the top left > Application
3. In VM Options textfield, write "`--module-path (JavaFX SDK Root Folder)\lib --add-modules javafx.controls,javafx.fxml`"
4. Put the appropriate code's name in the Name textfield
5. In Main Class textfield, write "`Main`"

## Playing the Game

### Controls
* Movement: `LEFT & RIGHT ARROW KEYS`
* Slow Down: `LEFT-SHIFT`
* Shoot: `Z`

### Objective
* Destroy as many asteroids as you can. 
* DO NOT let any asteroids pass through the screen
* DO NOT let the asteroids hit you.


