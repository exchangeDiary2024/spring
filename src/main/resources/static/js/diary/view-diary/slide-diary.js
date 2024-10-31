var startX = 0;

function addSlideEventByNoteContent(noteContent) {
    noteContent.addEventListener("touchstart", slidePage);
    noteContent.addEventListener("touchmove", movePage);
    noteContent.addEventListener("touchend", changePageBySlide);
}

function slidePage(event) {
    startX = event.touches[0].pageX;
    currentPage.noteContent.style.transition = "ease-out";
}

function movePage(event) {
    const changedX = -(startX - event.touches[0].pageX);

    currentPage.noteContent.style.transform = `translateX(${changedX}px)`;
    
}

function changePageBySlide(event) {
    currentPage.noteContent.style.transition = "transform 0.3s ease-out";
    currentPage.noteContent.style.transform = `translateX(0px)`;
}
