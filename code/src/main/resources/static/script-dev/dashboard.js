$(function() {
    $.mr.dashboard = {
        /**
         * convert detail data to plot data.
         * @param data - detail data
         * @returns {{radiation: Array, temperature: Array, humidity: Array, pm25: Array}}
         */
        convertDisplayData: function(data) {
            var count = data.radiationDatas.length;
            var chartsData = {
                radiation: [],
                temperature: [],
                humidity: [],
                pm25: []
            };
            var i;
            for (i = 0; i < count; i++) {
                chartsData.radiation.push([i, data.radiationDatas[i]]);
                chartsData.temperature.push([i, data.temperatureDatas[i]]);
                chartsData.humidity.push([i, data.humidityDatas[i]]);
                chartsData.pm25.push([i, data.pm25Datas[i]]);
            }

            return chartsData;
        },
        /**
         * display charts function.
         * @param data
         */
        displayCharts: function(data) {
            var chartsData = $.mr.dashboard.convertDisplayData(data);
            var plotGrid = {
                borderWidth: 1,
                backgroundColor: {
                    colors: ["#fff", "#e4f4f4"]
                },
                hoverable: true
            };
            var xaxis = {
                tickFormatter: function() {
                    return "";
                }
            };
            var plotOpt = {
                grid: plotGrid,
                xaxis: xaxis,
                legend: {
                    show: true
                },
                tooltip: true,
                tooltipOpts: {
                    content: '%y%'
                }
            };
            var barOpt = {
                show: true,
                barWidth: 0.6,
                align: "center"
            };
            $.plot($('#radiation-chart'), [{
                data: chartsData.radiation,
                bars: barOpt
            }], plotOpt);
            $.plot($('#temperature-chart'), [{
                data: chartsData.temperature,
                bars: barOpt
            }], plotOpt);
            $.plot($('#humidity-chart'), [{
                data: chartsData.humidity,
                bars: barOpt
            }], plotOpt);
            $.plot($('#pm25-chart'), [{
                data: chartsData.pm25,
                bars: barOpt
            }], plotOpt);
        },
        init: function() {
            $("a.menuItem").removeClass('active');
            $('#dashboardLink').addClass('active');

            // get charts' data.
            $.mr.ajax({
                url: contextPath + 'dashboard/getData',
                type: 'get',
                dataType: 'json',
                success: function(data) {
                    $.mr.dashboard.displayCharts(data);
                }
            });
        }
    };
});