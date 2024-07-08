package demo_healthlifting.infraestructure.database.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import demo_healthlifting.application.ports.output.CoachRepositoryOutputPort;
import demo_healthlifting.domain.model.Coach;
import demo_healthlifting.infraestructure.database.entity.CoachEntity;
import demo_healthlifting.infraestructure.database.mapper.CoachToCoachEntityMapper;
import demo_healthlifting.infraestructure.database.repository.CoachRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CoachRepositoryService implements CoachRepositoryOutputPort {

	@Autowired
	CoachRepository coachRepository;

	@Autowired
	CoachToCoachEntityMapper coachToCoachEntityMapper;

	/**
	 * Creates a new coach and evicts all entries in the "coaches" cache.
	 *
	 * @param input the coach to be created
	 * @return the ID of the newly created coach
	 */
	@Override
	@CacheEvict(value = "coaches", allEntries = true)
	public String createCoach(@Valid Coach input) {
		log.debug("createCoach");
		CoachEntity entity = coachToCoachEntityMapper.fromInputToOutput(input);
		return coachRepository.save(entity).getId();
	}

	/**
	 * Retrieves a coach by ID and caches the result.
	 *
	 * @param id the ID of the coach to be retrieved
	 * @return an Optional containing the coach if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Cacheable(value = "coaches", key = "#id")
	public Optional<Coach> getCoach(@Valid String id) {
		log.debug("getCoach");

		Optional<CoachEntity> resourceEntity = coachRepository.findById(id);
		return coachToCoachEntityMapper.fromOutputToInput(resourceEntity);
	}

	/**
	 * Retrieves a paginated list of coaches and caches the result.
	 *
	 * @param pageable the pagination information
	 * @return a paginated list of coaches
	 */
	@Override
	@Cacheable(value = "coaches", key = "#pageable")
	public Page<Coach> getCoaches(@Valid Pageable pageable) {
		log.debug("getCoaches");
		Page<CoachEntity> listEntity = coachRepository.findByEliminate(false, pageable);
		return coachToCoachEntityMapper.fromOutputToInput(listEntity);
	}

	/**
	 * Modifies an existing coach and evicts all entries in the "coaches" cache.
	 *
	 * @param input the coach with updated information
	 */
	@Override
	@CacheEvict(value = "coaches", allEntries = true)
	public void modifyCoach(@Valid Coach input) {
		log.debug("modifyCoaches");
		CoachEntity entity = coachToCoachEntityMapper.fromInputToOutput(input);
		coachRepository.save(entity);

	}

	/**
	 * Deletes a coach by marking them as eliminated and evicts all entries in the
	 * "coaches" cache.
	 *
	 * @param idCoach the ID of the coach to be deleted
	 */
	@Override
	@CacheEvict(value = "coaches", allEntries = true)
	public void deleteCoach(@Valid String idCoach) {
		log.debug("deleteAthletes");
		Optional<CoachEntity> optCoach = coachRepository.findByIdAndEliminate(idCoach, false);
		if (optCoach.isPresent()) {
			optCoach.get().setEliminate(true);
		}
		coachRepository.save(optCoach.get());

	}

	/**
	 * Retrieves a coach by ID, excluding eliminated coaches, and caches the result.
	 *
	 * @param id the ID of the coach to be retrieved
	 * @return an Optional containing the coach if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Cacheable(value = "coach", key = "#id")
	public Optional<Coach> getCoachById(@Valid String id) {
		log.debug("getCoachById");
		Optional<CoachEntity> coachEntity = coachRepository.findByIdAndEliminate(id, false);
		return coachToCoachEntityMapper.fromOutputToInput(coachEntity);
	}

	/**
	 * Finds a coach by their personal information document, excluding eliminated
	 * coaches, and caches the result.
	 *
	 * @param document the document of the coach to be found
	 * @return an Optional containing the coach if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Cacheable(value = "coaches", key = "#document")
	public Optional<Coach> findByCoachPersonalInformationDocument(@Valid String document) {
		log.debug("findByCoachPersonalInformationDocument");
		Optional<CoachEntity> coachEntityOpt = coachRepository.findByPersonalInformationDocumentAndEliminate(document,
				false);
		return coachEntityOpt.map(coachToCoachEntityMapper::fromOutputToInput);
	}

}
