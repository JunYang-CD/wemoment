package moment.model;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class PostItem {

    private long id;

    private String uuid;

    private String subject;

    private String ownerUuid;

    private List<CommentItem> comments;

    private Timestamp postTime;
}
