package nl.capgemini.events.bldtc_2021.twitter.tweet;

import io.github.redouane59.twitter.dto.tweet.Tweet;
import nl.capgemini.events.bldtc_2021.twitter.ValueSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class TweetProducer extends KafkaProducer<String, Tweet> {
    public TweetProducer(Properties properties) {
        super(properties, new StringSerializer(), new ValueSerializer<>());
    }
}
