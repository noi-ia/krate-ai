package com.co.solia.emotional.brand.controllers.endpoints.v1;

import com.co.solia.emotional.brand.controllers.docs.BrandControllerDoc;
import com.co.solia.emotional.brand.models.dtos.rq.BrandRqDto;
import com.co.solia.emotional.brand.models.dtos.rs.BrandRsDto;
import com.co.solia.emotional.brand.services.services.BrandService;
import com.co.solia.emotional.share.models.exceptions.InternalServerException;
import com.co.solia.emotional.share.models.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * controller implementation of {@link BrandControllerDoc} to map the brand services.
 *
 * @author luis.bolivar.ß
 */
@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping("/1/brand")
public class BrandController implements BrandControllerDoc {

    /**
     * dependency on {@link BrandService}.
     */
    private BrandService brandService;

    /**
     * create a new brand.
     * @param rq to create a new brand.
     * @return {@link ResponseEntity} of {@link BrandRsDto}.
     */
    @PostMapping(value = "/")
    public ResponseEntity<BrandRsDto> create(@RequestBody final BrandRqDto rq) {
        return brandService.create(rq)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                   log.error("[create]: error creating the brand.");
                   throw InternalServerException.builder()
                           .message("error creating the brand.")
                           .endpoint("/brand/")
                           .build();
                });
    }

    /**
     * get the brand by id.
     * @param id brand identifier.
     * @return {@link ResponseEntity} of {@link BrandRsDto}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandRsDto> getById(@PathVariable("id") final UUID id) {
        return brandService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.error("[getById]: error getting the brand by id.");
                    throw NotFoundException.builder()
                            .message("error getting the brand by id")
                            .endpoint("/brand/{id}")
                            .build();
                });
    }
}
