function addRightClickMenu(cssSelectorMenu, cssSelectorOnclick) {
    let menu = document.querySelector(cssSelectorMenu);
    let elements = document.querySelectorAll(cssSelectorOnclick);
    for (let elem of elements) {
        if (elem.addEventListener) {
            elem.addEventListener('contextmenu', function (e) {
                const posX = e.clientX;
                const posY = e.clientY;
                showMenu(posX, posY, menu, elem.getAttribute("data"), elem);
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
                showMenu(posX, posY, menu, elem.getAttribute("data"), elem);
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

function showMenu(x, y, menu, data, caller) {
    menu.style.top = y + "px";
    menu.style.left = x + "px";
    menu.style.visibility = "visible";
    menu.style.opacity = "1";
    if (data) menu.setAttribute("data", data);
    let menuItems = menu.querySelectorAll('.right-click-menu-item');
    let parentTable = closest(caller, "right-click-table");
    for (let menuItem of menuItems) {
        let visibility = JSON.parse(menuItem.getAttribute("data-visibility"));
        if (visibility && parentTable && visibility.includes(parentTable.id)) {
            menuItem.style.display = ""; // this resets to default from css
        } else {
            menuItem.style.display = "none";
        }
    }
    $(menu).resize();
}

function closest(el, selectorClass) {
    var retval = null;
    while (el) {
        if (el.className.indexOf(selectorClass) > -1) {
            retval = el;
            break
        }
        el = el.parentElement;
    }
    return retval;
}
