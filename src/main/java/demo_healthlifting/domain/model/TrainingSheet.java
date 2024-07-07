package demo_healthlifting.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSheet {

	private String id;

	private TrainingTypeRecord trainingTypeRecord;

	private String observations;

	private String coachId;

	private String athleteId;

	private String appointmentId;

}
