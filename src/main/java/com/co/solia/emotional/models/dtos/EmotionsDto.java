package com.co.solia.emotional.models.dtos;

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

    private Double felicidad;
    private Double tristeza;
    private Double enojo;
    private Double miedo;
    private Double sorpresa;
    private Double disgusto;
    private Double confianza;
    private Double alegria;
    private Double amor;
    private Double preocupacion;
    private Double culpa;
    private Double verguenza;
    private Double aversion;
    private Double esperanza;
    private Double orgullo;
    private Double motivacion;
    private Double satisfaccion;
    private Double frustracion;
}
