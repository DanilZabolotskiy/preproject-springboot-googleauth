$(document).ready(function () {
    $.get("/admin/users", function (data, status) {
        for (i = 0; i < data.length; i++) {
            renderUsersTable(data[i]);
        }
    });
});

function renderUsersTable(user) {
    var tBody = document.getElementById("usersTableBody");
    var tr = document.createElement('tr');
    var tdId = document.createElement('td');
    tdId.innerHTML = user.id;
    var tdLogin = document.createElement('td');
    tdLogin.innerHTML = user.login;
    var tdPass = document.createElement('td');
    tdPass.innerHTML = user.password;
    var tdRoles = document.createElement('td');
    tdRoles.innerHTML = getRolesNames(user);
    tr.appendChild(tdId);
    tr.appendChild(tdLogin);
    tr.appendChild(tdPass);
    tr.appendChild(tdRoles);

    var tdInputEdit = document.createElement('td');
    var inputEdit = document.createElement('input');
    inputEdit.type = "submit";
    inputEdit.className = "btn green";
    inputEdit.value = "Edit";
    inputEdit.dataset.toggle = "modal";
    inputEdit.dataset.target = "#editUser";
    inputEdit.onclick = function () {
        fillEditWindow(user.login);
    };
    tdInputEdit.appendChild(inputEdit);
    tr.appendChild(tdInputEdit);

    var tdInputDelete = document.createElement('td');
    var inputDelete = document.createElement('input');
    inputDelete.type = "submit";
    inputDelete.className = "btn red";
    inputDelete.value = "Delete";
    inputDelete.onclick = function () {
        deleteUserById(user.id);
    };
    tdInputDelete.appendChild(inputDelete);
    tr.appendChild(tdInputDelete);

    tBody.appendChild(tr);
}

function getRolesNames(user) {
    return user.roles.map(function (role) {
        return role.roleName;
    });
}


function fillEditWindow(userLogin) {
    $.get("/admin/user",
        {login: userLogin},
        function (user) {
            document.getElementById("userIdEdit").value = user.id;
            document.getElementById("userLoginEdit").value = user.login;
            document.getElementById("userPasswordEdit").value = user.password;

            var rolesNames = new Set(getRolesNames(user));

            if (rolesNames.has("admin")) {
                document.getElementById("checkboxAdminEdit").checked = true;
            } else {
                document.getElementById("checkboxAdminEdit").checked = false;
            }
            if (rolesNames.has("user")) {
                document.getElementById("checkboxUserEdit").checked = true;
            } else {
                document.getElementById("checkboxUserEdit").checked = false;
            }
        });
}


function addUser() {
    var roleSet = new Set(getCheckedCheckboxes());
    var roleAdmin;
    var roleUser;
    if (roleSet.has("admin")) {
        roleAdmin = "admin";
    }
    if (roleSet.has("user")) {
        roleUser = "user";
    }

    $.post("/admin/user",
        {
            login: $("#userLoginAdd").val(),
            password: $("#userPasswordAdd").val(),
            roleAdmin: roleAdmin,
            roleUser: roleUser
        },
        function () {
            location.reload();
        });
}

function deleteUserById(id) {
    $.ajax({
        url: "/admin/user",
        type: "DELETE",
        data: {id: id},
        success: function () {
            location.reload();
        }
    });
}

function updateUser() {
    var roleSet = new Set(getCheckedCheckboxes());
    var roleAdmin;
    var roleUser;
    if (roleSet.has("admin")) {
        roleAdmin = "admin";
    }
    if (roleSet.has("user")) {
        roleUser = "user";
    }

    $.ajax({
        url: "/admin/user",
        type: "PUT",
        data: {
            id: $("#userIdEdit").val(),
            login: $("#userLoginEdit").val(),
            password: $("#userPasswordEdit").val(),
            roleAdmin: roleAdmin,
            roleUser: roleUser
        },
        success: function () {
            location.reload();
        }
    });
}

function getCheckedCheckboxes() {
    var selectedCheckBoxes = document.querySelectorAll('input.checkbox:checked');
    return Array.prototype.map.call(selectedCheckBoxes, function (cb) {
        return cb.value
    })
}


