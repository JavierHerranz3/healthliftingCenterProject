package demo_healthlifting.application.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import demo_healthlifting.domain.model.TrainingSheet;
import jakarta.validation.Valid;

public interface TrainingSheetRepositoryOutputPort {
	TrainingSheet createTrainingSheet(@Valid TrainingSheet trainingSheet);

	Optional<TrainingSheet> getTrainingSheet(@Valid String idTrainingSheet);;

	Page<TrainingSheet> getTrainingSheets(@Valid Pageable pageable);

	void modifyTrainingSheet(@Valid TrainingSheet updated);

	void deleteTrainingSheet(@Valid String idTrainingSheet);

}
