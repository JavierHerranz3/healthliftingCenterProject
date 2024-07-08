package demo_healthlifting.infraestructure.apirest.dto.request;

import demo_healthlifting.domain.model.TrainingTypeRecord;
import jakarta.validation.constraints.NotNull;
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
public class PostPutTrainingSheetDto {
	@NotNull
	private TrainingTypeRecord trainingTypeRecord;

	private String observations;

	private String coachId;

	private String coachName;

	private String coachSurname;

	private String coachDocument;

	private String athleteId;

	private String athleteName;

	private String athleteSurname;

	private String athleteDocument;

	private String appointmentId;

}
