package demo_healthlifting.infraestructure.database.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import demo_healthlifting.application.ports.output.TrainingSheetRepositoryOutputPort;
import demo_healthlifting.domain.model.TrainingSheet;
import demo_healthlifting.infraestructure.database.entity.TrainingSheetEntity;
import demo_healthlifting.infraestructure.database.mapper.TrainingSheetToTrainingSheetEntityMapper;
import demo_healthlifting.infraestructure.database.repository.TrainingSheetRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TrainingSheetRepositoryService implements TrainingSheetRepositoryOutputPort {

	@Autowired
	TrainingSheetRepository trainingSheetRepository;

	@Autowired
	TrainingSheetToTrainingSheetEntityMapper trainingSheetToTrainingSheetEntityMapper;

	@Override
	@CacheEvict(value = "trainingSheet", allEntries = true)
	public TrainingSheet createTrainingSheet(@Valid TrainingSheet trainingSheet) {
		log.debug("createTrainingSheet");
		TrainingSheetEntity entity = trainingSheetToTrainingSheetEntityMapper.fromInputToOutput(trainingSheet);

		TrainingSheetEntity saveTrainingSheetEntity = trainingSheetRepository.save(entity);
		return trainingSheetToTrainingSheetEntityMapper.fromOutputToInput(saveTrainingSheetEntity);
	}

	@Override
	@Cacheable(value = "trainingSheet", key = "#id")
	public Optional<TrainingSheet> getTrainingSheet(@Valid String id) {
		log.debug("getTrainingSheet");

		Optional<TrainingSheetEntity> resourceEntity = trainingSheetRepository.findById(id);
		return trainingSheetToTrainingSheetEntityMapper.fromOutputToInput(resourceEntity);
	}

	@Override
	@Cacheable(value = "trainingSheet", key = "#pageable")
	public Page<TrainingSheet> getTrainingSheets(@Valid Pageable pageable) {
		log.debug("getTrainingSheets");
		Page<TrainingSheetEntity> listEntity = trainingSheetRepository.findByEliminate(false, pageable);
		return trainingSheetToTrainingSheetEntityMapper.fromOutputToInput(listEntity);
	}

	@Override
	@CacheEvict(value = "trainingSheet", allEntries = true)
	public void modifyTrainingSheet(@Valid TrainingSheet updated) {
		log.debug("modifyTrainingSheet");
		TrainingSheetEntity entity = trainingSheetToTrainingSheetEntityMapper.fromInputToOutput(updated);
		trainingSheetRepository.save(entity);

	}

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

}
