package imt.fisa.monsters.services;

import imt.fisa.monsters.persistence.dto.MonsterEntity;
import imt.fisa.monsters.persistence.dto.MonsterRepository;
import org.springframework.stereotype.Service;

@Service
public class MonsterService {

    private final MonsterRepository monsterRepository;

    public MonsterService(MonsterRepository monsterRepository){
        this.monsterRepository = monsterRepository;
    }



    public String saveMonster(MonsterEntity monster){
        return monsterRepository.save(monster).getId();
    }
}
