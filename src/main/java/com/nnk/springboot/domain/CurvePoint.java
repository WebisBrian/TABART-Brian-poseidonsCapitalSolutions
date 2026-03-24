package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "curve_point")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "curveId", "term", "value"})
public class CurvePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @NotNull(message = "Curve Id is mandatory")
    private Integer curveId;

    private LocalDateTime asOfDate;
    private Double term;
    private Double value;

    @Setter(AccessLevel.NONE)
    private LocalDateTime creationDate;

    public CurvePoint(Integer curveId, Double term, Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }

    @PrePersist
    private void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
}
