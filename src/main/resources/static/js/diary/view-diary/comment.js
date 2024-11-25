const commentBtn = document.querySelector(".comment-btn");
const note = document.querySelector(".note");
const comment = createCommentDiv();
const currentPathName = window.location.pathname;

init();

function createCommentDiv() {
    const commentDiv = document.createElement("div");

    commentDiv.classList.add("comment", "highlight");
    commentDiv.prepend(document.createElement("div"));
    commentDiv.children[0].classList.add( "comment-character-icon");
    return commentDiv;
}

function init() {
    commentBtn.addEventListener("click", clickCommentBtn);
}

function clickCommentBtn(event) {
    if (!event.currentTarget.classList.contains("selected")) {
        drawCommentProfileImage();
        addBlur();
        event.currentTarget.classList.add("selected");
    } else {
        removeBlur();
        note.firstChild.remove();
        event.currentTarget.classList.remove("selected");
    }
}

function drawCommentProfileImage() {
    fetch(`/api${currentPathName}/comments/verify`)
        .then(async response => {
            if (response.status === 200) {
                return response.json();
            }
            throw await response.json();
        })
        .then(data => createComment(data.profileImage))
        .catch(data => openNotificationModal("error",  [data.message], 2000));
}

function createComment(profileImage) {
    comment.children[0].classList.add(profileImage);
    note.prepend(comment);
}

function addBlur() {
    const blur = document.createElement("div");
    blur.classList.add("blur");
    document.querySelector(".background").appendChild(blur);

    const commentArea = document.createElement("div");
    commentArea.classList.add("comment-area");
    note.prepend(commentArea);
}

function removeBlur() {
    document.querySelector(".background").lastChild.remove();
    note.removeChild(note.children[0]);
}