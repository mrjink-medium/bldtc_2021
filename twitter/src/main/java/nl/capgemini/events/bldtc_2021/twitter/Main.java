package nl.capgemini.events.bldtc_2021.twitter;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.signature.TwitterCredentials;
import nl.capgemini.events.bldtc_2021.twitter.tweet.TweetConsumer;
import nl.capgemini.events.bldtc_2021.twitter.tweet.TweetProducer;
import nl.capgemini.events.bldtc_2021.twitter.user.UserConsumer;
import nl.capgemini.events.bldtc_2021.twitter.user.UserProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Predicate;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    static final String PATH_TO_CREDENTIALS =
            Objects.requireNonNull(System.getProperty("twitter.json"), "Please set twitter.json");
    static final String PATH_TO_KAFKA_CONFIG =
            Objects.requireNonNull(System.getProperty("kafka.properties"), "Please set kafka.properties");

    public static void main(String[] args) {
        try {
            TwitterCredentials twitterCredentials =
                    TwitterClient.OBJECT_MAPPER.readValue(new File(PATH_TO_CREDENTIALS), TwitterCredentials.class);
            TwitterClient twitterClient = new TwitterClient(twitterCredentials);

            Properties properties = loadConfig(PATH_TO_KAFKA_CONFIG);
            properties.put(ProducerConfig.ACKS_CONFIG, "1");

            TweetConsumer tweetConsumer = new TweetConsumer(new TweetProducer(properties), "twitter-tweet");
            UserConsumer userConsumer = new UserConsumer(new UserProducer(properties), "twitter-user");
            Predicate<TweetV2.TweetData> tweetTest =
                    data -> !data.isPossiblySensitive();
            twitterClient.startSampledStream(
                    (TweetStreamedListener) tweet -> {
                        TweetV2.TweetData data = ((TweetV2) tweet).getData();
                        if (tweetTest.test(data)) {
                            tweetConsumer.accept(data);
                            userConsumer.accept(tweet.getUser());
                        }
                    });
        } catch (IOException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failure!", e);
            }
        }
    }

    public static Properties loadConfig(final String configFile) throws IOException {
        if (!Files.exists(Paths.get(configFile))) {
            throw new FileNotFoundException(configFile);
        }
        final Properties cfg = new Properties();
        try (InputStream inputStream = new FileInputStream(configFile)) {
            cfg.load(inputStream);
        }
        return cfg;
    }
}
