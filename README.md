# Tetris
A Tetris game with an AI, made using JavaFX

This is a clone of guideline tetris, originaly made for a school project.

Executible .jar files for the opaque and transparent versions of the program can be found in [out/artifacts/Tetris_jar](https://github.com/elw773/Tetris/tree/master/out/artifacts/Tetris_jar) and [out/artifacts/TransparentTetris_jar](https://github.com/elw773/Tetris/tree/master/out/artifacts/TransparentTetris_jar)

The ['AI'](https://github.com/elw773/Tetris/blob/master/src/Main/AI.java) works by scoring possible moves (all x-positions and all orientations for the current tetromino and the held tetromino), based on the height of the highest square and the number of wells, holes and edges, and then changing the game input each frame to accomplish the move with the highest score.
