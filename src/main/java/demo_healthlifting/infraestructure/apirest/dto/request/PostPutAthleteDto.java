package demo_healthlifting.infraestructure.apirest.dto.request;

import java.util.List;

import demo_healthlifting.domain.model.PersonalInformation;
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
public class PostPutAthleteDto {
	@NotNull
	int age;

	String height;

	PersonalInformation personalInformation;

	List<String> idAppointments;

	List<String> idTrainingSheet;

}
