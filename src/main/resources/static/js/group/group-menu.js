const menuBtn = document.querySelector(".menu-btn");
const groupMenu = document.querySelector(".group-menu");
const menu = groupMenu.querySelector(".menu");
const groupSize = menu.querySelector(".group-size .size");
const groupMembers = menu.querySelector(".group-members");

menuBtn.addEventListener("click", openMenu);
groupMenu.addEventListener("click", closeMenu);

fetch(`/api/groups/${groupId}/members`)
    .then(response => response.json())
    .then(data => drawMenu(data));

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

function drawMenu(data) {
    groupSize.innerText = data.members.length;
    drawMembers(data.members);
    const members = groupMembers.querySelectorAll(".group-member");

    members[data.selfIndex].innerHTML = makeMyHtml() + members[data.selfIndex].innerHTML;
    members[data.leaderIndex].querySelector(".profile-image").innerHTML += makeLeaderHtml();
    members[data.currentWriterIndex].classList.add("order");
    if (data.selfIndex === data.leaderIndex) {
        groupMembers.classList.add("leader");
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
