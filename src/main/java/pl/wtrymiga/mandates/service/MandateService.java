package pl.wtrymiga.mandates.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.wtrymiga.mandates.model.Employee;
import pl.wtrymiga.mandates.model.Mandate;
import pl.wtrymiga.mandates.repository.EmployeeRepository;
import pl.wtrymiga.mandates.repository.MandateRepository;

@Service
public class MandateService {
	private final MandateRepository mandateRepository;
	private final EmailService emailService;
	private final EmployeeRepository employeeRepository;

	public MandateService(MandateRepository mandateRepository, EmployeeRepository employeeRepository,
			EmailService emailService) {
		this.mandateRepository = mandateRepository;
		this.employeeRepository = employeeRepository;
		this.emailService = emailService;
	}

	public Mandate update(Mandate mandate) {
		System.out.println(mandate);
		if (mandate.getAttachment() != null && mandate.getAttachment().length <= 0)
			mandate.setAttachment(null);
		if (mandate.getEmployee().getCompany() != null && mandate.getEmployee().getCompany().getId() == null)
			mandate.getEmployee().setCompany(null);
		if (mandate.getEmployee().getCompany() != null)
			mandate.setEmployee(employeeRepository.findById(mandate.getEmployee().getId()).orElseThrow());
		else {
			// Znajdź istniejącego pracownika
			employeeRepository.findByPhoneNumber(mandate.getEmployee().getPhoneNumber()).ifPresent(existing -> {
				mandate.getEmployee().setId(existing.getId());
			});
			final Employee savedEmployee = employeeRepository.save(mandate.getEmployee());
			mandate.setEmployee(savedEmployee);
		}
		// Zapisz zmieniony mandat
		return mandateRepository.save(mandate);
	}



	public List<Mandate> findAll() {
		return mandateRepository.findAll();
	}

	public Mandate findById(Long id) {
		return mandateRepository.findById(id).orElseThrow();
	}

	public Mandate add(Mandate mandate) {
		mandateRepository.findBySignature(mandate.getSignature()).ifPresent(existing -> {
			throw new IllegalArgumentException("Mandat z wpisaną sygnaturą już istnieje w systemie.");
		});
		sendEmailNotification(mandate);
		return update(mandate);
	}

	public void deleteById(Long id) {
		mandateRepository.deleteById(id);
	}

	public void markPaid(Long id) {
		final Mandate mandate = findById(id);
		mandate.setPaid(true);
		mandateRepository.save(mandate);
	}

	private void sendEmailNotification(Mandate mandate) {
		final var subject = "Mandat nr. " + mandate.getSignature() + " prośba o opłacenie";
		final var body = String.format(
				"Dzień dobry, Pragniemy poinformować, że właśnie został dodany do naszej aplikacji mandat o numerze %s na kwotę %.2f %s. Prosimy o dokonanie wpłaty do dnia %s. Szczegółowe informacje dotyczące mandatu znajdziesz pod linkiem: [link]. Wiadomość wysłana automatycznie. Nie odpowiadaj na nią.",
				mandate.getSignature(), mandate.getAmount(), mandate.getCurrency(), mandate.getPaymentDeadline());

		emailService.sendEmail("j.kowalski@test.pl", subject, body);
	}
}
