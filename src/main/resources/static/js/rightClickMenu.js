function addRightClickMenu(cssSelectorMenu, cssSelectorOnclick, trigger) {
    let menu = document.querySelector(cssSelectorMenu);
    let elements = document.querySelectorAll(cssSelectorOnclick);
    for (let elem of elements) {
        if (elem.addEventListener) {
            elem.addEventListener(trigger, function (e) {
                menuEvent(e, menu, elem);
            }, false);
        } else {
            elem.attachEvent(trigger, function (e) {
                menuEvent(e, menu, elem);
            });
        }
    }
}

function addMenuClosingEvent(triggerElement, triggerEvent) {
    if (triggerElement.addEventListener) {
        triggerElement.addEventListener(triggerEvent, function (e) {
            addBodyOnclick(e, menu);
        });
    } else {
        triggerElement.attachEvent(triggerEvent, function (e) {
            addBodyOnclick(e, menu);
        });
    }
}

function menuEvent(e, menu, elem) {
    const posX = e.clientX;
    const posY = e.clientY;
    showMenu(posX, posY, menu, elem.getAttribute("data"), elem);
    e.preventDefault();
}

function addBodyOnclick(e, menu) {
    if (e.target.classList.contains('no-body-onclick')) return;
    menu.style.opacity = "0";
    setTimeout(function () {
        menu.style.visibility = "hidden";
    }, 251);
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
    let retval = null;
    while (el) {
        if (el.className.indexOf(selectorClass) > -1) {
            retval = el;
            break
        }
        el = el.parentElement;
    }
    return retval;
}
