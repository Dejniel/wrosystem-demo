package pl.wtrymiga.mandates.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "mandates")
@Data
@NoArgsConstructor
@RequiredArgsConstructor

public class Mandate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NonNull
	@Column(nullable = false, unique = true)
	@Size(min = 5, message = "Sygnatura musi mieć od 5 znaków")
	private String signature;

	@NonNull
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate violationDate;

	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ViolationReason reason;

	@Column(length = 200)
	@Size(max = 200, message = "Wykroczenie do 200 znaków")
	private String customReason;

	@NonNull
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Currency currency;

	@NonNull
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal administrativeFee = BigDecimal.valueOf(100);

	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Currency administrativeFeeCurrency = Currency.PLN;

	@Lob
	private byte[] attachment;
	@NonNull
	@Column(nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate paymentDeadline;

	@NonNull
	@Column(nullable = false)
	private Boolean paid = false;

	@NonNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Employee employee;

}
