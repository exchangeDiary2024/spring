const commentBtn = document.querySelector(".comment-btn");
const commentArea = document.querySelector(".comment-area");
const background = document.querySelector(".background");
const currentPathName = window.location.pathname;

init();

function init() {
    commentBtn.addEventListener("click", clickCommentBtn);
}

function clickCommentBtn(event) {
    event.preventDefault();

    if (!event.currentTarget.classList.contains("selected")) {
        drawCommentProfileImage();
        addBlur();
        event.currentTarget.classList.add("selected");
    } else {
        removeBlur();
        commentArea.removeChild(document.querySelector(".comment"));
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
        .then(data => {
            const comment = createComment(data.profileImage);
            commentArea.appendChild(comment);
        })
        .catch(data => openNotificationModal("error",  [data.message], 2000));
}

function createComment(profileImage) {
    const comment = document.createElement("div");

    comment.classList.add("comment", "highlight", profileImage);
    comment.addEventListener("touchmove", moveProfileImage);
    comment.addEventListener("touchend", setProfileImage);
    return comment;
}

function addBlur() {
    const blur = document.createElement("div");
    blur.classList.add("blur");
    background.appendChild(blur);

    commentArea.classList.add("highlight");
}

function removeBlur() {
    background.lastChild.remove();
    commentArea.classList.remove("highlight");
}
