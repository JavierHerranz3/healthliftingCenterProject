package demo_healthlifting.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import demo_healthlifting.application.ports.input.TrainingSheetServiceInputPort;
import demo_healthlifting.application.ports.output.AthleteRepositoryOutputPort;
import demo_healthlifting.application.ports.output.CoachRepositoryOutputPort;
import demo_healthlifting.application.ports.output.TrainingSheetRepositoryOutputPort;
import demo_healthlifting.application.ports.utils.Constants;
import demo_healthlifting.application.ports.utils.Errors;
import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.mapper.HealthliftingPatchMapper;
import demo_healthlifting.domain.model.Athlete;
import demo_healthlifting.domain.model.Coach;
import demo_healthlifting.domain.model.TrainingSheet;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class trainingSheetService implements TrainingSheetServiceInputPort {

	@Autowired
	TrainingSheetRepositoryOutputPort trainingSheetRepository;

	@Autowired
	AthleteRepositoryOutputPort athleteRepository;

	@Autowired
	CoachRepositoryOutputPort coachRepository;

	@Autowired
	HealthliftingPatchMapper healthliftingPatchMapper;

	/**
	 * Creates a new training sheet.
	 *
	 * @param trainingSheet the training sheet to be created
	 * @return the ID of the newly created training sheet
	 * @throws BusinessException if there is a problem creating the training sheet
	 */
	@Override
	@Transactional
	public String createTrainingSheet(@Valid TrainingSheet trainingSheet) throws BusinessException {
		if (trainingSheet == null) {
			throw new IllegalArgumentException("El objeto trainingsheet no puede ser null.");
		}

		// Buscamos el atleta por ID y validamos su existencia
		Optional<Athlete> athleteOpt = athleteRepository.getAthleteById(trainingSheet.getAthleteId());
		if (!athleteOpt.isPresent()) {
			// Manejar el caso en que no exista el atleta
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

		// Guardamos la nueva ficha de entrenamiento en su repositorio
		TrainingSheet savedTrainingSheet = trainingSheetRepository.createTrainingSheet(trainingSheet);
		String exitId = savedTrainingSheet.getId();

		// Actualizamos el atleta con el ID generado de la ficha de entrenamiento
		Athlete athlete = athleteOpt.get();
		athlete.getIdTrainingSheet().add(exitId);
		athleteRepository.modifyAthlete(athlete);

		return exitId;
	}

	/**
	 * Retrieves a training sheet by ID.
	 *
	 * @param idTrainingSheet the ID of the training sheet to be retrieved
	 * @return an Optional containing the training sheet if found, or an empty
	 *         Optional if not
	 */
	@Override
	@Transactional
	public Optional<TrainingSheet> getTrainingSheet(@Valid String idTrainingSheet) {
		log.debug("getTrainingSheet");

		return trainingSheetRepository.getTrainingSheet(idTrainingSheet);
	}

	/**
	 * Retrieves a paginated list of training sheets by athlete ID.
	 *
	 * @param id       the ID of the athlete
	 * @param pageable the pagination information
	 * @return a paginated list of training sheets
	 * @throws BusinessException if no training sheets are found for the athlete or
	 *                           the athlete is not found
	 */
	@Transactional
	public Page<TrainingSheet> getTrainingSheetsByAthleteId(String id, Pageable pageable) throws BusinessException {
		log.debug("getTrainingSheetsByAthleteId");

		Optional<Athlete> athleteOpt = athleteRepository.getAthlete(id);
		if (athleteOpt.isPresent()) {
			Athlete athlete = athleteOpt.get();
			List<String> trainingSheetsIds = athlete.getIdTrainingSheet();
			if (trainingSheetsIds.isEmpty()) {
				throw new BusinessException("No training sheets found for the athlete.");
			}
			return trainingSheetRepository.getTrainingSheetsByAthleteId(athlete.getId(), pageable);
		} else {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}
	}

	/**
	 * Retrieves a paginated list of training sheets by coach ID.
	 *
	 * @param id       the ID of the coach
	 * @param pageable the pagination information
	 * @return a paginated list of training sheets
	 * @throws BusinessException if no training sheets are found for the coach or
	 *                           the coach is not found
	 */
	@Transactional
	public Page<TrainingSheet> getTrainingSheetsByCoachId(String id, Pageable pageable) throws BusinessException {
		log.debug("getTrainingSheetsByCoachId");

		Optional<Coach> coachOpt = coachRepository.getCoachById(id);
		if (coachOpt.isPresent()) {
			Coach coach = coachOpt.get();
			List<String> trainingSheetsIds = coach.getIdTrainingSheet();
			if (trainingSheetsIds.isEmpty()) {
				throw new BusinessException("No training sheets found for the coach.");
			}
			return trainingSheetRepository.getTrainingSheetsByCoachId(coach.getId(), pageable);
		} else {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}
	}

	/**
	 * Retrieves a paginated list of training sheets.
	 *
	 * @param pageable the pagination information
	 * @return a paginated list of training sheets
	 * @throws BusinessException if the pagination size exceeds the maximum allowed
	 */
	@Override
	@Transactional
	public Page<TrainingSheet> getTrainingSheets(@Valid Pageable pageable) throws BusinessException {
		log.debug("getTrainingSheets");

		if (pageable.getPageSize() >= Constants.MAXIMUM_PAGINATION) {
			throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
		}

		return trainingSheetRepository.getTrainingSheets(pageable);
	}

	/**
	 * Partially updates a training sheet's information.
	 *
	 * @param inputTrainingSheet the training sheet with updated information
	 * @throws BusinessException if the training sheet is not found
	 */
	@Override
	@Transactional
	public void modificationPartialTrainingSheet(@Valid TrainingSheet inputTrainingSheet) throws BusinessException {
		log.debug("modificationPartialTrainingSheet");

		Optional<TrainingSheet> optTrainingSheet = trainingSheetRepository.getTrainingSheet(inputTrainingSheet.getId());
		if (!optTrainingSheet.isPresent()) {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

		TrainingSheet updated = optTrainingSheet.get();
		healthliftingPatchMapper.update(updated, inputTrainingSheet);

		trainingSheetRepository.modifyTrainingSheet(updated);

	}

	/**
	 * Fully updates a training sheet's information.
	 *
	 * @param inputTrainingSheet the training sheet with updated information
	 * @throws BusinessException if the training sheet is not found
	 */
	@Override
	@Transactional
	public void modificationTotalTrainingSheet(@Valid TrainingSheet inputTrainingSheet) throws BusinessException {
		log.debug("modificationTotalTrainingSheet");

		Optional<TrainingSheet> optTrainingSheet = trainingSheetRepository.getTrainingSheet(inputTrainingSheet.getId());
		if (!optTrainingSheet.isPresent()) {
			throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
		}

		trainingSheetRepository.modifyTrainingSheet(inputTrainingSheet);

	}

	/**
	 * Deletes a training sheet by ID.
	 *
	 * @param idTrainingSheet the ID of the training sheet to be deleted
	 * @throws BusinessException if the training sheet is not found
	 */
	@Override
	@Transactional
	public void deleteTrainingSheet(@Valid String idTrainingSheet) throws BusinessException {
		log.debug("deleteTrainingSheet");

		Optional<TrainingSheet> optTrainingSheet = trainingSheetRepository.getTrainingSheet(idTrainingSheet);
		if (!optTrainingSheet.isPresent()) {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

		trainingSheetRepository.deleteTrainingSheet(idTrainingSheet);

	}

}
