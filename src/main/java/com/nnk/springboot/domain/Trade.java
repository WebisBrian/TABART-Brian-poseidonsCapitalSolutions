package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "tradeId")
@ToString(of = {"tradeId", "account", "type"})
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer tradeId;

    @NotBlank(message = "Account is mandatory")
    @Column(nullable = false)
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Column(nullable = false)
    private String type;

    @PositiveOrZero(message = "Buy quantity must be zero or positive")
    private Double buyQuantity;

    @PositiveOrZero(message = "Sell quantity must be zero or positive")
    private Double sellQuantity;

    @PositiveOrZero(message = "Buy price must be zero or positive")
    private Double buyPrice;

    @PositiveOrZero(message = "Sell price must be zero or positive")
    private Double sellPrice;
    private LocalDateTime tradeDate;
    private String security;
    private String status;
    private String trader;
    private String benchmark;
    private String book;
    private String creationName;

    @Setter(AccessLevel.NONE)
    private LocalDateTime creationDate;

    private String revisionName;

    @Setter(AccessLevel.NONE)
    private LocalDateTime revisionDate;

    private String dealName;
    private String dealType;
    private String sourceListId;
    private String side;

    public Trade(String account, String type, Double buyQuantity) {
        this.account = account;
        this.type = type;
        this.buyQuantity = buyQuantity;
    }

    @PrePersist
    private void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    private void onUpdate() {
        this.revisionDate = LocalDateTime.now();
    }
}
