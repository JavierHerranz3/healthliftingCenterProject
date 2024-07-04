package demo_healthlifting.infraestructure.apirest.mapper;

import org.mapstruct.Mapper;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.Coach;
import demo_healthlifting.infraestructure.apirest.dto.response.CoachDto;

@Mapper(componentModel = "spring")
public interface CoachToCoachDtoMapper extends BaseMapper<Coach, CoachDto> {

}
