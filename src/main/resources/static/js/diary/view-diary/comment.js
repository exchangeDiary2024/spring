const commentBtn = document.querySelector(".comment-btn");
const note = document.querySelector(".note");
const comment = createCommentDiv();
const currentPathName = window.location.pathname;

init();

function createCommentDiv() {
    const commentDiv = document.createElement("div");

    commentDiv.classList.add("comment");
    commentDiv.prepend(document.createElement("div"));
    commentDiv.children[0].classList.add("comment-character-icon");
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
        note.removeChild(document.querySelector(".note .comment"));
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
            initComment();
            comment.children[0].classList.add(data.profileImage);
            note.prepend(comment);
        })
        .catch(data => openNotificationModal("error",  [data.message], 2000));
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
    note.removeChild(document.querySelector(".note .comment-area"));
}

function initComment() {
    comment.classList.add("highlight");
    comment.style.left = "162px";
    comment.style.top = "198px";
    comment.addEventListener("touchstart", touchProfileImage);
    comment.addEventListener("touchmove", moveProfileImage);
    comment.addEventListener("touchend", setProfileImage);
}
