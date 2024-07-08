package demo_healthlifting.application.ports.input;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.Appointment;
import jakarta.validation.Valid;

public interface AppointmentServiceInputPort {

	String createAppointment(@Valid Appointment appointment) throws BusinessException;

	Optional<Appointment> getAppointment(@Valid String idAppointment);

	Page<Appointment> getAppointments(@Valid Pageable pageable) throws BusinessException;

	Page<Appointment> getAppointmentsByCoachDocument(String document, Pageable pageable) throws BusinessException;

	Page<Appointment> getAppointmentsByAthleteDocument(String document, Pageable pageable) throws BusinessException;

	void modificationPartialAppointment(@Valid Appointment inputAppointment) throws BusinessException;

	void modificationTotalAppointment(@Valid Appointment inputAppointment) throws BusinessException;

	void deleteAppointment(@Valid String id) throws BusinessException;

	Page<Appointment> getAppointmentsByCoachId(String id, Pageable pageable) throws BusinessException;

	Page<Appointment> getAppointmentsByAthleteId(String id, Pageable pageable) throws BusinessException;

}
