$(document).ready(function () {
    $.get("/get_auth_user", function (data) {
       document.getElementById("userLogin").innerText = data.login;
    });
});