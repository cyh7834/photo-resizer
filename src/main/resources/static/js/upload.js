function resize() {
    let formElement = document.getElementById("file-form");
    let inputElement = document.getElementById("file-input");
    let files = inputElement.files;
    let fileLength = files.length;

    if (fileLength === 0)
        return;

    for (let i = 0; i < fileLength; i++) {
        if (!isValidExtension(files[i])) {
            alert("지원하지 않는 형식의 포맷입니다.");

            return;
        }
    }

    formElement.submit();
}

function isValidExtension(file) {
    return file.type === "image/jpeg";
}