package demo_healthlifting.infraestructure.apirest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.Appointment;
import demo_healthlifting.infraestructure.apirest.dto.request.PostPutAppointmentDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppointmentToPostPutAppointmentDtoMapper extends BaseMapper<Appointment, PostPutAppointmentDto> {

}
