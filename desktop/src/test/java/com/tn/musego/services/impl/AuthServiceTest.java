package com.tn.musego.services.impl;

import com.tn.musego.entities.User;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.FunctionHelper;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Skander Ben Fredj
 * @created 22-Feb-23
 * @project musego
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthServiceTest {

    private static AuthService authService;

    @BeforeAll
    static void beforeAll() {

        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(false);
            System.out.println("before all");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        authService = new AuthService(conn);

    }

    @AfterAll
    static void afterAll() {
        Connection conn = DBConnection.getInstance().getConnection();
        try {
            conn.setAutoCommit(true);
            System.out.println("after all");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("successful login to account")
    @Order(1)
    void login() throws MyCustomException {
        User user = authService.login("skander@gmail.com", "skander123");
        assertNotNull(user.getUsername(), "username must not be empty");
    }

    @Test
    @DisplayName("login with incorrect password")
    @Order(1)
    void loginWrongPassword() {
        NumberFormatException thrown = Assertions.assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt("One");
        }, "NumberFormatException was expected");

        Assertions.assertEquals("For input string: \"One\"", thrown.getMessage());
//        Assertions.assertThrows(MyCustomException.class,() -> authService.login("skander@gmail.com", "skaer"));
        assertThrows(MyCustomException.class, () -> authService.login("skander@gmail.com", "skaer"));
    }

    @Test
    void signup() {
    }

    @Test
    @DisplayName("Test password validation")
    @Order(2)
    void testPasswordValidation() {
        String plaintext = "skander123";
        String hash = "$2a$10$oHAGljwq6XzPh1FhVQYgXuQLDLECBPNjlOZ7LqojPFsuJXAMVzP7W";
        System.out.println(FunctionHelper.passwordMatch(hash, plaintext));
    }


    @Test
    @DisplayName("encrypt password")
    @Order(2)
    void encryptPassword() {
        String plaintext = "skander123";
        String hash = "$2a$10$JDBV5yiNDgDl5RbrDDDpX.PqW7C7jGGjwBwtzBw249IbXCnRKAsQS";
        System.out.println(FunctionHelper.encryptPassword(plaintext));
    }
}