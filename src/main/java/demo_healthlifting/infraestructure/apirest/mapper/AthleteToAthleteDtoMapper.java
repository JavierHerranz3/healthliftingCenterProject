package demo_healthlifting.infraestructure.apirest.mapper;

import org.mapstruct.Mapper;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.Athlete;
import demo_healthlifting.infraestructure.apirest.dto.response.AthleteDto;

@Mapper(componentModel = "spring")
public interface AthleteToAthleteDtoMapper extends BaseMapper<Athlete, AthleteDto> {

}
