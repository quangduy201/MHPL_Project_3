// RENDER DATE RANGE PICKER
import {chartOptions, dateRangeOptions} from "./utils.js";

$(document).ready(function() {
    $('#member-date-range').daterangepicker(dateRangeOptions);
    $('#equipment1-date-range').daterangepicker(dateRangeOptions);
    $('#equipment2-date-range').daterangepicker(dateRangeOptions);
    $('#breach1-date-range').daterangepicker(dateRangeOptions);
    $('#breach2-date-range').daterangepicker(dateRangeOptions);
});

// RENDER MEMBER CHART
$(document).ready(function() {
    function updateChart(newData) {
        var ctx = document.getElementById("memberAreaChart");
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

        const memberChartOptions = chartOptions(
            labels,
            "Thành Viên",
            nums,
            (value, index, values) => {
                return number_format(value) + " người";
            },
            (tooltipItem, chart) => {
                var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + " người";
            }
        )

        new Chart(ctx, memberChartOptions);
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

        $("#member-dataTable tbody").html(html.join(""))
    }

    $('#member-dataTable').DataTable({
        searching: false
    });

    var currentDate = moment();
    var thirtyDaysAgo = moment().subtract(100, 'days');

    var endDate = currentDate.format('YYYY-MM-DD');
    var startDate = thirtyDaysAgo.format('YYYY-MM-DD');

    $('#member-date-range').data('daterangepicker').setStartDate(startDate);
    $('#member-date-range').data('daterangepicker').setEndDate(endDate);

    $.post('/admin/dashboard/member', {
        dateRange: startDate + ' - ' + endDate,
        khoa: $('#khoa').val(),
        nganh: $('#nganh').val()
    }, function(responseData) {
        updateChart(responseData[0]);
        updateTable(responseData[1]);
    });

    $('#member-date-range').on('apply.daterangepicker', function(ev, picker) {
        startDate = picker.startDate.format('YYYY-MM-DD');
        endDate = picker.endDate.format('YYYY-MM-DD');

        $.post('/admin/dashboard/member', {
            dateRange: startDate + ' - ' + endDate,
            khoa: $('#khoa').val(),
            nganh: $('#nganh').val()
        }, function(responseData) {
            updateChart(responseData[0]);
            updateTable(responseData[1]);
        });
    });
});

// RENDER EQUIPMENT 1 CHART
$(document).ready(function() {
    function updateChart(newData) {
        var ctx = document.getElementById("equipment1AreaChart");
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

        const equipment1ChartOptions = chartOptions(
            labels,
            "Thiết bị",
            nums,
            (value, index, values) => {
                return number_format(value) + " thiết bị";
            },
            (tooltipItem, chart) => {
                var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + " thiết bị";
            }
        )

        new Chart(ctx, equipment1ChartOptions);
    }

    function updateTable(newData) {
        var html = newData.map(i => `
            <tr>
                <td>${i[0]}</td>
                <td>${i[1]}</td>
                <td>${i[2]}</td>
                <td>${i[3]}</td>
                <td>${i[4]}</td>
                <td>${i[5]}</td>
            </tr>
        `)

        $("#equipment1-dataTable tbody").html(html.join(""))
    }

    $('#equipment1-dataTable').DataTable({
        searching: false
    });

    var currentDate = moment();
    var thirtyDaysAgo = moment().subtract(100, 'days');

    var endDate = currentDate.format('YYYY-MM-DD');
    var startDate = thirtyDaysAgo.format('YYYY-MM-DD');

    $('#equipment1-date-range').data('daterangepicker').setStartDate(startDate);
    $('#equipment1-date-range').data('daterangepicker').setEndDate(endDate);

    $.post('/admin/dashboard/equipment-1', {
        dateRange: startDate + ' - ' + endDate,
        khoa: $('#khoa').val(),
        nganh: $('#nganh').val()
    }, function(responseData) {
        updateChart(responseData[0]);
        updateTable(responseData[1]);
    });

    $('#equipment1-date-range').on('apply.daterangepicker', function(ev, picker) {
        startDate = picker.startDate.format('YYYY-MM-DD');
        endDate = picker.endDate.format('YYYY-MM-DD');

        $.post('/admin/dashboard/equipment-1', {
            dateRange: startDate + ' - ' + endDate,
            khoa: $('#khoa').val(),
            nganh: $('#nganh').val()
        }, function(responseData) {
            updateChart(responseData[0]);
            updateTable(responseData[1]);
        });
    });
});

// RENDER EQUIPMENT 2 CHART
$(document).ready(function() {
    function updateChart(newData) {
        var ctx = document.getElementById("equipment2AreaChart");
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

        const equipment2ChartOptions = chartOptions(
            labels,
            "Thiết bị",
            nums,
            (value, index, values) => {
                return number_format(value) + " thiết bị";
            },
            (tooltipItem, chart) => {
                var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + " thiết bị";
            }
        )

        new Chart(ctx, equipment2ChartOptions);
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

        $("#equipment2-dataTable tbody").html(html.join(""))
    }

    $('#equipment2-dataTable').DataTable({
        searching: false
    });

    var currentDate = moment();
    var thirtyDaysAgo = moment().subtract(100, 'days');

    var endDate = currentDate.format('YYYY-MM-DD');
    var startDate = thirtyDaysAgo.format('YYYY-MM-DD');

    $('#equipment2-date-range').data('daterangepicker').setStartDate(startDate);
    $('#equipment2-date-range').data('daterangepicker').setEndDate(endDate);

    $.post('/admin/dashboard/equipment-2', {
        dateRange: startDate + ' - ' + endDate,
        khoa: $('#khoa').val(),
        nganh: $('#nganh').val()
    }, function(responseData) {
        updateChart(responseData[0]);
        updateTable(responseData[1]);
    });

    $('#equipment2-date-range').on('apply.daterangepicker', function(ev, picker) {
        startDate = picker.startDate.format('YYYY-MM-DD');
        endDate = picker.endDate.format('YYYY-MM-DD');

        $.post('/admin/dashboard/equipment-2', {
            dateRange: startDate + ' - ' + endDate,
            khoa: $('#khoa').val(),
            nganh: $('#nganh').val()
        }, function(responseData) {
            updateChart(responseData[0]);
            updateTable(responseData[1]);
        });
    });
});

// RENDER BREACH 1 CHART
$(document).ready(function() {
    function updateChart(newData) {
        var ctx = document.getElementById("breach1AreaChart");
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

        const breach1ChartOptions = chartOptions(
            labels,
            "Vi phạm",
            nums,
            (value, index, values) => {
                return number_format(value) + " vi phạm";
            },
            (tooltipItem, chart) => {
                var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + " vi phạm";
            }
        )

        new Chart(ctx, breach1ChartOptions);
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

        $("#breach1-dataTable tbody").html(html.join(""))
    }

    $('#breach1-dataTable').DataTable({
        searching: false
    });

    var currentDate = moment();
    var thirtyDaysAgo = moment().subtract(100, 'days');

    var endDate = currentDate.format('YYYY-MM-DD');
    var startDate = thirtyDaysAgo.format('YYYY-MM-DD');

    $('#breach1-date-range').data('daterangepicker').setStartDate(startDate);
    $('#breach1-date-range').data('daterangepicker').setEndDate(endDate);

    $.post('/admin/dashboard/breach-1', {
        dateRange: startDate + ' - ' + endDate,
        khoa: $('#khoa').val(),
        nganh: $('#nganh').val()
    }, function(responseData) {
        updateChart(responseData[0]);
        updateTable(responseData[1]);
    });

    $('#breach1-date-range').on('apply.daterangepicker', function(ev, picker) {
        startDate = picker.startDate.format('YYYY-MM-DD');
        endDate = picker.endDate.format('YYYY-MM-DD');

        $.post('/admin/dashboard/breach-1', {
            dateRange: startDate + ' - ' + endDate,
            khoa: $('#khoa').val(),
            nganh: $('#nganh').val()
        }, function(responseData) {
            updateChart(responseData[0]);
            updateTable(responseData[1]);
        });
    });
});

// RENDER BREACH 2 CHART
$(document).ready(function() {
    function updateChart(newData) {
        var ctx = document.getElementById("breach2AreaChart");
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

        const breach2ChartOptions = chartOptions(
            labels,
            "Vi phạm",
            nums,
            (value, index, values) => {
                return number_format(value) + " vi phạm";
            },
            (tooltipItem, chart) => {
                var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
                return datasetLabel + ': ' + number_format(tooltipItem.yLabel) + " vi phạm";
            }
        )

        new Chart(ctx, breach2ChartOptions);
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

        $("#breach2-dataTable tbody").html(html.join(""))
    }

    $('#breach2-dataTable').DataTable({
        searching: false
    });

    var currentDate = moment();
    var thirtyDaysAgo = moment().subtract(100, 'days');

    var endDate = currentDate.format('YYYY-MM-DD');
    var startDate = thirtyDaysAgo.format('YYYY-MM-DD');

    $('#breach2-date-range').data('daterangepicker').setStartDate(startDate);
    $('#breach2-date-range').data('daterangepicker').setEndDate(endDate);

    $.post('/admin/dashboard/breach-2', {
        dateRange: startDate + ' - ' + endDate,
        khoa: $('#khoa').val(),
        nganh: $('#nganh').val()
    }, function(responseData) {
        updateChart(responseData[0]);
        updateTable(responseData[1]);
    });

    $('#breach2-date-range').on('apply.daterangepicker', function(ev, picker) {
        startDate = picker.startDate.format('YYYY-MM-DD');
        endDate = picker.endDate.format('YYYY-MM-DD');

        $.post('/admin/dashboard/breach-2', {
            dateRange: startDate + ' - ' + endDate,
            khoa: $('#khoa').val(),
            nganh: $('#nganh').val()
        }, function(responseData) {
            updateChart(responseData[0]);
            updateTable(responseData[1]);
        });
    });
});

