package demo_healthlifting.infraestructure.database.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import demo_healthlifting.infraestructure.database.entity.CoachEntity;
import jakarta.validation.Valid;

@Repository
@EnableMongoRepositories
public interface CoachRepository extends MongoRepository<CoachEntity, String> {

	Optional<CoachEntity> findByIdAndEliminate(@Valid String id, boolean eliminate);

	Page<CoachEntity> findByEliminate(boolean eliminate, @Valid Pageable pageable);

	Optional<CoachEntity> findByPersonalInformationAndEliminate(@Valid String document, boolean b);

}
