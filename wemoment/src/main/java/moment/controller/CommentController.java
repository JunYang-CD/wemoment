package moment.controller;

import moment.model.CommentItem;
import moment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("/moments/comment")
    CommentItem addComment(@RequestHeader("uuid") String uuid, @RequestBody CommentItem commentItem) {
        commentItem.setOwnerUuid(uuid);
        return commentService.addComment(commentItem);
    }
}
