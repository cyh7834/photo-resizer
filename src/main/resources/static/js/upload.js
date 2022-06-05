let dropzone = new Dropzone('#demo-upload', {
    init: function () {
        this.on("success", function(file, response) {
            let fileName;
            const li = document.createElement("li");
            li.className = "media mb-3";

            const mediaDiv = document.createElement("div");
            mediaDiv.className = "media-body";

            const container = document.createElement("div");
            container.className = "container";

            const row = document.createElement("div");
            row.className = "row";

            const leftDiv = document.createElement("div");
            leftDiv.className = "col-8";

            const fileNameH5 = document.createElement("h5");
            fileNameH5.className = "font-16 mb-1";
            fileNameH5.innerText = response.data

            const fileInfo = document.createElement("p");

            const rightDiv = document.createElement("div");
            rightDiv.className = "col-2";


            if (response.status === "OK") {
                fileName = response.data.fileName;
                fileInfo.innerText = response.data.fileSize;

                const downloadButton = document.createElement("button");
                downloadButton.className = "btn btn-danger";
                downloadButton.innerText = "Download";

                const a = document.createElement("a");
                a.setAttribute("href", '/download?uuid=' + response.data.uuid
                    + '&fileName=' + response.data.fileName);
                a.appendChild(downloadButton);

                rightDiv.appendChild(a);
            }
            else {
                fileName = file.upload.filename;
                fileInfo.innerText = " " + response.comment;
            }

            fileNameH5.innerText = fileName;
            leftDiv.appendChild(fileNameH5);
            leftDiv.appendChild(fileInfo);

            row.appendChild(leftDiv);
            row.appendChild(rightDiv);

            container.appendChild(row);
            mediaDiv.appendChild(container);

            li.appendChild(mediaDiv);

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