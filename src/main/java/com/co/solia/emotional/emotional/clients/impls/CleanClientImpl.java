package com.co.solia.emotional.emotional.clients.impls;

import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.emotional.clients.clients.CleanClient;
import com.co.solia.emotional.emotional.models.dtos.rs.CleanClientRsDto;
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
 * clean client implementation.
 *
 * @author luis.bolivar.
 */
@Component
@Slf4j
public class CleanClientImpl implements CleanClient {

    private static final String CLEAN_RESULT = "result";

    private final String cleanUrl;

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
     * constructor class.
     * @param cleanUrl url to clean service.
     */
    @Autowired
    public CleanClientImpl(@Value("${solia.emotional.clean.url}") final String cleanUrl) {
        this.cleanUrl = cleanUrl;
    }
    /**
     * {@inheritDoc}.
     * @param message to clean.
     * @return
     */
    @Override
    public Optional<CleanClientRsDto> cleanMessage(final String message) {
        final String url = getCleanUrl();
        final Request request = getCleanRequest(message, url);
        return callClean(request)
                .filter(Response::isSuccessful)
                .flatMap(CleanClientImpl::getResponse);
    }

    /**
     * get a {@link CleanClientRsDto} from the {@link Response}.
     * @param rs to map.
     * @return {@link Optional} of {@link CleanClientRsDto}.
     */
    private static Optional<CleanClientRsDto> getResponse(final Response rs) {
        Optional<CleanClientRsDto> result = Optional.empty();
        try {
            final CleanClientRsDto cleanRs = new Gson().fromJson(rs.body().string(), CleanClientRsDto.class);
            result = cleanRs != null && !cleanRs.getResult().isEmpty() ? Optional.of(cleanRs) : Optional.empty();
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
    private Optional<Response> callClean(final Request request) {
        Optional<Response> result = Optional.empty();
        try {
            Call call = client.newCall(request);
            Response response = call.execute();
            result = Optional.of(response);
            log.info("[callClean] call to clean ok.");
        } catch(Exception e) {
            log.error("[callClean]: Error calling the clean service: {}", e.getMessage());
        }

        return result;
    }

    /**
     * create the clean request.
     * @param message to clean.
     * @return {@link Request}.
     */
    private Request getCleanRequest(final String message, final String url) {
        final CleanRqDto rq = CleanRqDto.builder().message(message).build();
        return new Request.Builder()
                .url(url)
                .post(RequestBody.create(new Gson().toJson(rq).getBytes()))
                .addHeader("Content-Type", "application/json")
                .build();
    }

    /**
     * get a clean message.
     * @param message to clean.
     * @return {@link Optional} of {@link String}.
     */
    @Override
    public Optional<String> clean(final String message) {
        return cleanMessage(message)
                .filter(cleanRs -> cleanRs.getResult() != null)
                .filter(cleanRs -> cleanRs.getId() != null)
                .filter(cleanRs -> !cleanRs.getResult().isEmpty())
                .filter(cleanRs -> cleanRs.getResult().containsKey(CLEAN_RESULT))
                .filter(cleanRs -> cleanRs.getResult().get(CLEAN_RESULT) != null)
                .filter(cleanRs -> !cleanRs.getResult().get(CLEAN_RESULT).isEmpty())
                .filter(cleanRs -> !cleanRs.getResult().get(CLEAN_RESULT).isBlank())
                .map(cleanRs -> cleanRs.getResult().get(CLEAN_RESULT));
    }

    /**
     * get the clean url.
     * @return {@link String} with the url.
     */
    private String getCleanUrl(){
        return cleanUrl + "compute/";
    }
}
