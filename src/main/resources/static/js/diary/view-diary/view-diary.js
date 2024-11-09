const noteBody = document.querySelector(".note-body");
const pageBar = document.querySelector(".page-bar");
const pages = []
const currentPathname = window.location.pathname
var currentPage = null;
var prevPage = null;
var nextPage = null;

function viewDiary() {
    fetch(`/api${currentPathname}`)
        .then(response => response.json())
        .then(data => drawPageBar(data));
}

function drawPageBar(diary) {
    const pageBtns = pageBar.children;
    const contents = diary.contents;
    const html = makeDiaryTitleHTML(diary.profileImage, diary.nickname) + makeDiaryPageHTMLContainsImage(diary.imageFileName, contents[0].content);
    const page = { index: 0, noteContent: makeNoteContent(html) };
    pages.push(page);
    noteBody.appendChild(page.noteContent);
    pageBtns[0].classList.add("active");
    pageBtns[0].addEventListener("click", changePageByBtn);
    pageBtns[0].classList.add("fill");
    currentPage = page;

    for (var index = 1; index < contents.length; index++) {
        pageBtns[index].classList.add("active");
        pageBtns[index].addEventListener("click", changePageByBtn);
        const noteContent = makeNoteContent(makeDiaryPageHTML(contents[index].content));
        noteContent.style.transform = "translateX(100%)";
        noteBody.appendChild(noteContent);
        const page = { index: index, noteContent: noteContent };
        pages.push(page);
    }
    changePage(page);
}

function changePageByBtn(event) {
    event.preventDefault();
    const pageIndex = event.target.getAttribute("data-index");
    const targetPage = pages[pageIndex];
    const skipSize = currentPage.index - pageIndex;
    const time = 0.3 / skipSize;

    if (skipSize < 0) {
        for (var index=-1; index >= skipSize; index--) {
            changePageBySlide("next", `${time * index}s`);
        }
    } else {
        for (var index=1; index <= skipSize; index++) {
            changePageBySlide("prev", `${time * index}s`);
        }
    }
    changePage(targetPage);
}

function changePage(targetPage) {
    pageBar.children[currentPage.index].classList.remove("fill");
    currentPage = targetPage;
    prevPage = getPrevPage();
    nextPage = getNextPage();
    pageBar.children[currentPage.index].classList.add("fill");
}

function getNextPage() {
    if (currentPage.index === pages.length - 1) {
        return null
    }
    return pages[currentPage.index + 1];
}

function getPrevPage() {
    if (currentPage.index === 0) {
        return null
    }
    return pages[currentPage.index - 1];
}

function makeNoteContent(html) {
    const noteContent = document.createElement("div");
    noteContent.classList.add("note-content");
    noteContent.innerHTML = html;
    addSlideEventByNoteContent(noteContent);
    return noteContent;
}

function makeDiaryPageHTML(content) {
    return `
    <div class="diary-content-area">
        <p class="diary-content">${content}</p>
    </div>
    `;
}

function makeDiaryTitleHTML(profileImage, name) {
    if (profileImage === "green" || profileImage === "blue") {
        profileImage = `${profileImage} ${profileImage}-icon`
    }
    return `
    <div class="note-title">
        <div class="character"><img class="${profileImage} character-icon"></div>
        <span class="title-font">${name}의 일기</span>
        <div class="character"><img class="${profileImage} character-icon"></div>
    </div>`
}

function makeDiaryPageHTMLContainsImage(image, content) {
    var imageUrl = "/images/diary/view-page/default-image.png"
    if (image !== null) {
        imageUrl = `/images/upload${currentPathname.split("/", 3).join("/")}/${image}`
    }
    return `
    <div class="image">
        <img class="image-size" src='${imageUrl}' />
    </div>
    <div class="diary-content-area">
        <p class="diary-content">${content}</p>
    </div>
    `;
}

viewDiary();
