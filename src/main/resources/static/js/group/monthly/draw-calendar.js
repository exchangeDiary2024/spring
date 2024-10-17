const groupName = document.querySelector(".group-name");
const table = document.querySelector("table");
const year = document.querySelector(".year");
const month = document.querySelector(".month");
const trs = Array.from(table.children[0].children).slice(3);
const today = new Date();
const calendarBottom = document.querySelector(".calendar-bottom")

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
    addBorderToday();
    addEvents();
}

function clearDate() {
    trs.forEach(tr => Array.from(tr.children).forEach(td => td.innerText = ""));
}

function makeCircle(date, writtenDiaryDays) {
    const index = writtenDiaryDays.findIndex((day) => day.date === date);
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
                    <img id="${profileImage}" class=${profileImage}>
                </a>`;
    }
    return `<a class="date day${date} highlight written" href="/api/groups/${groupId}/diaries">
                <img id="${profileImage}" class="profile-icon"">
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

function addEvents() {
    const diaryDays = document.querySelectorAll("a.written");
    Array.from(diaryDays).forEach( date => {
        date.addEventListener("click", showDiary);
    })
}

function showDiary(event) {
    event.preventDefault();
    const url = event.currentTarget.href;

    fetch(`${url}?year=${year.innerText}&month=${month.innerText}&day=${event.target.innerText}`)
        .then(response => response.json())
        .then(data => window.location.href = `/group/${groupId}/diary/${data.diaryId}`);
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

function drawBottom() {
    fetch(`/api/groups/${groupId}/diaries?year=${today.getFullYear()}&month=${today.getMonth() + 1}&day=${today.getDate()}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("")
            }
            return response.json()
        })
        .then(data => {
            calendarBottom.innerHTML = `<a href="/group/${groupId}/diary/${data.diaryId}" class="bottom-font">
                                                        <span class="font-bold">오늘 일기가 업로드 되었어요.</span>
                                                        <br>
                                                        <span>날짜를 눌러 확인해보세요!</span>
                                                    </a>`
        })
        .catch(() => {
            calendarBottom.innerHTML = `<a href="/group/${groupId}/diary" class="bottom-font">
                                            <span>내가 일기를 작성할 차례에요.</span>
                                            <br>
                                            <span>기다리는 친구들을 위해</span>
                                            <br>
                                            <span class="font-bold">일기를 작성해주세요!</span>
                                        </a>`
        })
}

init();
