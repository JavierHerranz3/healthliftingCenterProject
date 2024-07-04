package demo_healthlifting.infraestructure.apirest.dto.request;

import demo_healthlifting.domain.model.TrainingTypeRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class PatchTrainingSheetDto {

	TrainingTypeRecord trainingTypeRecord;

	String trainingType;

	String observations;

	String coachId;

	String athleteId;

	String appointmentId;

}
