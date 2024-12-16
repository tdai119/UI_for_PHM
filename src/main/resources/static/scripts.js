document.addEventListener("DOMContentLoaded", () => {
    // Example: Add confirmation before submitting the form
    const uploadForm = document.querySelector("form");

    if (uploadForm) {
        uploadForm.addEventListener("submit", (e) => {
            const confirmUpload = confirm("Are you sure you want to upload this file?");
            if (!confirmUpload) {
                e.preventDefault();
            }
        });
    }

    // Example: Show an alert message after a result page loads
    const messageElement = document.querySelector(".message");
    if (messageElement) {
        alert(messageElement.textContent);
    }
});