package demo_healthlifting.infraestructure.apirest.dto.common;

import demo_healthlifting.domain.model.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInformationDto {
	String name;
	String surname;
	DocumentType documentType;
	String document;

}
