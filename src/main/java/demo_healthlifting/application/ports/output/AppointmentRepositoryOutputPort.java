package demo_healthlifting.application.ports.output;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.Appointment;
import jakarta.validation.Valid;

public interface AppointmentRepositoryOutputPort {

	Appointment createAppointment(@Valid Appointment appointment);

	Optional<Appointment> getAppointment(@Valid String idAppointment);

	Page<Appointment> getAppointmentsByCoachId(String id, Pageable pageable) throws BusinessException;

	Page<Appointment> getAppointmentsByAthleteId(String id, Pageable pageable) throws BusinessException;

	Page<Appointment> getAppointments(@Valid Pageable pageable);

	Page<Appointment> getAppointmentsByCoachPersonalInformationDocument(String document, Pageable pageable);

	Page<Appointment> getAppointmentsByAthletePersonalInformationDocument(String document, Pageable pageable);

	void modifyAppointment(Appointment updated);

	void deleteAppointment(@Valid String idAppointment);

	Appointment addAppointment(@Valid Appointment appointment);

}
