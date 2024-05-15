package com.co.solia.emotional.clean.services.impl;

import com.co.solia.emotional.clean.models.dtos.rq.CleanRqDto;
import com.co.solia.emotional.clean.models.dtos.rs.CleanRsDto;
import com.co.solia.emotional.clean.services.services.CleanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * implementation of {@link CleanService}.
 *
 * @author luis.bolivar.
 */
@Service
@Slf4j
public class CleanServiceImpl implements CleanService {
    @Override
    public Optional<CleanRsDto> clean(CleanRqDto cleanRq) {
        return Optional.empty();
    }
}
