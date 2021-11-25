package nl.capgemini.events.bldtc_2021.twitter;

import io.github.redouane59.twitter.IAPIEventListener;

/**
 * Default implementation of {@link IAPIEventListener} so we have to implement just one method.
 */
@FunctionalInterface
public interface TweetStreamedListener extends IAPIEventListener {
    @Override
    default void onStreamError(int httpCode, String error) {
    }

    @Override
    default void onUnknownDataStreamed(String json) {
    }

    @Override
    default void onStreamEnded(Exception e) {
    }
}
