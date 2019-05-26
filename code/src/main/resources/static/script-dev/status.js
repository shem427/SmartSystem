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
                        // TODO: mark click event.
                        /*qq.maps.event.addListener(marker, 'click', function() {
                            $.each(_self.hospitals, function(indx, m) {
                                if (m !== marker) {
                                    m.setMap(null);
                                } else {
                                    $.mr.mapinstance.panTo(marker.getPosition());
                                    $.mr.mapinstance.zoomTo(18);
                                    _self.initHospital(item.hospitalId);
                                }
                            });
                        });*/
                        _self.hospitals.push(marker);
                    });
                }
            });
        },
        _initUnitMegaMenu: function() {
            var unitSelectA = $('#unitSelectA');
            var unitSelectUl = $('#unitSelectUl');
            var unitSelectCancelBtn = $('#unitSelectCancelBtn');
            var unitSelectOKBtn = $('#unitSelectOKBtn');

            unitSelectA.unbind('click').click(function(e) {
                var currentUnitId = $('#currentUnitId').val();
                var unitIdArray = $('#currentUnitIdChain').val().split(",");
                var unitNameArray = $('#selectedUnit').val().split('/');
                var dropDownHeader = $('li.dropdown-header > ul');
                var li;
                unitNameArray[0] = '全系统';
                e.preventDefault();
                if (!unitSelectUl.is(':hidden')) {
                    return;
                }
                unitSelectUl.show();

                dropDownHeader.empty();
                for (var i = 0; i < unitIdArray.length; i++) {
                    if (i === unitIdArray.length - 1) {
                        li = $('<li class="selected"></li>').prop('id', unitIdArray[i]).text(unitNameArray[i]);
                    } else {
                        li = $('<li></li>').prop('id', unitIdArray[i]).html('<a href="#">' + unitNameArray[i] + '</a>');
                    }
                    dropDownHeader.append(li);
                }

                _self._initMegaHeadEvt();

                // show submenu in mega.
                _self._initMegaItemEvt(currentUnitId)
            });
            unitSelectCancelBtn.unbind('click').click(function() {
                unitSelectUl.hide();
            });
            unitSelectOKBtn.unbind('click').click(function() {
                var selectedUnitId = $('li.dropdown-header > ul > li:last').prop('id');
                $('#currentUnitId').val(selectedUnitId);
                _self.initHospital();
                unitSelectUl.hide();
            });
        },
        _initMegaItemEvt: function(unitId) {
            $.mr.ajax({
                url: 'unit/subUnitByUser',
                type: 'get',
                data: { id: unitId },
                success: function(units) {
                    var unitSelectUl = $('#unitSelectUl');
                    var lastDivider = unitSelectUl.find('li.divider:last');
                    unitSelectUl.find('li.dropdown-item').remove();
                    $.each(units, function(idx, unit) {
                        var li;
                        if (unit.isParent) {
                            li = '<li class="dropdown-item" id="' + unit.id + '"><a href="#">' + unit.name + '</a></li>';
                        } else {
                            li = '<li class="dropdown-item dropdown-content" id="' + unit.id + '">' + unit.name + '</li>';
                        }
                        lastDivider.before(li);
                    });
                    _self._initMegaMenuEvt();
                }
            });
        },
        _initMegaHeadEvt: function() {
            $('.dropdown-header > ul > li > a').unbind('click').click(function(e) {
                var unitId = $(this).parent().prop('id');
                var unitName = $(this).text();
                e.preventDefault();
                $(this).parent().addClass('selected').nextAll().remove();
                $(this).parent().empty().text(unitName);

                _self._initMegaItemEvt(unitId);
            });
        },
        _initMegaMenuEvt: function() {
            $('li.dropdown-item > a').unbind('click').click(function(e) {
                var unitId = $(this).parent().prop('id');
                var unitName = $(this).text();
                var dropDownHeader = $('li.dropdown-header > ul');
                var lastLi = dropDownHeader.find('li:last');
                var lastUnitName = lastLi.text();
                e.preventDefault();
                lastLi.removeClass('selected').text('').html('<a href="#">' + lastUnitName + '</a>');
                dropDownHeader.append($('<li class="selected"></li>').prop('id', unitId).text(unitName));

                _self._initMegaHeadEvt();

                // show submenu in mega.
                _self._initMegaItemEvt(unitId)
            });
        },
        initHospital: function() {
            var upLevel = $('#btnUpLevel');
            var pId = $('#currentUnitId').val();
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
                    $('#currentUnitId').val(status.unitId);
                    $('#currentUnitIdChain').val(status.unitIdChain);

                    $('#selectedUnit').val(status.unitPath);
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
                    _self._initUnitMegaMenu();
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
