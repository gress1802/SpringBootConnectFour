package CS402.hw4.models;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "tokens")
public class Token {
    @Id
    private String id;
    private String name;
    private String url;

    public Token() { }

    private Token(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static class Builder {
        private String name;
        private String url;

        public Builder() { }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Token build() {
            return new Token(name, url);
        }
    }

    /*
     * Getters and Setters for the Token class
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

