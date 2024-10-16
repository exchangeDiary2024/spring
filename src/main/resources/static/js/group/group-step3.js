const STEP3_HTML = `<div style="width: 100%; height: 34px;">
                                <span class="subject">나의 캐릭터를 골라요.</span>
                            </div>
                            <div class="line">
                                <div style="margin-left: 90px; height: 100%">
                                    <div class="character-btn">
                                        <a id="pring2" href="#" class="character-icon">
                                            <img src="/images/group/character/pring2.svg" class="character-icon">
                                        </a>
                                    </div>
                                    <div class="character-btn">
                                        <a id="pring3" href="#" class="character-icon">
                                            <img src="/images/group/character/pring3.svg" class="character-icon">
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="line">
                                <div style="margin-left: 35px; height: 100%">
                                    <div class="character-btn">
                                        <a id="pring1" href="#" class="character-icon">
                                            <img src="/images/group/character/pring1.svg" class="character-icon">
                                        </a>
                                    </div>
                                    <div class="character-btn">
                                        <a id="pring7" href="#" class="character-icon">
                                            <img src="/images/group/character/pring7.svg" class="character-icon">
                                        </a>
                                    </div>
                                    <div class="character-btn">
                                        <a id="pring4" href="#" class="character-icon">
                                            <img src="/images/group/character/pring4.svg" class="character-icon green">
                                        </a>
                                    </div>
                                </div>
                            </div>
                            <div class="line">
                                <div style="margin-left: 90px; height: 100%">
                                    <div class="character-btn">
                                        <a id="pring5" href="#" class="character-icon">
                                            <img src="/images/group/character/pring5.svg" class="character-icon blue">
                                        </a>                                    
                                    </div>
                                    <div class="character-btn">
                                        <a id="pring6" href="#" class="character-icon">
                                            <img src="/images/group/character/pring6.svg" class="character-icon">
                                        </a>
                                    </div>           
                                </div>
                            </div>`;

function drawStep3(direction) {
    const step_content = document.createElement("div");
    step_content.classList.add("step-content", direction);
    step_content.innerHTML = STEP3_HTML;
    note_body.appendChild(step_content);
    setTimeout(() => step_content.style.transform = "translateX(0)", 10);
    initStep3();
}

function initStep3() {
    const icons = note_body.querySelectorAll("a.character-icon");
    selectedIcon = null;

    Array.from(icons).forEach(icon => {
        icon.addEventListener("click", (event) => selectIcon(event));
    })

    if (groupData.groupId !== "") {
        viewSelectableCharacter();
    }
}

function selectIcon(event) {
    if (selectedIcon != null) {
        selectedIcon.classList.remove("selected");
    }
    selectedIcon = event.target.closest("div.character-btn");
    selectedIcon.classList.add("selected");
}

async function viewSelectableCharacter() {
    const groupId = groupData.groupId;

    const selectedImages = await fetch(`/api/groups/${groupId}/profile-image`)
        .then(response => response.json())
        .then(data => data.selectedImages);

    selectedImages.forEach(image => {
        const profileLocation = image.profileLocation
        const index = profileLocation.indexOf("pring");
        const name = profileLocation.substr(index, 6);
        const pring = document.getElementById(name);

        pring.classList.add("gray");
    });
}

function confirmStep3() {
    if (selectedIcon != null) {
        groupData.profileLocation = selectedIcon.children[0].children[0].src;
        return true;
    }
    openNotificationModal("error", ["캐릭터를 선택해주세요."], 2000);
    return false;
}
