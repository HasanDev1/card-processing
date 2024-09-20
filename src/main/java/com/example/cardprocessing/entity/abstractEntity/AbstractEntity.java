package com.example.cardprocessing.entity.abstractEntity;

import com.example.cardprocessing.entity.users.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode
@MappedSuperclass
@Data
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @DateTimeFormat(pattern = "dd.MM.YYYY HH:mm:ss")
    private LocalDateTime createDate;

    @LastModifiedDate
    @DateTimeFormat(pattern = "dd.MM.YYYY HH:mm:ss")
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    private Status status;
}
