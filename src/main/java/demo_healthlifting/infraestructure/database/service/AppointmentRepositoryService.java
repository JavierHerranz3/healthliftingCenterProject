package demo_healthlifting.infraestructure.database.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import demo_healthlifting.application.ports.output.AppointmentRepositoryOutputPort;
import demo_healthlifting.application.ports.utils.Errors;
import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.Appointment;
import demo_healthlifting.infraestructure.database.entity.AppointmentEntity;
import demo_healthlifting.infraestructure.database.entity.AthleteEntity;
import demo_healthlifting.infraestructure.database.entity.CoachEntity;
import demo_healthlifting.infraestructure.database.mapper.AppointmentToAppointmentEntityMapper;
import demo_healthlifting.infraestructure.database.repository.AppointmentRepository;
import demo_healthlifting.infraestructure.database.repository.AthleteRepository;
import demo_healthlifting.infraestructure.database.repository.CoachRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppointmentRepositoryService implements AppointmentRepositoryOutputPort {

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	CoachRepository coachRepository;

	@Autowired
	AthleteRepository athleteRepository;

	@Autowired
	AppointmentToAppointmentEntityMapper appointmentToAppointmentEntityMapper;

	/**
	 * Creates a new appointmentt and evicts all entries in the "tappointment"
	 * cache.
	 *
	 * @param appointmentt the appointment to be created
	 * @return the newly created training sheet
	 */
	@Override
	@CacheEvict(value = "appointments", allEntries = true)
	public Appointment createAppointment(@Valid Appointment appointment) {
		log.debug("createAppointment");
		AppointmentEntity appointmentEntity = appointmentToAppointmentEntityMapper.fromInputToOutput(appointment);
		appointmentEntity.setEliminate(false);
		AppointmentEntity savedAppointmentEntity = appointmentRepository.save(appointmentEntity);
		return appointmentToAppointmentEntityMapper.fromOutputToInput(savedAppointmentEntity);
	}

	/**
	 * Retrieves an appointment by ID and caches the result.
	 *
	 * @param id the ID of the appointment to be retrieved
	 * @return an Optional containing the appointment if found, or an empty Optional
	 *         if not
	 */
	@Override
	@Cacheable(value = "appointments", key = "#id")
	public Optional<Appointment> getAppointment(@Valid String id) {
		log.debug("getAppointment");

		Optional<AppointmentEntity> resourceEntity = appointmentRepository.findById(id);
		return appointmentToAppointmentEntityMapper.fromOutputToInput(resourceEntity);
	}

	/**
	 * Retrieves a paginated list of appointments and caches the result.
	 *
	 * @param pageable the pagination information
	 * @return a paginated list of appointments
	 */
	@Override
	@Cacheable(value = "appointments", key = "#pageable")
	public Page<Appointment> getAppointments(@Valid Pageable pageable) {
		log.debug("getAppointments");
		Page<AppointmentEntity> listEntity = appointmentRepository.findByEliminate(false, pageable);
		return appointmentToAppointmentEntityMapper.fromOutputToInput(listEntity);
	}

	/**
	 * Modifies an existing appointment and evicts all entries in the "appointment"
	 * cache.
	 *
	 * @param updated the appointment with updated information
	 */
	@Override
	@CacheEvict(value = "appointments", allEntries = true)
	public void modifyAppointment(@Valid Appointment input) {
		log.debug("modifyAppointment");
		AppointmentEntity entity = appointmentToAppointmentEntityMapper.fromInputToOutput(input);
		appointmentRepository.save(entity);

	}

	/**
	 * Deletes an appointment by marking it as eliminated and evicts all entries in
	 * the "appointment" cache.
	 *
	 * @param id the ID of the appointment to be deleted
	 */
	@Override
	@CacheEvict(value = "appointments", allEntries = true)
	public void deleteAppointment(@Valid String idAppointment) {
		log.debug("deleteAppointment");
		Optional<AppointmentEntity> optAppointment = appointmentRepository.findByIdAndEliminate(idAppointment, false);
		if (optAppointment.isPresent()) {
			optAppointment.get().setEliminate(true);
		}
		appointmentRepository.save(optAppointment.get());

	}

	@Override
	@CacheEvict(value = "appointments", allEntries = true)
	public Appointment addAppointment(@Valid Appointment appointment) {
		log.debug("addAppointment");

		AppointmentEntity appointmentEntity = appointmentToAppointmentEntityMapper.fromInputToOutput(appointment);
		appointmentEntity.setEliminate(false);
		AppointmentEntity savedAppointmentEntity = appointmentRepository.save(appointmentEntity);
		return appointmentToAppointmentEntityMapper.fromOutputToInput(savedAppointmentEntity);
	}

	/**
	 * Retrieves a paginated list of apointments by document and caches the result.
	 *
	 * @param document the document of the coach
	 * @param pageable the pagination information
	 * @return a paginated list of apointments
	 * @throws BusinessException if no apointments are found for the coach or the
	 *                           coach is not found
	 */
	@Override
	@Cacheable(value = "appointments", key = "#document")
	public Page<Appointment> getAppointmentsByCoachPersonalInformationDocument(String document, Pageable pageable) {
		log.debug("getAppointmentsByPersonalInformationDocument");

		Optional<CoachEntity> coachOpt = coachRepository.findByPersonalInformationDocumentAndEliminate(document, false);
		CoachEntity coach = coachOpt.get();
		List<String> appointmentIds = coach.getIdAppointments();
		Page<AppointmentEntity> appointmentEntities = appointmentRepository.findByIdInAndEliminate(appointmentIds,
				false, pageable);
		return appointmentEntities.map(appointmentToAppointmentEntityMapper::fromOutputToInput);
	}

	/**
	 * Retrieves a paginated list of apointments by document and caches the result.
	 *
	 * @param document the document of the athlete
	 * @param pageable the pagination information
	 * @return a paginated list of apointments
	 * @throws BusinessException if no apointments are found for the athlete or the
	 *                           athlete is not found
	 */
	@Override
	@Cacheable(value = "appointments", key = "#document")
	public Page<Appointment> getAppointmentsByAthletePersonalInformationDocument(String document, Pageable pageable) {
		log.debug("getAppointmentsByAthletePersonalInformationDocument");

		Optional<AthleteEntity> athleteOpt = athleteRepository.findByPersonalInformationDocumentAndEliminate(document,
				false);
		AthleteEntity athlete = athleteOpt.get();
		List<String> appointmentIds = athlete.getIdAppointments();
		Page<AppointmentEntity> appointmentEntities = appointmentRepository.findAthleteByEliminateAndIdIn(false,
				appointmentIds, pageable);
		return appointmentEntities.map(appointmentToAppointmentEntityMapper::fromOutputToInput);
	}

	/**
	 * Retrieves a paginated list of appointments by coach ID and caches the result.
	 *
	 * @param id       the ID of the coach
	 * @param pageable the pagination information
	 * @return a paginated list of appointments
	 * @throws BusinessException if no appointments are found for the coach or the
	 *                           coach is not found
	 */
	@Override
	@Transactional
	@Cacheable(value = "appointments", key = "#coachId")
	public Page<Appointment> getAppointmentsByCoachId(String coachId, Pageable pageable) throws BusinessException {
		log.debug("getAppointmentsByCoachId");

		log.debug("getAppointmentsByCoachId");

		Optional<CoachEntity> coachOpt = coachRepository.findByIdAndEliminate(coachId, false);
		if (coachOpt.isPresent()) {
			CoachEntity coach = coachOpt.get();
			List<String> appointmentIds = coach.getIdAppointments();
			Page<AppointmentEntity> appointmentEntities = appointmentRepository.findByIdInAndEliminate(appointmentIds,
					false, pageable);
			return appointmentEntities.map(appointmentToAppointmentEntityMapper::fromOutputToInput);
		} else {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}
	}

	/**
	 * Retrieves a paginated list of appointments by athlete ID and caches the
	 * result.
	 *
	 * @param id       the ID of the athlete
	 * @param pageable the pagination information
	 * @return a paginated list of appointments
	 * @throws BusinessException if no appointments are found for the athlete or the
	 *                           athlete is not found
	 */
	@Override
	@Cacheable(value = "appointments", key = "#id")
	public Page<Appointment> getAppointmentsByAthleteId(String id, Pageable pageable) throws BusinessException {
		log.debug("getAppointmentsByAthleteId");

		Optional<AthleteEntity> athleteOpt = athleteRepository.findByIdAndEliminate(id, false);
		if (athleteOpt.isPresent()) {
			AthleteEntity athlete = athleteOpt.get();
			List<String> appointmentIds = athlete.getIdAppointments();
			Page<AppointmentEntity> appointmentEntities = appointmentRepository.findAthleteByEliminateAndIdIn(false,
					appointmentIds, pageable);
			return appointmentEntities.map(appointmentToAppointmentEntityMapper::fromOutputToInput);
		} else {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}
	}
}
