$(function() {
    var _self;
    $.mr.status = {
        markHospital: function() {
            $.mr.ajax({
                url: 'status/markHospital',
                type: 'get',
                dataType: 'json',
                success: function(data) {
                    $.each(data, function(idx, item) {
                        new qq.maps.Marker({
                            position: new qq.maps.LatLng(item.lat, item.lng),
                            title: item.hospitalName,
                            draggable: true,
                            animation: qq.maps.MarkerAnimation.DROP,
                            map: $.mr.mapinstance
                        });
                    });
                }
            });
        },
        initStatus: function(pId) {
            $.mr.ajax({
                url: 'status/getStatus',
                type: 'get',
                dataType: 'json',
                data: pId,
                success: function(status) {
                    $('#normalLight').text(status.normalLight);
                    $('#abnormalLight').text(status.warningLight + status.errorLight);
                    $('#activeSensor').text(status.activeSensor);
                    $('#inactiveSensor').text(status.inactiveSensor);

                    Morris.Donut({
                        element: 'lightStatus',
                        data: [
                            {value: status.normalLight, label: $.mr.resource.STATUS_NORMAL},
                            {value: status.warningLight, label: $.mr.resource.STATUS_WARNING},
                            {value: status.errorLight, label: $.mr.resource.STATUS_ERROR}
                        ]
                    });

                    Morris.Donut({
                        element: 'sensorStatus',
                        data: [
                            {value: status.activeSensor, label: $.mr.resource.ACTIVE_SENSOR},
                            {value: status.inactiveSensor, label: $.mr.resource.INACTIVE_SENSOR}
                        ]
                    });
                }
            });
        }
    };
    _self = $.mr.status;
});