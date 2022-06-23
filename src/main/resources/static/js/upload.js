let dropzone = new Dropzone('#demo-upload', {
    init: function () {
        function createListElement(file) {
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
            fileNameH5.innerText = file.name;

            const fileInfo = document.createElement("p");

            const rightDiv = document.createElement("div");
            rightDiv.className = "col-2";

            let downloadUl = document.getElementById('download-ul');

            return {li, mediaDiv, container, row, leftDiv, fileNameH5, fileInfo, rightDiv, downloadUl};
        }

        function appendListElement(leftDiv, fileNameH5, fileInfo, row, rightDiv, container, mediaDiv, li, downloadUl) {
            leftDiv.appendChild(fileNameH5);
            leftDiv.appendChild(fileInfo);

            row.appendChild(leftDiv);
            row.appendChild(rightDiv);

            container.appendChild(row);
            mediaDiv.appendChild(container);

            li.appendChild(mediaDiv);

            downloadUl.appendChild(li);
        }

        this.on("success", function(file, response) {
            let {li, mediaDiv, container, row, leftDiv, fileNameH5, fileInfo, rightDiv, downloadUl} = createListElement(file);

            fileInfo.innerText = response.data.fileSize;

            const downloadButton = document.createElement("button");
            downloadButton.className = "btn btn-danger";
            downloadButton.innerText = "Download";

            const a = document.createElement("a");
            a.setAttribute("href", '/download?uuid=' + response.data.uuid + '&fileName=' + file.name);
            a.appendChild(downloadButton);

            rightDiv.appendChild(a);

            appendListElement(leftDiv, fileNameH5, fileInfo, row, rightDiv, container, mediaDiv, li, downloadUl);
        });

        this.on('error', function(file, response) {
            let {li, mediaDiv, container, row, leftDiv, fileNameH5, fileInfo, rightDiv, downloadUl} = createListElement(file);

            fileInfo.innerText = response.comment !== undefined ? " " + response.comment : response;

            appendListElement(leftDiv, fileNameH5, fileInfo, row, rightDiv, container, mediaDiv, li, downloadUl);
        });

        this.on("complete", function(file) {
            this.removeFile(file);
        });
    },
    previewTemplate: document.querySelector('#preview-template').innerHTML,
    url: "/resize",
    maxFiles: 10,
    parallelUploads: 3,
    thumbnailHeight: 120,
    thumbnailWidth: 120,
    maxFilesize: 20,
    maxThumbnailFilesize: 20,
    filesizeBase: 1000,
    paramName: "file",
    acceptedFiles: ".jpg, .jpeg",
    addRemoveLinks: true
});