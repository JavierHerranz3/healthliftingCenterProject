package demo_healthlifting.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalInformation {
	private String name;
	private String surname;
	private DocumentType documentType;
	private String document;

}
