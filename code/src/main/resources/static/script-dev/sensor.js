$(function() {
    var _self;
    $.mr.sensor = {
        _initSensorTable: function() {
            $.mr.table.create({
                selector: '#sensorTable',
                url: 'sensor/getSensors',
                sortName: 'SENSOR_ID',
                pageSize: 5,
                pageList: [5, 10, 20, 50],
                columns: [{
                    checkbox: true
                }, {
                    field: 'sensorName',
                    title: '传感器名称'
                }, {
                    field: 'sensorModel',
                    title: '传感器型号'
                }, {
                    field: 'sensorSn',
                    title: '传感器序列号'
                }, {
                    field: 'sensorRemark',
                    title: '备注'
                }, {
                    field: 'unitFullPath',
                    title: '关联紫外线灯路径'
                }],
                queryParams: function(params) {
                    var sensorNameLike = $('#sensorNameLike').val();
                    var sensorModelLike = $('#sensorModelLike').val();
                    return {
                        limit: params.limit,
                        offset: params.offset,
                        sortOrder: params.order,
                        sortField: params.sort,
                        sensorNameLike: sensorNameLike,
                        sensorModelLike: sensorModelLike
                    };
                }
            });
        },
        _initSearchEvt: function() {
            $('#sensorSearch').click(function() {
                $.mr.table.refresh({
                    selector: '#sensorTable',
                    params: {
                        silent: true
                    }
                });
            });
        },
        _initToolbarEvt: function() {
            $('#createSensorBtn').click(function() {
                _self._showSensorModal();
            });
            $('#updateSensorBtn').click(function() {
                var selections = $.mr.table.getSelections('#sensorTable');
                var sensorId;
                if (selections.length === 0) {
                    $.mr.messageBox.alert($.mr.resource.SENSOR_NO_SELECTION);
                } else if (selections.length > 1) {
                    $.mr.messageBox.alert($.mr.resource.SENSOR_EDIT_MULTI_SELECT);
                } else {
                    sensorId = selections[0].sensorId;
                    if (sensorId) {
                        _self._showSensorModal(sensorId);
                    }
                }
            });
            $('#deleteSensorBtn').click(function() {
                var selections = $.mr.table.getSelections('#sensorTable');
                var sensorIdArray = [];
                if (selections.length === 0) {
                    $.mr.messageBox.alert($.mr.resource.SENSOR_NO_SELECTION);
                    return;
                }
                $.each(selections, function(idx, item) {
                    sensorIdArray.push(item.sensorId);
                });
                $.mr.messageBox.confirm($.mr.resource.SENSOR_DELETE_CONFIRM + sensorIdArray.length, '', {
                    yes: function() {
                        $.mr.ajax({
                            url: 'sensor/deleteSensors',
                            type: 'post',
                            data: JSON.stringify(sensorIdArray),
                            contentType: 'application/json',
                            success: function (data) {
                                $.mr.table.refresh({
                                    selector: '#sensorTable',
                                    params: {
                                        silent: true
                                    }
                                });
                                $.mr.messageBox.info($.mr.resource.SENSOR_DELETE_SUCCESS + data.number);
                            }
                        });
                    }
                });
            });
        },
        _showSensorModal: function(sensorId) {
            var data;
            if (sensorId) {
                data = {sensorId: sensorId};
            }
            $.mr.modal.create({
                url: 'sensor/sensorModal',
                data: data
            });
        },
        init: function() {
            _self._initSensorTable();
            _self._initSearchEvt();
            _self._initToolbarEvt();
        }
    };
    _self = $.mr.sensor;
});