function getStatusIcon(status) {
    let svgs = [];
    svgs['ACCEPTED'] = "<svg class=\"bi bi-check-circle\" width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\" fill=\"currentColor\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
        "  <path fill-rule=\"evenodd\" d=\"M15.354 2.646a.5.5 0 010 .708l-7 7a.5.5 0 01-.708 0l-3-3a.5.5 0 11.708-.708L8 9.293l6.646-6.647a.5.5 0 01.708 0z\" clip-rule=\"evenodd\"/>\n" +
        "  <path fill-rule=\"evenodd\" d=\"M8 2.5A5.5 5.5 0 1013.5 8a.5.5 0 011 0 6.5 6.5 0 11-3.25-5.63.5.5 0 11-.5.865A5.472 5.472 0 008 2.5z\" clip-rule=\"evenodd\"/>\n" +
        "</svg>";
    svgs['PENDING'] = "<svg class=\"bi bi-clock\" width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\" fill=\"currentColor\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
        "  <path fill-rule=\"evenodd\" d=\"M8 15A7 7 0 108 1a7 7 0 000 14zm8-7A8 8 0 110 8a8 8 0 0116 0z\" clip-rule=\"evenodd\"/>\n" +
        "  <path fill-rule=\"evenodd\" d=\"M7.5 3a.5.5 0 01.5.5v5.21l3.248 1.856a.5.5 0 01-.496.868l-3.5-2A.5.5 0 017 9V3.5a.5.5 0 01.5-.5z\" clip-rule=\"evenodd\"/>\n" +
        "</svg>";
    svgs['IDEA_STORAGE'] = "<svg class=\"bi bi-layers\" width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\" fill=\"currentColor\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
        "  <path fill-rule=\"evenodd\" d=\"M3.188 8L.264 9.559a.5.5 0 000 .882l7.5 4a.5.5 0 00.47 0l7.5-4a.5.5 0 000-.882L12.813 8l-1.063.567L14.438 10 8 13.433 1.562 10 4.25 8.567 3.187 8z\" clip-rule=\"evenodd\"/>\n" +
        "  <path fill-rule=\"evenodd\" d=\"M7.765 1.559a.5.5 0 01.47 0l7.5 4a.5.5 0 010 .882l-7.5 4a.5.5 0 01-.47 0l-7.5-4a.5.5 0 010-.882l7.5-4zM1.563 6L8 9.433 14.438 6 8 2.567 1.562 6z\" clip-rule=\"evenodd\"/>\n" +
        "</svg>";
    svgs['DECLINED'] = "<svg class=\"bi bi-x-circle\" width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\" fill=\"currentColor\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
        "  <path fill-rule=\"evenodd\" d=\"M8 15A7 7 0 108 1a7 7 0 000 14zm0 1A8 8 0 108 0a8 8 0 000 16z\" clip-rule=\"evenodd\"/>\n" +
        "  <path fill-rule=\"evenodd\" d=\"M11.854 4.146a.5.5 0 010 .708l-7 7a.5.5 0 01-.708-.708l7-7a.5.5 0 01.708 0z\" clip-rule=\"evenodd\"/>\n" +
        "  <path fill-rule=\"evenodd\" d=\"M4.146 4.146a.5.5 0 000 .708l7 7a.5.5 0 00.708-.708l-7-7a.5.5 0 00-.708 0z\" clip-rule=\"evenodd\"/>\n" +
        "</svg>";
    svgs['NOT_SUBMITTED'] = "<svg class=\"bi bi-question-circle\" width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\" fill=\"currentColor\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
        "  <path fill-rule=\"evenodd\" d=\"M8 15A7 7 0 108 1a7 7 0 000 14zm0 1A8 8 0 108 0a8 8 0 000 16z\" clip-rule=\"evenodd\"/>\n" +
        "  <path d=\"M5.25 6.033h1.32c0-.781.458-1.384 1.36-1.384.685 0 1.313.343 1.313 1.168 0 .635-.374.927-.965 1.371-.673.489-1.206 1.06-1.168 1.987l.007.463h1.307v-.355c0-.718.273-.927 1.01-1.486.609-.463 1.244-.977 1.244-2.056 0-1.511-1.276-2.241-2.673-2.241-1.326 0-2.786.647-2.754 2.533zm1.562 5.516c0 .533.425.927 1.01.927.609 0 1.028-.394 1.028-.927 0-.552-.42-.94-1.029-.94-.584 0-1.009.388-1.009.94z\"/>\n" +
        "</svg>";
    return svgs[status];
}

function getStatusIconColor(status) {
    let colors = [];
    colors['ACCEPTED'] = "green";
    colors['PENDING'] = "#CBB942";
    colors['IDEA_STORAGE'] = "brown";
    colors['DECLINED'] = "red";
    colors['NOT_SUBMITTED'] = "blue";
    return colors[status];
}

function insertSVG(element, status) {
    element.innerHTML = getStatusIcon(element.getAttribute('data-status'));
}

function setSVGColor(element, status) {
    statusIcon.querySelector("svg").style.color = getStatusIconColor(element.getAttribute('data-status'));
}

window.onload = function () {
    let statusIcons = document.querySelectorAll(".statusIcon");
    for (let i = 0; i < statusIcons.length; i++) {
        statusIcon = statusIcons[i];
        dataStatus = statusIcon.getAttribute('data-status');
        insertSVG(statusIcon, dataStatus);
        setSVGColor(statusIcon, dataStatus);
    }
};