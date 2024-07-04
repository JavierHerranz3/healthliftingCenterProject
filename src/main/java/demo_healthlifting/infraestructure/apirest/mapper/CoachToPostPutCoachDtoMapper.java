package demo_healthlifting.infraestructure.apirest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.Coach;
import demo_healthlifting.infraestructure.apirest.dto.request.PostPutCoachDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoachToPostPutCoachDtoMapper extends BaseMapper<Coach, PostPutCoachDto> {

}
