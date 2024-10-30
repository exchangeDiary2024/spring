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
        html: ""
    },
    {
        index: 1,
        html: ""
    },
    {
        index: 2,
        html: ""
    },
    {
        index: 3,
        html: ""
    },
    {
        index: 4,
        html: ""
    }
]
var currentPage = pages[0];

function viewDiary() {
    drawPageBar();
    drawDiaryPage(currentPage.html, "stop");
    adjustCharacterSize();
}

function drawPageBar() {
    const pageBtns = pageBar.children;
    const contents = testDiaryData.contents;

    var index = 0;
    if (testDiaryData.image !== null) {
        pageBtns[index].classList.add("active");
        pageBtns[index].addEventListener("click", changePage);
        pages[index].html = makeDiaryPageHTMLContainsImage(testDiaryData.image, contents[index].content);
        index++;
    }

    for (index; index < contents.length; index++) {
        pageBtns[index].classList.add("active");
        pageBtns[index].addEventListener("click", changePage);
        pages[index].html = makeDiaryPageHTML(contents[index].content);
    }

    pageBtns[0].classList.add("fill");
}

function changePage(event) {
    event.preventDefault();
    const pageIndex = event.target.getAttribute("data-index");
    const page = pages[pageIndex];

    removeDiaryPage();
    drawDiaryPage(page.html, "stop");
    pageBar.children[currentPage.index].classList.remove("fill");
    event.target.classList.add("fill");

    currentPage = page;
}

function removeDiaryPage() {
    const noteContent = document.querySelector(".note-content");
    noteContent.remove();
}

function drawDiaryPage(html, direction) {
    const noteContent = document.createElement("div");
    noteContent.classList.add("note-content", direction);
    noteContent.innerHTML = html;
    noteBody.appendChild(noteContent);
}

function makeDiaryPageHTML(content) {
    return `
    <div class="diary-content-area">
        <p class="diary-content">${content}</p>
    </div>
    `;
}

function makeDiaryPageHTMLContainsImage(image, content) {
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
