package pl.wtrymiga.mandates.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NonNull
	@Column(nullable = false)
	@Size(min = 2, message = "Imie nie może być puste")
	private String firstName;
	@NonNull
	@Column(nullable = false)
	@Size(min = 2, message = "Nazwisko nie może być puste")
	private String lastName;
	@NonNull
	@Column(nullable = false)
	@Size(min = 2, message = "Wypełnij pole telefon")
	private String phoneNumber;

	@ManyToOne
	@JoinColumn
	private Company company;

	@Column
	private Boolean active;

}
