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

	@Override
	@CacheEvict(value = "athletes", allEntries = true)
	public String createAthlete(@Valid Athlete input) {
		log.debug("createAthlete");
		AthleteEntity entity = athleteToAthleteEntityMapper.fromInputToOutput(input);
		return athleteRepository.save(entity).getId();
	}

	@Override
	@Cacheable(value = "athletes", key = "#id")
	public Optional<Athlete> getAthlete(@Valid String id) {
		log.debug("getAthlete");

		Optional<AthleteEntity> resourceEntity = athleteRepository.findById(id);
		return athleteToAthleteEntityMapper.fromOutputToInput(resourceEntity);
	}

	@Override
	@Cacheable(value = "athletes", key = "#pageable")
	public Page<Athlete> getAthletes(@Valid Pageable pageable) {
		log.debug("getAthletes");
		Page<AthleteEntity> listEntity = athleteRepository.findByEliminate(false, pageable);
		return athleteToAthleteEntityMapper.fromOutputToInput(listEntity);
	}

	@Override
	@CacheEvict(value = "athletes", allEntries = true)
	public void modifyAthlete(@Valid Athlete input) {
		log.debug("modifyAthletes");
		AthleteEntity entity = athleteToAthleteEntityMapper.fromInputToOutput(input);
		athleteRepository.save(entity);

	}

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

	@Override
	@Cacheable(value = "persons", key = "#id")
	public Optional<Athlete> getAthleteById(@Valid String id) {
		log.debug("getAthleteById");
		Optional<AthleteEntity> athleteEntity = athleteRepository.findByIdAndEliminate(id, false);
		return athleteToAthleteEntityMapper.fromOutputToInput(athleteEntity);
	}

}
