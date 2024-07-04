package demo_healthlifting.infraestructure.apirest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.Athlete;
import demo_healthlifting.infraestructure.apirest.dto.request.PatchAthleteDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AthleteToPatchAthleteDtoMapper extends BaseMapper<Athlete, PatchAthleteDto> {

}
