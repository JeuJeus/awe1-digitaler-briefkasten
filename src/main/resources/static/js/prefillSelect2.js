function prefillSelect2(cssSelector) {
    let selects = document.querySelectorAll(cssSelector);
    selects.forEach((select) => {
        let options = select.options;
        for (option of options) {
            option.selected = true;
        }
        select.dispatchEvent(new Event('change'));
    })
}
