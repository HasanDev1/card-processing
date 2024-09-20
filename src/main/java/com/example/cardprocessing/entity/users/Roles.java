package com.example.cardprocessing.entity.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Getter
public class Roles implements GrantedAuthority {

    @Id
    private Long id;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
