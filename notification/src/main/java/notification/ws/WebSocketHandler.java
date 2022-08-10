package notification.ws;

import lombok.extern.slf4j.Slf4j;
import notification.mq.MessageSource;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@EnableBinding(MessageSource.class)
public class WebSocketHandler extends TextWebSocketHandler {

    @StreamListener("post_update")
    public void onReceive(String msg) {
        System.out.println(msg);
        for (Map.Entry<String, WebSocketSession> entry : sessionMap.entrySet()) {
            WebSocketSession session = entry.getValue();
            try {
                session.sendMessage(new TextMessage(msg));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        log.debug("afterConnectionEstablished");
        String uuid = getUuid(session);
        if (uuid != null) {
            sessionMap.put(uuid, session);
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        log.debug("handleMessage " + message.getPayload());
        log.debug("handleMessage " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        log.debug("afterConnectionClosed");
        String uuid = getUuid(session);
        if (uuid != null) {
            log.debug("afterConnectionClosed " + uuid);
            sessionMap.remove(uuid);
        }
    }

    private String getUuid(WebSocketSession session) {
        List<String> headerUuid = session.getHandshakeHeaders().get("uuid");
        if (headerUuid != null && !headerUuid.isEmpty()) {
            return headerUuid.get(0);
        }
        return null;
    }
}
