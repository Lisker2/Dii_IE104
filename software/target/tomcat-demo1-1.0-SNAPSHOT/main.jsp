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
        <link rel="apple-touch-icon" href="#">
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
                        <a href="main.jsp"><img src="assets/images/Personel.png" alt="medico" /></a>
                    </div>
                    <nav class="menu-area ">
                        <ul id="metismenu">
                            <li class="current-menu-item">
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


                                    <li><a href="appointment.jsp">Appointment</a></li>
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
                                <li><a class="Patient-btn" href="load.html">+ Load File</a></li>
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
                    <div class="welcome-area">
                        <div class="container-fluid">
                            <div class="section-header">
                                <div class="section-title">
                                    <h2 class="title">Welcome</h2>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-lg-12">
                                    <div class="welcome-slide">
                                        <div class="single-welcome">
                                            <div class="media align-items-center">
                                                <div class="welcome-icon bg-one">
                                                    <img src="assets/images/new/code6.png" alt="Patient" />
                                                </div>
                                                <div class="media-body">
                                                  <p class="m-0">Total Shift</p>
                                                  <h3 class="counter" id = "total_shift">0</h3>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="single-welcome">
                                            <div class="media align-items-center">
                                                <div class="welcome-icon bg-two">
                                                    <img src="assets/images/new/code5.png" alt="Patient" />
                                                </div>
                                                <div class="media-body">
                                                  <p class="m-0">Our Employees</p>
                                                  <h3 class="counter" id="employees">0</h3>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="single-welcome">
                                            <div class="media align-items-center">
                                                <div class="welcome-icon bg-three">
                                                    <img src="assets/images/new/code7.png" alt="Patient" />
                                                </div>
                                                <div class="media-body">
                                                  <p class="m-0">Shift Types</p>
                                                  <h3 class="counter" id="shift_types" >0</h3>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="single-welcome">
                                            <div class="media align-items-center">
                                                <div class="welcome-icon bg-four">
                                                    <img src="assets/images/new/code4.png" alt="Patient" />
                                                </div>
                                                <div class="media-body">
                                                  <p class="m-0">Contracts</p>
                                                  <h3 class="counter" id="contract">0</h3>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="body-overlay"></div>
        

        <script src="assets/js/jquery-1.11.2.min.js"></script>
        <script src="assets/js/popper.min.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <script src="assets/js/plugins.js"></script>
        <script src="assets/js/chart-active.js"></script>
        <script src="assets/js/main.js"></script>
        <script>
            function updateStatistics() {
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function() {
                    if (xhr.readyState === XMLHttpRequest.DONE) {
                        if (xhr.status === 200) {
                            var statistics = JSON.parse(xhr.responseText);
                            // Update the counters with the fetched data
                            document.querySelector("#contract").textContent = statistics.contract;
                            document.querySelector("#employees").textContent = statistics.employees;
                            document.querySelector("#shift_types").textContent = statistics.shiftTypes;
                            document.querySelector("#total_shift").textContent = statistics.totalShift;
                        }
                    }
                };
                xhr.open("GET", "/statisticsServlet", true);
                xhr.send();
            }
            // Call the updateStatistics function initially and then set an interval to update periodically
            updateStatistics();
            setInterval(updateStatistics, 1000);
        </script>
        
    </body>
</html>
