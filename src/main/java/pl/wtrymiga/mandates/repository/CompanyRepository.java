package pl.wtrymiga.mandates.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.wtrymiga.mandates.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
	Optional<Company> findByNip(String nip);
}