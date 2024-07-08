package demo_healthlifting.application.ports.input;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.TrainingSheet;
import jakarta.validation.Valid;

public interface TrainingSheetServiceInputPort {
	String createTrainingSheet(@Valid TrainingSheet trainingSheet) throws BusinessException;

	Optional<TrainingSheet> getTrainingSheet(@Valid String idTrainingSheet);

	Page<TrainingSheet> getTrainingSheets(@Valid Pageable pageable) throws BusinessException;

	Page<TrainingSheet> getTrainingSheetsByAthleteId(@Valid String athleteId, Pageable pageable)
			throws BusinessException;

	Page<TrainingSheet> getTrainingSheetsByCoachId(@Valid String coachId, Pageable pageable) throws BusinessException;

	void modificationPartialTrainingSheet(@Valid TrainingSheet inputTrainingSheet) throws BusinessException;

	void modificationTotalTrainingSheet(@Valid TrainingSheet inputTrainingSheet) throws BusinessException;

	void deleteTrainingSheet(@Valid String idTrainingSheet) throws BusinessException;

}