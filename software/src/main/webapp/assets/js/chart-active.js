(function($) {
     
     
     /* ----------------------
     PatientHistoryChart
     -------------------------- */
     var ctx = document.getElementById('PatientHistoryChart').getContext("2d");
     var PatientHistoryChart = new Chart(ctx, {
     type: 'line',
     data: {
          labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "July","Aug","Sep","Oct","Nov","Dec"],
          datasets: [{
               label: "New Patient",
               borderColor: "#FF55BF",
               pointBorderColor: "transparent",
               pointBackgroundColor: "transparent",
               pointHoverBackgroundColor: "#ffffff",
               pointHoverBorderColor: "#FF55BF",
               pointBorderWidth: 4,
               pointRadius: 5,
               pointHoverRadius: 5,
               pointHoverBorderWidth: 4,
               fill: false,
               borderWidth: 4,
               data: [1000, 1400, 1000, 1900, 1200, 1300, 1000, 1600, 1000, 1700, 1000, 600]
          },
          {
               label: "Old Patient",
               borderColor: "#605BFF",
               pointBorderColor: "transparent",
               pointBackgroundColor: "transparent",
               pointHoverBackgroundColor: "#ffffff",
               pointHoverBorderColor: "#605BFF",
               pointBorderWidth: 4,
               pointRadius: 5,
               pointHoverRadius: 5,
               pointHoverBorderWidth: 4,
               fill: false,
               borderWidth: 4,
               data: [500, 800, 300, 800, 600, 800, 600, 500, 900, 600, 700, 200]
          }]
     },
     options: {
          legend: {
               display: false,
               position: "top",
               align: "center",
          },
          scales: {
               yAxes: [{
                    ticks: {
                         fontFamily: "Inter', sans-serif",
                         fontColor: '#C2C2DD',
                         fontSize: 15,
                         beginAtZero:true,
                         padding: 20,
                         max: 2000,
                         min: 0,
                         stepSize: 500,
                    },
                    gridLines: {
                         zeroLineWidth: 1,
                         zeroLineColor: "#F0F0F5",
                         drawTicks: false,
                         display: true,
                         color: "#F0F0F5",
                         borderDash: [5],
                         lineWidth: 1,
                    }
     
               }],
               xAxes: [{
                    ticks: {
                         fontFamily: "Inter', sans-serif",
                         beginAtZero:true,
                         fontColor: '#C2C2DD',
                         fontSize: 15,
                         padding: 20
                         },
                    gridLines: {
                         
                         drawTicks: false,
                         zeroLineColor: "transparent",
                         display: false,
                         lineWidth: 0,
                    },
               }]
          }
     }
     });

     /* ----------------------
     DiseasesChart
     -------------------------- */

     /* ----------------------
     TotalPatient
     -------------------------- */
     var ctx = document.getElementById('TotalPatient').getContext("2d");
     var TotalPatient = new Chart(ctx, {
     type: 'bar',
     data: {
          labels: ["Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri"],
          datasets: [{
               barPercentage: .6,
               backgroundColor: "#6B66FF",
               categoryPercentage: .4,
               data: [160, 220, 190, 180, 200, 160, 220]
          },
          {
               barPercentage: .6,
               categoryPercentage: .4,
               backgroundColor: "#FF8F6B",
               data: [180, 150, 160, 230, 170, 190, 150]
          }]
     },
     options: {
          legend: {
               display: false,
               position: "top",
               align: "center",
          },
          scales: {
               yAxes: [{
                    ticks: {
                         fontFamily: "Inter', sans-serif",
                         fontColor: '#C2C2DD',
                         fontSize: 15,
                         beginAtZero:true,
                         padding: 20,
                         max: 250,
                         min: 0,
                         stepSize: 50,
                    },
                    gridLines: {
                         zeroLineWidth: 1,
                         zeroLineColor: "#F0F0F5",
                         drawTicks: false,
                         display: true,
                         color: "#F0F0F5",
                         borderDash: [5],
                         lineWidth: 1,
                    }
     
               }],
               xAxes: [{
                    ticks: {
                         fontFamily: "Inter', sans-serif",
                         beginAtZero:true,
                         fontColor: '#C2C2DD',
                         fontSize: 15,
                         fontStyle: "normal",
                         padding: 20
                    },
                    gridLines: {
                         
                         drawTicks: false,
                         zeroLineColor: "transparent",
                         display: false,
                         lineWidth: 0,
                    },
               }]
          }
     }
     });


     /* ----------------------
     revenueChart
     -------------------------- */
     var ctx = document.getElementById('revenueChart').getContext("2d");
     var revenueChart = new Chart(ctx, {
     type: 'line',
     data: {
          labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "July","Aug","Sep","Oct","Nov","Dec"],
          datasets: [{
               label: "Income",
               borderColor: "#FF55BF",
               pointBorderColor: "transparent",
               pointBackgroundColor: "transparent",
               pointHoverBackgroundColor: "#ffffff",
               pointHoverBorderColor: "#FF55BF",
               pointBorderWidth: 4,
               pointRadius: 5,
               pointHoverRadius: 5,
               pointHoverBorderWidth: 4,
               fill: false,
               borderWidth: 4,
               data: [1000, 1400, 1000, 1900, 1200, 1300, 1000, 1600, 1000, 1700, 1000, 600]
          },
          {
               label: "Expense",
               borderColor: "#605BFF",
               pointBorderColor: "transparent",
               pointBackgroundColor: "transparent",
               pointHoverBackgroundColor: "#ffffff",
               pointHoverBorderColor: "#605BFF",
               pointBorderWidth: 4,
               pointRadius: 5,
               pointHoverRadius: 5,
               pointHoverBorderWidth: 4,
               fill: false,
               borderWidth: 4,
               data: [500, 800, 300, 800, 600, 800, 600, 500, 900, 600, 700, 200]
          }]
     },
     options: {
          legend: {
               display: false,
               position: "top",
               align: "center",
          },
          scales: {
               yAxes: [{
                    ticks: {
                         display: false,
                         fontFamily: "Inter', sans-serif",
                         fontColor: '#C2C2DD',
                         fontSize: 15,
                         beginAtZero:true,
                    },
                    gridLines: {
                         zeroLineBorderDash: [5],
                         zeroLineWidth: 1,
                         zeroLineColor: "#F0F0F5",
                         drawTicks: false,
                         display: true,
                         color: "#F0F0F5",
                         borderDash: [5],
                         lineWidth: 1,
                    }
     
               }],
               xAxes: [{
                    ticks: {
                         fontFamily: "Inter', sans-serif",
                         beginAtZero:true,
                         fontColor: '#C2C2DD',
                         fontSize: 15,
                         padding: 20
                    },
                    gridLines: {
                         zeroLineWidth: 1,
                         zeroLineBorderDash: [5],
                         zeroLineColor: "#F0F0F5",
                         drawTicks: false,
                         display: true,
                         color: "#F0F0F5",
                         borderDash: [5],
                         lineWidth: 1,
                    },
               }]
          },
          tooltips: {
               labelColor:"red",
               callbacks: {
                   label: function(tooltipItem) {
                       return "$" + Number(tooltipItem.yLabel);
                   }
               }
          },
     }
     });


    





     
      
               
})(jQuery);