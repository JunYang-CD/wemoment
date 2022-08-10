package moment.model;

import lombok.Data;

@Data
public class CommentItem {

    private long id;

    private String uuid;

    private String ownerUuid;

    private String postUuid;

    private String toUuid;

    private String comment;
}
