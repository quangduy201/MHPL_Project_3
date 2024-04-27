// RENDER DATE RANGE PICKER
$(document).ready(function() {
    $('#date-range').daterangepicker({
        opens: 'left',
        drops: 'down',
        autoApply: true,
        locale: {
            format: 'YYYY-MM-DD',
            separator: ' - ',
            applyLabel: 'Apply',
            cancelLabel: 'Cancel',
            fromLabel: 'From',
            toLabel: 'To',
            customRangeLabel: 'Custom',
            daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
            monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
            firstDay: 0
        }
    });
});

// RENDER CHART
$(document).ready(function() {
    function updateChart(newData) {
        console.log(newData)
        var ctx = document.getElementById("myAreaChart");
        var labels = newData.map(i => i[0]);
        var nums = newData.map(i => i[1]);

        var currentDate = moment();
        var formattedDate = currentDate.format('DD-MM-YYYY');

        if (newData.length === 0) {
            labels = [];
            nums = [];
        } else {
            if (!labels.includes(formattedDate)) {
                labels.push(formattedDate);
                nums.push(0);
            }
        }

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: "Thành viên",
                    lineTension: 0.3,
                    backgroundColor: "rgba(78, 115, 223, 0.05)",
                    borderColor: "rgba(78, 115, 223, 1)",
                    pointRadius: 3,
                    pointBackgroundColor: "rgba(78, 115, 223, 1)",
                    pointBorderColor: "rgba(78, 115, 223, 1)",
                    pointHoverRadius: 3,
                    pointHoverBackgroundColor: "rgba(78, 115, 223, 1)",
                    pointHoverBorderColor: "rgba(78, 115, 223, 1)",
                    pointHitRadius: 10,
                    pointBorderWidth: 3,
                    data: nums,
                }],
            },
            options: {
                maintainAspectRatio: false,
                layout: {
                    padding: {
                        left: 10,
                        right: 25,
                        top: 25,
                        bottom: 0
                    }
                },
                scales: {
                    xAxes: [{
                        time: {
                            unit: 'date'
                        },
                        gridLines: {
                            display: false,
                            drawBorder: false
                        },
                        ticks: {
                            maxTicksLimit: 10
                        }
                    }],
                    yAxes: [{
                        ticks: {
                            min: 0,
                            padding: 10,
                            stepSize: 1,
                            callback: function(value, index, values) {
                                return number_format(value) + " người";
                            }
                        },
                        gridLines: {
                            color: "rgb(234, 236, 244)",
                            zeroLineColor: "rgb(234, 236, 244)",
                            drawBorder: false,
                            borderDash: [2],
                            zeroLineBorderDash: [2]
                        }
                    }],
                },
                legend: {
                    display: false
                },
                tooltips: {
                    backgroundColor: "rgb(255,255,255)",
                    bodyFontColor: "#858796",
                    titleMarginBottom: 10,
                    titleFontColor: '#6e707e',
                    titleFontSize: 14,
                    borderColor: '#dddfeb',
                    borderWidth: 1,
                    xPadding: 15,
                    yPadding: 15,
                    displayColors: false,
                    intersect: false,
                    mode: 'index',
                    caretPadding: 10,
                    callbacks: {
                        label: function(tooltipItem, chart) {
                            var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                            return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + " người";
                        }
                    }
                }
            }
        });
    }

    function updateTable(newData) {
        var html = newData.map(i => `
            <tr>
                <td>${i[0]}</td>
                <td>${i[1]}</td>
                <td>${i[2]}</td>
                <td>${i[3]}</td>
                <td>${i[4]}</td>
            </tr>
        `)
        $("#dataTable tbody").html(html.join(""))
    }

    $('#dataTable').DataTable();

    var currentDate = moment();
    var thirtyDaysAgo = moment().subtract(100, 'days');

    var endDate = currentDate.format('YYYY-MM-DD');
    var startDate = thirtyDaysAgo.format('YYYY-MM-DD');

    $('#date-range').data('daterangepicker').setStartDate(startDate);

    $('#date-range').data('daterangepicker').setEndDate(endDate);


    $.post('/admin', {
        dateRange: startDate + ' - ' + endDate,
        khoa: $('#khoa').val(),
        nganh: $('#nganh').val()
    }, function(responseData) {
        // Update the chart with the new data
        updateChart(responseData[0]);
        updateTable(responseData[1]);
    });

    // Event handler for date range selection
    $('#date-range').on('apply.daterangepicker', function(ev, picker) {
        var startDate = picker.startDate.format('YYYY-MM-DD');
        var endDate = picker.endDate.format('YYYY-MM-DD');

        // AJAX request to update data
        $.post('/admin', {
            dateRange: startDate + ' - ' + endDate,
            khoa: $('#khoa').val(),
            nganh: $('#nganh').val()
        }, function(responseData) {
            // Update the chart with the new data
            updateChart(responseData[0]);
            updateTable(responseData[1]);
        });
    });
});

