package com.co.solia.emotional.campaign.clients.impls;

import com.co.solia.emotional.campaign.clients.clients.EmotionalClient;
import com.co.solia.emotional.campaign.models.dtos.rs.EmotionalClientRsDto;
import com.co.solia.emotional.campaign.models.mappers.EmotionalMapper;
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

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * implementation of {@link EmotionalClient}.
 *
 * @author luis.bolivar.
 */
@Component
@Slf4j
public class EmotionalClientImpl implements EmotionalClient {

    /**
     * emotional url to consume its api.
     */
    private final String emotionalUrl;

    public EmotionalClientImpl(@Value("${solia.emotional.emotional.url}") final String emotionalUrl) {
        this.emotionalUrl = emotionalUrl;
    }

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
     * get a emotional processing by id.
     *
     * @param id to get the emotional processing.
     * @return {@link Optional} of {@link EmotionalClientRsDto}.
     */
    @Override
    public Optional<EmotionalClientRsDto> getById(final UUID id) {
        final Request rq = getRqToGetById(getUrlGetById(id));
        return callGetById(rq).flatMap(rs -> {
           log.info("[getById]: response: {}", rs.body());
           return EmotionalMapper.getFromRs(rs);
        });
    }

    /**
     * call the keyphrase endpoint.
     * @param rq for call.
     * @return {@link Optional} of {@link Response}.
     */
    private Optional<Response> callGetById(final Request rq) {
        Optional<Response> result = Optional.empty();
        try {
            final Call call = client.newCall(rq);
            final Response response = call.execute();
            result = Optional.of(response);
            log.info("[callGetById] call to keyphrase ok.");
        } catch(Exception e) {
            log.error("[callGetById]: error getting the emotional processing by id error: {}", e.getMessage());
        }

        return result;
    }

    /**
     * get the request to call emotional api.
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

    /**
     * get the url to call the emotional to get by id.
     * @param id to get the emotional processing.
     * @return {@link String} with the url.
     */
    private String getUrlGetById(final UUID id) {
        return emotionalUrl + "compute/unique/" + id;
    }

}
