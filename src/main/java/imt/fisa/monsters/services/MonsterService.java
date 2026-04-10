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


    public MonsterEntity getMonsterById(String id){
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return monsterRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No monster found with id: " + id));
    }



    public String saveMonster(MonsterEntity monster){
        if (monster == null) {
            throw new IllegalArgumentException("monster must not be null");
        }

        // on reçoit un monsterEntity qui est en fait un template, avec comme id l'id du template. Il faut donc le modifier pour que ce soit l'id propre à cette entité monstre.
        monster.setId(null);
        return monsterRepository.save(monster).getId();
    }
}
