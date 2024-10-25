const circleCenter = document.querySelector(".circle-center")
const leaderBtns = circleCenter.querySelector(".leader-btns");
var selectedMember = null;

function selectGroupMember(event) {
    const member = event.target.closest(".group-member");
    selectedMember = member;
    member.classList.add("selected");
    openLeaderMenu(member);
    groupMenu.addEventListener("click", unselectGroupMember);
}

function openLeaderMenu(member) {
    if (member.classList.contains("order")) {
        leaderBtns.classList.add("order");
    } else {
        leaderBtns.classList.remove("order");
    }
    circleCenter.classList.replace("group-size", "leader-btns");
}

function unselectGroupMember(event) {
    if (event.target.closest(".group-member") ===  null && event.target.closest(".circle-center") ===  null) {
        selectedMember.classList.remove("selected");
        selectedMember = null;
        closeLeaderMenu();
        groupMenu.removeEventListener("click", unselectGroupMember);
    }
}

function closeLeaderMenu() {
    circleCenter.classList.replace("leader-btns", "group-size");
}
