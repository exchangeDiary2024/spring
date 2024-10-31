var startX = 0;
var slideType = "stop";

function addSlideEventByNoteContent(noteContent) {
    noteContent.addEventListener("touchstart", slidePage);
    noteContent.addEventListener("touchmove", movePage);
    noteContent.addEventListener("touchend", changePageBySlide);
}

function slidePage(event) {
    startX = event.touches[0].pageX;
    slideType = "stop";
    removePageTransform(prevPage);
    removePageTransform(nextPage);
    removePageTransform(currentPage);
}

function movePage(event) {
    const changedX = -(startX - event.touches[0].pageX);

    movepageTransform(prevPage, -window.innerWidth + changedX);
    movepageTransform(nextPage, window.innerWidth + changedX);
    movepageTransform(currentPage, changedX);

    slideType = "stop";
    if (changedX <= -100 && nextPage !== null) {
        slideType = "next";
    }
    if (changedX >= 100 && prevPage !== null) {
        slideType = "prev";
    }
}

function changePageBySlide() {
    changePage(getPageBySlideType(slideType));
    addPageTransform(prevPage, "-100%");
    addPageTransform(nextPage, "100%");
    addPageTransform(currentPage, "0%");
}

function removePageTransform(page) {
    if (page !== null) {
        page.noteContent.style.transition = "ease-out";
    }
}

function movepageTransform(page, changedX) {
    if (page !== null) {
        page.noteContent.style.transform = `translateX(${changedX}px)`;
    }
}

function addPageTransform(page, percentage) {
    if (page !== null) {
        page.noteContent.style.transition = "transform 0.3s ease-out";
        page.noteContent.style.transform = `translateX(${percentage})`;
    }
}

function getPageBySlideType(type) {
    if (type === "stop") {
        return currentPage;
    }
    if (type === "next") {
        return nextPage;
    }
    return prevPage;
}
