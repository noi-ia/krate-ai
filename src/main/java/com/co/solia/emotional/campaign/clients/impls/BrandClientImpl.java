package com.co.solia.emotional.campaign.clients.impls;

import com.co.solia.emotional.campaign.clients.clients.BrandClient;
import com.co.solia.emotional.campaign.models.dtos.rs.BrandClientRsDto;
import com.co.solia.emotional.campaign.models.mappers.BrandMapper;
import com.co.solia.emotional.campaign.models.mappers.EmotionalMapper;
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
 * implementation of {@link BrandClient}.
 *
 * @author luis.bolivar.
 */
@Component
@Slf4j
public class BrandClientImpl implements BrandClient {

    /**
     * emotional url to consume its api.
     */
    private final String brandUrl;

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
     * @param brandUrl emotional url.
     */
    @Autowired
    public BrandClientImpl(@Value("${solia.emotional.brand.url}") final String brandUrl){
        this.brandUrl = brandUrl;
    }

    /**
     * get a brand by id.
     *
     * @param id to get the brand.
     * @return {@link Optional} of {@link BrandClientRsDto}
     */
    @Override
    public Optional<BrandClientRsDto> getById(final UUID id) {
        final Request rq = getRqToGetById(getUrlGetById(id));
        return callGetBrandById(rq).flatMap(rs -> {
            log.info("[getById]: response: {}", rs.body());
            return BrandMapper.getFromRs(rs);
        });
    }

    /**
     * get a brand url to get brand by id.
     * @param id brand identifier.
     * @return {@link String} with the brand url.
     */
    private String getUrlGetById(final UUID id) {
        return brandUrl + id;
    }

    /**
     * call the keyphrase endpoint.
     * @param rq for call.
     * @return {@link Optional} of {@link Response}.
     */
    private Optional<Response> callGetBrandById(final Request rq) {
        Optional<Response> result = Optional.empty();
        try {
            final Call call = client.newCall(rq);
            final Response response = call.execute();
            result = Optional.of(response);
            log.info("[callGetBrandById] call to keyphrase ok.");
        } catch(Exception e) {
            log.error("[callGetBrandById]: error getting the brand processing by id error: {}", e.getMessage());
        }

        return result;
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
