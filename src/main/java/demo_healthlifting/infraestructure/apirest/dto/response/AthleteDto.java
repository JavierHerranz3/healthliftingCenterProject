package demo_healthlifting.infraestructure.apirest.dto.response;

import java.util.List;

import demo_healthlifting.domain.model.PersonalInformation;
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
public class AthleteDto {
	private String id;

	private int age;

	private String height;

	private PersonalInformation personalInformation;

	private List<String> idAppointments;

	private List<String> idTrainingSheet;

}
