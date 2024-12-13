package pl.wtrymiga.mandates.config;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

	public DataLoader(CompanyRepository companyRepository, EmployeeRepository employeeRepository,
			MandateRepository mandateRepository) {
		this.companyRepository = companyRepository;
		this.employeeRepository = employeeRepository;
		this.mandateRepository = mandateRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		loadCompanies();
		loadEmployees();
		loadUnassignedEmployees();
		loadMandates();
	}

	private void loadCompanies() {
		companyRepository.saveAll(List.of(new Company(Generator.nip(), "EcoPro"), new Company(Generator.nip(), "NetX"),
				new Company(Generator.nip(), "TechLab")));
	}

	private void loadEmployees() {
		for (final Company company : companyRepository.findAll())
			for (int i = 0; i < 10; ++i) {
				final var employee = new Employee(Generator.name(), Generator.surname(), Generator.phone());
				employee.setCompany(company);
				employee.setActive(ThreadLocalRandom.current().nextBoolean());
				employeeRepository.save(employee);
			}
	}

	private void loadUnassignedEmployees() {
		for (int i = 0; i < 5; ++i)
			employeeRepository.save(new Employee(Generator.name(), Generator.surname(), Generator.phone()));
	}

	private void loadMandates() {
		final var employees = employeeRepository.findAll();

		for (int i = 0; i < 15; ++i) {
			final var employee = employees.get(ThreadLocalRandom.current().nextInt(employees.size()));
			final var mandate = new Mandate(Generator.signature(), Generator.datePast(), Generator.violation(),
					Generator.money(), Generator.currency(), Generator.dateFuture(), employee);
			mandate.setPaid(ThreadLocalRandom.current().nextDouble() > .6);
			if (ThreadLocalRandom.current().nextDouble() > .7)
				mandate.setAttachment(Generator.pdf());
			if (mandate.getReason() == ViolationReason.OTHER)
				mandate.setCustomReason("To zła kobieta była");
			mandateRepository.save(mandate);
		}
	}

	private static class Generator {
		private final static String[] firstNames = { "Jan", "Anna", "Piotr", "Katarzyna", "Tomasz", "Monika", "Michał",
				"Ewa", "Paweł", "Agnieszka" };
		private final static String[] lastNames = { "Kowalski", "Nowak", "Wiśniewski", "Wójcik", "Kowalczyk",
				"Kamiński", "Lewandowski", "Zieliński", "Szymański", "Dąbrowski" };

		public static final BigDecimal money() {
			return new BigDecimal(ThreadLocalRandom.current().nextDouble() * 1000).setScale(2, RoundingMode.HALF_UP);
		}

		public static final Currency currency() {
			return Currency.values()[ThreadLocalRandom.current().nextInt(Currency.values().length)];
		}

		public static final ViolationReason violation() {
			return ViolationReason.values()[ThreadLocalRandom.current().nextInt(ViolationReason.values().length)];
		}

		public static final LocalDate datePast() {
			return LocalDate.now().minusDays(ThreadLocalRandom.current().nextInt(100));
		}

		public static final LocalDate dateFuture() {
			return LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(100));
		}

		public static final String signature() {
			return "MDT" + (ThreadLocalRandom.current().nextInt(900000) + 100000);
		}

		public static final String name() {
			return firstNames[ThreadLocalRandom.current().nextInt(firstNames.length)];
		}

		public static final String surname() {
			return lastNames[ThreadLocalRandom.current().nextInt(lastNames.length)];
		}

		public static final String phone() {
			return "" + (ThreadLocalRandom.current().nextInt(900000000) + 100000000);
		}

		public static final String nip() {
			return "" + (ThreadLocalRandom.current().nextLong(9000000000L) + 1000000000);
		}

		public static final byte[] pdf() {
			final String[] texts = { "Kocha", "Lubi", "Szanuje", "Nie Kocha", "Nie lubi", "Nie Szanuje" };
			final String text = texts[ThreadLocalRandom.current().nextInt(texts.length)];

			final String pdf = "%PDF-1.4\n" + "1 0 obj<</Type/Catalog/Pages 2 0 R>>endobj\n"
					+ "2 0 obj<</Type/Pages/Count 1/Kids[3 0 R]>>endobj\n"
					+ "3 0 obj<</Type/Page/Parent 2 0 R/MediaBox[0 0 300 200]/Contents 4 0 R/Resources<</Font<</F1 5 0 R>>>>>>endobj\n"
					+ "4 0 obj<</Length " + (text.length() + 50) + ">>stream\n" + "BT /F1 24 Tf 100 150 Td (" + text
					+ ") Tj ET\n" + "endstream\nendobj\n"
					+ "5 0 obj<</Type/Font/Subtype/Type1/BaseFont/Helvetica>>endobj\n" + "xref\n0 6\n"
					+ "0000000000 65535 f \n" + "0000000010 00000 n \n" + "0000000058 00000 n \n"
					+ "0000000111 00000 n \n" + "0000000219 00000 n \n" + "0000000320 00000 n \n"
					+ "trailer<</Size 6/Root 1 0 R>>\n" + "startxref\n373\n" + "%%EOF";
			return pdf.getBytes(StandardCharsets.UTF_8);
		}
	}
}
