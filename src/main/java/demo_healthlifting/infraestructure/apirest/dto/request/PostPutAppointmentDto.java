package demo_healthlifting.infraestructure.apirest.dto.request;

import java.time.LocalDateTime;

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
public class PostPutAppointmentDto {
	@NotNull
	LocalDateTime date;
	@NotNull
	String coachId;

	String coachName;

	String coachSurname;

	String coachDocument;
	@NotNull
	String athleteId;

	String athleteName;

	String athleteSurname;

	String athleteDocument;
	@NotNull
	TrainingTypeRecord trainingTypeRecord;

}
