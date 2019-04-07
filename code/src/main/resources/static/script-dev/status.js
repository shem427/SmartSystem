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
        }
    };
    _self = $.mr.status;
});