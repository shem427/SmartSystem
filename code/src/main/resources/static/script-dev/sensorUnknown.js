$(function() {
    var _self;
    $.mr.sensorUnknown = {
        _initSensorTable: function() {
            $.mr.table.create({
                selector: '#sensorUnknownTable',
                url: 'sensorUnknown/getSensors',
                sortName: 'RADIATION_MODEL_ID',
                pageSize: 5,
                pageList: [5, 10, 20, 50],
                columns: [{
                    field: 'radiationModelId',
                    title: '紫外模块ID'
                }, {
                    field: 'dataCount',
                    title: '数据件数'
                }, {
                    title: '&nbsp;',
                    formatter: function(value, row) {
                        return '<button class="btn btn-xs btn-default btnRegistSensor" id="' + row.radiationModelId + '">登录</button>';
                    }
                }],
                queryParams: function(params) {
                    return {
                        limit: params.limit,
                        offset: params.offset,
                        sortOrder: params.order,
                        sortField: params.sort
                    };
                },
                onLoadSuccess: function() {
                    _self._setTableEvt();
                }
            });
        },
        _setTableEvt: function() {
            $('.btnRegistSensor').unbind().click(function() {
                // TODO:
            });
        },
        init: function() {
            _self._initSensorTable();
        }
    };
    _self = $.mr.sensorUnknown;
});
