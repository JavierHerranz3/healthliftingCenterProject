package demo_healthlifting.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import demo_healthlifting.domain.model.Appointment;
import demo_healthlifting.domain.model.Athlete;
import demo_healthlifting.domain.model.Coach;
import demo_healthlifting.domain.model.TrainingSheet;
import jakarta.validation.Valid;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface HealthliftingPatchMapper {

	void update(@MappingTarget Athlete updated, @Valid Athlete inputAthlete);

	void update(@MappingTarget Coach updated, @Valid Coach inputCoach);

	void update(@MappingTarget Appointment updated, @Valid Appointment inputAppointment);

	void update(@MappingTarget TrainingSheet updated, @Valid TrainingSheet inputTrainingSheet);

}
