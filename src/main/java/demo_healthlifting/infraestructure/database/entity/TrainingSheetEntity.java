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

	TrainingTypeRecord trainingTypeRecord;

	String observations;

	String coachId;

	String athleteId;

	String appointmentId;

	boolean eliminate;

}
