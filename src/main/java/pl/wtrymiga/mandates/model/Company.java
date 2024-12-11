package pl.wtrymiga.mandates.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NonNull
	@Column(nullable = false, unique = true)

	@Size(min = 10, max = 10, message = "Podaj poprawny NIP")
	private String nip;
	@NonNull
	@Column(nullable = false, unique = true)

	@Size(min = 2, message = "Podaj nazwę firmy")
	private String name;

}
