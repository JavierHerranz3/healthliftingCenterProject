package demo_healthlifting.infraestructure.database.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.TrainingSheet;
import demo_healthlifting.infraestructure.database.entity.TrainingSheetEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingSheetToTrainingSheetEntityMapper extends BaseMapper<TrainingSheet, TrainingSheetEntity> {

}
