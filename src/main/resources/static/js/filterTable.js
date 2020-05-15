function enableFilter(cssSelectorInput, cssSelectorToBeFiltered) {
    $(cssSelectorInput).on("keyup", function () {
        var value = $(this).val().toLowerCase();
        console.log(value)
        $(cssSelectorToBeFiltered).filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
}

