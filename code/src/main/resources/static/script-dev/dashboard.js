$(function() {
    var _self;
    $.mr.dashboard = {
        init: function() {
            var level = parseInt($('#unitLevel').val());
            var unitType = $('#unitType').val();
            var titleIcon = $('#titleIcon');
            var iconCls = $('.iconCls');
            debugger;
            var icon = _self._getIconClsByUnitType(unitType);


            $('.status-icon').tooltip();
            $('.panel-icon a').tooltip();

            titleIcon.addClass('fa fa-' + icon + ' fa-fw');
            iconCls.each(function(idx, item) {
                var subUnitType = $(item).parent().find('input[type="hidden"]').val();
                var subIcon = _self._getIconClsByUnitType(subUnitType);
                $(item).addClass('fa fa-' + subIcon + ' fa-5x');
            });

            $('.status-icon').click(function(e) {
                e.preventDefault();
                var unitId = $(this).prop('id');
                var unitName = $(this).find('div.caption > p').text();
                var unitType = $('#unitType-' + unitId).val();
                $.mr.ajax({
                    url: 'dashboard/index',
                    type: 'get',
                    data: {
                        parentId: unitId,
                        level: level ? (level + 1) : 1,
                        unitName: unitName,
                        unitType: unitType
                    },
                    dataType: 'html',
                    success: function(data) {
                        $('#page-wrapper').empty().append(data);
                        history.pushState(data, null, location.href);
                    }
                });
            });

            $('#btnRefresh').click(function(e) {
                e.preventDefault();
                var parentId = $('#parentId').val();
                var unitName = $('#unitName').val();
                var unitType = $('#unitType').val();
                $.mr.ajax({
                    url: 'dashboard/index',
                    type: 'get',
                    data: {
                        parentId: parentId,
                        level: level,
                        unitName: unitName,
                        unitType: unitType
                    },
                    dataType: 'html',
                    success: function(data) {
                        $('#page-wrapper').empty().append(data);
                        history.replaceState(data, null, location.href)
                    }
                });
            });

            $('#btnUpLevel').click(function(e) {
                e.preventDefault();
                var parentId = $('#parentId').val();
                $.mr.ajax({
                    url: 'dashboard/upIndex',
                    type: 'get',
                    data: {
                        currentId: parentId,
                        level: level ? (level - 1) : 1
                    },
                    dataType: 'html',
                    success: function(data) {
                        $('#page-wrapper').empty().append(data);
                        history.pushState(data, null, location.href);
                    }
                });
            });
            setTimeout(function() { $('#btnRefresh').trigger('click'); }, 30000);
        },
        _getIconClsByUnitType: function(unitType) {
            return $.mr.resource.UNIT_ICON_TYPE[unitType];
        },
        initGraphic: function() {
            $('.panel-icon a').tooltip();
            _self._processGraphic();
            $('.btnUpLevel').click(function(e) {
                e.preventDefault();
                var unitId = $('#unitId').val();
                var level = parseInt($('#unitLevel').val());
                $.mr.ajax({
                    url: 'dashboard/upIndex',
                    type: 'get',
                    data: {
                        currentId: unitId,
                        level: level ? (level - 1) : 1
                    },
                    dataType: 'html',
                    success: function(data) {
                        $('#page-wrapper').empty().append(data);
                        history.pushState(data, null, location.href);
                    }
                });
            });
            setTimeout(function() { _self._processGraphic(); }, 10000);

            $('#exportBtn').click(function() {
                $('#downloadForm').submit();
            });
        },
        _processGraphic: function() {
            $.mr.ajax({
                url: 'dashboard/getUnitDatas',
                type: 'get',
                dataType: 'json',
                data: {
                    unitId: $('#unitId').val()
                },
                success: function(data) {
                    var radiaitionData = data.unitDatas;
                    var lineChartData = _self._initLineChartData(radiaitionData);
                    var pieChartData = _self._initPieChartData(data);
                    _self._plot(lineChartData, pieChartData);
                }
            });
            $.mr.table.create({
                selector: '#unitDataTable',
                url: 'dashboard/getRadiationData',
                sortName: 'RAD_ID',
                sortOrder: 'desc',
                pageSize: 5,
                pageList: [5, 10, 20, 50],
                columns: [{
                    field: 'radNo',
                    title: 'No.'
                }, {
                    field: 'radValue',
                    title: '辐射值（单位：uW/cm<sup>2</sup>）'
                }, {
                    field: 'uploadTime',
                    title: '辐照时间'
                }],
                queryParams: function(params) {
                    return {
                        limit: params.limit,
                        offset: params.offset,
                        sortOrder: params.order,
                        sortField: params.sort,
                        unitId: $('#unitId').val()
                    };
                }
            });
        },
        _initLineChartData: function(data) {
            var ret = [];
            for (var i = 0; i < data.length; i++) {
                ret.push([i, data[i]])
            }

            return ret;
        },
        _initPieChartData: function(data) {
            return [{
                label: $.mr.resource.STATUS_NORMAL,
                data: data.normalCount
            }, {
                label: $.mr.resource.STATUS_WARNING,
                data: data.warningCount
            }, {
                label: $.mr.resource.STATUS_ERROR,
                data: data.errorCount
            }];
        },
        _plot: function(lineChartData, pieChartData) {
            var radiationLineChart = $('#radiationLineChart');
            var radiationPieChart = $('#radiationPieChart');
            var series = [{
                data: lineChartData,
                lines: {
                    fill: true
                }
            }];
            $.plot(radiationLineChart, series, {
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
                legend: {
                    show: true
                }
            });
            $.plot(radiationPieChart, pieChartData, {
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
    };
    _self = $.mr.dashboard;
});
