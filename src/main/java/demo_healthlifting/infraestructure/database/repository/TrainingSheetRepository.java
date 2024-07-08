package demo_healthlifting.infraestructure.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import demo_healthlifting.infraestructure.database.entity.TrainingSheetEntity;
import jakarta.validation.Valid;

@Repository
@EnableMongoRepositories
public interface TrainingSheetRepository extends MongoRepository<TrainingSheetEntity, String> {

	Page<TrainingSheetEntity> findByEliminate(boolean eliminate, @Valid Pageable pageable);

	Optional<TrainingSheetEntity> findByIdAndEliminate(@Valid String id, boolean eliminate);

	Page<TrainingSheetEntity> findByIdInAndEliminate(List<String> trainingSheetsIds, boolean b, Pageable pageable);

	Page<TrainingSheetEntity> findByAthleteIdAndEliminateFalse(String id, Pageable pageable);

}
