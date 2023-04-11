package CS402.hw4.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
public class UserController {

    @GetMapping("/api/v2/who")
    public ResponseEntity<?> who(HttpSession session) {
        if (session != null) {
            Object user = session.getAttribute("user");
            if (user != null) {
                return ResponseEntity.status(HttpStatus.OK).body(user);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("No authenticated User");
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("No authenticated User");
        }
    }
}

