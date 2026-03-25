package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Size(max = 30)
    @Column(nullable = false, length = 30)
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
    @Column(length = 125)
    private String security;
    @Size(max = 10)
    @Column(length = 10)
    private String status;
    @Column(length = 125)
    private String trader;
    @Column(length = 125)
    private String benchmark;
    @Column(length = 125)
    private String book;
    @Column(length = 125)
    private String creationName;

    @Setter(AccessLevel.NONE)
    private LocalDateTime creationDate;

    @Column(length = 125)
    private String revisionName;

    @Setter(AccessLevel.NONE)
    private LocalDateTime revisionDate;

    @Column(length = 125)
    private String dealName;
    @Column(length = 125)
    private String dealType;
    @Column(length = 125)
    private String sourceListId;
    @Column(length = 125)
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
