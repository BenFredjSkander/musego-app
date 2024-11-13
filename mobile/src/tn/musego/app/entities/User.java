package tn.musego.app.entities;


import tn.musego.app.utils.DateHelper;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;


public class User {
    private int id;

    private String username;

    private String email;

    private boolean locked;

    private String phone;

    private Date createdDate;

    public User(int id, String username, String email, boolean locked, String phone, Date createdDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.locked = locked;
        this.phone = phone;
        this.createdDate = createdDate;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", locked=" + locked +
                ", createdDate=" + createdDate +
                '}';
    }

    public User mapFromJson(Map<String, Object> map) {
        try {
            return new User(
                    ((Double) map.get("id")).intValue(),
                    (String) map.get("username"),
                    (String) map.get("email"),
                    Boolean.parseBoolean((String) map.get("locked")),
                    (String) map.get("phone"),
                    DateHelper.convertDateFormat(map.get("createdDate").toString())
            );
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
