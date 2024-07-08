package demo_healthlifting.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import demo_healthlifting.application.ports.input.AppointmentServiceInputPort;
import demo_healthlifting.application.ports.input.AthleteServiceInputPort;
import demo_healthlifting.application.ports.input.CoachServiceInputPort;
import demo_healthlifting.application.ports.output.AppointmentRepositoryOutputPort;
import demo_healthlifting.application.ports.output.AthleteRepositoryOutputPort;
import demo_healthlifting.application.ports.output.CoachRepositoryOutputPort;
import demo_healthlifting.application.ports.utils.Constants;
import demo_healthlifting.application.ports.utils.Errors;
import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.mapper.HealthliftingPatchMapper;
import demo_healthlifting.domain.model.Appointment;
import demo_healthlifting.domain.model.Athlete;
import demo_healthlifting.domain.model.Coach;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HealthliftingService
		implements AthleteServiceInputPort, CoachServiceInputPort, AppointmentServiceInputPort {

	@Autowired
	AthleteRepositoryOutputPort athleteRepository;

	@Autowired
	CoachRepositoryOutputPort coachRepository;

	@Autowired
	AppointmentRepositoryOutputPort appointmentRepository;

	@Autowired
	HealthliftingPatchMapper healthliftingPatchMapper;

	/**
	 * Creates a new athlete.
	 *
	 * @param athlete the athlete to be created
	 * @return the ID of the newly created athlete
	 */
	@Override
	@Transactional
	public String createAthlete(@Valid Athlete athlete) {
		log.debug("createAthlete");

		List<String> appointments = new ArrayList<>();
		athlete.setIdAppointments(appointments);

		List<String> trainingSheet = new ArrayList<>();
		athlete.setIdTrainingSheet(trainingSheet);

		// Create the athlete
		String nuevoId = athleteRepository.createAthlete(athlete);
		athlete.setId(nuevoId);

		return nuevoId;
	}

	/**
	 * Creates a new coach.
	 *
	 * @param coach the coach to be created
	 * @return the ID of the newly created coach
	 */
	@Override
	@Transactional
	public String createCoach(@Valid Coach coach) {
		log.debug("createCoach");

		List<String> appointments = new ArrayList<>();
		coach.setIdAppointments(appointments);

		List<String> trainingSheet = new ArrayList<>();
		coach.setIdTrainingSheet(trainingSheet);

		// Create the coach
		String nuevoId = coachRepository.createCoach(coach);
		coach.setId(nuevoId);

		return nuevoId;
	}

	/**
	 * Creates a new appointment.
	 *
	 * @param appointment the appointment to be created
	 * @return the ID of the newly created appointment
	 * @throws BusinessException if there is a problem creating the appointment
	 */
	@Override
	@Transactional
	public String createAppointment(@Valid Appointment appointment) throws BusinessException {
		log.debug("Entering createAppointment with appointment: {}", appointment);

		if (appointment == null) {
			log.error("Appointment object is null");
			throw new IllegalArgumentException("El objeto appointment no puede ser null.");
		}

		String exitId = null;

		// Buscar al atleta por su ID
		log.debug("Looking for athlete with ID: {}", appointment.getAthleteId());
		Optional<Athlete> athleteOpt = athleteRepository.getAthleteById(appointment.getAthleteId());
		if (athleteOpt.isPresent()) {
			Athlete athlete = athleteOpt.get();
			log.debug("Atleta encontrado: {}", athlete);

			// Buscar al entrenador por su ID
			log.debug("Looking for coach with ID: {}", appointment.getCoachId());
			Optional<Coach> coachOpt = coachRepository.getCoachById(appointment.getCoachId());
			if (coachOpt.isPresent()) {
				Coach coach = coachOpt.get();
				log.debug("Entrenador encontrado: {}", coach);

				// Establecer los datos del atleta en la cita
				appointment.setAthleteId(athlete.getId());
				appointment.setAthleteName(athlete.getPersonalInformation().getName());
				appointment.setAthleteSurname(athlete.getPersonalInformation().getSurname());
				appointment.setAthleteDocument(athlete.getPersonalInformation().getDocument());

				// Establecer los datos del entrenador en la cita
				appointment.setCoachId(coach.getId());
				appointment.setCoachName(coach.getPersonalInformation().getName());
				appointment.setCoachSurname(coach.getPersonalInformation().getSurname());
				appointment.setCoachDocument(coach.getPersonalInformation().getDocument());

				// Guardar la cita
				log.debug("Saving the appointment");
				Appointment savedAppointment = appointmentRepository.addAppointment(appointment);
				exitId = savedAppointment.getId();
				log.debug("Appointment saved with ID: {}", exitId);

				// Añadir la cita a la lista de citas del atleta
				log.debug("Adding appointment to athlete's list of appointments");
				athlete.getIdAppointments().add(savedAppointment.getId());
				athleteRepository.modifyAthlete(athlete);

				// Añadir la cita a la lista de citas del entrenador
				log.debug("Adding appointment to coach's list of appointments");
				coach.getIdAppointments().add(savedAppointment.getId());
				coachRepository.modifyCoach(coach);

			} else {
				log.error("Entrenador no encontrado con ID: {}", appointment.getCoachId());
				throw new BusinessException(Errors.PERSON_NOT_FOUND);
			}
		} else {
			log.error("Atleta no encontrado con ID: {}", appointment.getAthleteId());
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}
		log.debug("Exiting createAppointment with exitId: {}", exitId);
		return exitId;
	}

	/**
	 * Retrieves an athlete by ID.
	 *
	 * @param idAthlete the ID of the athlete to be retrieved
	 * @return an Optional containing the athlete if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Transactional
	public Optional<Athlete> getAthlete(@Valid String idAthlete) {
		log.debug("getAthlete");

		return athleteRepository.getAthlete(idAthlete);
	}

	/**
	 * Retrieves a coach by ID.
	 *
	 * @param idCoach the ID of the coach to be retrieved
	 * @return an Optional containing the coach if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Transactional
	public Optional<Coach> getCoach(@Valid String idCoach) {
		log.debug("getCoach");

		return coachRepository.getCoach(idCoach);
	}

	/**
	 * Finds an athlete by their personal information document.
	 *
	 * @param document the document of the athlete to be found
	 * @return an Optional containing the athlete if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Transactional
	public Optional<Athlete> findByAthletePersonalInformationDocument(@Valid String document) {
		return athleteRepository.findByAthletePersonalInformationDocument(document);
	}

	/**
	 * Finds a coach by their personal information document.
	 *
	 * @param document the document of the coach to be found
	 * @return an Optional containing the coach if found, or an empty Optional if
	 *         not
	 */
	@Override
	@Transactional
	public Optional<Coach> findByCoachPersonalInformationDocument(@Valid String document) {
		log.debug("findByCoachPersonalInformationDocument");

		return coachRepository.findByCoachPersonalInformationDocument(document);
	}

	/**
	 * Retrieves a paginated list of appointments by coach document.
	 *
	 * @param document the document of the coach
	 * @param pageable the pagination information
	 * @return a paginated list of appointments
	 * @throws BusinessException if the coach is not found
	 */
	@Override
	@Transactional
	public Page<Appointment> getAppointmentsByCoachDocument(String document, Pageable pageable)
			throws BusinessException {
		log.debug("getAppointmentsByCoachDocument");

		Optional<Coach> coachOpt = coachRepository.findByCoachPersonalInformationDocument(document);
		if (coachOpt.isPresent()) {
			Coach coach = coachOpt.get();
			return appointmentRepository.getAppointmentsByCoachPersonalInformationDocument(
					coach.getPersonalInformation().getDocument(), pageable);
		} else {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}
	}

	/**
	 * Retrieves a paginated list of appointments by athlete document.
	 *
	 * @param document the document of the athlete
	 * @param pageable the pagination information
	 * @return a paginated list of appointments
	 * @throws BusinessException if the coach is not found
	 */
	@Override
	@Transactional
	public Page<Appointment> getAppointmentsByAthleteDocument(String document, Pageable pageable)
			throws BusinessException {
		log.debug("getAppointmentsByAthleteDocument");

		Optional<Athlete> athleteOpt = athleteRepository.findByAthletePersonalInformationDocument(document);
		if (athleteOpt.isPresent()) {
			Athlete athlete = athleteOpt.get();
			return appointmentRepository.getAppointmentsByAthletePersonalInformationDocument(
					athlete.getPersonalInformation().getDocument(), pageable);
		} else {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}
	}

	/**
	 * Retrieves an appointment by ID.
	 *
	 * @param idAppointment the ID of the appointment to be retrieved
	 * @return an Optional containing the appointment if found, or an empty Optional
	 *         if not
	 */
	@Override
	@Transactional
	public Optional<Appointment> getAppointment(@Valid String idAppointment) {
		log.debug("getAppointment");

		return appointmentRepository.getAppointment(idAppointment);
	}

	/**
	 * Retrieves a paginated list of appointments by coach ID.
	 *
	 * @param id       the ID of the coach
	 * @param pageable the pagination information
	 * @return a paginated list of appointments
	 * @throws BusinessException if the coach is not found
	 */
	@Override
	@Transactional
	public Page<Appointment> getAppointmentsByCoachId(String id, Pageable pageable) throws BusinessException {
		log.debug("getAppointmentByCoachId");

		Optional<Coach> coachOpt = coachRepository.getCoachById(id);
		if (coachOpt.isPresent()) {
			Coach coach = coachOpt.get();
			return appointmentRepository.getAppointmentsByCoachId(coach.getId(), pageable);
		} else {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}
	}

	/**
	 * Retrieves a paginated list of appointments by athlete ID.
	 *
	 * @param id       the ID of the athlete
	 * @param pageable the pagination information
	 * @return a paginated list of appointments
	 * @throws BusinessException if the athlete is not found
	 */
	@Override
	@Transactional
	public Page<Appointment> getAppointmentsByAthleteId(String id, Pageable pageable) throws BusinessException {
		log.debug("getAppointmentsByAthleteId");

		Optional<Athlete> athleteOpt = athleteRepository.getAthlete(id);
		if (athleteOpt.isPresent()) {
			Athlete athlete = athleteOpt.get();
			return appointmentRepository.getAppointmentsByAthleteId(athlete.getId(), pageable);
		} else {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}
	}

	/**
	 * Retrieves a paginated list of athletes.
	 *
	 * @param pageable the pagination information
	 * @return a paginated list of athletes
	 * @throws BusinessException if the pagination size exceeds the maximum allowed
	 */
	@Override
	@Transactional
	public Page<Athlete> getAthletes(@Valid Pageable pageable) throws BusinessException {
		log.debug("getAthletes");

		if (pageable.getPageSize() >= Constants.MAXIMUM_PAGINATION) {
			throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
		}

		return athleteRepository.getAthletes(pageable);
	}

	/**
	 * Retrieves a paginated list of coaches.
	 *
	 * @param pageable the pagination information
	 * @return a paginated list of coaches
	 * @throws BusinessException if the pagination size exceeds the maximum allowed
	 */
	@Override
	@Transactional
	public Page<Coach> getCoaches(@Valid Pageable pageable) throws BusinessException {
		log.debug("getCoaches");

		if (pageable.getPageSize() >= Constants.MAXIMUM_PAGINATION) {
			throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
		}

		return coachRepository.getCoaches(pageable);
	}

	/**
	 * Retrieves a paginated list of appointments.
	 *
	 * @param pageable the pagination information
	 * @return a paginated list of appointments
	 * @throws BusinessException if the pagination size exceeds the maximum allowed
	 */
	@Override
	@Transactional
	public Page<Appointment> getAppointments(@Valid Pageable pageable) throws BusinessException {
		log.debug("getAppointments");

		if (pageable.getPageSize() >= Constants.MAXIMUM_PAGINATION) {
			throw new BusinessException(Errors.MAXIMUM_PAGINATION_EXCEEDED);
		}

		return appointmentRepository.getAppointments(pageable);
	}

	/**
	 * Partially updates an athlete's information.
	 *
	 * @param inputAthlete the athlete with updated information
	 * @throws BusinessException if the athlete is not found
	 */
	@Override
	@Transactional
	public void modificationPartialAthlete(@Valid Athlete inputAthlete) throws BusinessException {
		log.debug("modificationPartialPerson");

		Optional<Athlete> optAthlete = athleteRepository.getAthlete(inputAthlete.getId());
		if (!optAthlete.isPresent()) {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

		Athlete updated = optAthlete.get();
		healthliftingPatchMapper.update(updated, inputAthlete);

		athleteRepository.modifyAthlete(updated);

	}

	/**
	 * Partially updates a coach's information.
	 *
	 * @param inputCoach the coach with updated information
	 * @throws BusinessException if the coach is not found
	 */
	@Override
	@Transactional
	public void modificationPartialCoach(@Valid Coach inputCoach) throws BusinessException {
		log.debug("modificationPartialCoach");

		Optional<Coach> optCoach = coachRepository.getCoach(inputCoach.getId());
		if (!optCoach.isPresent()) {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

		Coach updated = optCoach.get();
		healthliftingPatchMapper.update(updated, inputCoach);

		coachRepository.modifyCoach(updated);

	}

	/**
	 * Partially updates an appointment's information.
	 *
	 * @param inputAppointment the appointment with updated information
	 * @throws BusinessException if the appointment is not found
	 */
	@Override
	@Transactional
	public void modificationPartialAppointment(@Valid Appointment inputAppointment) throws BusinessException {
		log.debug("modificationPartialAppointment");

		Optional<Appointment> optAppointment = appointmentRepository.getAppointment(inputAppointment.getId());
		if (!optAppointment.isPresent()) {
			throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
		}

		Appointment updated = optAppointment.get();
		healthliftingPatchMapper.update(updated, inputAppointment);

		appointmentRepository.modifyAppointment(updated);

	}

	/**
	 * Fully updates an athlete's information.
	 *
	 * @param inputAthlete the athlete with updated information
	 * @throws BusinessException if the athlete is not found
	 */
	@Override
	@Transactional
	public void modificationTotalAthlete(@Valid Athlete inputAthlete) throws BusinessException {
		log.debug("modificationTotalAthlete");

		Optional<Athlete> optAthlete = athleteRepository.getAthlete(inputAthlete.getId());
		if (!optAthlete.isPresent()) {
			throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
		}

		athleteRepository.modifyAthlete(inputAthlete);

	}

	/**
	 * Fully updates a coach's information.
	 *
	 * @param inputCoach the coach with updated information
	 * @throws BusinessException if the coach is not found
	 */
	@Override
	@Transactional
	public void modificationTotalCoach(@Valid Coach inputCoach) throws BusinessException {
		log.debug("modificationTotalCoach");

		Optional<Coach> optCoach = coachRepository.getCoach(inputCoach.getId());
		if (!optCoach.isPresent()) {
			throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
		}

		coachRepository.modifyCoach(inputCoach);

	}

	/**
	 * Fully updates an appointment's information.
	 *
	 * @param inputAppointment the appointment with updated information
	 * @throws BusinessException if the appointment is not found
	 */
	@Override
	@Transactional
	public void modificationTotalAppointment(@Valid Appointment inputAppointment) throws BusinessException {
		log.debug("modificationTotalAppointment");

		Optional<Appointment> optAppointment = appointmentRepository.getAppointment(inputAppointment.getId());
		if (!optAppointment.isPresent()) {
			throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
		}

		appointmentRepository.modifyAppointment(inputAppointment);

	}

	/**
	 * Deletes an athlete by ID.
	 *
	 * @param idAthlete the ID of the athlete to be deleted
	 * @throws BusinessException if the athlete is not found
	 */
	@Override
	@Transactional
	public void deleteAthlete(@Valid String idAthlete) throws BusinessException {
		log.debug("deleteAthlete");

		Optional<Athlete> optAthlete = athleteRepository.getAthlete(idAthlete);
		if (!optAthlete.isPresent()) {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

		athleteRepository.deleteAthlete(idAthlete);

	}

	/**
	 * Deletes a coach by ID.
	 *
	 * @param idCoach the ID of the coach to be deleted
	 * @throws BusinessException if the coach is not found
	 */
	@Override
	@Transactional
	public void deleteCoach(@Valid String idCoach) throws BusinessException {
		log.debug("deleteCoach");

		Optional<Coach> optCoach = coachRepository.getCoach(idCoach);
		if (!optCoach.isPresent()) {
			throw new BusinessException(Errors.PERSON_NOT_FOUND);
		}

		coachRepository.deleteCoach(idCoach);

	}

	/**
	 * Deletes an appointment by ID.
	 *
	 * @param idAppointment the ID of the appointment to be deleted
	 * @throws BusinessException if the appointment is not found
	 */
	@Override
	@Transactional
	public void deleteAppointment(@Valid String idAppointment) throws BusinessException {
		log.debug("deleteAppointment");

		Optional<Appointment> optAppointment = appointmentRepository.getAppointment(idAppointment);
		if (!optAppointment.isPresent()) {
			throw new BusinessException(Errors.APPOINTMENT_NOT_FOUND);
		}

		appointmentRepository.deleteAppointment(idAppointment);

	}

}
