const textarea = document.querySelector(".diary-content");
var changed = false;

textarea.addEventListener("scroll", checkHeight);

function checkHeight(event) {
    
    if (changed) {
        changed = false;
    } else {
        const textarea = event.target;
        textarea.value.split("\n");
        textarea.value = textarea.value.slice(0, -1);
        changed = true;
    }
}
