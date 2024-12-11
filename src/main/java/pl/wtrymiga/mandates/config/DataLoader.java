package pl.wtrymiga.mandates.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import pl.wtrymiga.mandates.model.Company;
import pl.wtrymiga.mandates.model.Currency;
import pl.wtrymiga.mandates.model.Employee;
import pl.wtrymiga.mandates.model.Mandate;
import pl.wtrymiga.mandates.model.ViolationReason;
import pl.wtrymiga.mandates.repository.CompanyRepository;
import pl.wtrymiga.mandates.repository.EmployeeRepository;
import pl.wtrymiga.mandates.repository.MandateRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

	private final CompanyRepository companyRepository;
	private final EmployeeRepository employeeRepository;
	private final MandateRepository mandateRepository;

	private final String[] firstNames = { "Jan", "Anna", "Piotr", "Katarzyna", "Tomasz", "Monika", "Michał", "Ewa",
			"Paweł", "Agnieszka" };
	private final String[] lastNames = { "Kowalski", "Nowak", "Wiśniewski", "Wójcik", "Kowalczyk", "Kamiński",
			"Lewandowski", "Zieliński", "Szymański", "Dąbrowski" };

	public DataLoader(CompanyRepository companyRepository, EmployeeRepository employeeRepository,
			MandateRepository mandateRepository) {
		this.companyRepository = companyRepository;
		this.employeeRepository = employeeRepository;
		this.mandateRepository = mandateRepository;
	}

	private void loadCompanies() {
		companyRepository.saveAll(List.of(new Company("1234567890", "EcoPro"), new Company("9876543210", "NetX"),
				new Company("5678901234", "TechLab")));
	}

	private void loadEmployees() {
		final var companies = companyRepository.findAll();
		final var random = new Random();

		for (final Company company : companies)
			for (var i = 0; i < 10; i++) {
				final var employee = new Employee(firstNames[random.nextInt(firstNames.length)],
						lastNames[random.nextInt(lastNames.length)], "555-000-" + (random.nextInt(900) + 100));
				employee.setCompany(company);
				employee.setActive(random.nextBoolean());
				employeeRepository.save(employee);
			}
	}

	private void loadUnassignedEmployees() {
		final var random = new Random();
		for (var i = 0; i < 5; i++)
			employeeRepository.save(new Employee(firstNames[random.nextInt(firstNames.length)],
					lastNames[random.nextInt(lastNames.length)], "555-999-" + (random.nextInt(900) + 100)));
	}

	private void loadMandates() {
		final var employees = employeeRepository.findAll();
		final var random = new Random();

		for (var i = 0; i < 15; i++) {
			final var employee = employees.get(random.nextInt(employees.size()));

			final var mandate = new Mandate("MDT" + i + random.nextInt(1000),
					LocalDate.now().minusDays(random.nextInt(100)),
					ViolationReason.values()[random.nextInt(ViolationReason.values().length)],
					BigDecimal.valueOf(random.nextInt(1000) + 100), random.nextBoolean() ? Currency.PLN : Currency.EUR,
					LocalDate.now().plusDays(random.nextInt(60)), employee);
			if (mandate.getReason() == ViolationReason.OTHER)
				mandate.setCustomReason("To zła kobieta była");
			mandateRepository.save(mandate);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		loadCompanies();
		loadEmployees();
		loadUnassignedEmployees();
		loadMandates();
	}
}
