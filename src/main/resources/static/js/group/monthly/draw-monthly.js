const table = document.querySelector("table");
const year = document.querySelector(".year");
const month = document.querySelector(".month");
const trs = Array.from(table.children[0].children).slice(3);
const today = new Date();

async function init() {
    year.innerText = today.getFullYear();
    month.innerText = today.getMonth() + 1;

    await drawDateOfCalendar();
    drawBottom();
}

async function drawDateOfCalendar() {
    const firstDay = new Date(year.innerText, month.innerText - 1, 1).getDay();
    const lastDate = new Date(year.innerText, month.innerText, 0).getDate();

    let date = 1;
    let day = firstDay
    let column = 0;

    const writtenDiaryDays = await fetch(`/api/groups/${groupId}/diaries/monthly?year=${year.innerText}&month=${month.innerText}`)
        .then(response => response.json())
        .then(data => data.days);

    while (date <= lastDate) {
        trs[column].children[day].innerHTML = makeCircle(date, writtenDiaryDays);
        date++;
        day++;
        if (day === 7) {
            day = 0;
            column++;
        }
    }
    changeGrayProfile(writtenDiaryDays);
    addBorderToday();
    addEvents();
}

function clearDate() {
    trs.forEach(tr => Array.from(tr.children).forEach(td => td.innerText = ""));
}

function makeCircle(date, writtenDiaryDays) {
    const index = writtenDiaryDays.findIndex((day) => day.day === date);
    if (index !== -1) {
        return getProfileImageHtml(writtenDiaryDays[index].profileImage, date);
    }
    if (isToday(date)) {
        return `<a class="date day${date} highlight" href="/group/${groupId}/diary">${date}</a>`;
    }
    return `<span class="date day${date}">${date}</span>`;
}

function getProfileImageHtml(profileImage, date) {
    if (profileImage === "blue" || profileImage === "green") {
        return `<a class="date day${date} highlight written" href="/api/groups/${groupId}/diaries">
                    <img class="${profileImage} profile-icon ${profileImage}-icon">
                </a>`;
    }
    return `<a class="date day${date} highlight written" href="/api/groups/${groupId}/diaries">
                <img class="${profileImage} profile-icon">
            </a>`;
}

function isToday(date) {
    if (today.getFullYear() !== Number(year.innerText)) {
        return false
    }
    if (today.getMonth() !== Number(month.innerText) - 1) {
        return false
    }
    return today.getDate() === date;
}

function changeGrayProfile(days) {
    days.forEach(day => {
       if (!day.canView) {
           const dayBtn = document.querySelector(`.day${day.day}`);

           dayBtn.classList.add("cannot-view");
           dayBtn.children[0].classList.add("gray");
       }
    });
}

function addBorderToday() {
    if (isToday(today.getDate())) {
        const firstDay = new Date(today.getFullYear(), today.getMonth(), 1).getDay() - 1;
        const todayDate = today.getDate();
        const column = Math.floor((todayDate + firstDay) / 7);
        const row = (todayDate + firstDay) % 7;
        trs[column].children[row].querySelector("a").classList.add("today");
    }
}

function addEvents() {
    const diaryDays = document.querySelectorAll("a.written");
    Array.from(diaryDays).forEach( date => {
        date.addEventListener("click", showDiary);
    })
}

function showDiary(event) {
    event.preventDefault();
    const url = event.currentTarget.href;
    const day = event.currentTarget.classList[1].substr(3);

    fetch(`${url}?year=${year.innerText}&month=${month.innerText}&day=${day}`)
        .then(response => response.json())
        .then(data => window.location.href = `/group/${groupId}/diary/${data.diaryId}`);
}

init();
