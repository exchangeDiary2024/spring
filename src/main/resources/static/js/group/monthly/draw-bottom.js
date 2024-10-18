function drawBottom() {
    const calendarBottom = document.querySelector(".calendar-bottom")

    fetch(`/api/groups/${groupId}/diaries/status`)
        .then(response => response.json())
        .then(async data => calendarBottom.innerHTML = await getCalendarBottomHtml(data));
}

/*
TODO: API 수정 후
    1. 마지막 일기 작성자에 대한 처리 필요
    2. diaryId
*/

async function getCalendarBottomHtml(diaryStatus) {
    console.log(diaryStatus);
    if (diaryStatus.isMyOrder) {
        if (diaryStatus.writtenTodayDiary) {
            return `<a href="/group/${groupId}/diary/${data.diaryId}" class="bottom-font">
                        <span class="font-bold">오늘 일기가 업로드 되었어요.</span><br>
                        <span>날짜를 눌러 확인해보세요!</span>
                    </a>`;
        }
        return `<a href="/group/${groupId}/diary" class="bottom-font">
                    <span>내가 일기를 작성할 차례에요.</span><br>
                    <span>기다리는 친구들을 위해</span><br>
                    <span class="font-bold">일기를 작성해주세요!</span>
                </a>`;
    }
    if (!diaryStatus.writtenTodayDiary) {
        return `<div class="bottom-font">
                    <span class="font-bold">아직 친구가 일기를 작성하지 않았어요!</span>
                </div>`;
    }
    return `<div class="bottom-font">
                <span class="font-bold">친구가 일기가 업로드 했어요.</span><br>
                <span>내 차례가 올 때까지 일기를 기다려요!</span> 
            </div>`;
}

drawBottom();