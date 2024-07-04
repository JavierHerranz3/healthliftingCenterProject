package demo_healthlifting.infraestructure.apirest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import demo_healthlifting.domain.mapper.BaseMapper;
import demo_healthlifting.domain.model.TrainingSheet;
import demo_healthlifting.infraestructure.apirest.dto.request.PatchTrainingSheetDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrainingSheetToPatchTrainingSheetDtoMapper extends BaseMapper<TrainingSheet, PatchTrainingSheetDto> {

}
