const testDiaryData = {
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

function viewDiary() {
    drawPageBar();
    adjustCharacterSize();
}

function drawPageBar() {
    const pageBtns = pageBar.children;
    const contents = testDiaryData.contents;
    const html = makeDiaryPageHTMLContainsImage(testDiaryData.image, contents[0].content);
    pageBtns[0].classList.add("active");
    pageBtns[0].addEventListener("click", changePage);
    pageBtns[0].classList.add("fill");
    pages[0].noteContent = makeNoteContent(html);
    noteBody.appendChild(pages[0].noteContent);

    for (var index = 1; index < contents.length; index++) {
        pageBtns[index].classList.add("active");
        pageBtns[index].addEventListener("click", changePage);
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
    page.noteContent.style.display = "none";
}

function changePage(event) {
    event.preventDefault();

    pageBar.children[currentPage.index].classList.remove("fill");
    undisplayPage(currentPage);

    const pageIndex = event.target.getAttribute("data-index");
    const page = pages[pageIndex];
    event.target.classList.add("fill");
    currentPage = page;
    displayPage(currentPage);
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

function adjustCharacterSize() {
    const characters = document.querySelectorAll(".character-icon");

    if (characters[0].classList.contains("green") || characters[0].classList.contains("blue")) {
        characters.forEach(character => character.classList.add(`${characters[0].classList[0]}-icon`));
    }
}

viewDiary();
