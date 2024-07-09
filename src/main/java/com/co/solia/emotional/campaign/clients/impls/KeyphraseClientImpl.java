package com.co.solia.emotional.campaign.clients.impls;

import com.co.solia.emotional.campaign.clients.clients.KeyphraseClient;
import com.co.solia.emotional.campaign.models.dtos.rs.KeyphraseClientRsDto;
import com.co.solia.emotional.campaign.models.mappers.KeyphraseMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * implementation of {@link KeyphraseClient}.
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

    /**
     * default constructor.
     * @param keyphraseUrl keyphrase url.
     */
    @Autowired
    public KeyphraseClientImpl(@Value("${solia.emotional.keyphrase.url}") final String keyphraseUrl){
        this.keyphraseUrl = keyphraseUrl;
    }

    /**
     * get the keyphrase by id.
     *
     * @param id to get the keyphrase.
     * @return {@link Optional} of {@link KeyphraseClientRsDto}.
     */
    @Override
    public Optional<KeyphraseClientRsDto> getKeyphraseById(UUID id) {
        final Request rq = getRqToGetById(getUrlGetById(id));
        return callGetKeyphraseById(rq).flatMap(rs -> {
            log.info("[getKeyphraseById]: response: {}", rs.body());
            return KeyphraseMapper.getFromRs(rs);
        });
    }

    /**
     * call the keyphrase endpoint.
     * @param rq for call.
     * @return {@link Optional} of {@link Response}.
     */
    private Optional<Response> callGetKeyphraseById(final Request rq) {
        Optional<Response> result = Optional.empty();
        try {
            final Call call = client.newCall(rq);
            final Response response = call.execute();
            result = Optional.of(response);
            log.info("[callGetKeyphraseById] call to keyphrase ok.");
        } catch(Exception e) {
            log.error("[callGetKeyphraseById]: error getting the brand processing by id error: {}", e.getMessage());
        }

        return result;
    }

    /**
     * get a keyphrase url to get keyphrase by id.
     * @param id keyphrase identifier.
     * @return {@link String} with the keyphrase url.
     */
    private String getUrlGetById(final UUID id) {
        return keyphraseUrl + "compute/keyphrase/" + id;
    }

    /**
     * get the request to call brand api.
     * @param url to consume the api.
     * @return {@link Request}.
     */
    private Request getRqToGetById(final String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .build();
    }
}
