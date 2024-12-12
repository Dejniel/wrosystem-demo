document.addEventListener("DOMContentLoaded", function(){
	// Obługa przycisku "Opłać"
	document.querySelectorAll(".buttonPay").forEach(btn =>
		btn.onclick = async ({ target }) => {
			try {
				const res = await fetch('/api/set/mandate/mark', {
					method: 'POST',
					body: new URLSearchParams({ id: target.dataset.id })
				});
				const { message, type } = await res.json();
				showAlert(message, type);
				if (res.ok) {
					target.innerText = '✓';
					target.classList.add("disabled","btn-success");
					target.classList.remove("btn-danger");
					target.closest("tr").querySelector(".buttonDelete").classList.add("disabled");
					target.closest("tr").querySelector(".buttonEdit").classList.add("disabled");
				}
			} catch { showAlert("Błąd sieci. Spróbuj ponownie.", "danger"); }
		}
	);
	// Obsługa przycisku "Usuń"
	document.querySelectorAll(".buttonDelete").forEach(btn =>
		btn.onclick = async ({ target }) => {
			try {
				const res = await fetch('/api/delete/mandate', {
					method: 'DELETE',
					body: new URLSearchParams({ id: target.dataset.id })
				});
				const { message, type } = await res.json();
				showAlert(message, type);
				if (res.ok) table.row(target.closest("tr")).remove().draw();
			} catch { showAlert("Błąd sieci. Spróbuj ponownie.", "danger"); }
		}
	);
	// Sortowanie tabeli
	const table = $('#mandatesTable').DataTable({
		language: { 
			url: "https://cdn.datatables.net/plug-ins/1.13.5/i18n/pl.json" ,
			search: "", 
			lengthMenu: "_MENU_",
			paginate: {
				  previous: '<i class="fas fa-chevron-left"></i>',
				  next: '<i class="fas fa-chevron-right"></i>'
			}},
		order: new Array( new Array(11, "asc") ),
		columnDefs: [{ orderable: false, targets: [12] }],
		classes: {
			sWrapper: "table-responsive",
			sFilter: "float-end m-4 mx-2 w-50",
			sLength: "float-start m-4 mx-2",
			sProcessing: "alert alert-info",
			sInfo: "d-none",
			sPageButton: "btn btn-primary btn-sm mx-1" 
		},
		drawCallback: function () {
			$('select[name="mandatesTable_length"]').addClass('form-select form-select-sm');
			$('#mandatesTable_filter>label').addClass('w-100');
			$('#mandatesTable_filter input').addClass('form-control form-control-sm w-100 d-block');
			$('#mandatesTable_paginate').addClass('text-center mt-3 ms-auto');
		}
	});
	$('#mandatesTable thead tr:eq(1) th').each(function (i) {
		$('input', this).on('keyup change', function () {
			table.column(i).search(this.value).draw();
		});
	});
	
});