document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("uploadForm");
    const fileInput = document.getElementById("fileInput");
    const statusDiv = document.getElementById("status");
    const downloadDiv = document.getElementById("downloadLink");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const file = fileInput.files[0];
        if (!file) {
            statusDiv.textContent = "Выберите файл!";
            return;
        }

        statusDiv.textContent = "Загрузка...";
        downloadDiv.textContent = "";

        try {
            const formData = new FormData();
            formData.append("file", file);

            const response = await fetch("/upload", {
                method: "POST",
                body: formData,
                headers: {
                    "Authorization": "Bearer SECRET_TOKEN"
                }
            });

            if (!response.ok) {
                statusDiv.textContent = `Ошибка: ${response.status}`;
                return;
            }

            const text = await response.text();
            statusDiv.textContent = "Файл успешно загружен!";
            downloadDiv.innerHTML = `<a href="${text.split('http://localhost:8000')[1]}" target="_blank">Скачать файл</a>`;
        } catch (err) {
            statusDiv.textContent = "Ошибка при загрузке файла";
            console.error(err);
        }
    });
});
