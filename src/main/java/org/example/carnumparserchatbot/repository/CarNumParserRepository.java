package org.example.carnumparserchatbot.repository;

import org.example.carnumparserchatbot.entity.CarNumParserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarNumParserRepository extends JpaRepository<CarNumParserEntity, Long> {
    List<CarNumParserEntity> findAllByChatId(String chatId);
    void deleteAllByChatId(String chatId);
}
