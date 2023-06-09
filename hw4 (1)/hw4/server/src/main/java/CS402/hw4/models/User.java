package CS402.hw4.models;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import CS402.hw4.repositories.TokenRepository;



@Document(collection = "users")
public class User implements UserDetails{
    //id autogenerated by mongoDB
    @Autowired
    private TokenRepository tokenRepository;

    @Id
    private String id;

    @Field("first")
    private String first;

    @Field("last")
    private String last;

    @Field("username")
    private String username;

    @Field("password")
    private String password;

    @Field("defaults")
    private Theme defaults;

    public User() { }

    private User(String username, String password, String firstName, String lastName) {
        this.first = firstName;
        this.last = lastName;
        this.username = username;
        //Encode the password using BCrypt
        this.password = new BCryptPasswordEncoder().encode(password);
        //Using "Manuel Referencing" to set the default Theme object. *Theme(color, Alexia Token ID, Charlie Token ID)*
        this.defaults = new Theme.ThemeBuilder().color("E66465").playerTokenId(tokenRepository.findById("64290524fae0517d4fee5688").get()).computerTokenId(tokenRepository.findById("642908c8b80d2537512ad9e9").get()).build();
        
    }

    public static class Builder {
        private String username;
        private String password;
        private String firstName;
        private String lastName;

        public Builder() { }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public User build() {
            return new User(username, password, firstName, lastName);
        }
    }

    /*
     * Getters and Setters for the User class
     */

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Theme getDefaults() {
        return defaults;
    }

    public void setDefaults(Theme defaults) {
        this.defaults = defaults;
    }

    public String getId() {
        return id;
    }

    //returning an empty list because there are no roles defined for the user in this application
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
