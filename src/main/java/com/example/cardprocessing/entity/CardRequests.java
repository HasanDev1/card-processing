package com.example.cardprocessing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
public class CardRequests {

    @Id
    private UUID idempotencyKey;

    @OneToOne
    @JoinColumn(name = "card_etag_id", referencedColumnName = "etag")
    private Cards cards;

}
