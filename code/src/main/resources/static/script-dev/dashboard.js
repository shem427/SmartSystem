$(function() {
    var _self;
    $.mr.dashboard = {
        init: function() {
            var level = parseInt($('#unitLevel').val());
            var titleIcon = $('#titleIcon');
            var titleText = $('#titleText');
            var iconCls = $('.iconCls');

            var levelInfo = $.mr.resource.UNIT_LEVEL[level];

            $('.status-icon').tooltip();

            titleIcon.addClass('fa fa-' + levelInfo.iconCls + ' fa-fw');
            titleText.text(levelInfo.title);
            iconCls.addClass('fa fa-' + levelInfo.iconCls + ' fa-5x');

            $('.status-icon').click(function() {
                var unitId = $(this).prop('id');
                $.mr.ajax({
                    url: 'dashboard/index',
                    type: 'get',
                    data: {
                        parentId: unitId,
                        level: level ? (level + 1) : 1
                    },
                    dataType: 'html',
                    success: function(data) {
                        $('#page-wrapper').empty().append(data);
                    }
                });
            });

            $('#btnRefresh').click(function() {
                var parentId = $('#parentId').val();
                $.mr.ajax({
                    url: 'dashboard/index',
                    type: 'get',
                    data: {
                        parentId: parentId,
                        level: level
                    },
                    dataType: 'html',
                    success: function(data) {
                        $('#page-wrapper').empty().append(data);
                    }
                });
            });

            $('#btnUpLevel').click(function() {
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
                    }
                });
            });
        },
        initGraphic: function() {
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
                pageSize: 5,
                pageList: [5, 10, 20, 50],
                columns: [{
                    field: 'radNo',
                    title: 'No.'
                }, {
                    field: 'radValue',
                    title: '辐射值'
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
                yaxis: {
                    min: 0,
                    max: 110
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