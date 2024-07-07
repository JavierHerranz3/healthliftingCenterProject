package demo_healthlifting.application.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import demo_healthlifting.domain.model.Coach;
import jakarta.validation.Valid;

public interface CoachRepositoryOutputPort {

	String createCoach(@Valid Coach coach);

	Optional<Coach> getCoach(@Valid String idCoach);

	Page<Coach> getCoaches(@Valid Pageable pageable);

	void modifyCoach(@Valid Coach updated);

	void deleteCoach(@Valid String idCoach);

	Optional<Coach> getCoachById(@Valid String coachId);

	Optional<Coach> findByCoachPersonalInformationDocument(@Valid String document);

}
