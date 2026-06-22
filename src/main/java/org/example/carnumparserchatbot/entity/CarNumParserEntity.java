package org.example.carnumparserchatbot.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "car_numbers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CarNumParserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // хранит сообщение, для последующей валидации
    @Column(nullable = false)
    private String chatId;

    // хранит найденный номер
    @Column(nullable = false)
    private String number;

    // хранит имя отправителя номера
    @Column(nullable = false)
    private String nameSender;

    // время сохранения
    @Column(nullable = false)
    private Instant savedAt;

    @PrePersist
    protected void onCreate() {
        if (savedAt == null) {
            savedAt = Instant.now();
        }
    }
}
