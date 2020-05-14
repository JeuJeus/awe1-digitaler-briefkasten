function prefillSelect2(cssSelector) {
    let selects = document.querySelectorAll(cssSelector);
    selects.forEach((select) => {
        let options = select.options;
        for (let option of options) {
            option.selected = true;
        }
        select.dispatchEvent(new Event('change'));
    })
}

function triggerSelect2(cssSelector) {
    let selects = document.querySelectorAll(cssSelector);
    selects.forEach((select) => {
        select.dispatchEvent(new Event('change'));
    })
}
