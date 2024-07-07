package demo_healthlifting.application.ports.input;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.Athlete;
import jakarta.validation.Valid;

public interface AthleteServiceInputPort {

	String createAthlete(@Valid Athlete athlete);

	Optional<Athlete> getAthlete(@Valid String idAthlete);

	Page<Athlete> getAthletes(@Valid Pageable pageable) throws BusinessException;

	void modificationPartialAthlete(@Valid Athlete inputAthlete) throws BusinessException;

	void modificationTotalAthlete(@Valid Athlete inputAthlete) throws BusinessException;

	void deleteAthlete(@Valid String idAthlete) throws BusinessException;

	Optional<Athlete> findByAthletePersonalInformationDocument(@Valid String document);

}
