package demo_healthlifting.infraestructure.apirest.dto.request;

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
public class PatchAthleteDto {
	int age;

	String height;

	PersonalInformation personalInformation;

}
