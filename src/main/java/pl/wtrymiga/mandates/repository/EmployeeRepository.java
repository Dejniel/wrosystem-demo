package pl.wtrymiga.mandates.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.wtrymiga.mandates.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	List<Employee> findByCompanyId(Long companyId);

	Optional<Employee> findByPhoneNumber(String phoneNumber);
}