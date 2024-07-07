package demo_healthlifting.application.ports.input;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.Coach;
import jakarta.validation.Valid;

public interface CoachServiceInputPort {

	String createCoach(@Valid Coach coach);

	Optional<Coach> getCoach(@Valid String idCoach);

	Page<Coach> getCoaches(@Valid Pageable pageable) throws BusinessException;

	void modificationPartialCoach(@Valid Coach inputCoach) throws BusinessException;

	void modificationTotalCoach(@Valid Coach inputCoach) throws BusinessException;

	void deleteCoach(@Valid String idCoach) throws BusinessException;

	Optional<Coach> findByCoachPersonalInformationDocument(@Valid String document);

}
