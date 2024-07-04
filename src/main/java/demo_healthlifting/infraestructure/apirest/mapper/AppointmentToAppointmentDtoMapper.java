package demo_healthlifting.infraestructure.apirest.mapper;

import org.mapstruct.Mapper;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.Appointment;
import demo_healthlifting.infraestructure.apirest.dto.response.AppointmentDto;

@Mapper(componentModel = "spring")
public interface AppointmentToAppointmentDtoMapper extends BaseMapper<Appointment, AppointmentDto> {

}
