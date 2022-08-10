package user.entity;

import javax.persistence.*;

@Entity
@Table(name = "friend_entity")
public class FriendRelationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="owner_uuid",referencedColumnName = "uuid")
    private UserEntity owner;

    @OneToOne
    @JoinColumn(name = "friend_uuid", referencedColumnName = "uuid")
    private UserEntity friend;

    private String nickName;

    @Column(nullable = true)
    private String status;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public UserEntity getFriend() {
        return friend;
    }

    public void setFriend(UserEntity friend) {
        this.friend = friend;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
