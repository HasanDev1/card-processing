package com.example.cardprocessing.entity.users;

import jakarta.persistence.Entity;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Admin extends Users{

    private String who;

}
