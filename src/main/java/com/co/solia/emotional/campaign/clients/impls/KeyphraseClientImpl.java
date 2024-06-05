package com.co.solia.emotional.campaign.clients.impls;

import com.co.solia.emotional.campaign.clients.clients.KeyphraseClient;
import com.co.solia.emotional.campaign.models.dtos.rq.KeyphraseClientRqDto;
import com.co.solia.emotional.campaign.models.dtos.rs.KeyphraseClientRsDto;
import com.co.solia.emotional.keyphrase.models.dtos.rq.EmotionalClientRqDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * client to call the keyphrase result.
 *
 * @author luis.bolivar.
 */
@Component
@Slf4j
public class KeyphraseClientImpl implements KeyphraseClient {

    /**
     * emotional url to consume its api.
     */
    private final String keyphraseUrl;

    /**
     * build the client to consume the other endpoints.
     */
    private final OkHttpClient client = new OkHttpClient()
            .newBuilder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .connectionPool(new ConnectionPool(5, 60, TimeUnit.SECONDS))
            .build();

    public KeyphraseClientImpl(@Value("${solia.emotional.keyphrase.url}") final String keyphraseUrl) {
        this.keyphraseUrl = keyphraseUrl;
    }

    /**
     * generate the keyphrases associated to the messages.
     *
     * @param request to call the keyphrases.
     * @param emotion to generate the keyphrases.
     * @return {@link Optional} of {@link KeyphraseClientRsDto}.
     */
    @Override
    public Optional<KeyphraseClientRsDto> generateKeyphrases(final KeyphraseClientRqDto request, final String emotion) {
        log.info("[generateKeyphrases]: call the keyphrases.");
        final String url = getKeyphraseUrl(emotion);
        getRq(request, url);
        return Optional.empty();
    }

    /**
     * generate the keyphrases associated to the messages.
     *
     * @param messages to call the keyphrases.
     * @param emotion  to generate the keyphrases.
     * @return {@link Optional} of {@link KeyphraseClientRsDto}.
     */
    @Override
    public Optional<KeyphraseClientRsDto> generateKeyphrases(final List<String> messages, final String emotion) {
        return generateKeyphrases(KeyphraseClientRqDto.builder()
                .messages(messages)
                .build(), emotion);
    }

    /**
     * call the keyphrase endpoint.
     * @param rq for call.
     * @return {@link Optional} of {@link Response}.
     */
    private Optional<Response> call(final Request rq) {
        Optional<Response> result = Optional.empty();
        try {
            final Call call = client.newCall(rq);
            final Response response = call.execute();
            result = Optional.of(response);
            log.info("[call] call to keyphrase ok.");
        } catch(Exception e) {
            log.error("[call]: Error calling the keyphrase service: {}", e.getMessage());
        }

        return result;
    }

    /**
     * get the url to consume the keyphrase endpoint.
     * @param emotion to generate the keyphrase.
     * @return {@link String} the keyphrase url.
     */
    private String getKeyphraseUrl(final String emotion){
        return keyphraseUrl + "compute/" + emotion;
    }

    /**
     * get the request to call keyphrase api.
     * @param rq for generate the request.
     * @param url to consume the api.
     * @return {@link Request}.
     */
    private Request getRq(final KeyphraseClientRqDto rq, final String url) {
        return new Request.Builder()
                .url(url)
                .post(RequestBody.create(new Gson().toJson(rq).getBytes()))
                .addHeader("Content-Type", "application/json")
                .build();
    }
}
