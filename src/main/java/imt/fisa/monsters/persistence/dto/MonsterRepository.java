package imt.fisa.monsters.persistence.dto;

import imt.fisa.monsters.services.MonsterService;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface MonsterRepository extends MongoRepository<MonsterEntity, String> {
    List<MonsterEntity> findAll();

    Optional<MonsterEntity> findById(String id);
}
