package demo_healthlifting.infraestructure.database.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import demo_healthlifting.application.ports.output.AppointmentRepositoryOutputPort;
import demo_healthlifting.domain.model.Appointment;
import demo_healthlifting.infraestructure.database.entity.AppointmentEntity;
import demo_healthlifting.infraestructure.database.mapper.AppointmentToAppointmentEntityMapper;
import demo_healthlifting.infraestructure.database.repository.AppointmentRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AppointmentRepositoryService implements AppointmentRepositoryOutputPort {

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	AppointmentToAppointmentEntityMapper appointmentToAppointmentEntityMapper;

	@Override
	@CacheEvict(value = "appointments", allEntries = true)
	public Appointment createAppointment(@Valid Appointment appointment) {
		log.debug("createAppointment");
		AppointmentEntity appointmentEntity = appointmentToAppointmentEntityMapper.fromInputToOutput(appointment);
		appointmentEntity.setEliminate(false);
		AppointmentEntity savedAppointmentEntity = appointmentRepository.save(appointmentEntity);
		return appointmentToAppointmentEntityMapper.fromOutputToInput(savedAppointmentEntity);
	}

	@Override
	@Cacheable(value = "appointments", key = "#id")
	public Optional<Appointment> getAppointment(@Valid String id) {
		log.debug("getAppointment");

		Optional<AppointmentEntity> resourceEntity = appointmentRepository.findById(id);
		return appointmentToAppointmentEntityMapper.fromOutputToInput(resourceEntity);
	}

	@Override
	@Cacheable(value = "appointments", key = "#pageable")
	public Page<Appointment> getAppointments(@Valid Pageable pageable) {
		log.debug("getAppointments");
		Page<AppointmentEntity> listEntity = appointmentRepository.findByEliminate(false, pageable);
		return appointmentToAppointmentEntityMapper.fromOutputToInput(listEntity);
	}

	@Override
	@CacheEvict(value = "appointments", allEntries = true)
	public void modifyAppointment(@Valid Appointment input) {
		log.debug("modifyAppointment");
		AppointmentEntity entity = appointmentToAppointmentEntityMapper.fromInputToOutput(input);
		appointmentRepository.save(entity);

	}

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

}
