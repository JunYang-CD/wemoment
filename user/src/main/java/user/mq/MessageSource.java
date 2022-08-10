package user.mq;

import org.springframework.messaging.MessageChannel;
import org.springframework.cloud.stream.annotation.Output;

public interface MessageSource {

    @Output("user_update")
    MessageChannel userChannel();
}
