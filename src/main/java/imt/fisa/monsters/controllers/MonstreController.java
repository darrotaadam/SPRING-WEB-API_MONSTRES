package imt.fisa.monsters.controllers;

import imt.fisa.monsters.controllers.httpdto.CreationMonstreReponse;
import imt.fisa.monsters.exceptions.UnauthorizedException;
import imt.fisa.monsters.persistence.dto.MonsterEntity;
import imt.fisa.monsters.persistence.dto.MonsterRepository;
import imt.fisa.monsters.services.AuthService;
import imt.fisa.monsters.services.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MonstreController {


    private final AuthService authService;
    private final MonsterService monsterService;

    @Value("${auth.internal.secret}")
    private String internalSecret;


    public MonstreController(AuthService authService, MonsterService monsterService) {
        this.authService = authService;
        System.out.println("[*] MonstreController");
        this.monsterService = monsterService;
    }


    @GetMapping(value="/get-monstre/{id}")
    public ResponseEntity<MonsterEntity> getMonstreById(
            @RequestHeader("X-INTERNAL-API-KEY") String InternalApiKey,
            @PathVariable String id
    ){
        System.out.println("[*] /get-monstre?id=" + id);
        if( !InternalApiKey.equals(internalSecret)){
            throw new UnauthorizedException("Clé d'API interne invalide");
        }

        MonsterEntity monster = monsterService.getMonsterById(id);
        return ResponseEntity.ok(monster);
    }


    @PostMapping("/ajoute-monstre")
    public ResponseEntity<CreationMonstreReponse> getAllMonstres(
        @RequestHeader("X-INTERNAL-API-KEY") String InternalApiKey,
        @RequestBody MonsterEntity monsterEntity )
    {

        System.out.println("[*] /ajoute-monstre");
        if( !InternalApiKey.equals(internalSecret)){
            throw new UnauthorizedException("Clé d'API interne invalide");
        }

        String monsterId = monsterService.saveMonster(monsterEntity);

        return ResponseEntity.ok(new CreationMonstreReponse(monsterId));

    }
}
