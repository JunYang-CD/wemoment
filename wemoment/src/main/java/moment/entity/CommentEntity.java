package moment.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(unique = true, nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String ownerUuid;

    @ManyToOne
    @JoinColumn(name = "post_uuid", referencedColumnName = "uuid")
    private PostEntity postEntity;

    private String toUuid;

    private String comment;
}
