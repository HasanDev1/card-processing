package com.example.cardprocessing.entity;

import com.example.cardprocessing.entity.users.Status;
import com.example.cardprocessing.entity.users.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Cards {
    @Id
    private UUID etag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private Status status;

    private UUID cardId;

    private Long balance;

    @Column(length = 3)
    @Enumerated(EnumType.STRING)
    private Currency currency;
}
