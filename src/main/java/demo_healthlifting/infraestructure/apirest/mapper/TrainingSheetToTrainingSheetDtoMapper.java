package demo_healthlifting.infraestructure.apirest.mapper;

import org.mapstruct.Mapper;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.TrainingSheet;
import demo_healthlifting.infraestructure.apirest.dto.response.TrainingSheetDto;

@Mapper(componentModel = "spring")
public interface TrainingSheetToTrainingSheetDtoMapper extends BaseMapper<TrainingSheet, TrainingSheetDto> {

}
