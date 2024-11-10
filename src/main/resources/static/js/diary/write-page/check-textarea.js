const textareas = document.querySelectorAll("textarea");
const textareaValues = Array.from(textareas).reduce((object, textarea) => {
    object[textarea] = textarea.value;
    return object
})

addEventTextareas();

function addEventTextareas() {
    Array.from(textareas).forEach(textarea => {
        textarea.addEventListener("input", checkHeight);
        textarea.addEventListener("click", closeModal);
    });
}

function checkHeight(event) {
    const textarea = event.target;

    if (textarea.scrollHeight !== textarea.offsetHeight) {
        textarea.value = textareaValues[textarea];
    }

    textareaValues[textarea] = textarea.value;
}
