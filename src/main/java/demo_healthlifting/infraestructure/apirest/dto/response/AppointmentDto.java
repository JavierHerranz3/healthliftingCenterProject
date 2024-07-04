package demo_healthlifting.infraestructure.apirest.dto.response;

import java.time.LocalDateTime;

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
public class AppointmentDto {

	String id;

	LocalDateTime date;

	String coachId;

	String coachName;

	String coachSurname;

	String coachDocument;

	String athleteId;

	String athleteName;

	String athleteSurname;

	String athleteDocument;

	TrainingTypeRecord trainingTypeRecord;

}
