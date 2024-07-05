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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import demo_healthlifting.application.ports.input.CoachServiceInputPort;
import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.Coach;
import demo_healthlifting.infraestructure.apirest.dto.request.PatchCoachDto;
import demo_healthlifting.infraestructure.apirest.dto.request.PostPutCoachDto;
import demo_healthlifting.infraestructure.apirest.dto.response.CoachDto;
import demo_healthlifting.infraestructure.apirest.mapper.CoachToCoachDtoMapper;
import demo_healthlifting.infraestructure.apirest.mapper.CoachToPatchCoachDtoMapper;
import demo_healthlifting.infraestructure.apirest.mapper.CoachToPostPutCoachDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
@RestController
@RequestMapping("/coaches")
@CrossOrigin(origins = "localhost:4200") // Aqui ponemos el localhost para que no haya problemas al conectarlo con
// el back
public class CoachController {
	@Autowired
	CoachServiceInputPort coachService;

	@Autowired
	CoachToPatchCoachDtoMapper coachToPatchCoachDtoMapper;

	@Autowired
	CoachToPostPutCoachDtoMapper coachToPostPutCoachDtoMapper;

	@Autowired
	CoachToCoachDtoMapper coachToCoachDtoMapper;

	@GetMapping
	public ResponseEntity getCoaches(Pageable pageable) {
		log.debug("getCoaches");

		Page<Coach> listDomain;
		try {
			listDomain = coachService.getCoaches(pageable);
		} catch (BusinessException e) {
			log.error("Error Getting Coaches");
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(coachToPatchCoachDtoMapper.fromInputToOutput(listDomain));
	}

	@GetMapping("/{coachId}")
	public ResponseEntity getCoach(@PathVariable("coachId") String idCoach) {
		log.debug("getCoach");

		Optional<Coach> domain = coachService.getCoach(idCoach);

		if (domain.isPresent()) {
			return ResponseEntity.ok(domain.get());
		} else {
			log.error("Error getting coach");
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/list")
	public ResponseEntity getCoachDocument(@PathVariable("list") @RequestParam String document) {
		log.debug("getAthleteDocument");
		Optional<Coach> coachOpt = coachService.findByPersonalInformationCoach(document);
		if (coachOpt.isPresent()) {
			return ResponseEntity.ok(coachToCoachDtoMapper.fromInputToOutput(coachOpt));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity postCoach(@RequestBody PostPutCoachDto coachDto) {
		log.debug("postCoach");

		Coach coach = coachToPostPutCoachDtoMapper.fromOutputToInput(coachDto);

		String idNewCoach = coachService.createCoach(coach);

		URI locationHeader = createUri(idNewCoach);

		CoachDto response = CoachDto.builder().id(idNewCoach).personalInformation(coach.getPersonalInformation())
				.idAppointments(coach.getIdAppointments()).idTrainingSheet(coach.getIdTrainingSheet()).build();

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{coachId}")
	public ResponseEntity patchCoach(@PathVariable("coachId") String id, @RequestBody PatchCoachDto dto) {
		log.debug("patchCoach");

		Coach domain = coachToPatchCoachDtoMapper.fromOutputToInput(dto);
		domain.setId(id);
		try {
			coachService.modificationPartialCoach(domain);
		} catch (BusinessException e) {
			log.error("Error modify Coach", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{coachId}")
	public ResponseEntity deleteCoach(@Valid @PathVariable("coachId") String id) throws BusinessException {

		log.debug("deleteCoach");

		try {
			coachService.deleteCoach(id);
		} catch (BusinessException e) {
			log.error("Error Delete Coach", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
	}

	private URI createUri(String id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
	}
}
