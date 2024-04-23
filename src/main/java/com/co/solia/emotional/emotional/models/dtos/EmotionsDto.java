package com.co.solia.emotional.emotional.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * emotions supported by the emotional estimation.
 *
 * @author luis.bolivar.
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmotionsDto {

    @Builder.Default
    private Double felicidad = 0.0;

    @Builder.Default
    private Double tristeza = 0.0;

    @Builder.Default
    private Double enojo = 0.0;

    @Builder.Default
    private Double miedo = 0.0;

    @Builder.Default
    private Double sorpresa = 0.0;

    @Builder.Default
    private Double disgusto = 0.0;

    @Builder.Default
    private Double confianza = 0.0;

    @Builder.Default
    private Double alegria = 0.0;

    @Builder.Default
    private Double amor = 0.0;

    @Builder.Default
    private Double preocupacion = 0.0;

    @Builder.Default
    private Double culpa = 0.0;

    @Builder.Default
    private Double verguenza = 0.0;

    @Builder.Default
    private Double aversion = 0.0;

    @Builder.Default
    private Double esperanza = 0.0;

    @Builder.Default
    private Double orgullo = 0.0;

    @Builder.Default
    private Double motivacion = 0.0;

    @Builder.Default
    private Double satisfaccion = 0.0;

    @Builder.Default
    private Double frustracion = 0.0;
}
