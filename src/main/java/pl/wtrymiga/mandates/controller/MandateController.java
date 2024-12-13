package pl.wtrymiga.mandates.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import pl.wtrymiga.mandates.model.Employee;
import pl.wtrymiga.mandates.model.Mandate;
import pl.wtrymiga.mandates.service.MandateService;

@Controller
public class MandateController {
	private final MandateService mandateService;

	public MandateController(MandateService mandateService) {
		this.mandateService = mandateService;
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

}
