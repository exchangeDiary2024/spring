const DEFAULT_HEIGHT = 200;
const textarea = document.querySelector(".diary-content");

textarea.addEventListener("input", syncHeight);

function syncHeight(event) {
    const target = event.target;

    target.style.height = `${DEFAULT_HEIGHT}px`;
    target.style.height = `${target.scrollHeight - 4}px`;
}
