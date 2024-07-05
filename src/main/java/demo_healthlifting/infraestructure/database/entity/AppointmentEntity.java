package demo_healthlifting.infraestructure.database.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import demo_healthlifting.domain.model.TrainingTypeRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("APPOINTMENTS")
public class AppointmentEntity {
	@Id
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

	boolean eliminate;
}
