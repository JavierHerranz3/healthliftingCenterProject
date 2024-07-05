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

import demo_healthlifting.application.ports.input.AthleteServiceInputPort;
import demo_healthlifting.domain.exception.BusinessException;
import demo_healthlifting.domain.model.Athlete;
import demo_healthlifting.infraestructure.apirest.dto.request.PatchAthleteDto;
import demo_healthlifting.infraestructure.apirest.dto.request.PostPutAthleteDto;
import demo_healthlifting.infraestructure.apirest.dto.response.AthleteDto;
import demo_healthlifting.infraestructure.apirest.mapper.AthleteToAthleteDtoMapper;
import demo_healthlifting.infraestructure.apirest.mapper.AthleteToPatchAthleteDtoMapper;
import demo_healthlifting.infraestructure.apirest.mapper.AthleteToPostPutAthleteDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
@RestController
@RequestMapping("/athletes")
@CrossOrigin(origins = "localhost:4200") // Aqui ponemos el localhost para que no haya problemas al conectarlo con
// el back
public class AthleteController {

	@Autowired
	AthleteServiceInputPort athleteService;

	@Autowired
	AthleteToPatchAthleteDtoMapper athleteToPatchAthleteDtoMapper;

	@Autowired
	AthleteToPostPutAthleteDtoMapper athleteToPostPutAthleteDtoMapper;

	@Autowired
	AthleteToAthleteDtoMapper athleteToAthleteDtoMapper;

	@GetMapping
	public ResponseEntity getAthletes(Pageable pageable) {
		log.debug("getAthletes");

		Page<Athlete> listDomain;
		try {
			listDomain = athleteService.getAthletes(pageable);
		} catch (BusinessException e) {
			log.error("Error Getting Athletes");
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(athleteToAthleteDtoMapper.fromInputToOutput(listDomain));
	}

	@GetMapping("/{athleteId}")
	public ResponseEntity getAthlete(@PathVariable("athleteId") String idAhtlete) {
		log.debug("getAthlete");

		Optional<Athlete> domain = athleteService.getAthlete(idAhtlete);

		if (domain.isPresent()) {
			return ResponseEntity.ok(domain.get());
		} else {
			log.error("Error getting athlete");
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/list")
	public ResponseEntity getAthleteDocument(@PathVariable("list") @RequestParam String document) {
		log.debug("getAthleteDocument");
		Optional<Athlete> athleteOpt = athleteService.findByPersonalInformationAthlete(document);
		if (athleteOpt.isPresent()) {
			return ResponseEntity.ok(athleteToAthleteDtoMapper.fromInputToOutput(athleteOpt));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity postAthlete(@Valid @RequestBody PostPutAthleteDto athleteDto) throws BusinessException {
		log.debug("postAthlete");
		Athlete athlete = athleteToPostPutAthleteDtoMapper.fromOutputToInput(athleteDto);
		String idNewAthlete = athleteService.createAthlete(athlete);
		URI locationHeader = createUri(idNewAthlete);

		AthleteDto response = AthleteDto.builder().id(idNewAthlete).age(athlete.getAge()).height(athlete.getHeight())
				.personalInformation(athlete.getPersonalInformation()).idAppointments(athlete.getIdAppointments())
				.idTrainingSheet(athlete.getIdTrainingSheet()).build();

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{athleteDetailId}")
	public ResponseEntity patchAthlete(@PathVariable("athleteDetailId") String id, @RequestBody PatchAthleteDto dto) {
		log.debug("patchAthlete");

		Athlete domain = athleteToPatchAthleteDtoMapper.fromOutputToInput(dto);
		domain.setId(id);
		try {
			athleteService.modificationPartialAthlete(domain);
			return ResponseEntity.ok().build();
		} catch (BusinessException e) {
			log.error("Error modify Athlete", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@DeleteMapping("/{athleteId}")
	public ResponseEntity deleteAthlete(@Valid @PathVariable("athleteId") String id) throws BusinessException {

		log.debug("deleteAthlete");

		try {
			athleteService.deleteAthlete(id);
		} catch (BusinessException e) {
			log.error("Error Delete Athlete", e);
			return ResponseEntity.badRequest().body(e.getMessage());
		}

		return ResponseEntity.noContent().build();
	}

	private URI createUri(String id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
	}
}
