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

	@Override
	@CacheEvict(value = "coaches", allEntries = true)
	public String createCoach(@Valid Coach input) {
		log.debug("createCoach");
		CoachEntity entity = coachToCoachEntityMapper.fromInputToOutput(input);
		return coachRepository.save(entity).getId();
	}

	@Override
	@Cacheable(value = "coaches", key = "#id")
	public Optional<Coach> getCoach(@Valid String id) {
		log.debug("getCoach");

		Optional<CoachEntity> resourceEntity = coachRepository.findById(id);
		return coachToCoachEntityMapper.fromOutputToInput(resourceEntity);
	}

	@Override
	@Cacheable(value = "coaches", key = "#pageable")
	public Page<Coach> getCoaches(@Valid Pageable pageable) {
		log.debug("getCoaches");
		Page<CoachEntity> listEntity = coachRepository.findByEliminate(false, pageable);
		return coachToCoachEntityMapper.fromOutputToInput(listEntity);
	}

	@Override
	@CacheEvict(value = "coaches", allEntries = true)
	public void modifyCoach(@Valid Coach input) {
		log.debug("modifyCoaches");
		CoachEntity entity = coachToCoachEntityMapper.fromInputToOutput(input);
		coachRepository.save(entity);

	}

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

	@Override
	@Cacheable(value = "persons", key = "#id")
	public Optional<Coach> getCoachById(@Valid String id) {
		log.debug("getCoachById");
		Optional<CoachEntity> coachEntity = coachRepository.findByIdAndEliminate(id, false);
		return coachToCoachEntityMapper.fromOutputToInput(coachEntity);
	}

	@Override
	public Optional<Coach> findByPersonalInformationCoach(@Valid String document) {
		log.debug("findByPersonalInformation");

		Optional<CoachEntity> coachEntityOpt = coachRepository.findByPersonalInformationAndEliminate(document, false);
		return coachToCoachEntityMapper.fromOutputToInput(coachEntityOpt);
	}
}
