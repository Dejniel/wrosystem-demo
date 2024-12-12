function showAlert(message, type) {
    const alertBox = document.getElementById("alert-box");
    alertBox.classList.replace(alertBox.classList[2], `alert-${type}`);
    alertBox.textContent = message;
    alertBox.classList.remove("d-none");
    setTimeout(() => alertBox.classList.add("d-none"), 10000);
}