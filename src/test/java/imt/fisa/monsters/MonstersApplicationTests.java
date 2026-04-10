package imt.fisa.monsters;

import imt.fisa.monsters.persistence.dto.MonsterEntity;
import imt.fisa.monsters.persistence.dto.MonsterRepository;
import imt.fisa.monsters.services.MonsterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MonstersApplicationTests {

	@Mock
	private MonsterRepository monsterRepository;

	@InjectMocks
	private MonsterService monsterService;

	// =========================================================================
	// getMonsterById
	// =========================================================================

	@Test
	void testGetMonsterById_idNull_leveIllegalArgument() {
		assertThatThrownBy(() -> monsterService.getMonsterById(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("null");
	}

	@Test
	void testGetMonsterById_idInconnu_leveIllegalArgument() {
		when(monsterRepository.findById("id-inexistant")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> monsterService.getMonsterById("id-inexistant"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("No monster found");
	}

	@Test
	void testGetMonsterById_idValide_retourneMonstre() {
		MonsterEntity monster = new MonsterEntity();
		monster.setId("abc123");
		monster.setName("Flamewing");

		when(monsterRepository.findById("abc123")).thenReturn(Optional.of(monster));

		MonsterEntity result = monsterService.getMonsterById("abc123");

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo("abc123");
		assertThat(result.getName()).isEqualTo("Flamewing");
	}

	// =========================================================================
	// saveMonster
	// =========================================================================

	@Test
	void testSaveMonster_monstreNull_leveIllegalArgument() {
		assertThatThrownBy(() -> monsterService.saveMonster(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("null");
	}

	/**
	 * saveMonster doit mettre l'id à null avant de sauvegarder afin que MongoDB
	 * génère un nouvel identifiant unique (le monstre est une instance, pas un template).
	 */
	@Test
	void testSaveMonster_metsIdANullAvantSauvegarde() {
		MonsterEntity template = new MonsterEntity();
		template.setId("template-id-42"); // l'id du template d'origine

		MonsterEntity saved = new MonsterEntity();
		saved.setId("nouveau-id-mongo");

		when(monsterRepository.save(any(MonsterEntity.class))).thenReturn(saved);

		String returnedId = monsterService.saveMonster(template);

		// Vérification : l'id passé au repository doit être null
		verify(monsterRepository).save(argThat(m -> m.getId() == null));
		assertThat(returnedId).isEqualTo("nouveau-id-mongo");
	}

	@Test
	void testSaveMonster_retourneIdGenereParMongoDB() {
		MonsterEntity template = new MonsterEntity();
		template.setId("template-1");

		MonsterEntity savedWithNewId = new MonsterEntity();
		savedWithNewId.setId("mongo-generated-999");

		when(monsterRepository.save(any(MonsterEntity.class))).thenReturn(savedWithNewId);

		String id = monsterService.saveMonster(template);

		assertThat(id).isEqualTo("mongo-generated-999");
		assertThat(id).isNotEqualTo("template-1")
				.as("L'id retourné doit être l'id généré par MongoDB, pas l'id du template");
	}
}