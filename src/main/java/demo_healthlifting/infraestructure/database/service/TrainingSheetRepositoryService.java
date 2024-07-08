package demo_healthlifting.infraestructure.database.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import demo_healthlifting.application.ports.output.TrainingSheetRepositoryOutputPort;
import demo_healthlifting.application.ports.utils.Errors;
import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.TrainingSheet;
import demo_healthlifting.infraestructure.database.entity.AthleteEntity;
import demo_healthlifting.infraestructure.database.entity.CoachEntity;
import demo_healthlifting.infraestructure.database.entity.TrainingSheetEntity;
import demo_healthlifting.infraestructure.database.mapper.TrainingSheetToTrainingSheetEntityMapper;
import demo_healthlifting.infraestructure.database.repository.AthleteRepository;
import demo_healthlifting.infraestructure.database.repository.CoachRepository;
import demo_healthlifting.infraestructure.database.repository.TrainingSheetRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TrainingSheetRepositoryService implements TrainingSheetRepositoryOutputPort {

	@Autowired
	TrainingSheetRepository trainingSheetRepository;

	@Autowired
	AthleteRepository athleteRepository;

	@Autowired
	CoachRepository coachRepository;

	@Autowired
	TrainingSheetToTrainingSheetEntityMapper trainingSheetToTrainingSheetEntityMapper;

	/**
	 * Creates a new training sheet and evicts all entries in the "trainingSheet"
	 * cache.
	 *
	 * @param trainingSheet the training sheet to be created
	 * @return the newly created training sheet
	 */
	@Override
	@CacheEvict(value = "trainingSheet", allEntries = true)
	public TrainingSheet createTrainingSheet(@Valid TrainingSheet trainingSheet) {
		log.debug("createTrainingSheet");
		TrainingSheetEntity entity = trainingSheetToTrainingSheetEntityMapper.fromInputToOutput(trainingSheet);

		TrainingSheetEntity saveTrainingSheetEntity = trainingSheetRepository.save(entity);
		return trainingSheetToTrainingSheetEntityMapper.fromOutputToInput(saveTrainingSheetEntity);
	}

	/**
	 * Retrieves a training sheet by ID and caches the result.
	 *
	 * @param id the ID of the training sheet to be retrieved
	 * @return an Optional containing the training sheet if found, or an empty
	 *         Optional if not
	 */
	@Override
	@Cacheable(value = "trainingSheet", key = "#id")
	public Optional<TrainingSheet> getTrainingSheet(@Valid String id) {
		log.debug("getTrainingSheet");

		Optional<TrainingSheetEntity> resourceEntity = trainingSheetRepository.findById(id);
		return trainingSheetToTrainingSheetEntityMapper.fromOutputToInput(resourceEntity);
	}

	/**
	 * Retrieves a paginated list of training sheets and caches the result.
	 *
	 * @param pageable the pagination information
	 * @return a paginated list of training sheets
	 */
	@Override
	@Cacheable(value = "trainingSheet", key = "#pageable")
	public Page<TrainingSheet> getTrainingSheets(@Valid Pageable pageable) {
		log.debug("getTrainingSheets");
		Page<TrainingSheetEntity> listEntity = trainingSheetRepository.findByEliminate(false, pageable);
		return trainingSheetToTrainingSheetEntityMapper.fromOutputToInput(listEntity);
	}

	/**
	 * Modifies an existing training sheet and evicts all entries in the
	 * "trainingSheet" cache.
	 *
	 * @param updated the training sheet with updated information
	 */
	@Override
	@CacheEvict(value = "trainingSheet", allEntries = true)
	public void modifyTrainingSheet(@Valid TrainingSheet updated) {
		log.debug("modifyTrainingSheet");
		TrainingSheetEntity entity = trainingSheetToTrainingSheetEntityMapper.fromInputToOutput(updated);
		trainingSheetRepository.save(entity);

	}

	/**
	 * Deletes a training sheet by marking it as eliminated and evicts all entries
	 * in the "trainingSheet" cache.
	 *
	 * @param id the ID of the training sheet to be deleted
	 */
	@Override
	@CacheEvict(value = "trainingSheet", allEntries = true)
	public void deleteTrainingSheet(@Valid String id) {
		log.debug("deleteTrainingSheet");
		Optional<TrainingSheetEntity> optTrainingSheet = trainingSheetRepository.findByIdAndEliminate(id, false);
		if (optTrainingSheet.isPresent()) {
			optTrainingSheet.get().setEliminate(true);
		}
		trainingSheetRepository.save(optTrainingSheet.get());

	}

	/**
	 * Retrieves a paginated list of training sheets by athlete ID and caches the
	 * result.
	 *
	 * @param id       the ID of the athlete
	 * @param pageable the pagination information
	 * @return a paginated list of training sheets
	 * @throws BusinessException if no training sheets are found for the athlete or
	 *                           the athlete is not found
	 */
	@Override
	@Cacheable(value = "trainingSheet", key = "#id")
	public Page<TrainingSheet> getTrainingSheetsByAthleteId(String id, Pageable pageable) throws BusinessException {
		log.debug("getTrainingSheetsByAthleteId");
		Optional<AthleteEntity> athleteOpt = athleteRepository.findByIdAndEliminate(id, false);
		if (athleteOpt.isPresent()) {
			AthleteEntity athlete = athleteOpt.get();
			List<String> trainingSheetsIds = athlete.getIdTrainingSheet();
			if (trainingSheetsIds.isEmpty()) {
				throw new BusinessException("No training sheets found for the athlete.");
			}

			Page<TrainingSheetEntity> trainingSheetsEntities = trainingSheetRepository
					.findByIdInAndEliminate(trainingSheetsIds, false, pageable);
			return trainingSheetsEntities.map(trainingSheetToTrainingSheetEntityMapper::fromOutputToInput);
		}

		throw new BusinessException(Errors.PERSON_NOT_FOUND);
	}

	/**
	 * Retrieves a paginated list of training sheets by coach ID and caches the
	 * result.
	 *
	 * @param id       the ID of the coach
	 * @param pageable the pagination information
	 * @return a paginated list of training sheets
	 * @throws BusinessException if no training sheets are found for the coach or
	 *                           the coach is not found
	 */
	@Override
	@Cacheable(value = "trainingSheet", key = "#id")
	public Page<TrainingSheet> getTrainingSheetsByCoachId(String id, Pageable pageable) throws BusinessException {
		log.debug("getTrainingSheetsByCoachId");
		Optional<CoachEntity> coachOpt = coachRepository.findByIdAndEliminate(id, false);
		if (coachOpt.isPresent()) {
			CoachEntity coach = coachOpt.get();
			List<String> trainingSheetsIds = coach.getIdTrainingSheet();
			if (trainingSheetsIds.isEmpty()) {
				throw new BusinessException("No training sheets found for the coach.");
			}

			Page<TrainingSheetEntity> trainingSheetsEntities = trainingSheetRepository
					.findByIdInAndEliminate(trainingSheetsIds, false, pageable);
			return trainingSheetsEntities.map(trainingSheetToTrainingSheetEntityMapper::fromOutputToInput);
		}

		throw new BusinessException(Errors.PERSON_NOT_FOUND);
	}
}
