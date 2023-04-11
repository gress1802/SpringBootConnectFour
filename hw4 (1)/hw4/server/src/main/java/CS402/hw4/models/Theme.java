package CS402.hw4.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "def")
public class Theme {
    //playerTokenString and computerTokenString are the IDs of the tokens in the database
    private String color;
    private Token playerToken;
    private Token computerToken;
   
    public Theme() { }

    private Theme(String color, Token playerToken, Token computerToken) {
        this.color = color;
        this.playerToken = playerToken;
        this.computerToken = computerToken;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Token getPlayerToken() {
        return playerToken;
    }

    public void setPlayerToken(Token playerTokenString) {
        this.playerToken = playerTokenString;
    }

    public Token getComputerToken() {
        return computerToken;
    }

    public void setComputerToken(Token computerTokenString) {
        this.computerToken = computerTokenString;
    }

    @Override
    public String toString() {
        return "Theme [color=" + color + ", computerToken=" + playerToken + ", playerToken="
                + playerToken + "]";
    }

    public static class ThemeBuilder {
        private String color;
        private Token playerTokenT;
        private Token computerTokenT;

        public ThemeBuilder() { }

        public ThemeBuilder color(String color) {
            this.color = color;
            return this;
        }

        public ThemeBuilder playerTokenId(Token playerTokenString) {
            this.playerTokenT = playerTokenString;
            return this;
        }

        public ThemeBuilder computerTokenId(Token computerTokenString) {
            this.computerTokenT = computerTokenString;
            return this;
        }

        public Theme build() {
            return new Theme(color, playerTokenT, computerTokenT);
        }
    }


}
