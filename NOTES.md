
# REST API Minesweeper
### As a user, I should be able to:  
- start a new game  
- reset current game  
- click cell  
- flag cell  
- enter my username  
- change my username  
- track current game's time  
- resume previous game
- modify gameplay: rows, colums, mines (Bonus: random?)  

### Extra miles:  
- authentication: password, security.  
- stats?
  
  
### Cases:  
- start a new game: Create a new board for the specified gameplay parameters.    
    - Username not specified —> unauthorized response.  
    - User has no current game —> ok response, returns a new board with selected gameplay parameters  
    - User currently running a game —> ok response, returns game’s choice validation.  
-  Reset a game: ok response, returns a new board with selected gameplay parameters.  
- click cell: hit a specified cell and get the result.  
    - first cell click —> ok response, starts the game and returns the affected cells with the corresponding output.  
    - any cell click —> ok responnse:  
        - if cell already clicked: return no affectation.  
        - if cell contains mine: mark game as over, returns all mines cells.  
        - if cell is not a mine: get revealed cells and return output information.  
        - if last cell clicked: mark game as over and return game ending state.  
- flag a cell: put a mark in a cell to block from clicking.  
    - if cell already clicked —> return no affectation.  
    - if cell already flagged —> ok response, remove flag.  
    - if cell empty (no click, no flag) —> ok response: add flag to cell and lower mines count.  
- sign in: log in with a username.  
    - username exists:   
        - bonus: user’s home? stats and current game.  
        - run start new game.  
    - username does not exists: bad request  —> user does not exists.  
- sign up: set up a new username.  
    - username exists —> bad request.  
    - username does not exists: ok response -> save user and run start new game.  
- sign out: ?¿

