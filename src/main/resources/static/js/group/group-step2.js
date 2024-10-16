const STEP2_HTML_CREATE = `
                        <div style="width: 100%; height: 34px;">
                            <span class="subject">스프링의 이름을 지어주세요.</span>
                        </div>
                        <div class="input-box">
                            <div class="input-textarea">
                                <label>
                                    <textarea class="group-name input-value" placeholder="그룹명" spellcheck="false" maxlength="10"></textarea>
                                </label>
                            </div>
                        </div>
                        <div class="error-message" style="width: 283px; height: 34px; float: left; position: relative; left: 46px; top: 24px;">
                            <span class="error-text"></span>
                        </div>`

const STEP2_HTML_JOIN = `
                        <div style="width: 100%; height: 34px;">
                            <span class="subject">그룹코드를 입력해주세요.</span>
                        </div>
                        <div class="input-box">
                            <div class="input-textarea">
                                <label>
                                    <textarea class="group-code input-value" placeholder="그룹코드" spellcheck="false"></textarea>
                                </label>
                            </div>
                        </div>`

function drawStep2(html, direction) {
    const step_content = document.createElement("div");
    step_content.classList.add("step-content", direction);
    step_content.innerHTML = html;
    note_body.appendChild(step_content);
    setTimeout(() => step_content.style.transform = "translateX(0)", 10);
    initStep2();
}

function drawCreateGroup(direction) {
    drawStep2(STEP2_HTML_CREATE, direction);
}

function drawJoinGroup(direction) {
    drawStep2(STEP2_HTML_JOIN, direction);
}

function initStep2() {
    inputValue = document.querySelector(".input-value");
    error = document.querySelector(".error-text");

    inputValue.addEventListener("input", () => {
        changeBoxBorderStyle();
        if (isCreate()) {
            error.innerText = verifyGroupName();
        }
    })
}

async function confirmStep2() {
    if (isCreate() && error.innerText === "") {
        if (inputValue.value === "") {
            openNotificationModal("error", ["그룹명을 입력해주세요."], 2000);
            return false;
        }
        groupData.groupName = inputValue.value;
        return true;
    }
    if (isJoin()) {
        if (inputValue.value === "") {
            openNotificationModal("error", ["그룹코드를 입력해주세요."], 2000);
            return false;
        }
        return await matchGroupByGroupCode()
    }
    return false;
}

function isCreate() {
    return inputValue.classList.contains("group-name");
}

function isJoin() {
    return inputValue.classList.contains("group-code");
}

function changeBoxBorderStyle() {
    const inputBox = document.querySelector(".input-box");

    if (inputValue.value !== "" && !inputBox.classList.contains("input")) {
        inputBox.classList.add("input");
    }
    if (inputValue.value === "" && inputBox.classList.contains("input")) {
        inputBox.classList.remove("input");
    }
}

function verifyGroupName() {
    const groupName = document.querySelector(".group-name");
    const specialChar = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/;
    const whiteSpace = /^\s/;

    if (specialChar.test(groupName.value)) {
        return "특수문자는 사용할 수 없습니다.";
    }
    if (whiteSpace.test(groupName.value)) {
        return "공백으로 시작할 수 없습니다.";
    }
    if (groupName.value.length > 10) {
        return "최대 10자까지 입력 가능합니다.";
    }
    return "";
}

async function matchGroupByGroupCode() {
    const groupCode = document.querySelector(".group-code");

    return fetch("/api/groups/code/verify", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "code": groupCode.value
        })
    })
        .then(response => {
        if (response.status !== 200) {
            throw new Error();
        }
        return response.json();
    })
        .then(data => {
            groupData.groupId = data.groupId;
            return true;
        })
        .catch(() => {
            openNotificationModal("error", ["그룹코드가 유효하지 않습니다."], 2000);
            return false;
        });
}
