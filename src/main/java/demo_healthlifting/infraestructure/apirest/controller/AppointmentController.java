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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import demo_healthlifting.application.ports.input.AppointmentServiceInputPort;
import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.Appointment;
import demo_healthlifting.infraestructure.apirest.dto.request.PatchAppointmentDto;
import demo_healthlifting.infraestructure.apirest.dto.request.PostPutAppointmentDto;
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
	public ResponseEntity getAppointment(Pageable pageable) {
		log.debug("getAppointment");

		Page<Appointment> listDomain;
		try {
			listDomain = appointmentService.getAppointments(pageable);
		} catch (BusinessException e) {
			log.error("Error Getting Appointments");
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(appointmentToPatchAppointmentDtoMapper.fromInputToOutput(listDomain));
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

	/**
	 * post/create a new appointment
	 * 
	 * @param appointmentDto
	 * @return response entity with creation location header
	 */
	@PostMapping
	public ResponseEntity postAppointment(@RequestBody PostPutAppointmentDto appointmentDto) {
		log.debug("postAppointment");

		try {
			// Convertir el DTO a dominio
			Appointment appointmentDomain = appointmentToPostPutAppointmentDtoMapper.fromOutputToInput(appointmentDto);

			// Llamar al servicio para agregar la cita
			String appointmentId = appointmentService.createAppointment(appointmentDomain);
			URI locationHeader = createUri(appointmentId);
			return ResponseEntity.created(locationHeader).build();
		} catch (Exception e) {
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
		} catch (BusinessException e) {
			log.error("Error modify Appointment", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
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
