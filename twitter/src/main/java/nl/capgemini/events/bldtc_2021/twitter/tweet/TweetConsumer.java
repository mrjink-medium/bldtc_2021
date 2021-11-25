package nl.capgemini.events.bldtc_2021.twitter.tweet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.redouane59.twitter.dto.tweet.Tweet;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import nl.capgemini.events.bldtc_2021.twitter.ConsumerProducer;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TweetConsumer extends ConsumerProducer<TweetV2.TweetData, String, Tweet> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TweetConsumer.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public TweetConsumer(Producer<String, Tweet> producer, String topic) {
        super(producer, topic);
    }

    @Override
    public void accept(TweetV2.TweetData data) {
        send(data.getId(), data);
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(OBJECT_MAPPER.writeValueAsString(data));
            }
        } catch (JsonProcessingException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Can't serialize tweet {}", data.getId(), e);
            }
        }
    }
}
