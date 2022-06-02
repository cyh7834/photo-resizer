let dropzone = new Dropzone('#demo-upload', {
    init: function () {
        this.on("success", function(file, response) {
            let fileName, uuid;
            const li = document.createElement("li");
            const span = document.createElement("span");
            const a = document.createElement("a");

            if (response.status === "OK") {
                fileName = response.data.fileName;
                uuid = response.data.uuid;
                a.setAttribute("href", '/download?uuid=' + uuid + '&fileName=' + fileName);
                a.innerText = " [download]";
            }
            else {
                fileName = file.upload.filename;
                a.innerText = " " + response.comment;
            }
            span.innerText = fileName;
            li.appendChild(span);
            li.appendChild(a);

            document.getElementById('download-ul').appendChild(li);
        })
    },
    previewTemplate: document.querySelector('#preview-template').innerHTML,
    url: "/resize",
    maxFiles: 10,
    parallelUploads: 3,
    thumbnailHeight: 120,
    thumbnailWidth: 120,
    maxFilesize: 500,
    filesizeBase: 1000,
    paramName: "file",
    acceptedFiles: ".jpg, .jpeg",
    addRemoveLinks: true
});