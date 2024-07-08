package demo_healthlifting.infraestructure.apirest.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import demo_healthlifting.application.ports.input.AppointmentServiceInputPort;
import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.Appointment;
import demo_healthlifting.infraestructure.apirest.dto.request.PatchAppointmentDto;
import demo_healthlifting.infraestructure.apirest.dto.request.PostPutAppointmentDto;
import demo_healthlifting.infraestructure.apirest.dto.response.AppointmentDto;
import demo_healthlifting.infraestructure.apirest.mapper.AppointmentToAppointmentDtoMapper;
import demo_healthlifting.infraestructure.apirest.mapper.AppointmentToPatchAppointmentDtoMapper;
import demo_healthlifting.infraestructure.apirest.mapper.AppointmentToPostPutAppointmentDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
@RestController
@RequestMapping("/appointments")
@CrossOrigin(origins = "localhost:4200") // Aqui ponemos el localhost para que no haya problemas al conectarlo con
// el back
public class AppointmentController {
	@Autowired
	AppointmentServiceInputPort appointmentService;

	@Autowired
	AppointmentToPatchAppointmentDtoMapper appointmentToPatchAppointmentDtoMapper;

	@Autowired
	AppointmentToPostPutAppointmentDtoMapper appointmentToPostPutAppointmentDtoMapper;

	@Autowired
	AppointmentToAppointmentDtoMapper appointmentToAppointmentDtoMapper;

	/**
	 * Receive a list of all appointments
	 * 
	 * @param pageable the information
	 * @return response entity with the list of appointments
	 */
	@GetMapping
	public ResponseEntity getAppointments(Pageable pageable) {
		log.debug("getAppointments");

		Page<Appointment> listDomain;
		try {
			listDomain = appointmentService.getAppointments(pageable);
		} catch (BusinessException e) {
			log.error("Error Getting Appointments");
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(appointmentToAppointmentDtoMapper.fromInputToOutput(listDomain));
	}

	/**
	 * Receive an appointment by its ID
	 * 
	 * @param id of the appointmentID
	 * @return response entity with the appointment
	 */
	@GetMapping("/{appointmentId}")
	public ResponseEntity getAppointment(@PathVariable("appointmentId") String idAppointment) {
		log.debug("getAppointment");

		Optional<Appointment> domain = appointmentService.getAppointment(idAppointment);

		if (domain.isPresent()) {
			return ResponseEntity.ok(appointmentToAppointmentDtoMapper.fromInputToOutput(domain.get()));
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/coaches/searchByDocument")
	public ResponseEntity getAppointmentsByCoachDocument(@RequestParam String document, Pageable pageable) {

		try {
			Page<Appointment> coachAppointments = appointmentService.getAppointmentsByCoachDocument(document, pageable);
			log.debug("Retrieved appointments: {}", coachAppointments.getContent());
			return ResponseEntity.ok(coachAppointments);
		} catch (BusinessException e) {
			log.error("Error getting appointments", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping("/athletes/searchByDocument")
	public ResponseEntity getAppointmentsByAthleteDocument(@RequestParam String document, Pageable pageable) {

		try {
			Page<Appointment> athletesAppointments = appointmentService.getAppointmentsByAthleteDocument(document,
					pageable);
			log.debug("Retrieved appointments: {}", athletesAppointments.getContent());
			return ResponseEntity.ok(athletesAppointments);
		} catch (BusinessException e) {
			log.error("Error getting appointments", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping("/coaches/{id}")
	public ResponseEntity getAppointmentsByCoachId(@PathVariable("id") String id, Pageable pageable) {
		try {
			Page<Appointment> coachAppointments = appointmentService.getAppointmentsByCoachId(id, pageable);
			log.debug("Retrieved appointments: {}", coachAppointments.getContent());
			return ResponseEntity.ok(coachAppointments);
		} catch (BusinessException e) {
			log.error("Error getting appointments", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/athletes/{id}")
	public ResponseEntity getAppointmentsByAthleteId(@PathVariable("id") String id, Pageable pageable) {
		try {
			Page<Appointment> athletesAppointments = appointmentService.getAppointmentsByAthleteId(id, pageable);
			log.debug("Retrieved appointments: {}", athletesAppointments.getContent());
			return ResponseEntity.ok(athletesAppointments);
		} catch (BusinessException e) {
			log.error("Error getting appointments", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * post/create a new appointment
	 * 
	 * @param appointmentDto
	 * @return response entity with creation location header
	 */
	@PostMapping
	public ResponseEntity postAppointment(@RequestBody @Valid PostPutAppointmentDto appointmentDto) {
		try {
			// Convertir el DTO a dominio y crear la cita
			Appointment appDomain = appointmentToPostPutAppointmentDtoMapper.fromOutputToInput(appointmentDto);
			String appointmentId = appointmentService.createAppointment(appDomain);

			// Crear la respuesta
			AppointmentDto response = AppointmentDto.builder().id(appointmentId).date(appDomain.getDate())
					.athleteId(appDomain.getAthleteId()).athleteName(appDomain.getAthleteName())
					.athleteSurname(appDomain.getAthleteSurname()).athleteDocument(appDomain.getAthleteDocument())
					.coachId(appDomain.getCoachId()).coachName(appDomain.getCoachName())
					.coachSurname(appDomain.getCoachSurname()).coachDocument(appDomain.getCoachDocument())
					.trainingTypeRecord(appDomain.getTrainingTypeRecord()).build();

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Registrar el error con más detalles
			log.error("Error al crear la cita: ", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	/**
	 * Partially modifies an appointment
	 * 
	 * @param id  of appointment ID
	 * @param dto of appointment DTO
	 * @return response entity with the result of the method
	 */
	@PatchMapping("/{appointmentId}")
	public ResponseEntity patchAppointment(@PathVariable("appointmentId") String id,
			@RequestBody PatchAppointmentDto dto) {
		log.debug("patchAppointment");

		Appointment domain = appointmentToPatchAppointmentDtoMapper.fromOutputToInput(dto);
		domain.setId(id);
		try {
			appointmentService.modificationPartialAppointment(domain);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			log.error("Error modify Appointment", e);
			return ResponseEntity.badRequest().body(null); // O puedes retornar e.getMessage() si prefieres
		}
	}

	/**
	 * Deletes an existent appointment by ID
	 * 
	 * @param id of the appointment
	 * @return response entity with the result of the method
	 * @throws BusinessException if there is an error deleting the appointment
	 */
	@DeleteMapping("/{appointmentId}")
	public ResponseEntity deleteAppointment(@Valid @PathVariable("appointmentId") String id) throws BusinessException {

		log.debug("deleteAppointment");

		try {
			appointmentService.deleteAppointment(id);
		} catch (BusinessException e) {
			log.error("Error Delete Appointment", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
	}

	private URI createUri(String id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
	}
}
