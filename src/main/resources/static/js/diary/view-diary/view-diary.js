const testDiaryData = {
    profileImage: "blue",
    nickname: "주한핑",
    image: null,
    contents: [
        {
            content: "첫번째 페이지"
        },
        {
            content: "두번째 페이지"
        },
        {
            content: "세번째 페이지"
        },
        {
            content: "네번째 페이지"
        },
        {
            content: "다섯번째 페이지"
        }
    ]
}

const noteBody = document.querySelector(".note-body");
const pageBar = document.querySelector(".page-bar");
const pages = [
    {
        index: 0,
        noteContent: null
    },
    {
        index: 1,
        noteContent: null
    },
    {
        index: 2,
        noteContent: null
    },
    {
        index: 3,
        noteContent: null
    },
    {
        index: 4,
        noteContent: null
    }
]
var currentPage = pages[0];
var prevPage = null;
var nextPage = pages[1];

function viewDiary() {
    drawPageBar();
}

function drawPageBar() {
    const pageBtns = pageBar.children;
    const contents = testDiaryData.contents;
    const html = makeDiaryTitleHTML(testDiaryData.profileImage, testDiaryData.nickname) + makeDiaryPageHTMLContainsImage(testDiaryData.image, contents[0].content);
    pageBtns[0].classList.add("active");
    // pageBtns[0].addEventListener("click", changePage);
    pageBtns[0].classList.add("fill");
    pages[0].noteContent = makeNoteContent(html);
    noteBody.appendChild(pages[0].noteContent);

    for (var index = 1; index < contents.length; index++) {
        pageBtns[index].classList.add("active");
        // pageBtns[index].addEventListener("click", changePage);
        const noteContent = makeNoteContent(makeDiaryPageHTML(contents[index].content));
        pages[index].noteContent = noteContent
        undisplayPage(pages[index]);
        noteBody.appendChild(noteContent);
    }
}

function displayPage(page) {
    page.noteContent.style.display = "block";
}

function undisplayPage(page) {
    page.noteContent.style.transform = "translateX(100%)";
}

function changePageTest(event) {
    event.preventDefault();

    pageBar.children[currentPage.index].classList.remove("fill");
    undisplayPage(currentPage);

    const pageIndex = event.target.getAttribute("data-index");
    const page = pages[pageIndex];
    event.target.classList.add("fill");
    currentPage = page;
    prevPage = getPrevPage();
    nextPage = getNextPage();
    displayPage(currentPage);
}

function changePage(targetPage) {
    pageBar.children[currentPage.index].classList.remove("fill");
    currentPage = targetPage;
    prevPage = getPrevPage();
    nextPage = getNextPage();
    pageBar.children[currentPage.index].classList.add("fill");
}

function getNextPage() {
    if (pages[currentPage.index + 1] === undefined) {
        return null
    }
    return pages[currentPage.index + 1];
}

function getPrevPage() {
    if (pages[currentPage.index - 1] === undefined) {
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
    if (image === null) {
        return makeDiaryPageHTML(content);
    }
    return `
    <div class="image">
        <img class="image-size" src='${image}' />
    </div>
    <div class="diary-content-area">
        <p class="diary-content">${content}</p>
    </div>
    `;
}

viewDiary();
