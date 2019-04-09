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
        initStatus: function(pId) {
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
                    $('#pageTitle').text('状态统计 - ' + status.unitName);
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
        }
    };
    _self = $.mr.status;
});