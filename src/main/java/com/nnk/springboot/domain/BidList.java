package com.nnk.springboot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bid_list")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "bidListId")
@ToString(of = {"bidListId", "account", "type"})
public class BidList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer bidListId;

    @NotBlank(message = "Account is mandatory")
    @Column(nullable = false)
    @Size(max = 30)
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Column(nullable = false)
    @Size(max = 30)
    private String type;

    @PositiveOrZero(message = "Bid quantity must be zero or positive")
    private Double bidQuantity;

    @PositiveOrZero(message = "Ask quantity must be zero or positive")
    private Double askQuantity;

    @PositiveOrZero(message = "Bid price must be zero or positive")
    private Double bid;

    @PositiveOrZero(message = "Ask price must be zero or positive")
    private Double ask;

    private String benchmark;
    private LocalDateTime bidListDate;
    private String commentary;
    private String security;

    @Size(max = 10)
    private String status;

    private String trader;
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

    public BidList(String account, String type, Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
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
