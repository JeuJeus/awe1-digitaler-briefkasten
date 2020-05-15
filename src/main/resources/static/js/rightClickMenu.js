function addRightClickMenu(cssSelectorMenu, cssSelectorOnclick) {
    var menu = document.getElementById(cssSelectorMenu);
    var elements = document.querySelectorAll(cssSelectorOnclick);
    for (let elem of elements) {
        if (elem.addEventListener) {
            elem.addEventListener('contextmenu', function (e) {
                const posX = e.clientX;
                const posY = e.clientY;
                showMenu(posX, posY, menu, elem.getAttribute("data"));
                e.preventDefault();
            }, false);
            document.addEventListener('click', function (e) {
                menu.style.opacity = "0";
                setTimeout(function () {
                    menu.style.visibility = "hidden";
                }, 501);
            }, false);
        } else {
            elem.attachEvent('oncontextmenu', function (e) {
                const posX = e.clientX;
                const posY = e.clientY;
                showMenu(posX, posY, menu, elem.getAttribute("data"));
                e.preventDefault();
            });
            document.attachEvent('onclick', function (e) {
                menu.style.opacity = "0";
                setTimeout(function () {
                    menu.style.visibility = "hidden";
                }, 501);
            });
        }
    }
}

function showMenu(x, y, menu, data) {
    menu.style.top = y + "px";
    menu.style.left = x + "px";
    menu.style.visibility = "visible";
    menu.style.opacity = "1";
    if (data) menu.setAttribute("data", data);
}
