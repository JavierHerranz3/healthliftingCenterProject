package demo_healthlifting.infraestructure.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.Coach;
import demo_healthlifting.infraestructure.database.entity.CoachEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CoachToCoachEntityMapper extends BaseMapper<Coach, CoachEntity> {

}