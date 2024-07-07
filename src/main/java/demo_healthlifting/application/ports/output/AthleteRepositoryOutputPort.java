package demo_healthlifting.application.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import demo_healthlifting.domain.model.Athlete;
import jakarta.validation.Valid;

public interface AthleteRepositoryOutputPort {

	String createAthlete(@Valid Athlete athlete);

	Optional<Athlete> getAthlete(@Valid String idAthlete);;

	Page<Athlete> getAthletes(@Valid Pageable pageable);

	void modifyAthlete(@Valid Athlete updated);

	void deleteAthlete(@Valid String idAthlete);

	Optional<Athlete> getAthleteById(@Valid String athleteId);

	Optional<Athlete> findByAthletePersonalInformationDocument(@Valid String document);

}
