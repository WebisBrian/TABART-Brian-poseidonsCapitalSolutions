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
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String account;

    @NotBlank(message = "Type is mandatory")
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String type;

    @PositiveOrZero(message = "Bid quantity must be zero or positive")
    private Double bidQuantity;

    @PositiveOrZero(message = "Ask quantity must be zero or positive")
    private Double askQuantity;

    @PositiveOrZero(message = "Bid price must be zero or positive")
    private Double bid;

    @PositiveOrZero(message = "Ask price must be zero or positive")
    private Double ask;

    @Column(length = 125)
    private String benchmark;
    private LocalDateTime bidListDate;
    @Column(length = 125)
    private String commentary;
    @Column(length = 125)
    private String security;

    @Size(max = 10)
    @Column(length = 10)
    private String status;

    @Column(length = 125)
    private String trader;
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
