package com.tn.musego.entities;

import com.tn.musego.entities.enums.RoleEnum;
import com.tn.musego.utils.DateHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private Date createdDate;
    private String username;
    private String email;
    private String password;
    private boolean verified;

    private boolean locked = false;

    private String profilePic;

    private Date birthDate;

    private String phoneNumber;

    private String speciality;

    private Date hiringDate;

    private Set<RoleEnum> roles = new HashSet<>();

    public User(String username, String email, String password, boolean enabled, String profilePic, Date birthDate, String phoneNumber, String speciality, Date hiringDate, Set<RoleEnum> role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.verified = enabled;
        this.profilePic = profilePic;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.speciality = speciality;
        this.hiringDate = hiringDate;
        this.roles = role;
    }

    public Set<RoleEnum> getRoles() {
        roles.add(RoleEnum.ROLE_USER);
        return roles;
    }

    public User mapFromResultSet(ResultSet resultSet) throws SQLException {
        JSONArray jsonArr = new JSONArray((String)resultSet.getObject("roles"));
        return new User(
                resultSet.getLong("id"),
                DateHelper.dateFromTimestamp(resultSet.getTimestamp("created_date")),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getBoolean("is_verified"),
                resultSet.getBoolean("locked"),
                resultSet.getString("profile_pic"),
                DateHelper.dateFromTimestamp(resultSet.getTimestamp("birthdate")),
                resultSet.getString("phone_number"),
                resultSet.getString("speciality"),
                DateHelper.dateFromTimestamp(resultSet.getTimestamp("hiring_date")),
                jsonArr.toList().stream().map(o -> RoleEnum.valueOf((String) o)).collect(Collectors.toSet())
        );
    }

}
