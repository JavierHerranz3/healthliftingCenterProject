package demo_healthlifting.infraestructure.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.Athlete;
import demo_healthlifting.infraestructure.database.entity.AthleteEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AthleteToAthleteEntityMapper extends BaseMapper<Athlete, AthleteEntity> {

}
