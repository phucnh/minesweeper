## Minesweeper
This is the minesweeper game that has been made for [CyberAgent](http://www.cyberagent.co.jp/) new graduates challenges 2015.

The game has been made using [Java language](https://java.com/en/), test on [Java Development Kit, Java SE 7, Update 72](http://www.oracle.com/technetwork/java/javase/7u72-relnotes-2296190.html) and Java Runtime Environment, Java SE 7, update 76 [Click here for download jre 7u76](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html)

This game had built on console base, for run it, please read the User Guide below.

[Screenshots](https://drive.google.com/open?id=0B3seRHYL3yb-fkZxcmxlNWpYRVhlTGF6YUVjRjZjQ2R4TWIyRjFfenloTW5HS3paeFB2WW8&authuser=0)

## User Guide
### Run game from Java Archive file
The game has compiled to Java Archive, executable file (minesweeper.jar). For run the game, please follow below step

1. Check out this source code, or download the jar file on repository

2. Run jar file with java command
    ``` shell
    java -jar minesweeper.jar
    ```
    
    Or using [GNU Make](https://www.gnu.org/software/make/) for linux or mac environment
    ``` shell
    make run-jar
    ```

### Run game from source code
The source code has contain the automatic [GNU Make](https://www.gnu.org/software/make/) for linux or mac environment.

For build and run this game from source code, please follow below step

1. Checkout this source code, or download the zip file
    ``` shell   
    git checkout https://github.com/phucnh/minesweeper.git minesweeper
    cd minesweeper
    ```
    
2. Set the JAVA_HOME environment variable
    ``` shell
    export JAVA_HOME=<your-java-path>
    ```

    For Example
    ``` shell
    export JAVA_HOME=/home/phucnh/jdk1.7.0_72
    ```
    
3. Go to the checkout path and run the game from make file
    ``` shell
    make run
    ```

## Development
This game has been designed using [Template method](http://en.wikipedia.org/wiki/Template_method_pattern) and [Model - View - Controller](http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller) pattern. 

### Packages
1. **Components:** this package contains the game's element that have own logic
    - _Square_: The abstract class, contain the base attribute and behavior of a square.
    - _Empty Square_: Extend from Square, the concrete class of Square, represent Empty Square (no mine around)
    - _Mine Square_: Extend from Square, the concrete class of Square, represent Mine Square
    - _Number Square_: Extend from Square, the concrete class of Square, represent Number Square with value is number of mine around
        
2. **View**: contains the game view, the view will interact with user, show the message and get user input
    - _GameView_: The abstract class, contain the base attribute and behavior of a View
    - _ConsoleView_: Extend from GameView, implement the game for console environment

3. **Logic**: contains the game controller that control the game logic using Components package and control the view to interact with user. The controller is implemented follow [Template method](http://en.wikipedia.org/wiki/Template_method_pattern).
    - _GameController_: The abstract class, contain the base logic of game. The basic game flow function: play(), showMainMenu, showSetting,createNewGame(), isGameEnd()...
    - _ConsoleGameController_: Extend from GameController, implement controller for console environment

## Test
This game has been tested by [Junit 4.2](http://junit.org/) with [Mockito](http://mockito.org) and [Java Hamcrest](http://hamcrest.org/JavaHamcrest/) on [Java Development Kit, Java SE 7, Update 72](http://www.oracle.com/technetwork/java/javase/7u72-relnotes-2296190.html) and Java Runtime Environment, Java SE 7, update 76 [Click here for download jre 7u76](http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html)

All test-case has been written in document. Please, refrence to this [Google Doc](https://drive.google.com/open?id=0B3seRHYL3yb-fmJVeHF0aEJJTEcwbG1OaDhFR1JkRmVwRmZLSnhIdGItbkljZW5tdEtqNWs&authuser=0)

For testing this source code, please follow below step

1. Checkout this source code, or download the zip file
    ``` shell   
    git checkout https://github.com/phucnh/minesweeper.git minesweeper
    cd minesweeper
    ```
    
2. Set the JAVA_HOME environment variable
    ``` shell
    export JAVA_HOME=<your-java-path>
    ```

    For Example
    ``` shell
    export JAVA_HOME=/home/phucnh/jdk1.7.0_72
    ```
    
3. Go to the checkout path and run the test from make file
    - For run all test
    ``` shell
    make test
    ```
    
    - For run test on each component
        1. Bulid the test code
        ``` shell
        make build-test
        ```
        2. Run each element test
            - Test game's components: Board, Square
            ``` shell
            make test-components-board
            make test-components-square
            make test-components-num-square
            ```
            Or
            ``` shell
            make test-components
            ```
            - Test game's controller:
            ``` shell
            make test-controller
            ```
            - Test game's view
            ```shell
            make test-view
            ```

## Contact
When this game has any problem, please contact to me

Skype: phucnh89

Email: [nguyenhongphuc.hut@gmail.com](nguyenhongphuc.hut@gmail.com)

**Thank you very much**
