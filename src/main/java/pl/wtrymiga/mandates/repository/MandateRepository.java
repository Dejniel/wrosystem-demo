package pl.wtrymiga.mandates.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.wtrymiga.mandates.model.Mandate;

@Repository
public interface MandateRepository extends JpaRepository<Mandate, Long> {
	Optional<Mandate> findBySignature(String signature);
}