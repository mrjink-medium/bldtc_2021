# .BLD Tech Conference 2021
## Exploring streaming data with KSQL
### Introduction

This project goes with the .BLD() Tech Conference 2021 talk I did on 25 November 2021.

### Setup

To start the Kafka cluster, run the [docker-compose.yaml](docker-compose.yaml) in the project with `docker-compose up -d`.

To run the [Main class](twitter/src/main/java/nl/capgemini/events/bldtc_2021/twitter/Main.java), you need to configure two properties:
1. `-Dtwitter.json=/path/to/twitter.json`
2. `-Dkafka.properties=/path/to/kafka.properties`

Example files are present in the project.

### Example queries

#### Tweet languages
Create a stream with Tweet ids and languages:

`CREATE STREAM TWEET_LANGUAGE (ID STRING, LANG STRING) WITH (KAFKA_TOPIC='twitter-tweet', VALUE_FORMAT='JSON');`

Query the stream:

`SELECT * FROM TWEET_LANGUAGE EMIT CHANGES;`

Create a table with languages and counts:

`CREATE TABLE LANGUAGES AS SELECT LANG, COUNT(*) AS CNT FROM TWEET_LANGUAGE GROUP BY LANG;`

Query the table:

`SELECT * FROM LANGUAGES EMIT CHANGES;`

Create a stream with Tweets:

`CREATE STREAM TWEET (ID STRING, AUTHOR_ID STRING, TEXT STRING) WITH (KAFKA_TOPIC='twitter-tweet', VALUE_FORMAT='JSON');`

Create a stream with Users:

`CREATE STREAM USER (ID STRING, USERNAME STRING) WITH (KAFKA_TOPIC='twitter-user', VALUE_FORMAT='JSON');`

Join two streams:

`CREATE STREAM TWEET_AUTHOR AS SELECT TWEET.ID AS TWEET_ID, TEXT, AUTHOR_ID, USERNAME FROM TWEET INNER JOIN USER WITHIN 10 MINUTES ON TWEET.AUTHOR_ID = USER.ID;`

### Links

[The .BLD() community Meetup](https://www.meetup.com/bld-tech-talks/)

[The .BLD() community YouTube channel](https://www.youtube.com/channel/UCBnrzJyB2zLu62UHDapM3iQ)

[Apache Kafka](https://kafka.apache.org/)

[Confluent Platform Quickstart](https://docs.confluent.io/platform/current/quickstart/ce-docker-quickstart.html)

[Twitter Developer: API](https://developer.twitter.com/en/products/twitter-api)

[This code on GitHub](https://github.com/mrjink-medium/bldtc_2021)

[The slides on SlideShare](https://www.slideshare.net/mrjink/bld-tech-conference-data-exploration-with-ksql/)
