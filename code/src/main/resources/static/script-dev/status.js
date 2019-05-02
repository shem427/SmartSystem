$(function() {
    var _self;
    $.mr.status = {
        hospitals: [],
        markHospital: function() {
            $.mr.ajax({
                url: 'status/markHospital',
                type: 'get',
                dataType: 'json',
                success: function(data) {
                    $.each(data, function(idx, item) {
                        var marker = new qq.maps.Marker({
                            position: new qq.maps.LatLng(item.lat, item.lng),
                            title: item.hospitalName,
                            draggable: true,
                            animation: qq.maps.MarkerAnimation.DOWN,
                            map: $.mr.mapinstance
                        });
                        qq.maps.event.addListener(marker, 'click', function() {
                            $.each(_self.hospitals, function(indx, m) {
                                if (m !== marker) {
                                    m.setMap(null);
                                } else {
                                    $.mr.mapinstance.panTo(marker.getPosition());
                                    $.mr.mapinstance.zoomTo(18);
                                    _self.initStatus(item.hospitalId);
                                }
                            });
                        });
                        _self.hospitals.push(marker);
                    });
                }
            });
        },
        initHospital: function(pId) {
            var upLevel = $('#btnUpLevel');
            $('.panel-icon a').tooltip();
            if (!pId || pId === 'UT00000000000000') {
                upLevel.hide();
            } else {
                upLevel.show();
            }
            $.mr.ajax({
                url: 'status/getStatus',
                type: 'get',
                dataType: 'json',
                data: {pUnitId: pId},
                success: function(status) {
                    $('#pageTitle').text('状态 - ' + status.unitName);
                    $('#normalLight').text(status.normalLight);
                    $('#abnormalLight').text(status.warningLight + status.errorLight);
                    $('#activeSensor').text(status.activeSensor);
                    $('#inactiveSensor').text(status.inactiveSensor);

                    $('#lightStatus').empty();
                    Morris.Donut({
                        element: 'lightStatus',
                        data: [
                            {value: status.normalLight, label: $.mr.resource.STATUS_NORMAL},
                            {value: status.warningLight, label: $.mr.resource.STATUS_WARNING},
                            {value: status.errorLight, label: $.mr.resource.STATUS_ERROR}
                        ]
                    });

                    $('#sensorStatus').empty();
                    Morris.Donut({
                        element: 'sensorStatus',
                        data: [
                            {value: status.activeSensor, label: $.mr.resource.ACTIVE_SENSOR},
                            {value: status.inactiveSensor, label: $.mr.resource.INACTIVE_SENSOR}
                        ]
                    });
                }
            });

            upLevel.unbind().click(function(e) {
                e.preventDefault();
                _self.initStatus(undefined);
                $.each(_self.hospitals, function(indx, m) {
                    m.setMap($.mr.mapinstance);
                });
                $.mr.mapinstance.zoomTo(4)
            });
        },
        initStatus: function() {
            // init Tree
            _self.unitTree = $.mr.tree.create({
                selector: '#unitTree',
                url: 'unit/subUnitByUser',
                checkEnabled: false,
                editEnabled: false,
                selectedMulti: false,
                callback: {
                    beforeClick: function(treeId, treeNode, clickFlag) {
                        var pieChart = $('#pieChart');
                        if (clickFlag === 0) {
                            // cancel select.
                            pieChart.empty();
                            return false;
                        }
                        $.mr.ajax({
                            url: 'status/getUnitStatus',
                            type: 'get',
                            data: { unitId: treeNode.id },
                            contentType: 'application/json',
                            success: function (data) {
                                var pieData = _self._initPieChartData(data);
                                var pieChart = $('#pieChart');
                                $.plot(pieChart, pieData, {
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
                            }
                        });
                    }
                }
            });
        },
        _initPieChartData: function(data) {
            return [{
                label: $.mr.resource.STATUS_NORMAL,
                data: data.normalRadiationData
            }, {
                label: $.mr.resource.STATUS_WARNING,
                data: data.warningRadiationData
            }, {
                label: $.mr.resource.STATUS_ERROR,
                data: data.errorRadiationData
            }];
        },
    };
    _self = $.mr.status;
});