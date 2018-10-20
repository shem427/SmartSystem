$(function() {
	var data = [],
		temperatureData = [], 
		humidityData = [], 
		pm25Data = [];
	var initDataArray = function(length) {
		var previous, y, 
			prevTemp, yTemp, 
			prevHum, yHum, 
			prevPm25, yPm25,
			indx;
		if (data.length) {
			data = data.slice(1);
		}
		while (data.length < length) {
			indx = data.length - 1;
			// 紫外线辐射强度
			previous = data.length ? data[indx] : 80;
			y = previous + Math.random() * 10 - 5;
			data.push(y < 0 ? 0 : y > 100 ? 100 : y);

			// 温度
			prevTemp = temperatureData.length ? temperatureData[indx] : 25;
			yTemp = prevTemp + Math.random() - 0.5;
			temperatureData.push(yTemp < 0 ? 0 : yTemp > 50 ? 50 : yTemp);

			// 湿度
			prevHum = humidityData.length ? humidityData[indx] : 57;
			yHum = prevHum + Math.random() - 0.5;
			humidityData.push(yHum < 30 ? 30 : yHum > 100 ? 100 : yHum)

			// PM2.5
			prevPm25 = pm25Data.length ? pm25Data[indx] : 50;
			yPm25 = prevPm25 + Math.random() * 10 - 5;
			pm25Data.push(yPm25 < 0 ? 0 : yPm25 > 250 ? 250 : yPm25);
		}
	};
	
	var initLineChartData = function() {
		var res = [];
        for (var i = 0; i < data.length; i++) {
            res.push([i, data[i]])
        }

        return res;
	};
	initDataArray(500);
	
	var initPieChartData = function(qualified, failed) {
		var a = 0, b = 0, c = 0, v;
		for (var i = 0; i < data.length; i++) {
			v = data[i];
			if (v >= qualified) {
				a++;
			} else if (v < failed) {
				c++;
			} else {
				b++;
			}
		}
		return [
			{label:'合格',data:a},
			{label:'延长照射时间',data:b},
			{label:'不合格',data:c},
		];
	};
	
	var series = [{
		data: initLineChartData(),
		lines: {
			fill: true
		}
	}];
	
	var plotLine = $.plot($('#radiation-line-chart'), series, {
        grid: {
            borderWidth: 1,
            minBorderMargin: 20,
            labelMargin: 10,
            backgroundColor: {
                colors: ["#fff", "#e4f4f4"]
            },
            margin: {
                top: 8,
                bottom: 20,
                left: 20
            },
            markings: function(axes) {
                var markings = [];
                var xaxis = axes.xaxis;
                for (var x = Math.floor(xaxis.min); x < xaxis.max; x += xaxis.tickSize * 2) {
                    markings.push({
                        xaxis: {
                            from: x,
                            to: x + xaxis.tickSize
                        },
                        color: "rgba(232, 232, 255, 0.2)"
                    });
                }
                return markings;
            }
        },
        xaxis: {
            tickFormatter: function() {
                return "";
            }
        },
        yaxis: {
            min: 0,
            max: 110
        },
        legend: {
            show: true
        }
    });
    
    var initDataTable = function() {
    	var tbody = $('#sensor-data > tbody');
    	var tr;
    	for (var i = 0; i < data.length; i++) {
    		//tr = '<tr><td>' + (i + 1) + '</td><td>' + data[i] + '</td><td>' + temperatureData[i] + '</td><td>' + humidityData[i] + '</td><td>' + pm25Data[i] + '</td></tr>';
    		tr = '<tr><td>' + (i + 1) + '</td><td>' + data[i] + '</td></tr>';
    		$(tr).appendTo(tbody);
    	}
    };

    var pieData = initPieChartData(70, 40);
    var plotPie = $.plot($('#radiation-pie-chart'), pieData, {
		series: {
			pie: { 
				innerRadius: 0.3,
				show: true
			}
		},
		grid: {
			hoverable: true,
			clickable: true
		},
		legend: {
			show: false
		}
	});
    setInterval(function updateRandom() {
        series[0].data = initLineChartData(initDataArray(500)),
        plotLine.setData(series);
        plotLine.draw();
        
        plotPie.setData(initPieChartData(70, 40));
        plotPie.draw();
    }, 1000);
	
	initDataTable();
	$('#sensor-data').DataTable({
		responsive: true,
		searching: false,
		ordering:  false
	});
});