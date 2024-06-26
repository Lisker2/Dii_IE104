<!DOCTYPE html>
<html class="no-js" lang="en">

    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Personel Scheduling Helper</title>
        <meta name="description" content="Hospital Management Responsive  HTML5 Template " />
        <meta name="keywords" content="business,corporate, creative, woocommerach, design, gallery, minimal, modern, landing page, cv, designer, freelancer, html, one page, personal, portfolio, programmer, responsive, vcard, one page" />
        <meta name="author" content="Hospital Management" />

        <!-- Place favicon.ico in the root directory -->
        <link rel="apple-touch-icon" href="images/new/code1.png">
        <link rel="shortcut icon" href="assets/images/new/code1.png" type="image/x-icon">
        <!-- fonts file -->
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap" rel="stylesheet">
        <!-- css file  -->
        <link rel="stylesheet" href="assets/css/plugins.css">
        <link rel="stylesheet" href="assets/css/flaticon.css">
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/responsive.css">
        <script src="assets/js/modernizr-3.11.2.min.js"></script>
    </head>

    <body>
        <!-- pre-loder-area area start here  -->
        <div class="preloader">
            <span class="loader">
                <span class="loader-inner"></span>
            </span>
        </div>
        <!-- pre-loder-area area start here  -->
        <div class="main-wrapper">
            <div class="main-sidebar ">
                <div class="sidebar-wrap scrollbar-inner">
                    <div class="brand-area">
                        <a href="main.jsp"><img src="assets/images/Personel.png" alt="Personel" /></a>
                    </div>
                    <nav class="menu-area ">
                        <ul id="metismenu">
                            <li>
                                <a href="main.jsp"><i class="flaticon-home"></i> Dashboard</a>
                            </li>
                            <li class="">
                                <a class="has-arrow" href="#" ><i class="flaticon-patient"></i> Shift</a>
                                <ul>
                                    <li><a href="shift-types.html">Shift Types</a></li>
                                    <li><a href="contract-profile.html">Contract Profile</a></li>

                                </ul>
                            </li>
                            <li>
                                <a class="has-arrow" href="#"><i class="flaticon-doctor"></i> Employee</a>
                                <ul>
                                    <li><a href="all-employees.html">All Employees</a></li>


                                    <li class="current-menu-item"><a href="appointment.jsp">Appointment</a></li>
                                </ul>
                            </li>

                            <li>
                                <a href="load.html"><i class="flaticon-scissors"></i> Load File </a>
                            </li>

                            <li>
                                <a href="history.html"><i class="flaticon-medical"></i> History </a>
                            </li>

                        </ul>
                    </nav>
                </div>
            </div>
            <div class="main-content-wraper">
                <header class="header-area d-none d-lg-block">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="header-left">
                                <div class="search-form">
                                    <form action="#">
                                        <div class="form-group mb-0">
                                          <input type="text" class="form-control" id="search" placeholder="  " />
                                          <button type="submit" class="search-btn"><span class="flaticon-loupe"></span></button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <ul class="header-right d-flex align-items-center justify-content-end">
                                <li><a class="Patient-btn" href="/load.html">+  Load File</a></li>
                                <li class="notification"><a href="#"><span class="flaticon-bell"></span></a></li>
                                <li class="user-area"><a href="select.html"><img src="assets/images/new/code3.png" alt="user-image" /></a></li>
                            </ul>
                        </div>
                    </div>
                </header>
                <div class="mobile-header d-block d-lg-none">
                    <div class="container-fluid">
                        <div class="row align-items-center">
                            <div class="col-3">
                                <div class="toggle-bar">
                                    <a class="menu-bar" href="#"><span class="flaticon-menu"></span></a>
                                </div>
                            </div>
                            <div class="col-6">
                                <div class="page-title text-center">
                                    <h2 class="title">Welcome</h2>
                                </div>
                            </div>
                            <div class="col-3">
                                <div class="user-area text-right">
                                    <a class="user-image" href="select.html">
                                        <img src="assets/images/new/code3.png" alt="user" />
                                    </a>
                                </div>
                            </div>
                            <div class="col-lg-12">
                                <div class="mobile-search-form">
                                    <form action="#">
                                        <div class="form-group mb-0">
                                          <input type="text" class="form-control" id="mobile-search" placeholder="  " />
                                          <button type="submit" class="search-btn"><span class="flaticon-loupe"></span></button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="page-wrape">
                    <div class="container-fluid">
                        <div class="section-header">
                            <div class="row align-items-center">
                                <div class="col-md-6 col-sm-6">
                                    <div class="section-title">
                                        <h2 class="title">Appointment</h2>
                                    </div>
                                </div>
                                <div class="col-md-6 col-sm-6">
                                    <div class="right-area text-left text-sm-right mt-4 mt-sm-0">
                                        <a class="primary-btn" href="#" data-toggle="modal" data-target="#addAppointment">+  Add Appointment</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-lg-12">
                                <div class="section-wrapper">
                                    <div class="draggable-events" id="draggable-events"></div>
                                    <div id='full-calendar'></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="e-info-modal modal fade" id="e-info-modal" tabindex="-1" role="dialog" aria-hidden="true">
            <div class="modal-dialog e-info-dialog modal-dialog-centered" id="c-event" role="document">
                <div class="modal-content">
                    <div class="modal-header e-info-header ">
                        <h6 class="modal-title e-info-title">Project Update</h6>
                        <div class="e-info-action">
                            <button type="button" class="btn-icon btn-close" data-dismiss="modal" aria-label="Close">
                                <span class="fas fa-times"></span>
                            </button>
                        </div>
                    </div>
                    <div class="modal-body">
                        <ul class="e-info-list">
                            <li>
                                <span data-feather="doctor-name"></span>
                                <span class="list-line">
                                    <span class="list-label">Dr. Sara Graham</span>
                                </span>
                            </li>
                            <li>
                                <span data-feather="calendar"></span>
                                <span class="list-line">
                                    <span class="list-label">Date :</span>
                                    <span class="list-meta"> Thursday, January 23</span>
                                </span>
                            </li>
                            <li>
                                <span data-feather="clock"></span>
                                <span class="list-line">
                                    <span class="list-label">Time :</span>
                                    <span class="list-meta"> 23⋅5:00 – 6:00 am</span>
                                </span>
                            </li>
                            <li>
                                <span data-feather="align-left"></span>
                                <span class="list-line">
                                    <span class="list-text"> Lorem ipsum dolor sit amet consetetur sadipscing elitr sed diam consetetur sadipscing elitr sed diam</span>
                                </span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal fade addappointment-from" id="addAppointment" tabindex="-1" aria-labelledby="addAppointmentLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title" id="addAppointmentLabel">Add New Appointment</h5>
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                  </button>
                </div>
                <div class="modal-body">
                    <div class="appointmentinfo-wrap">
                        <div class="appointment-typy">
                            <a class="slect-btn active" href="#">Event</a>
                            <a class="slect-btn" href="#">Reminder</a>
                        </div>
                        <div class="appointment-color d-flex align-items-center">
                            <span class="color-title">Color</span>
                            <ul>
                                <li><a class="color color-one " href="#"></a></li>
                                <li><a class="color color-two active" href="#"></a></li>
                                <li><a class="color color-three" href="#"></a></li>
                                <li><a class="color color-four" href="#"></a></li>
                                <li><a class="color color-five" href="#"></a></li>
                            </ul>
                        </div>
                        <div class="primary-form">
                            <form action="#">
                                <div class="form-group">
                                    <label for="title">Title</label>
                                    <input type="text" class="form-control" id="title" name="title" placeholder="Meeting" />
                                </div>
                                <div class="form-group">
                                    <label for="doctor">Select Doctor</label>
                                    <select class="form-control wide" id="doctor">
                                        <option>Dr. Sara Graham</option>
                                        <option>Dr. Jara Graham</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="date">Date</label>
                                    <input type="date" class="form-control" id="date" name="date"/>
                                </div>
                                <div class="form-time d-flex justify-content-between">
                                    <div class="form-group w-50 mr-2">
                                        <label for="from">From</label>
                                        <input type="time" class="form-control" id="from" name="from"/>
                                    </div>
                                    <div class="form-group w-50 ml-2">
                                        <label for="to">To</label>
                                        <input type="time" class="form-control" id="to" name="to"/>
                                    </div>
                                </div>
                                <div class="button-list d-flex justify-content-between">
                                    <button type="button" class="cancel-btn w-50 mr-2" data-dismiss="modal">Cancel</button>
                                    <button type="button" class="appointment-btn w-50 ml-2">Add Appointment</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
              </div>
            </div>
        </div>

        <div class="body-overlay"></div>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                var calendarElement = document.getElementById("full-calendar");

                fetchEventDataAndPopulateCalendar(calendarElement);

                function fetchEventDataAndPopulateCalendar(calendarElement) {
                    fetch('/calenderServlet', { method: 'GET' })
                        .then(response => response.json())
                        .then(eventData => {
                            let minDate = eventData.reduce((min, event) => {
                                return event.start < min ? event.start : min;
                            }, eventData[0].start);
                            var calendar = new FullCalendar.Calendar(calendarElement, {
                                headerToolbar: { left: "today,prev,title,next", right: "timeGridDay,dayGridMonth,listMonth" },
                                views: { listMonth: { buttonText: "Schedule", titleFormat: { month: "short", weekday: "short" } } },
                                listDayFormat: !0,
                                listDayAltFormat: !0,
                                allDaySlot: !1,
                                contentHeight: 1000,
                                eventMaxHeight: 10,
                                events: eventData,
                                eventLimit: true,
                                eventLimitText:"More Events",
                                eventDidMount: function (info) {
                                    info.el.style.backgroundColor = info.event.extendedProps.color;
                                    info.el.classList.add(info.event.extendedProps.className);
                                },
                                eventDisplay: 'block',
                                initialView: 'timeGridDay',
                                initialDate: minDate,

                            });

                            calendar.render();
                        })
                        .catch(error => {
                            console.error('Error fetching event data:', error);
                        });
                }
            });
        </script>

        <script src="assets/js/jquery-1.11.2.min.js"></script>
        <script src="assets/js/popper.min.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <script src="assets/js/plugins.js"></script>
        <script src="assets/js/full-calendar-active.js"></script>
        <script src="assets/js/main.js"></script>
        
    </body>
</html>
