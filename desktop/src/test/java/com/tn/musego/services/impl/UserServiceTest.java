package com.tn.musego.services.impl;

import com.tn.musego.entities.User;
import com.tn.musego.entities.enums.RoleEnum;
import com.tn.musego.exceptions.MyCustomException;
import com.tn.musego.utils.DBConnection;
import com.tn.musego.utils.DateHelper;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
//
//    private static UserService userService;
//
//
//    @BeforeAll
//    static void beforeAll() {
//
//        Connection conn = DBConnection.getInstance().getConnection();
//        try {
//            conn.setAutoCommit(false);
//            System.out.println("before all");
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        userService = new UserService(conn);
//
//    }
//
//    @AfterAll
//    static void afterAll() {
//        Connection conn = DBConnection.getInstance().getConnection();
//        try {
//            conn.setAutoCommit(true);
//            System.out.println("after all");
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    @DisplayName("Create new user")
//    @Order(1)
//    void createEntity() {
//        System.out.println("order 1");
////        User user = new User(
////                "youssef",
////                "youssef@gmail.com",
////                "hello123",
////                false,
////                "",
////                DateHelper.dateFromString("8/09/1991"),
////                "+46952687",
////                "artiste",
////                DateHelper.dateFromString("5/05/2001"),
////                RoleEnum.ROLE_ADMIN
////        );
//        User user2 = new User(
//                "skander",
//                "skander@gmail.com",
//                "skander",
//                true,
//                "",
//                DateHelper.dateFromString("8/09/1999"),
//                "+46952687",
//                "",
//                DateHelper.dateFromString("5/05/2001"),
//                RoleEnum.ROLE_USER
//        );
////        userService.createEntity(user);
//        userService.createEntity(user2);
//    }
//
//    @Test
//    @DisplayName("Get user by id")
//    @Order(2)
//    void getEntityByID() throws MyCustomException {
//        System.out.println("order 2");
//
//        User userFromDb = userService.getEntityByID(1L);
//        assertEquals("youssef@gmail.com", userFromDb.getEmail(), "Email not matching for user1");
//        userFromDb = userService.getEntityByID(2L);
//        assertEquals("skander@gmail.com", userFromDb.getEmail(), "Email not matching for user2");
//    }
//
//    @Test
//    @DisplayName("Get all users")
//    @Order(3)
//    void getAll() {
//        System.out.println("order 3");
//
//        List<User> users = userService.getAll();
//        assertEquals(2, users.size(), "problem inserting users");
//    }
//
//    @Test
//    @DisplayName("Update user")
//    @Order(6)
//    void updateEntity() throws MyCustomException {
//        System.out.println("order 6");
//
//        User userFromDb = userService.getEntityByID(2L);
//        userFromDb.setRoles(RoleEnum.ROLE_FORMATEUR);
//        userFromDb.setSpeciality("poterie");
//        userService.updateEntity(userFromDb);
//        userFromDb = userService.getEntityByID(2L);
//        assertEquals(RoleEnum.ROLE_FORMATEUR, userFromDb.getRoles(), "Error updating user role");
//        assertEquals("poterie", userFromDb.getSpeciality(), "Error updating user speciality");
//
//    }
//
//    @Test
//    @DisplayName("Delete user by id")
//    @Order(7)
//    void deleteEntityById() throws MyCustomException {
//        System.out.println("order 7");
//
//        userService.deleteEntityById(2L);
//        User userFromDb = userService.getEntityByID(2L);
//        assertNull(userFromDb.getUsername(), "Username should be null");
//    }
//
//    @Test
//    @DisplayName("Get user by criteria")
//    @Order(4)
//    void getEntityByCriteriaSingle() throws MyCustomException {
//        System.out.println("order 4");
//
//        User userFromDb = userService.getEntityByCriteriaSingle(Map.of("id", 1L));
//        User userFromDb2 = userService.getEntityByCriteriaSingle(Map.of("email", "youssef@gmail.com", "phone_number", "+46952687"));
//
//
//        assertEquals("youssef", userFromDb.getUsername(), "v1 username not matching");
//        assertEquals("youssef", userFromDb2.getUsername(), " v2 username not matching");
//
//    }
//
//    @Test
//    @DisplayName("Get User list by criteria")
//    @Order(5)
//    void getEntityByCriteriaList() {
//        System.out.println("order 5");
//        List<User> users = userService.getEntityByCriteriaList(Map.of("enabled", false, "role", RoleEnum.ROLE_USER.toString()));
//        assertEquals(2, users.size(), "users list by criteria not matching");
//        users = userService.getEntityByCriteriaList(Map.of("enabled", false, "role", RoleEnum.ROLE_ADMIN.toString()));
//        assertEquals(2, users.size(), "users list by criteria not matching");
//
//    }

}