package nl.capgemini.events.bldtc_2021.twitter.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.redouane59.twitter.dto.user.User;
import nl.capgemini.events.bldtc_2021.twitter.ConsumerProducer;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserConsumer extends ConsumerProducer<User, String, User> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserConsumer.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public UserConsumer(Producer<String, User> producer, String topic) {
        super(producer, topic);
    }

    @Override
    public void accept(User user) {
        send(user.getId(), user);
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(OBJECT_MAPPER.writeValueAsString(user));
            }
        } catch (JsonProcessingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Can't serialize user {}", user.getId(), e);
            }
        }
    }
}
