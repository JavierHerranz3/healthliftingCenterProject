package demo_healthlifting.infraestructure.database.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import demo_healthlifting.domain.model.PersonalInformation;
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
@Document("ATHLETES")
public class AthleteEntity {
	@Id
	String id;

	int age;

	String height;

	PersonalInformation personalInformation;

	List<String> idAppointments;

	List<String> idTrainingSheet;

	boolean eliminate;

}
