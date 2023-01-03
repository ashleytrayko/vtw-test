function join() {
    const answer = confirm("회원가입 하시겠습니까?");
    const username = $("#username").val();
    const password = $("#password").val();
    if(username === null || username === "" || password === null || password === ""){
        alert("정보를 입력해주세요");
        return false;
    }
    if (answer === true) {
        let data = {
            username: username,
            password: password
        };

        $.ajax({
            type: "POST",
            url: "/join",
            data: JSON.stringify(data),
            contentType: "application/json; charset-utf-8",
        }).done(function (response){
            if(response === "success"){
                location.href="/";
            }else{
                alert("회원가입 실패!");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error))
        })
    }
}

function writeBoard() {
    const answer = confirm("작성 하시겠습니까?");
    const subject = $("#subject").val();
    const contents = $("#contents").val();
    if(subject === null || subject === "" || contents === null || contents === ""){
        alert("정보를 입력해주세요");
        return false;
    }
    if (answer === true) {
        let data = {
            subject: subject,
            contents: contents
        };

        $.ajax({
            type: "POST",
            url: "/write",
            data: JSON.stringify(data),
            contentType: "application/json; charset-utf-8",
        }).done(function (response){
            if(response === "success"){
                location.href="/";
            }else{
                alert("글 쓰기 실패..");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error))
        })
    }
}

function deleteBoard(boardNo) {
    const answer = confirm("삭제 하시겠습니까?");
    if(answer === true){
        $.ajax({
            type: "GET",
            url: "/delete/"+boardNo,
            contentType: "application/json; charset-utf-8",
        }).done(function (response){
            if(response === "success"){
                alert("삭제성공");
                location.href="/";
            }else{
                alert("글 쓰기 실패..");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error))
        })
    }
}

function updateBoard(boardNo) {
    const answer = confirm("수정 하시겠습니까?");
    const subject = $("#subject").val();
    const contents = $("#contents").val();
    if(subject === null || subject === "" || contents === null || contents === ""){
        alert("정보를 입력해주세요");
        return false;
    }
    if (answer === true) {
        let data = {
            subject: subject,
            contents: contents
        };


        $.ajax({
            type: "POST",
            url: "/update/"+boardNo,
            data: JSON.stringify(data),
            contentType: "application/json; charset-utf-8",
        }).done(function (response){
            if(response === "success"){
                location.href="/";
            }else{
                alert("글 수정 실패..");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error))
        })
    }
}
function resign() {
    const answer = confirm("탈퇴 하시겠습니까?");
    if(answer === true){
        $.ajax({
            type: "GET",
            url: "/resign",
        }).done(function (response) {
            alert("탈퇴완료");
            location.href = "/logout";
        }).fail(function (error){
            alert("탈퇴실패");
        })
    }
}

function updateUser(userId) {
    const answer = confirm("수정 하시겠습니까?");
    const username = $("#username").val();
    const password = $("#password").val();
    if(username === null || username === "" || password === null || password === ""){
        alert("정보를 입력해주세요");
        return false;
    }
    if (answer === true) {
        let data = {
            userId: userId,
            username: username,
            password: password
        };

        $.ajax({
            type: "POST",
            url: "/updateUser",
            data: JSON.stringify(data),
            contentType: "application/json; charset-utf-8",
        }).done(function (response){
            if(response === "success"){
                location.href="/";
            }else{
                alert("글 수정 실패..");
            }
        }).fail(function (error) {
            alert(JSON.stringify(error))
        })
    }
}
