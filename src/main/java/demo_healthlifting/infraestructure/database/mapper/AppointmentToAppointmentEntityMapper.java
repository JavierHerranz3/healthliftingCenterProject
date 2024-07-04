package demo_healthlifting.infraestructure.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.Appointment;
import demo_healthlifting.infraestructure.database.entity.AppointmentEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentToAppointmentEntityMapper extends BaseMapper<Appointment, AppointmentEntity> {

}
