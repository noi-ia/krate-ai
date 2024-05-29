package com.co.solia.emotional.keyphrase.clients.impls;

import com.co.solia.emotional.emotional.clients.impls.CleanClientImpl;
import com.co.solia.emotional.keyphrase.clients.clients.EmotionalClient;
import com.co.solia.emotional.keyphrase.models.dtos.rq.EmotionalClientRqDto;
import com.co.solia.emotional.keyphrase.models.dtos.rs.EmotionalClientRsDto;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * emotion client implementation to call the emotional api.
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
     * @param emotionalUrl emotional url.
     */
    @Autowired
    public EmotionalClientImpl(@Value("${solia.emotional.emotional.url}") final String emotionalUrl){
        this.emotionalUrl = emotionalUrl;
    }

    /**
     * {@inheritDoc}.
     * @param emotionalRq
     * @return
     */
    @Override
    public Optional<EmotionalClientRsDto> compute(final EmotionalClientRqDto emotionalRq) {
        final String url = getEmotionalUrl();
        final Request rq = getEmotionalRq(emotionalRq, url);
        return callEmotional(rq)
                .filter(Response::isSuccessful)
                .flatMap(EmotionalClientImpl::getResponse);
    }

    /**
     * get a {@link EmotionalClientRsDto} from the {@link Response}.
     * @param rs to map.
     * @return {@link Optional} of {@link EmotionalClientRsDto}.
     */
    private static Optional<EmotionalClientRsDto> getResponse(final Response rs) {
        Optional<EmotionalClientRsDto> result = Optional.empty();
        try {
            final EmotionalClientRsDto emotionalRs =
                    new Gson().fromJson(rs.body().string(), EmotionalClientRsDto.class);
            result = emotionalRs != null && !emotionalRs.getEmotions().isEmpty() ?
                    Optional.of(emotionalRs) : Optional.empty();
        } catch(Exception e) {
            log.error("[getResponse] error parsing the response: {}", e.getMessage());
        }
        return result;
    }

    /**
     * call the clean endpoint.
     * @param request for call.
     * @return {@link Optional} of {@link Response}.
     */
    private Optional<Response> callEmotional(final Request request) {
        Optional<Response> result = Optional.empty();
        try {
            final Call call = client.newCall(request);
            final Response response = call.execute();
            result = Optional.of(response);
            log.info("[callEmotional] call to emotional ok.");
        } catch(Exception e) {
            log.error("[callEmotional]: Error calling the emotional service: {}", e.getMessage());
        }

        return result;
    }

    /**
     * get the request to call emotional api.
     * @param emotionalRq for generate the request.
     * @param url to consume the api.
     * @return {@link Request}.
     */
    private Request getEmotionalRq(final EmotionalClientRqDto emotionalRq, final String url) {
        return new Request.Builder()
                .url(url)
                .post(RequestBody.create(new Gson().toJson(emotionalRq).getBytes()))
                .addHeader("Content-Type", "application/json")
                .build();
    }

    /**
     * get the url to consume the emotional endpoints.
     * @return {@link String} the emotion url.
     */
    private String getEmotionalUrl(){
        return emotionalUrl + "compute/unique/";
    }


}
