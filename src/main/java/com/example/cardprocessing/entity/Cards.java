package com.example.cardprocessing.entity;

import com.example.cardprocessing.entity.users.Status;
import com.example.cardprocessing.entity.users.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cards {
    @Id
    private UUID idempotencyKey;

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
