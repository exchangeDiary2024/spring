const table = document.querySelector("table");
const year = document.querySelector(".year");
const month = document.querySelector(".month");
const trs = Array.from(table.children[0].children).slice(3);
const today = new Date();
const calendarBottom = document.querySelector(".calendar-bottom")
const groupId = localStorage.getItem("groupId");

function init() {
    year.innerText = today.getFullYear();
    month.innerText = today.getMonth() + 1;

    drawDateOfCalendar();
    drawBottom();
}

async function drawDateOfCalendar() {
    const firstDay = new Date(year.innerText, month.innerText - 1, 1).getDay();
    const lastDate = new Date(year.innerText, month.innerText, 0).getDate();

    let date = 1;
    let day = firstDay
    let column = 0;

    const days = await fetch(`/api/groups/${groupId}/diaries/monthly?year=${year.innerText}&month=${month.innerText}`)
        .then(response => response.json())
        .then(data => data.days);
    const diaryDays = days.map(day => Number(day.date))

    while (date <= lastDate) {
        trs[column].children[day].innerHTML = makeCircle(date, diaryDays);
        date++;
        day++;
        if (day === 7) {
            day = 0;
            column++;
        }
    }
    addEvents();
    drawToday();
}

function clearDate() {
    trs.forEach(tr => Array.from(tr.children).forEach(td => td.innerText = ""));
}

function makeCircle(date, diaryDays) {
    if (diaryDays.includes(date)) {
        return `<a class="date day${date} diary highlight" href="/api/groups/${groupId}/diaries">${date}</a>`;
    }
    if (isToday(date)) {
        return `<a class="date day${date} today highlight" href="/diary">${date}</a>`;
    }
    return `<span class="date day${date}">${date}</span>`;
}

function isToday(date) {
    if (today.getFullYear() != year.innerText) {
        return false
    }
    if (today.getMonth() != month.innerText - 1) {
        return false
    }
    return today.getDate() == date;
}

function addEvents() {
    const diaryDays = document.querySelectorAll("a.diary");
    Array.from(diaryDays).forEach( date => {
        date.addEventListener("click", showDiary);
    })
}

function showDiary(event) {
    event.preventDefault();
    const url = event.target.href
    fetch(`${url}?year=${year.innerText}&month=${month.innerText}&day=${event.target.innerText}`)
        .then(response => response.json())
        .then(data => window.location.href = `/diary/${data.diaryId}`);
}

function drawBottom() {
    fetch(`/api/groups/${groupId}/diaries?year=${today.getFullYear()}&month=${today.getMonth() + 1}&day=${today.getDate()}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("")
            }
            return response.json()
        })
        .then(data => {
            calendarBottom.innerHTML = `<a href="/diary/${data.diaryId}" class="bottom-font">
                                                        <span class="font-bold">오늘 일기가 업로드 되었어요.</span>
                                                        <br>
                                                        <span>날짜를 눌러 확인해보세요!</span>
                                                    </a>`
        })
        .catch(() => {
            calendarBottom.innerHTML = `<a href="/diary" class="bottom-font">
                                            <span>내가 일기를 작성할 차례에요.</span>
                                            <br>
                                            <span>기다리는 친구들을 위해</span>
                                            <br>
                                            <span class="font-bold">일기를 작성해주세요!</span>
                                        </a>`
        })
}

function drawToday() {
    if (isToday(today.getDate())) {
        const firstDay = new Date(today.getFullYear(), today.getMonth(), 1).getDay() - 1;
        const todayDate = today.getDate();
        const column = Math.floor((todayDate + firstDay) / 7);
        const row = (todayDate + firstDay) % 7;
        trs[column].children[row].querySelector("a").classList.add("today");
    }
}

init();
