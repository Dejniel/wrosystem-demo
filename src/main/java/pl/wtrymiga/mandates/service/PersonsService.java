package pl.wtrymiga.mandates.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.wtrymiga.mandates.model.Company;
import pl.wtrymiga.mandates.model.Employee;
import pl.wtrymiga.mandates.repository.CompanyRepository;
import pl.wtrymiga.mandates.repository.EmployeeRepository;

@Service
public class PersonsService {
	private final CompanyRepository companiesRepository;
	private final EmployeeRepository employeeRepository;

	public PersonsService(CompanyRepository companiesRepository, EmployeeRepository employeeRepository) {
		this.companiesRepository = companiesRepository;
		this.employeeRepository = employeeRepository;
	}

	public List<Company> findAll() {
		return companiesRepository.findAll();
	}

	public List<Employee> findByCompanyId(Long companyId) {
		return employeeRepository.findByCompanyId(companyId);
	}

}
