package demo_healthlifting.application.service;

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
import demo_healthlifting.domain.model.TrainingSheet;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class trainingSheetService implements TrainingSheetServiceInputPort {

	@Autowired
	TrainingSheetRepositoryOutputPort trainingSheetRepositoryOutputPort;

	@Autowired
	AthleteRepositoryOutputPort athleteRepository;

	@Autowired
	CoachRepositoryOutputPort coachRepository;

	@Autowired
	HealthliftingPatchMapper healthliftingPatchMapper;

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
		TrainingSheet savedTrainingSheet = trainingSheetRepositoryOutputPort.createTrainingSheet(trainingSheet);
		String exitId = savedTrainingSheet.getId();

		// Actualizamos el atleta con el ID generado de la ficha de entrenamiento
		Athlete athlete = athleteOpt.get();
		athlete.getIdTrainingSheet().add(exitId);
		athleteRepository.modifyAthlete(athlete);

		return exitId;
	}

	@Override
	@Transactional
	public Optional<TrainingSheet> getTrainingSheet(@Valid String idTrainingSheet) {
		log.debug("getTrainingSheet");

		return trainingSheetRepositoryOutputPort.getTrainingSheet(idTrainingSheet);
	}

	@Override
	@Transactional
	public Page<TrainingSheet> getTrainingSheets(@Valid Pageable pageable) throws BusinessException {
		log.debug("getTrainingSheets");

		if (pageable.getPageSize() >= Constants.MAXIMUM_PAGINATION) {
			throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
		}

		return trainingSheetRepositoryOutputPort.getTrainingSheets(pageable);
	}

	@Override
	@Transactional
	public void modificationPartialTrainingSheet(@Valid TrainingSheet inputTrainingSheet) throws BusinessException {
		log.debug("modificationPartialTrainingSheet");

		Optional<TrainingSheet> optTrainingSheet = trainingSheetRepositoryOutputPort
				.getTrainingSheet(inputTrainingSheet.getId());
		if (!optTrainingSheet.isPresent()) {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

		TrainingSheet updated = optTrainingSheet.get();
		healthliftingPatchMapper.update(updated, inputTrainingSheet);

		trainingSheetRepositoryOutputPort.modifyTrainingSheet(updated);

	}

	@Override
	@Transactional
	public void modificationTotalTrainingSheet(@Valid TrainingSheet inputTrainingSheet) throws BusinessException {
		log.debug("modificationTotalTrainingSheet");

		Optional<TrainingSheet> optTrainingSheet = trainingSheetRepositoryOutputPort
				.getTrainingSheet(inputTrainingSheet.getId());
		if (!optTrainingSheet.isPresent()) {
			throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
		}

		trainingSheetRepositoryOutputPort.modifyTrainingSheet(inputTrainingSheet);

	}

	@Override
	@Transactional
	public void deleteTrainingSheet(@Valid String idTrainingSheet) throws BusinessException {
		log.debug("deleteTrainingSheet");

		Optional<TrainingSheet> optTrainingSheet = trainingSheetRepositoryOutputPort.getTrainingSheet(idTrainingSheet);
		if (!optTrainingSheet.isPresent()) {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

		trainingSheetRepositoryOutputPort.deleteTrainingSheet(idTrainingSheet);

	}

}
