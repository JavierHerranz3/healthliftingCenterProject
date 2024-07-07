package demo_healthlifting.domain.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Appointment {
	private String id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime date;

	private String coachId;

	private String coachName;

	private String coachSurname;

	private String coachDocument;

	private String athleteId;

	private String athleteName;

	private String athleteSurname;

	private String athleteDocument;

	private TrainingTypeRecord trainingTypeRecord;

}
