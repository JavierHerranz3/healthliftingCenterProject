package demo_healthlifting.infraestructure.database.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import demo_healthlifting.domain.model.TrainingTypeRecord;
import jakarta.persistence.Id;
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
@Document("TRAININGSHEETS")
public class TrainingSheetEntity {
	@Id
	String id;

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

	boolean eliminate;

}
