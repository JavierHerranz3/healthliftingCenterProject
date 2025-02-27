package demo_healthlifting.infraestructure.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import demo_healthlifting.infraestructure.database.entity.AppointmentEntity;
import jakarta.validation.Valid;

@Repository
@EnableMongoRepositories
public interface AppointmentRepository extends MongoRepository<AppointmentEntity, String> {

	Optional<AppointmentEntity> findByIdAndEliminate(@Valid String id, boolean eliminate);

	Page<AppointmentEntity> findByEliminate(boolean eliminate, @Valid Pageable pageable);

	Page<AppointmentEntity> findByIdAndEliminate(boolean b, List<String> appointmentIds, Pageable pageable);

	Page<AppointmentEntity> findAthleteByEliminateAndIdIn(boolean b, List<String> appointmentIds, Pageable pageable);

	Page<AppointmentEntity> findByIdInAndEliminate(List<String> appointmentIds, boolean b, Pageable pageable);

}
