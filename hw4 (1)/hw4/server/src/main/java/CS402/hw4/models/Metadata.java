package CS402.hw4.models;

import java.util.List;

public class Metadata {
    private List<Token> tokens;
    private Theme defaultTheme;

    public Metadata() { }

    private Metadata(List<Token> tokens2, Theme defaultTheme) {
        this.tokens = tokens2;
        this.defaultTheme = defaultTheme;
    }

    public static class Builder {
        private List<Token> tokens;
        private Theme defaultTheme;

        public Builder() { }

        public Builder tokens(List<Token> tokens2) {
            this.tokens = tokens2;
            return this;
        }

        public Builder defaultTheme(Theme defaultTheme) {
            this.defaultTheme = defaultTheme;
            return this;
        }

        public Metadata build() {
            return new Metadata(tokens, defaultTheme);
        }
    }

    /*
     * Getters and Setters for the Metadata class
     */

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Theme getDefaultTheme() {
        return defaultTheme;
    }

    public void setDefaultTheme(Theme defaultTheme) {
        this.defaultTheme = defaultTheme;
    }
}

