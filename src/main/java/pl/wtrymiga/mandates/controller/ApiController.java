package pl.wtrymiga.mandates.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.wtrymiga.mandates.controller.Message.MessageType;
import pl.wtrymiga.mandates.model.Company;
import pl.wtrymiga.mandates.model.Employee;
import pl.wtrymiga.mandates.model.Mandate;
import pl.wtrymiga.mandates.service.MandateService;
import pl.wtrymiga.mandates.service.PersonsService;

@RestController
@RequestMapping("/api")
public class ApiController {

	private final MandateService mandateService;
	private final PersonsService personsService;

	public ApiController(MandateService mandateService, PersonsService companiesService) {
		this.mandateService = mandateService;
		this.personsService = companiesService;
	}

	@PostMapping("/set/mandate")
	public Message saveMandate(@ModelAttribute Mandate mandate,
			@RequestParam(required = false, defaultValue = "false") boolean force) {
		if (force) {
			if (mandateService.findById(mandate.getId()).getPaid())
				return new Message("Nie można edytować opłaconego mandatu!", MessageType.warning);
			mandateService.update(mandate);
		} else
			mandateService.add(mandate);
		return new Message("Mandat zapisany pomyślnie!", MessageType.success);
	}

	@PostMapping("/set/mandate/mark")
	public Message markPaid(@RequestParam Long id) {
		mandateService.markPaid(id);
		return new Message("Oznaczono jako opłacony!", MessageType.success);
	}

	@DeleteMapping("/delete/mandate")
	public Message deleteMandate(@RequestParam Long id) {
		if (mandateService.findById(id).getPaid())
			return new Message("Nie można skasować opłaconego mandatu!", MessageType.warning);
		mandateService.deleteById(id);
		return new Message("Skasowano wpis!", MessageType.success);
	}

	@GetMapping("/get/companies")
	public List<Company> getCompanies() {
		return personsService.findAll();

	}

	@GetMapping("/get/employees")
	public List<Employee> getEmployeesByCompany(@RequestParam Long companyId) {
		return personsService.findByCompanyId(companyId);
	}

	@GetMapping("/get/attachment")
	public ResponseEntity<byte[]> getAttachment(@RequestParam Long id) {
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf")
				.header(HttpHeaders.CONTENT_TYPE, "application/pdf").body(mandateService.findById(id).getAttachment());
	}
}
