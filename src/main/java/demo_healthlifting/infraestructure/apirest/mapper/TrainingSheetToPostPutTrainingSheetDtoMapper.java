package demo_healthlifting.infraestructure.apirest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.TrainingSheet;
import demo_healthlifting.infraestructure.apirest.dto.request.PostPutTrainingSheetDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingSheetToPostPutTrainingSheetDtoMapper
		extends BaseMapper<TrainingSheet, PostPutTrainingSheetDto> {

}
