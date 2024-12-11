package pl.wtrymiga.mandates.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.wtrymiga.mandates.controller.Message.MessageType;
import pl.wtrymiga.mandates.model.Company;
import pl.wtrymiga.mandates.model.Employee;
import pl.wtrymiga.mandates.model.Mandate;
import pl.wtrymiga.mandates.service.MandateService;
import pl.wtrymiga.mandates.service.PersonsService;

@Controller
@RequestMapping
public class MandateController {

	private final MandateService mandateService;
	private final PersonsService personsService;

	public MandateController(MandateService mandateService, PersonsService companiesService) {
		this.mandateService = mandateService;
		this.personsService = companiesService;
	}

	// Lista mandatów
	@GetMapping
	public String listMandates(Model model) {
		model.addAttribute("mandates", mandateService.findAll());
		return "mandates/list";
	}

	// Formularz dodawania
	@GetMapping("/new")
	public String newMandate(Model model) {
		final var mandate = new Mandate();
		mandate.setEmployee(new Employee());
		model.addAttribute("mandate", mandate);
		return "mandates/form";
	}

	// Formularz edycji
	@GetMapping("/edit/{id}")
	public String editMandate(@PathVariable Long id, Model model) {
		model.addAttribute("forceUpdate", true);
		model.addAttribute("mandate", mandateService.findById(id));
		return "mandates/form";
	}

	// Ajaxy

	@PostMapping("/api/set/mandate")
	@ResponseBody
	public Message saveMandate(@ModelAttribute Mandate mandate,
			@RequestParam(required = false, defaultValue = "false") boolean force) {
		if (mandateService.findById(mandate.getId()).getPaid())
			return new Message("Nie można edytować opłaconego mandatu!", MessageType.warning);
		if (force)
			mandateService.update(mandate);
		else
			mandateService.add(mandate);
		return new Message("Mandat zapisany pomyślnie!", MessageType.success);
	}

	@PostMapping("/api/set/mandate/mark")
	@ResponseBody
	public Message markPaid(@RequestParam Long id) {
		mandateService.markPaid(id);
		return new Message("Oznaczono jako opłacony!", MessageType.success);
	}

	@DeleteMapping("/api/delete/mandate")
	@ResponseBody
	public Message deleteMandate(@RequestParam Long id) {
		if (mandateService.findById(id).getPaid())
			return new Message("Nie można skasować opłaconego mandatu!", MessageType.warning);
		mandateService.deleteById(id);
		return new Message("Skasowano wpis!", MessageType.success);
	}

	@GetMapping("/api/get/companies")
	@ResponseBody
	public List<Company> getCompanies() {
		return personsService.findAll();

	}

	@GetMapping("/api/get/employees")
	@ResponseBody
	public List<Employee> getEmployeesByCompany(@RequestParam Long companyId) {
		return personsService.findByCompanyId(companyId);
	}

	@GetMapping("/attachment")
	@ResponseBody
	public ResponseEntity<byte[]> getAttachment(@RequestParam Long id) {
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attachment.pdf")
				.header(HttpHeaders.CONTENT_TYPE, "application/pdf").body(mandateService.findById(id).getAttachment());
	}
}
