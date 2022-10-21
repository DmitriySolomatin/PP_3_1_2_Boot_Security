document.addEventListener('DOMContentLoaded', function () {


    let is_admin = false;

    if (window.location.pathname === "/user" || window.location.pathname === "/admin") {
        document.getElementById("v-pills-profile-adult").addEventListener("click", function () {
            fetch("http://localhost:8080/api/adult", {method: "GET"})
                .then((response) => response.json())
                .then(function (response) {
                    console.log(response);
                    if (response.success === true) {
                        document.getElementById("alert_success").style.display = 'block';
                    } else {
                        document.getElementById("alert_error").style.display = 'block';
                    }

                    setTimeout(function () {   //  call a 3s setTimeout when the loop is called
                        window.location.replace(response.url);
                    }, 3000)
                });
        })

        //Получим текущего пользователя
        fetch("http://localhost:8080/api/current")
            .then((response) => response.json())
            .then(function (data) {
                let roles = '';
                data.roles.forEach(function (r) {
                    roles += r.name.replace("ROLE_", "") + ' ';
                    if (r.name === "ROLE_ADMIN") {
                        is_admin = true;
                    }
                })

                if (data.roles.some(e => e.name === 'ROLE_ADMIN')) {
                    document.getElementById("v-pills-home-tab").classList.add("active");
                    document.getElementById("v-pills-home").classList.add("active");
                } else {
                    document.getElementById("v-pills-profile-tab").classList.add("active");
                    document.getElementById("v-pills-profile").classList.add("active");
                    document.getElementById("v-pills-home-tab").remove();
                    document.getElementById("v-pills-home").remove();
                }

                document.getElementById("me_login").innerHTML = data.email;
                document.getElementById("me_roles").innerHTML = roles;

                let user_row = document.getElementsByClassName("user_row")[0];
                user_row.getElementsByClassName("user_id")[0].innerHTML = data.id;
                user_row.getElementsByClassName("user_name")[0].innerHTML = data.name;
                user_row.getElementsByClassName("user_lastName")[0].innerHTML = data.lastName;
                user_row.getElementsByClassName("user_age")[0].innerHTML = data.age;
                user_row.getElementsByClassName("user_email")[0].innerHTML = data.email;


                user_row.getElementsByClassName("user_roles")[0].innerHTML = roles;

            }).then(function () {
            if (is_admin) {
                //Сгенерируем все select-option
                fetch("http://localhost:8080/api/roles")
                    .then((response) => response.json())
                    .then(function (data) {
                        document.querySelectorAll('[name="roles[]"]').forEach(function (e) {
                            data.forEach(function (r) {
                                let option_node = document.createElement("option");
                                let option_content = document.createTextNode(r.name);
                                option_node.appendChild(option_content);
                                option_node.setAttribute("value", r.id);
                                e.appendChild(option_node);
                            })
                        })
                    })


                loadFields();

                function loadFields() {
                    //Список пользователей
                    fetch("http://localhost:8080/api/users")
                        .then((response) => response.json())
                        .then(function (data) {
                            let tab = document.getElementById("home-tab-pane");
                            let container = tab.getElementsByTagName("tbody")[0];

                            let row_template = tab.getElementsByClassName("list_row")[0].cloneNode(true);

                            container.innerHTML = '';

                            data.forEach(function (e) {
                                let new_row = row_template.cloneNode(true);
                                new_row.getElementsByClassName("user_id")[0].innerHTML = e.id;
                                new_row.getElementsByClassName("user_name")[0].innerHTML = e.name;
                                new_row.getElementsByClassName("user_lastName")[0].innerHTML = e.lastName;
                                new_row.getElementsByClassName("user_age")[0].innerHTML = e.age;
                                new_row.getElementsByClassName("user_email")[0].innerHTML = e.email;

                                let roles = '';
                                e.roles.forEach(function (r) {
                                    roles += r.name.replace("ROLE_", "") + ' ';
                                })

                                new_row.getElementsByClassName("user_roles")[0].innerHTML = roles;
                                container.appendChild(new_row);


                            })

                            return data;
                        }).then(function (data) {
                        //Подготовка формы редактирования
                        document.querySelectorAll('[data-bs-target="#user_edit"]').forEach(function (e) {
                            e.addEventListener("click", function (b) {
                                let parent_row = findAncestor(b.target, "list_row");
                                let form = document.getElementById("user_edit").getElementsByTagName("form")[0];
                                let user = data.filter(item => {
                                    return item.id == parent_row.getElementsByClassName("user_id")[0].innerHTML
                                })[0];

                                form.querySelectorAll('[name="id"]')[0].value = user.id;
                                form.querySelectorAll('[name="name"]')[0].value = user.name;
                                form.querySelectorAll('[name="lastName"]')[0].value = user.lastName;
                                form.querySelectorAll('[name="age"]')[0].value = user.age;
                                form.querySelectorAll('[name="email"]')[0].value = user.email;

                                form.querySelectorAll('option').forEach(function (e) {
                                    e.removeAttribute("selected");
                                })
                                user.roles.forEach(function (e) {
                                    form.querySelectorAll('option[value="' + e.id + '"]')[0].setAttribute("selected", "true");
                                })
                            })
                        });

                        //Подготовка формы удаления
                        document.querySelectorAll('[data-bs-target="#user_delete"]').forEach(function (e) {
                            e.addEventListener("click", function (b) {
                                let parent_row = findAncestor(b.target, "list_row");
                                let form = document.getElementById("user_delete").getElementsByTagName("form")[0];
                                let user = data.filter(item => {
                                    return item.id == parent_row.getElementsByClassName("user_id")[0].innerHTML
                                })[0];

                                form.querySelectorAll('[name="id"]')[0].value = user.id;
                                form.querySelectorAll('[name="name"]')[0].value = user.name;
                                form.querySelectorAll('[name="lastName"]')[0].value = user.lastName;
                                form.querySelectorAll('[name="age"]')[0].value = user.age;
                                form.querySelectorAll('[name="email"]')[0].value = user.email;

                                form.querySelectorAll('option').forEach(function (e) {
                                    e.removeAttribute("selected");
                                })
                                user.roles.forEach(function (e) {
                                    form.querySelectorAll('option[value="' + e.id + '"]')[0].setAttribute("selected", "true");
                                })

                                form.querySelectorAll("input, select").forEach(function (e) {
                                    e.setAttribute("disabled", "true");
                                })
                            })
                        });
                    })
                }

                //Отправка формы
                function registerEventButton(button_id, form_id, url, method) {
                    document.getElementById(button_id).addEventListener("click", function (e) {
                        let form = document.getElementById(form_id);

                        if (form_id === "user_delete_form") {
                            form.querySelectorAll("input, select").forEach(function (e) {
                                e.removeAttribute("disabled");
                            })
                        }

                        let form_data = new FormData(form);

                        fetch(url, {
                            method: method,
                            body: form_data,
                        }).then(function (response) {
                            if (form_id !== "user_add_form") {
                                let my_modal = document.getElementById(form_id.replace("_form", ""));
                                let bootstrap_modal = bootstrap.Modal.getInstance(my_modal);
                                bootstrap_modal.hide();
                            } else {
                                let my_tab = document.getElementById("home-tab");
                                let bootstrap_tab = bootstrap.Tab.getInstance(my_tab);
                                bootstrap_tab.show();
                            }

                            if (form_id === "user_delete_form") {
                                form.querySelectorAll("input, select").forEach(function (e) {
                                    e.setAttribute("disabled", "true");
                                })
                            }

                            loadFields();
                            console.log(response.status)
                        });

                    })
                }

                registerEventButton("user_edit_form_button", "user_edit_form", "http://localhost:8080/api/edit", "put");
                registerEventButton("user_delete_form_button", "user_delete_form", "http://localhost:8080/api/delete", "delete");
                registerEventButton("user_add_form_button", "user_add_form", "http://localhost:8080/api/add", "post");

            }
        });

    } else if (window.location.pathname === "/registration") {
        document.getElementById("user_registration_form_button").addEventListener("click", function (e) {
            let form = document.getElementById("user_registration_form");
            let form_data = new FormData(form);
            fetch("http://localhost:8080/api/registration", {
                method: "POST",
                body: form_data,
            }).then(function (response) {
                window.location.replace("http://localhost:8080/user");
            });
        })
    }


    function findAncestor(el, cls) {
        while ((el = el.parentElement) && !el.classList.contains(cls)) ;
        return el;
    }

}, false);