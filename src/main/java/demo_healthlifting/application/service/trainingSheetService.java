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
			throw new IllegalArgumentException("El objeto medicalRecord no puede ser null.");
		}
		String exitId = null;

		// Buscamos la persona en la que quieren guardar una ficha médica y validamos
		Optional<Athlete> athleteOpt = athleteRepository.getAthleteById(trainingSheet.getAthleteId());
		if (athleteOpt.isPresent()) {
			Athlete athlete = athleteOpt.get();

			// Obtenemos la ficha médica y seteamos los datos de la visita
			trainingSheet.setAthleteId(exitId);
			trainingSheet.setCoachId(exitId);

			// Guardamos la nueva ficha médica en su repo
			TrainingSheet savedTrainingSheet = trainingSheetRepositoryOutputPort.createTrainingSheet(trainingSheet);
			exitId = savedTrainingSheet.getId();

			// Actualizamos la persona con el id generado de la ficha
			Athlete athleteSave = athleteOpt.get();
			athleteSave.getIdTrainingSheet().add(savedTrainingSheet.getId());
			athleteRepository.modifyAthlete(athleteSave);

		} else {

			// Manejar el caso en que no exista alguna persona
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

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
