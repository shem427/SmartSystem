$(function() {
	var radiationData = [],
		temperatureData = [],
		humidityData = [],
		pm25Data = [];
	var initRadiationData = function(length) {
		for (var i = 0; i < length; i++) {
			radiationData.push([i, Math.random() * 70]);
			temperatureData.push([i, Math.random() * 70]);
			humidityData.push([i, Math.random() * 70]);
			pm25Data.push([i, Math.random() * 70]);
		}
	};
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
	
	initRadiationData(5);
	$.plot($('#radiation-chart'), [{
			data: radiationData,
			bars: {
				show: true,
				barWidth: 0.6,
				align: "center"
			}
		}], {
        grid: plotGrid,
        xaxis: xaxis,
        legend: {
            show: true
        },
        tooltip: true,
		tooltipOpts: {
			content: '%y%'
		}
    });
    $.plot($('#temperature-chart'), [{
			data: temperatureData,
			bars: {
				show: true,
				barWidth: 0.6,
				align: "center"
			}
		}], {
        grid: plotGrid,
        xaxis: xaxis,
        legend: {
            show: true
        },
        tooltip: true,
		tooltipOpts: {
			content: '%y%'
		}
    });
    $.plot($('#humidity-chart'), [{
			data: humidityData,
			bars: {
				show: true,
				barWidth: 0.6,
				align: "center"
			}
		}], {
        grid: plotGrid,
        xaxis: xaxis,
        legend: {
            show: true
        },
        tooltip: true,
		tooltipOpts: {
			content: '%y%'
		}
    });
    $.plot($('#pm25-chart'), [{
			data: pm25Data,
			bars: {
				show: true,
				barWidth: 0.6,
				align: "center"
			}
		}], {
        grid: plotGrid,
        xaxis: xaxis,
        legend: {
            show: true
        },
        tooltip: true,
		tooltipOpts: {
			content: '%y%'
		}
   });
});