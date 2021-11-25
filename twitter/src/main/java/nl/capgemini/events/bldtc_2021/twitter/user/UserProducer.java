package nl.capgemini.events.bldtc_2021.twitter.user;

import io.github.redouane59.twitter.dto.user.User;
import nl.capgemini.events.bldtc_2021.twitter.ValueSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class UserProducer extends KafkaProducer<String, User> {
    public UserProducer(Properties properties) {
        super(properties, new StringSerializer(), new ValueSerializer<>());
    }
}
