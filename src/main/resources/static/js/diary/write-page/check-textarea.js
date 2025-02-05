const textareas = Array.from(document.querySelectorAll(".diary-content"));
const textareaValues = textareas.map(textarea => textarea.value);
var isCalled = false;
addEventTextareas();

function addEventTextareas() {
    Array.from(textareas).forEach(textarea => {
        textarea.addEventListener("keyup", checkPrevPage);
        textarea.addEventListener("input", checkNextPage);
        textarea.addEventListener("click", closeModal);
    });
}

function checkPrevPage(event) {
    if (event.key === "Backspace" && !isCalled) {
        const textarea = event.target;
        const index = textareas.indexOf(textarea);

        if (textarea.value[0] === "\n") {
            textareaValues[index] = textarea.value.slice(1);
            textarea.value = textareaValues[index];
            textarea.selectionEnd = 0;
        } 
        else if (index > 0) {
            textarea.blur();
            changePageBySlide("prev", "0.3s");
        }
    }
    isCalled = event.key === "Process";
}

function checkNextPage(event) {
    const textarea = event.target;
    const index = textareas.indexOf(textarea);
    isCalled = true;

    if (textarea.scrollHeight !== textarea.offsetHeight) {
        if (textarea.value.length === textareaValues[index].length) {
            textareaValues[index] = textareaValues[index].slice(0, -1);
        }
        const selection = textarea.selectionEnd - 1;
        textarea.value = textareaValues[index];
        textarea.selectionEnd = selection;
        if (canTurnPage(index, event)) {
            textarea.blur();
            isCalled = false;
            changePageBySlide("next", "0.3s");
        }
    }
    textareaValues[index] = textarea.value;
}

function canTurnPage(index, event) {
    const textarea = event.target
    if (textarea.selectionEnd !== textarea.value.length) {
        return false;
    }
    return index < 4 && event.inputType === "insertLineBreak";
}
