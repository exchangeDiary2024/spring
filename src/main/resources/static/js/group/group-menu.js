const menuBtn = document.querySelector(".menu-btn");
const groupMenu = document.querySelector(".group-menu");
const menu = groupMenu.querySelector(".menu");
const groupMembers = menu.querySelector(".group-members");

menuBtn.addEventListener("click", openMenu);
groupMenu.addEventListener("click", closeMenu);
drawMembers(6);

function openMenu() {
    groupMenu.style.display = "block";
    groupMenu.classList.add("blur");
    setTimeout(() => menu.style.transform = "translateX(0)", 10);
}

function closeMenu(event) {
    if (event.target === groupMenu) {
        menu.style.transform = "translateX(100%)"
        groupMenu.classList.remove("blur");
        setTimeout(() => groupMenu.style.display = "none", 300);
    }
}

function drawMembers(memberSize) {
    const centerX = 88;
    const centerY = 119;
    const r = 79;

    // 테스트를 위한 코드 입니다.
    const testCharacters = ["red", "orange", "yellow", "green", "blue", "navy", "purple"];
    const testNicknames = ["하니", "민지", "해린", "해린", "다니엘", "버니", "혜인"];
    for (number = 0; number < memberSize; number++) {
        const groupMember = document.createElement("div");
        const radian = getRadian(getAngle(number, memberSize));
        groupMember.classList.add("group-member");
        groupMember.innerHTML = makeMemberHtml(testCharacters[number], testNicknames[number]);
        groupMember.style.left = `${centerX - r * Math.cos(radian)}px`;
        groupMember.style.top = `${centerY - r * Math.sin(radian)}px`;
        groupMembers.appendChild(groupMember);
    }
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
    return `<div class="my"><span style='color: #FFF; text-align: center; font-family: "SOYOMaple"; font-size: 6px; font-style: normal; font-weight: 700; line-height: 100%; letter-spacing: 0.06px;'>나</span></div>
            <a class="profile-image" href="#">
                <img class="crown" />
                <img class="${characterName} character-icon" />
            </a>
            <span class="profile-nickname">${memberName}</span>`
}
