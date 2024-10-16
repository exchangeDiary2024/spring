const STEP4_HTML = `
                        <div style="width: 100px; height: 100px;">
                            <img class="selected-pring" src="" style="width: 100px; height: 100px;">
                        </div>
                        <div style="width: 100%; height: 34px; position: relative; top: 16px;">
                            <span class="subject">캐릭터 이름을 지어주세요.</span>
                        </div>
                        <div class="input-box">
                            <div class="input-textarea">
                                <label>
                                    <textarea class="nickname input-value" placeholder="이름" maxlength="10" spellcheck="false"></textarea>
                                </label>
                            </div>
                        </div>
                        <div class="error-message" style="width: 283px; height: 34px; float: left; position: relative; left: 46px; top: 24px;">
                            <span class="error-text"></span>
                        </div>`;

function drawStep4(direction) {
    const step_content = document.createElement("div");
    step_content.classList.add("step-content", direction);
    step_content.innerHTML = STEP4_HTML;
    note_body.appendChild(step_content);
    setTimeout(() => step_content.style.transform = "translateX(0)", 10);
    initStep4();
}

function initStep4() {
    nicknameBox = document.querySelector(".input-box");
    nickname = document.querySelector(".nickname");
    error = document.querySelector(".error-text");

    setProfileImage();
    nickname.addEventListener("input", async () => {
        if (nickname.value === "") {
            nicknameBox.style.border = "0.5px solid #767676";
        } else {
            nicknameBox.style.border = "2px solid #FC0";
            error.innerText = await verifyNickname();
        }
    });
}

async function confirmStep4() {
    if (groupData.groupId === "") {
        if (error.innerText !== "") {
            return false;
        }
        steps[5].draw = drawStep5Create;
        return await createGroup();
    }
    steps[5].draw = drawStep5Join;
    if (error.innerText !== "") {
        return false;
    }
    return await joinGroup();
}

function setProfileImage() {
    const pring = document.querySelector(".selected-pring");

    pring.src = groupData.profileLocation;
}

async function verifyNickname() {
    const specialChar = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"₩]/;

    if (specialChar.test(nickname.value)) {
        return "특수문자는 사용할 수 없습니다.";
    }
    if (await isDuplicateNickname()) {
        return "이미 존재하는 이름입니다.";
    }
    return "";
}

async function isDuplicateNickname() {
    if (groupData.groupId === "") {
        return false;
    }
    return await fetch(`/api/groups/${groupData.groupId}/nickname/verify?nickname=${nickname.value}`)
        .then(response => response.status !== 200);
}

async function createGroup() {
    return await fetch("/api/groups",{
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "groupName": groupData.groupName,
            "profileLocation": groupData.profileLocation,
            "nickname": nickname.value
        })
    })
        .then(response => {
            if (response.status !== 201) {
                throw response.status;
            }
            return response.json();
        })
        .then(data => {
            groupData.groupCode = data.code;
            groupData.groupId = data.groupId;
            return true;
        })
        .catch(status => {
            if (status === 409) {
                openNotificationModal("error", ["그룹 정원이 가득 찼습니다."], 2000);
                return false;
            }
            openNotificationModal("error", ["그룹 생성에 실패했습니다."], 2000);
            return false;
        });
}

async function joinGroup() {
    return await fetch(`/api/groups/${groupData.groupId}/join`,{
        method: "PATCH",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "profileLocation": groupData.profileLocation,
            "nickname": nickname.value
        })
    })
        .then(response => {
            if (response.status === 200) {
                return true;
            }
            openNotificationModal("error", ["이미 선택된 캐릭터입니다.<br>다시 선택해주세요."], 2000);
            return false;
        });
}
