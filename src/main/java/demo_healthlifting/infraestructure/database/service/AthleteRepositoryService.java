package demo_healthlifting.infraestructure.database.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import demo_healthlifting.application.ports.output.AthleteRepositoryOutputPort;
import demo_healthlifting.domain.model.Athlete;
import demo_healthlifting.infraestructure.database.entity.AthleteEntity;
import demo_healthlifting.infraestructure.database.mapper.AthleteToAthleteEntityMapper;
import demo_healthlifting.infraestructure.database.repository.AthleteRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AthleteRepositoryService implements AthleteRepositoryOutputPort {

	@Autowired
	AthleteRepository athleteRepository;

	@Autowired
	AthleteToAthleteEntityMapper athleteToAthleteEntityMapper;

	/**
	 * Creates a new athlete and evicts all entries in the "athletes" cache.
	 *
	 * @param input the athlete to be created
	 * @return the ID of the newly created athlete
	 */
	@Override
	@CacheEvict(value = "athletes", allEntries = true)
	public String createAthlete(@Valid Athlete input) {
		log.debug("createAthlete");
		AthleteEntity entity = athleteToAthleteEntityMapper.fromInputToOutput(input);
		return athleteRepository.save(entity).getId();
	}

	/**
	 * Retrieves an athlete by ID and caches the result.
	 *
	 * @param id the ID of the athlete to be retrieved
	 * @return an Optional containing the athlete if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Cacheable(value = "athletes", key = "#id")
	public Optional<Athlete> getAthlete(@Valid String id) {
		log.debug("getAthlete");

		Optional<AthleteEntity> resourceEntity = athleteRepository.findById(id);
		return athleteToAthleteEntityMapper.fromOutputToInput(resourceEntity);
	}

	/**
	 * Retrieves a paginated list of athletes and caches the result.
	 *
	 * @param pageable the pagination information
	 * @return a paginated list of athletes
	 */
	@Override
	@Cacheable(value = "athletes", key = "#pageable")
	public Page<Athlete> getAthletes(@Valid Pageable pageable) {
		log.debug("getAthletes");
		Page<AthleteEntity> listEntity = athleteRepository.findByEliminate(false, pageable);
		return athleteToAthleteEntityMapper.fromOutputToInput(listEntity);
	}

	/**
	 * Modifies an existing athlete and evicts all entries in the "athletes" cache.
	 *
	 * @param input the athlete with updated information
	 */
	@Override
	@CacheEvict(value = "athletes", allEntries = true)
	public void modifyAthlete(@Valid Athlete input) {
		log.debug("modifyAthletes");
		AthleteEntity entity = athleteToAthleteEntityMapper.fromInputToOutput(input);
		athleteRepository.save(entity);

	}

	/**
	 * Deletes an athlete by marking them as eliminated and evicts all entries in
	 * the "athletes" cache.
	 *
	 * @param idAthlete the ID of the athlete to be deleted
	 */
	@Override
	@CacheEvict(value = "athletes", allEntries = true)
	public void deleteAthlete(@Valid String idAthlete) {
		log.debug("deleteAthletes");
		Optional<AthleteEntity> optAthlete = athleteRepository.findByIdAndEliminate(idAthlete, false);
		if (optAthlete.isPresent()) {
			optAthlete.get().setEliminate(true);
		}
		athleteRepository.save(optAthlete.get());

	}

	/**
	 * Retrieves an athlete by ID, excluding eliminated athletes, and caches the
	 * result.
	 *
	 * @param id the ID of the athlete to be retrieved
	 * @return an Optional containing the athlete if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Cacheable(value = "athletes", key = "#id")
	public Optional<Athlete> getAthleteById(@Valid String id) {
		log.debug("getAthleteById");
		Optional<AthleteEntity> athleteEntity = athleteRepository.findByIdAndEliminate(id, false);
		return athleteToAthleteEntityMapper.fromOutputToInput(athleteEntity);
	}

	/**
	 * Finds an athlete by their personal information document, excluding eliminated
	 * athletes, and caches the result.
	 *
	 * @param document the document of the athlete to be found
	 * @return an Optional containing the athlete if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Cacheable(value = "athletes", key = "#document")
	public Optional<Athlete> findByAthletePersonalInformationDocument(@Valid String document) {
		log.debug("findByAthletePersonalInformationDocument");
		Optional<AthleteEntity> athleteEntityOpt = athleteRepository
				.findByPersonalInformationDocumentAndEliminate(document, false);
		return athleteEntityOpt.map(athleteToAthleteEntityMapper::fromOutputToInput);
	}
}
