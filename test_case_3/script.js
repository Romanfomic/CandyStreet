$(document).ready(function () {
    $(document).ready(function () {
        // Значения по умолчанию
        var DateBegin = new Date();
        var DateEnd = new Date();
        DateEnd.setHours(23, 59, 59, 999);
        DateBegin.setHours(0, 0, 0, 0);
        var fromTime = DateBegin.getTime();
        var toTime = DateEnd.getTime();
        fromTime = 1637366400000;
        toTime = 1637625600000;

        var onlyUncompleteTasks = false;
        var sorting = false;

        // Функция форматирования даты
        function formatDateTime(inputDateTime) {
            // Создаем объект Date из строки
            const dateObj = new Date(inputDateTime);
        
            // Получаем компоненты даты и времени
            const day = dateObj.getDate();
            const month = dateObj.getMonth() + 1; 
            const year = dateObj.getFullYear();
            const hours = dateObj.getHours();
            const minutes = dateObj.getMinutes();
        
            // Добавляем ведущий ноль, если компонент меньше 10
            const formattedDay = day < 10 ? '0' + day : day;
            const formattedMonth = month < 10 ? '0' + month : month;
        
            // Форматируем строку даты и времени
            const formattedDateTime = `${formattedDay}.${formattedMonth}.${year} ${hours}:${minutes}`;
        
            return formattedDateTime;
        }

        // Список задч
        var tasks = [];

        // Функция для запроса данных для списка
        function populateTaskList(fromTime, toTime, status) {
            fetch(`https://todo.doczilla.pro/api/todos/date?from=${fromTime}&to=${toTime}${!status ? "&status=" + status : ""}`)
            .then(response => response.json())
            .then(fetchedTasks => {
                tasks = fetchedTasks; // Обновление списка
                outpuTaskList(fetchedTasks);
            });
        }

        // Функция вывода списка
        function outpuTaskList(tasks) {
            const taskListElement = document.getElementById('taskList');
            taskListElement.innerHTML = ''; // Очистка списка

            $.each(tasks, function (index, task) {
                $("#taskList").append("<li><a class='taskLink' data-taskindex='" + index + "' href='#'>" + task.name + "</a></li>");
            });
        }

        // Функция вывода окна задачи
        function showTaskDetails(index, selectedTasks = tasks) {
            $(".dropdown-list").remove();
            var task = selectedTasks[index];
            $("#taskDetails").html("<h3>" + task.name + "</h3><br>" + task.fullDesc + "<br><br>" + formatDateTime(task.date) + "<br>Completed: " + task.status);
            $("#taskModal").dialog({
                modal: true,
                title: "Task Details",
                buttons: {
                    Ok: function () {
                        $(this).dialog("close");
                    }
                }
            });
        }

        // Инициализация выбора даты
        $("#datepicker").datepicker();
        $("#datepicker").on("change", function () {
            var selectedDateStr = $(this).val();
            DateBegin = new Date(selectedDateStr);
            DateEnd = new Date(selectedDateStr);
            DateBegin.setHours(0, 0, 0, 0);
            DateEnd.setHours(23, 59, 59, 999);
            fromTime = DateBegin.getTime();
            toTime = DateEnd.getTime();

            populateTaskList(fromTime, toTime, !onlyUncompleteTasks);
        });

        $("#start-date, #end-date").datepicker({
            onSelect: function (selectedDate) {
              if (this.id === "start-date") {
                DateBegin = new Date(selectedDate);
                fromTime = DateBegin.getTime();
              } else {
                DateEnd = new Date(selectedDate);
                toTime = DateEnd.getTime();
              }
      
              populateTaskList(fromTime, toTime, !onlyUncompleteTasks);
            }
        });

        // Обработка нажатия клавиши в поле ввода
        $("#taskSearch").on("keyup", function (event) {
            // Проверка, была ли нажата клавиша Enter (код 13)
            if (event.keyCode === 13) {
                // Вызываем функцию для добавления выпадающего списка
                addDropdownList();
            }
        });

        $("#taskSearch").on("input", function () {
            // Проверка, если поле ввода пустое, скрыть выпадающий список
            if ($(this).val().trim() === '') {
                $(".dropdown-list").remove();
            }
        });

        // Функция для добавления выпадающего списка
        function addDropdownList() {
            var searchTerm = $("#taskSearch").val().toLowerCase();
            if(searchTerm == "") {
                $(".dropdown-list").remove();
                return;
            }

            // Фильтрация задач, содержащих введенную строку
            fetch(`https://todo.doczilla.pro/api/todos/find?q=${searchTerm}&limit=5`)
            .then(response => response.json())
            .then(matchingTasks => {
                // Создание выпадающего списка
                var dropdownList = $("<ul>").addClass("dropdown-list");

                // Добавление элементов списка
                matchingTasks.forEach(task => {
                    var listItem = $("<li>").text(task.name);
                    listItem.on("click", function () {
                        // Обработка выбора задачи                        
                        showTaskDetails(matchingTasks.indexOf(task), matchingTasks);
                    });
                    dropdownList.append(listItem);
                });

                // Вставка выпадающего списка после поля ввода
                $("#taskSearch").after(dropdownList);
            });
        }

        // Обработка нажатия кнопки вывода задач за сегодня
        $("#todayTasks").click(function () {
            var currentDateBegin = new Date();
            var currentDateEnd = new Date();
            currentDateEnd.setHours(23, 59, 59, 999);
            currentDateBegin.setHours(0, 0, 0, 0);
            fromTime = currentDateBegin.getTime();
            toTime = currentDateEnd.getTime();
            populateTaskList(fromTime, toTime, !onlyUncompleteTasks);
        });

        // Обработка нажатия кнопки вывода задач за неделю
        $("#weekTasks").click(function () {
            var today = new Date();
            var weekStart = new Date(today.getFullYear(), today.getMonth(), today.getDate() - today.getDay());
            var weekEnd = new Date(today.getFullYear(), today.getMonth(), today.getDate() - today.getDay() + 6);
            fromTime = weekStart.getTime();
            toTime = weekEnd.getTime();
            populateTaskList(fromTime, toTime, !onlyUncompleteTasks);
        });

        // Обработка сортировки
        $("#sortTasks").change(function () {
            var sortOrder = $(this).val() === "asc" ? 1 : -1;
            tasks.sort(function (a, b) {
                return sortOrder * (new Date(a.date) - new Date(b.date));
            });
            outpuTaskList(tasks);
        });

        // Обработка вывода только незавершенных задач
        $("#uncompletedTasks").change(function () {
            onlyUncompleteTasks = !onlyUncompleteTasks;
            populateTaskList(fromTime, toTime, !onlyUncompleteTasks);
        });

        // Обработка кликов по ссылкам на задачи
        $("#taskList").on("click", ".taskLink", function () {
            var taskIndex = $(this).data("taskindex");
            showTaskDetails(taskIndex);
        });

        // Первоначальное заполнение списка задач
        populateTaskList(fromTime, toTime, !onlyUncompleteTasks);
    });
});