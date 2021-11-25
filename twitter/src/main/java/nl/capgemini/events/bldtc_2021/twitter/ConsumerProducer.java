package nl.capgemini.events.bldtc_2021.twitter;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public abstract class ConsumerProducer<S, K, V> implements Consumer<S> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerProducer.class);

    private final Producer<K, V> producer;
    private final String topic;

    public ConsumerProducer(Producer<K, V> producer, String topic) {
        this.producer = producer;
        this.topic = topic;
    }

    public void send(K key, V value) {
        producer.send(new ProducerRecord<>(topic, key, value), (m, e) -> {
            if (e != null) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Failed to send to topic {}!", topic, e);
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Produced record to topic {}} partition [{}}] @ offset {}}", m.topic(), m.partition(), m.offset());
                }
            }
        });
    }
}
