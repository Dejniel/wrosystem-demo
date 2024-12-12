document.addEventListener("DOMContentLoaded", function(){
	
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

	const employeeType = document.getElementById("employeeType");
	const companySelect = document.getElementById("company");
	const employeeSelect = document.getElementById("employee");
	const reasonSelect = document.getElementById("reason");
	
	reasonSelect.onchange = () => document.getElementById("other-reason").classList.toggle("d-none", reasonSelect.value !== "OTHER");
	reasonSelect.onchange();
	
	const fetchOptions = async (url, select, defaultText, mapFn) => {
		const data = await fetch(url).then(res => res.json());
		select.innerHTML = `<option value="">${defaultText}</option>` + data.map(mapFn).join('');
	};

	employeeType.onchange =  async() => {
		document.getElementById("physical-fields").classList.toggle("d-none", employeeType.value !== "PHYSICAL");
		document.getElementById("office-fields").classList.toggle("d-none", employeeType.value !== "OFFICE");
		document.querySelectorAll("#physical-fields input").forEach(input => {
			input.required = employeeType.value === "PHYSICAL";  
		});
		document.querySelectorAll("#office-fields input").forEach(input => {
			input.required = employeeType.value === "OFFICE";  
		});
		if (employeeType.value === "PHYSICAL")
			await fetchOptions('/api/get/companies', companySelect, "Wybierz firmę...", c => `<option value="${c.id}">${c.name}</option>`);
		if(employeeType.value === "OFFICE")
			companySelect.value = '';
	};		
	companySelect.onchange = () => fetchOptions(
		`/api/get/employees?companyId=${companySelect.value}`, 
		employeeSelect, 
		"Wybierz pracownika...", 
		e => `<option value="${e.id}">${e.firstName} ${e.lastName}</option>`
	);


	(async () => {
		await employeeType.onchange();
		if(companySelect.dataset.selected){
			companySelect.value = companySelect.dataset.selected;
			await companySelect.onchange();
			employeeSelect.value = employeeSelect.dataset.selected || "";
		}
	})();
	
	

	const fileToUrl = () => URL.createObjectURL(new Blob([new Uint8Array(document.getElementById('attachment').value.split(',').map(Number).map(n => (n + 256) % 256))], { type: 'application/pdf' }));
	document.getElementById("fileUpload").addEventListener("change", async e => {
		if (e.target.files[0]) {
			document.getElementById("attachment").value = Array.from(new Uint8Array(await e.target.files[0].arrayBuffer())).map(b => b > 127 ? b - 256 : b).join(',');
			document.getElementById('fileMenu').classList.remove('d-none');
		}
	});
	const previewFile = () => {
		window.open(fileToUrl(), '_blank');
		console.log(fileToUrl());
	};
	const downloadFile = () => {
		const link = document.createElement('a');
		link.href = fileToUrl();
		link.download = document.getElementById("signature").value + "_attachment.pdf";
		link.click();
		URL.revokeObjectURL(link);
	};
	const removeFile = () => { 
		document.getElementById('attachment').value = ''; 
		document.getElementById('fileMenu').classList.add('d-none');
	};
	if(document.getElementById('attachment').value)
		document.getElementById('fileMenu').classList.remove('d-none');
});