package CS402.hw4.models;

import java.util.UUID;

public class Game {
    private String id;
    private Theme theme;
    private String status;
    private String start;
    private String finish;
    private char[][] grid;
    private String sid;

    public Game() { }

    private Game(Theme theme, String status, String start, String finish, char[][] grid, String sid) {
        this.id = UUID.randomUUID().toString();
        this.theme = theme;
        this.status = status;
        this.start = start;
        this.finish = finish;
        this.grid = grid;
        this.sid = sid;
    }

    public static class Builder {
        private Theme theme;
        private String status;
        private String start;
        private String finish;
        private char[][] grid;
        private String sid;

        public Builder() { }

        public Builder theme(Theme theme) {
            this.theme = theme;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder start(String start) {
            this.start = start;
            return this;
        }

        public Builder finish(String finish) {
            this.finish = finish;
            return this;
        }

        public Builder grid(char[][] grid) {
            this.grid = grid;
            return this;
        }

        public Builder sid(String sid) {
            this.sid = sid;
            return this;
        }

        public Game build() {
            return new Game(theme, status, start, finish, grid, sid);
        }
    }

    /*
     * Getters and Setters for the Game class
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public char[][] getGrid() {
        return grid;
    }

    public void setGrid(char[][] grid) {
        this.grid = grid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}

