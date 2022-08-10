package model;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private String name;
    private String phone;
    private Integer gender;
    private Integer age;
    private String uuid;
    private List<FriendRelation> friendRelations;
}
