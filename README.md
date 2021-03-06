Basket Stats
============

#### Basketball stat recorder

![Alt text](/doc/basket-stats.png?raw=true "Screenshot")

How To Run on…
--------------
### Windows
* [**Download**](https://github.com/gto76/basket-stats/archive/master.zip)
* Extract
* Run **`run.bat`**

### Mac 
```
$ git clone https://github.com/gto76/basket-stats.git
$ cd basket-stats
$ ./run-mac
```

### Linux
```
$ git clone https://github.com/gto76/basket-stats.git
$ cd basket-stats
$ ./run
```

Features
--------

* Written in Java.
* Multilevel Undo.
* Prints complete stats on console after every input.
* Games are saved in text file that is the same as the last output on console.
* Players can be pulled out/put in the game, so plus/minus stat can be tracked.
* New players can be added to teams during game.
* Value of different shots can be adjusted at the start of a new game.
* What stats are being recorded can be changed at any time during game.



Output
------
```
--------------------------------------------------------------------------------------------------------------------------------
Sun Jun 14 23:30:00 CEST 1998
EnergySolutions Arena
--------------------------------------------------------------------------------------------------------------------------------
 
CHICAGO BULLS: 67
  
UTAH JAZZ: 58
   
--------------------------------------------------------------------------------------------------------------------------------
BOX SCORE:
--------------------------------------------------------------------------------------------------------------------------------
CHICAGO BULLS
                        FGM-A   3PM-A   FTM-A   +/-     OFF     DEF     TOT     AST     PF      ST      TO      BS      PTS     
Michael Jordan          15-35   3-7     12-15   9       0       1       1       1       2       4       1       0       45      
Toni Kukoc              7-14    1-2     0-0     9       0       3       3       4       3       0       0       0       15      
Dennis Rodman           3-3     0-0     1-2     9       4       4       8       1       5       2       2       1       7       
Totals                  25-52   4-9     13-17           4       8       12      6       10      6       3       1       67      
                        48.1%   44.4%   76.5%   
--------------------------------------------------------------------------------------------------------------------------------
UTAH JAZZ
                        FGM-A   3PM-A   FTM-A   +/-     OFF     DEF     TOT     AST     PF      ST      TO      BS      PTS     
Karl Malone             11-19   0-0     9-11    -9      5       6       11      7       2       1       5       0       31      
Jeff Hornacek           6-12    1-3     4-4     -9      1       5       6       0       0       1       3       0       17      
John Stockton           4-10    1-4     1-2     -9      0       3       3       5       4       0       3       0       10      
Totals                  21-41   2-7     14-17           6       14      20      12      6       2       11      0       58      
                        51.2%   28.6%   82.4%   
--------------------------------------------------------------------------------------------------------------------------------
```

UML Diagram Of Core Classes
---------------------------
![Alt text](/doc/uml-diagram.png?raw=true "Screenshot")
