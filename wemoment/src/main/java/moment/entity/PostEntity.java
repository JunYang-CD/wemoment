package moment.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.sql.Timestamp;

@Entity
@Data
public class PostEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    private String subject;

    @Column(nullable = false)
    private String ownerUuid;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="post_uuid", referencedColumnName = "uuid")
    private List<CommentEntity> comments;

    private Timestamp postTime;
}
