package demo_healthlifting.domain.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Athlete {

	private String id;

	private int age;

	private String height;

	private PersonalInformation personalInformation;

	private List<String> idAppointments;

	private List<String> idTrainingSheet;

}
