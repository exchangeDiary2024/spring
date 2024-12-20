function addEventInCommentTextarea() {
    const textarea = document.querySelector(".comment-textarea");

    textarea.addEventListener("keydown", (event) => {
        if (!isIME(event)) {
            if (event.key === "Backspace") {
                removeStickerCharacter(event, textarea);
                removeNewLine(event, textarea)
            }
            if (event.key === "Enter") {
                addNewLine(event, textarea)
            }
            adjustHeightByTextarea();
        }
    });
}

function isIME(event) {
    return event.isComposing || event.key === "Process"
}

function removeStickerCharacter(event, textarea) {
    if (textarea.lastChild && textarea.lastChild.nodeType === Node.ELEMENT_NODE && textarea.lastElementChild.tagName === "DIV") {
        event.preventDefault();
        textarea.lastElementChild.remove();
    }
}

function removeNewLine(event, textarea) {
    if (textarea.innerHTML.lastIndexOf("<br>") === textarea.innerHTML.length - 5) {
        event.preventDefault();
        textarea.innerHTML = textarea.innerHTML.substring(0, textarea.innerHTML.length - 5);
        moveCursorToEnd(textarea);
    }
}

function addNewLine(event, textarea) {
    event.preventDefault();
    textarea.appendChild(document.createElement("br"));
    textarea.appendChild(document.createTextNode("\u200B"));
    moveCursorToEnd(textarea);
}

function moveCursorToEnd(element) {
    const range = document.createRange();
    const selection = window.getSelection();

    range.selectNodeContents(element);
    range.collapse(false);
    selection.removeAllRanges();
    selection.addRange(range);
}
