package demo_healthlifting.infraestructure.apirest.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import demo_healthlifting.application.ports.input.TrainingSheetServiceInputPort;
import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.TrainingSheet;
import demo_healthlifting.infraestructure.apirest.dto.request.PatchTrainingSheetDto;
import demo_healthlifting.infraestructure.apirest.dto.request.PostPutTrainingSheetDto;
import demo_healthlifting.infraestructure.apirest.dto.response.TrainingSheetDto;
import demo_healthlifting.infraestructure.apirest.mapper.TrainingSheetToPatchTrainingSheetDtoMapper;
import demo_healthlifting.infraestructure.apirest.mapper.TrainingSheetToPostPutTrainingSheetDtoMapper;
import demo_healthlifting.infraestructure.apirest.mapper.TrainingSheetToTrainingSheetDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
@RestController
@RequestMapping("/trainingsheets")
@CrossOrigin(origins = "localhost:4200") // Aqui ponemos el localhost para que no haya problemas al conectarlo con
// el back
public class TrainingSheetController {

	@Autowired
	TrainingSheetServiceInputPort trainingSheetService;

	@Autowired
	TrainingSheetToPatchTrainingSheetDtoMapper trainingSheetToPatchTrainingSheetDtoMapper;

	@Autowired
	TrainingSheetToPostPutTrainingSheetDtoMapper trainingSheetToPostPutTrainingSheetDtoMapper;

	@Autowired
	TrainingSheetToTrainingSheetDtoMapper trainingSheetToTrainingSheetDtoMapper;

	@PostMapping
	public ResponseEntity postTrainingSheet(@RequestBody PostPutTrainingSheetDto trainingSheetDto)
			throws BusinessException {
		TrainingSheet trainingSheet = trainingSheetToPostPutTrainingSheetDtoMapper.fromOutputToInput(trainingSheetDto);
		String idNewTrainingSheet = trainingSheetService.createTrainingSheet(trainingSheet);
		URI locationHeader = createUri(idNewTrainingSheet);

		TrainingSheetDto response = TrainingSheetDto.builder().id(idNewTrainingSheet)
				.trainingTypeRecord(trainingSheet.getTrainingTypeRecord()).observations(trainingSheet.getObservations())
				.coachId(trainingSheet.getCoachId()).athleteId(trainingSheet.getAthleteId())
				.appointmentId(trainingSheet.getAppointmentId()).build();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{trainingSheetId}")
	public ResponseEntity getTrainingSheet(@PathVariable("trainingSheetId") String id) {
		log.debug("getTrainingSheet");

		Optional<TrainingSheet> domain = trainingSheetService.getTrainingSheet(id);

		if (domain.isPresent()) {
			return ResponseEntity.ok(domain.get());
		} else {
			log.error("Error getting trainingSheet");
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/athletes/{athleteId}")
	public ResponseEntity getTrainingSheetsByAthleteId(@PathVariable String athleteId, Pageable pageable) {
		try {
			Page<TrainingSheet> trainingSheets = trainingSheetService.getTrainingSheetsByAthleteId(athleteId, pageable);
			return ResponseEntity.ok(trainingSheets);
		} catch (BusinessException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/coaches/{coachId}")
	public ResponseEntity getTrainingSheetsByCoachId(@PathVariable String coachId, Pageable pageable) {
		try {
			Page<TrainingSheet> trainingSheets = trainingSheetService.getTrainingSheetsByCoachId(coachId, pageable);
			return ResponseEntity.ok(trainingSheets);
		} catch (BusinessException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity getTrainingSheets(Pageable pageable) {
		log.debug("getTrainingSheets");

		Page<TrainingSheet> listDomain;
		try {
			listDomain = trainingSheetService.getTrainingSheets(pageable);
		} catch (BusinessException e) {
			log.error("Error Getting trainingSheets");
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(trainingSheetToTrainingSheetDtoMapper.fromInputToOutput(listDomain));
	}

	@PatchMapping("/{trainingSheetId}")
	public ResponseEntity patchTrainingSheet(@PathVariable("trainingSheetId") String id,
			@RequestBody PatchTrainingSheetDto dto) {
		log.debug("patchTrainingSheet");

		TrainingSheet domain = trainingSheetToPatchTrainingSheetDtoMapper.fromOutputToInput(dto);
		domain.setId(id);
		try {
			trainingSheetService.modificationPartialTrainingSheet(domain);
		} catch (BusinessException e) {
			log.error("Error modify TrainingSheet", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{trainingSheetId}")
	public ResponseEntity deleteTrainingSheet(@Valid @PathVariable("trainingSheetId") String id)
			throws BusinessException {

		log.debug("deleteTrainingSheet");

		try {
			trainingSheetService.deleteTrainingSheet(id);
		} catch (BusinessException e) {
			log.error("Error Delete TrainingSheet", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
	}

	private URI createUri(String id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
	}

}
