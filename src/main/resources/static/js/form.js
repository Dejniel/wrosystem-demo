document.addEventListener("DOMContentLoaded", function(){
	// Walidacja danych
	document.getElementById("mandateForm").onsubmit = async (e) => {
		e.preventDefault();
		if (!e.target.checkValidity()) {
			e.stopPropagation();
			e.target.classList.add("was-validated"); 
			return;
		}
		const formData = new FormData(e.target);
		try {
			const res = await fetch(e.target.action, {
				method: "POST",
				body: formData
			});
			const { message, type } = await res.json();
			showAlert(message, type);
			if (res.ok && !formData.get("force")) {
				e.target.reset(); 
				e.target.classList.remove("was-validated"); 
			}
		} catch {
			showAlert("Błąd sieci. Spróbuj ponownie.", "danger");
		}
	};


	// Dynamiczne wartości
	const fetchOptions = async (url, select, defaultText, mapFn) => {
		const data = await fetch(url).then(res => res.json());
		select.innerHTML = `<option value="">${defaultText}</option>` + data.map(mapFn).join('');
	};
	const employeeTypeSelect = document.getElementById("employeeType");
	const companySelect = document.getElementById("company");
	employeeTypeSelect.onchange =  async() => {
		document.getElementById("physical-fields").classList.toggle("d-none", employeeTypeSelect.value !== "PHYSICAL");
		document.getElementById("office-fields").classList.toggle("d-none", employeeTypeSelect.value !== "OFFICE");
		document.querySelectorAll("#physical-fields input").forEach(input => {
			input.required = employeeTypeSelect.value === "PHYSICAL";  
		});
		document.querySelectorAll("#office-fields input").forEach(input => {
			input.required = employeeTypeSelect.value === "OFFICE";  
		});
		if (employeeTypeSelect.value === "PHYSICAL")
			await fetchOptions('/api/get/companies', companySelect, "Wybierz firmę...", c => `<option value="${c.id}">${c.name}</option>`);
		if(employeeTypeSelect.value === "OFFICE")
			companySelect.value = '';
	};	
	const employeeSelect = document.getElementById("employee");	
	companySelect.onchange = () => companySelect.value && fetchOptions(
		`/api/get/employees?companyId=${companySelect.value}`, 
		employeeSelect, 
		"Wybierz pracownika...", 
		e => `<option value="${e.id}">${e.firstName} ${e.lastName}</option>`
	);
	(async () => {
		await employeeTypeSelect.onchange();
		if(companySelect.dataset.selected){
			companySelect.value = companySelect.dataset.selected;
			await companySelect.onchange();
			employeeSelect.value = employeeSelect.dataset.selected || "";
		}
	})();
	
	const reasonSelect = document.getElementById("reason");
	reasonSelect.onchange = () => document.getElementById("other-reason").classList.toggle("d-none", reasonSelect.value !== "OTHER");
	reasonSelect.onchange();
	
	
	// Obsługa załączników
	const attachmentInput = document.getElementById("attachment");
	const fileMenuContainer = document.getElementById('fileMenu');
	const fileToUrl = () => URL.createObjectURL(new Blob([new Uint8Array(attachmentInput.value.split(',').map(Number).map(n => (n + 256) % 256))], { type: 'application/pdf' }));
	
	
	document.getElementById("fileUpload").addEventListener("change", async e => {
		if (e.target.files[0]) {
			attachmentInput.value = Array.from(new Uint8Array(await e.target.files[0].arrayBuffer())).map(b => b > 127 ? b - 256 : b).join(',');
			fileMenuContainer.classList.remove('d-none');
		}
	});
	document.getElementById("filePreview").addEventListener("click", async () => {
		window.open(fileToUrl(), '_blank');
		console.log(fileToUrl());
	});
	document.getElementById("fileDownload").addEventListener("click", async () => {
		const link = document.createElement('a');
		link.href = fileToUrl();
		link.download = document.getElementById("signature").value + "_attachment.pdf";
		link.click();
		URL.revokeObjectURL(link);
	});
	document.getElementById("fileDelete").addEventListener("click", async () => { 
		attachmentInput.value = ''; 
		fileMenuContainer.classList.add('d-none');
	});
	
	if(attachmentInput.value)
		fileMenuContainer.classList.remove('d-none');
});