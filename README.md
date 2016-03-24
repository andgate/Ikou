# Ikou
Tile maze game!

## Todo list for v2
Player Mods
  - Player tile color can be changed
  - Unlockable skins.
  - DLC skins.

Gameplay
  - Levels only last

Tutorial Mode
  - An option in Single Player
  - Turns off automatically

Single Player Mode (with game settings)
  - Game settings (map sizes, number of floors, tutorial)
  - Map size slider, from Small to Large. Small is 10x10, large is 100.
    - No more infinitely growing maps that destroy the heap!

Multiplayer Mode
 - Join a game (local/internet)
 - Host a game
   - Setup number of floors, map sizes, number of players, local/internet
 - Continuous play should be convenient, so players don't stop.

Warp to start
  - Triple tap or start button?
  - Warp to start tile
  - Special effect where the old screen fades out while new screen fades in.
  - Other players should see player fade out and fade into the start.
    - This means warping will send a signal to the other players

Graphic Effects
  - SSAO/HBAO for aesthetic purposes
  - Fade in/fade out for warping
  - Trails to show path travelled.

Tilemap engine
  - Better names for a more distinct dataflow
  - Data-oriented design considerations
  - Greedy meshing, in order to reduce vert counts.
  - Optional, comprehensive benchmarking
  - Object pooling to possibly prevent breaking the heap for large tilemaps.

Maze generator
  - New name system for levels "<adjective>.<verb>.<noun>" that store the level seed.
  - Prevent unsolvable closed mazes
  - Open maze generation

Font rendering improvements
  - Font rendering is slow, needs to be 

Settings
  - Window resolutions (desktop only)
  - Fullscreen (desktop only)
  - SSAO/HBAO/None
  - Anti-alias (maybe?)