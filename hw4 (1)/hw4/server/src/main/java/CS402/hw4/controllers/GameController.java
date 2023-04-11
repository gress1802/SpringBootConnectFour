package CS402.hw4.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import CS402.hw4.models.Game;
import CS402.hw4.models.Theme;
import CS402.hw4.models.Token;
import CS402.hw4.models.User;
import CS402.hw4.models.Game.Builder;
import CS402.hw4.repositories.GameRepository;
import CS402.hw4.repositories.TokenRepository;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v2")
public class GameController {
    
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @GetMapping("/sids")
    public ResponseEntity<?> getGamesBySid(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            System.out.println(user);
            String sid = user.getId();
            System.out.println("SID: " + sid);
            List<Game> games = gameRepository.getGamesBySid(sid);
            System.out.println(games);
            return new ResponseEntity<>(games, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * This endpoint creates a new game object and then adds it to the list of games associated with the user in the database
     * This endpoint also delivers the new game object to the client
     * The name of the player token is in the body of the request
     * The name of the computer token is in the body of the request
     * The color is in the query with the key "color"
     */
    @PostMapping("/sids")
    public ResponseEntity<?> createGame(HttpSession session, @RequestParam String color, @RequestBody Map<String, String> tokens ) {
        Object authenticatedUser = session.getAttribute("user");
        if (authenticatedUser != null && authenticatedUser instanceof User) {
            User authUser = (User) authenticatedUser;
            String sid = authUser.getId();

            //Validate the color
            String colorCodeRegex = "^#[0-9A-Fa-f]{6}$";
            if(!color.matches(colorCodeRegex)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid color code");
            }

            //Validate the Tokens
            String playerTokenName = tokens.get("playerToken");
            String computerTokenName = tokens.get("computerToken");
            System.out.println("Player token name: " + playerTokenName + " Computer token name: " + computerTokenName);
            System.out.println("color: " + color);
            System.out.println("Player token name: " + playerTokenName + " Computer token name: " + computerTokenName);
            if(playerTokenName == null || computerTokenName == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token name(s) not provided");
            }
            Optional<Token> playerToken = tokenRepository.findByName(playerTokenName);
            Optional<Token> computerToken = tokenRepository.findByName(computerTokenName);

            System.out.println("Player token: " + playerToken + " Computer token: " + computerToken);

            if(!playerToken.isPresent() || !computerToken.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error finding token(s)");
            }

            //Create the new theme object using builder pattern
            System.out.println("player token: " + playerToken.get().getName() + " computer token: " + computerToken.get().getName());
            Theme theme = new Theme.ThemeBuilder()
                .color(color)
                .playerTokenId(playerToken.get())
                .computerTokenId(computerToken.get())
                .build();
            Game newGame = new Builder()
                .sid(sid)
                .grid(getEmptyBoard())
                .theme(theme)
                .status("UNFINISHED")
                .start(getCurrentTime())
                .build();

            //Save the new game to the database and send it back in the response
            gameRepository.save(newGame);
            System.out.println("\n new game: " + newGame.getTheme().getComputerToken() + "\n");
            return ResponseEntity.status(HttpStatus.OK).body(newGame);
            
            
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    //this is a function that returns a 5 row by 7 column array of empty strings
    private char[][] getEmptyBoard() {
        char[][] board = new char[5][7];
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 7; j++) {
                board[i][j] = ' ';
            }
        }
        return board;
    }

    //This is a function that gets the current time in the following form Day Month Date Year
    //For example Thur Feb 14 2019
    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        String day = now.getDayOfWeek().toString();
        String month = now.getMonth().toString();
        String date = Integer.toString(now.getDayOfMonth());
        String year = Integer.toString(now.getYear());
        return day + " " + month + " " + date + " " + year;
    }

    //This is an endpoint that delivers a game object associated with the given sid and gid
    @GetMapping("/gids/{gid}")
    public ResponseEntity<Game> getGameBySidAndGid(@PathVariable(value = "gid") String gid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String sid = user.getId();
        if(sid == null || gid == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(gameRepository.getGameBySidAndId(sid, gid));
    }

    /*
     * This is an endpoint that makes the move and edits the game object (player move)
     * it first checks if the move is valid, then it makes the move for the player and the computer
     * it then checks if the game is over and updates the game object accordingly and finally returns the game object in the body of the response
     */
    @PostMapping("/gids/{gid}")
    public ResponseEntity<?> makeMove(@PathVariable(value = "gid") String gid, @RequestParam("move") String column, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String sid = user.getId();
        if(sid == null || gid == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error finding sid or gid");
        }
        Game game = gameRepository.getGameBySidAndId(sid, gid);
        if(game == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error finding game in database");
        }
        else {
            int move;
            try {
                move = Integer.parseInt(column);
            } catch(NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing the move to integer");
            }
            game = makeMove(game, move);
            gameRepository.save(game);
            return ResponseEntity.status(HttpStatus.OK).body(game);
        }

    }

    private Game makeMove(Game game, int move) {
        char[][] grid = game.getGrid();
        int isFull = 0;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i][move] != ' ') {
                isFull++;
            }
        }
        if (isFull == 5) {
            return game;
        }
        boolean gameOverFull;
        boolean gameOverX;
        boolean gameOverO;
        grid = gridMove(grid, move, 'x');
        int rand = new Random().nextInt(7);
        grid = gridMove(grid, rand, 'o');
        gameOverFull = isFullBoard(grid);
        gameOverX = fourInARow(grid, 'x');
        gameOverO = fourInARow(grid, 'o');
        if (gameOverFull || gameOverX || gameOverO) {
            game.setFinish(getCurrentTime());
            if(gameOverX) { 
                game.setStatus("VICTORY");
            }
            else { 
                game.setStatus("LOSS"); 
            }
            if (gameOverFull) {
                game.setStatus("TIE");
            }
        }
        return game;
    }

    private char[][] gridMove(char[][] grid, int move, char token) {
        if (token == 'x') {
            for (int i = grid.length - 1; i >= 0; i--) {
                if (grid[i][move] == ' ') {
                    grid[i][move] = token;
                    break;
                }
            }
        } else {
            boolean moveDone = false;
            boolean full = isFullBoard(grid);
            while (!moveDone && !full) {
                for (int i = grid.length - 1; i >= 0; i--) {
                    if (grid[i][move] == ' ') {
                        grid[i][move] = token;
                        moveDone = true;
                        break;
                    }
                }
                move = new Random().nextInt(7);
            }
        }
        return grid;
    }



    private boolean isFullBoard(char[][] grid) {
        int count = 0;
        for (char[] chars : grid) {
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] != ' ') {
                    count++;
                }
            }
        }
        return count == 35;
    }

    private boolean fourInARow(char[][] grid, char token) {
        // check vertically
        for (int i = 0; i < grid.length - 3; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == token && grid[i + 1][j] == token && grid[i + 2][j] == token && grid[i + 3][j] == token) {
                    return true;
                }
            }
        }
        // check horizontally
        for (char[] chars : grid) {
            for (int j = 0; j < chars.length - 3; j++) {
                if (chars[j] == token && chars[j + 1] == token && chars[j + 2] == token && chars[j + 3] == token) {
                    return true;
                }
            }
        }
        // check diagonally from right to left
        for (int i = 0; i < grid.length - 3; i++) {
            for (int j = 0; j < grid[i].length - 3; j++) {
                if (grid[i][j] == token && grid[i + 1][j + 1] == token && grid[i + 2][j + 2] == token && grid[i + 3][j + 3] == token) {
                    return true;
                }
            }
        }
        // check diagonally left to right
        for (int i = 0; i < grid.length - 3; i++) {
            for (int j = 3; j < grid[i].length; j++) {
                if (grid[i][j] == token && grid[i + 1][j - 1] == token && grid[i + 2][j - 2] == token && grid[i + 3][j - 3] == token) {
                    return true;
                }
            }
        }
        return false;
    }
}
