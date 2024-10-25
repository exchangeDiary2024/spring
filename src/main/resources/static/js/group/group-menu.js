const menuBtn = document.querySelector(".menu-btn");
const groupMenu = document.querySelector(".group-menu");
const menu = groupMenu.querySelector(".menu");
const groupMembers = menu.querySelector(".group-members");
const groupSize = menu.querySelector(".group-size .size");
const groupLeaveBtn = menu.querySelector(".group-leave");
const groupCodeBtn = menu.querySelector(".group-code");

menuBtn.addEventListener("click", openMenu);
groupMenu.addEventListener("click", closeMenu);
groupLeaveBtn.addEventListener("click", groupLeave);
groupCodeBtn.addEventListener("click", () => {
    try {
        navigator.clipboard.writeText(groupCodeBtn.getAttribute("data-code"))
        openNotificationModal("success", ["코드 복사에 성공했습니다."], 500);
    } catch {
        openNotificationModal("error", ["오류가 발생했습니다."], 2000);
    }
});

function openMenu() {
    fetch(`/api/groups/${groupId}/members`)
        .then(response => response.json())
        .then(data => drawMenu(data));
    groupMenu.style.display = "block";
    groupMenu.classList.add("blur");
    setTimeout(() => menu.style.transform = "translateX(0)", 10);
}

function closeMenu(event) {
    if (event.target === groupMenu) {
        menu.style.transform = "translateX(100%)"
        groupMenu.classList.remove("blur");
        setTimeout(() => groupMenu.style.display = "none", 300);
        removeMembers();
    }
}

function drawMenu(data) {
    groupSize.innerText = data.members.length;
    drawMembers(data.members);
    const members = groupMembers.querySelectorAll(".group-member");

    members[data.selfIndex].innerHTML = makeMyHtml() + members[data.selfIndex].innerHTML;
    members[data.leaderIndex].querySelector(".profile-image").innerHTML += makeLeaderHtml();
    members[data.currentWriterIndex].classList.add("order");
    if (data.selfIndex === data.leaderIndex) {
        groupMembers.classList.add("leader");
        members.forEach(member => member.addEventListener("click", selectGroupMember));
    }
}

function drawMembers(members) {
    const centerX = 88;
    const centerY = 119;
    const r = 79;
    const memberSize = members.length;
    var index = 0;

    members.forEach(member => {
        const groupMember = document.createElement("div");
        const radian = getRadian(getAngle(index, memberSize));
        groupMember.classList.add("group-member");
        groupMember.innerHTML = makeMemberHtml(member.profileImage, member.nickname);
        groupMember.style.left = `${centerX - r * Math.cos(radian)}px`;
        groupMember.style.top = `${centerY - r * Math.sin(radian)}px`;
        groupMembers.appendChild(groupMember);
        index++;
    });
}

function getRadian(angle) {
    return angle * Math.PI / 180;
}

function getAngle(number, memberSize) {
    const angle = 270 / (memberSize - 1) * number;
    if (isFinite(angle)) {
        return angle;
    }
    return 0;
}

function makeMemberHtml(characterName, memberName) {
    return `<a class="profile-image" href="#">
                <img class="${characterName} character-icon" />
            </a>
            <span class="profile-nickname">${memberName}</span>`
}

function makeMyHtml() {
    return `<div class="my">
                <span style='color: #FFF; text-align: center; font-family: "SOYOMaple"; font-size: 6px; font-style: normal; font-weight: 700; line-height: 100%; letter-spacing: 0.06px;'>나</span>
            </div>`
}

function makeLeaderHtml() {
    return '<img class="crown" />'
}

function removeMembers() {
    const members = groupMembers.querySelectorAll(".group-member");
    Array.from(members).forEach(member => {
        member.remove();
    })
}

async function groupLeave(event) {
    event.preventDefault();
    const result = await openConfirmModal("정말 탈퇴하시겠어요?", "탈퇴할 시 모든 데이터가 영구적으로 삭제됩니다.");

    if (result) {
        const url = event.target.closest("a").href;

        fetch(url, {
            method: "PATCH"
        })
        .then(response => {
             if (response.status === 200) {
                 openNotificationModal("success", ["탈퇴를 완료했어요.", "새로운 스프링을 시작해 보아요!"], 2000, () => window.location.href = '/group');
             } else {
                 openNotificationModal("error", ["오류가 발생했습니다."], 2000);
             }
        })
    }
}
