package com.co.solia.emotional.clean.models.dtos.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * class to map the request for cleaning messages.
 *
 * @author luis.bolivar.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CleanBatchRqDto {

    /**
     * messages to clean.
     */
    private List<String> messages;
}
