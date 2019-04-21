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
            $('#btnSelectUnit').click(function() {
                $.mr.modal.create({
                    url: 'unit/unitSelectModal',
                    afterDisplaying: function(dialog) {
                        var selectUnitBtn = $('#selectUnit', dialog);

                        _self._initUnitModalTree('#unitModalTree', selectUnitBtn);

                        selectUnitBtn.prop('disabled', true);
                        selectUnitBtn.click(function() {
                            var selectedUnitId = $('#selectedUnitId').val();
                            $.mr.ajax({
                                url: 'unit/getUnitFullPath',
                                type: 'get',
                                dataType: 'json',
                                data: {unitId: selectedUnitId},
                                async: false,
                                success: function(data) {
                                    $('#parentUnitFullPath').val(data.value);
                                    $.mr.modal.destroy({
                                        selector: '#unitSelectModal'
                                    });
                                }
                            });
                        });
                    }
                });
            });
        },
        _initUnitModalTree: function(treeSelector, selectBtn) {
            _self._unitModalTree = $.mr.tree.create({
                selector: treeSelector,
                url: 'unit/subUnit',
                checkEnabled: false,
                editEnabled: false,
                selectedMulti: false,
                callback: {
                    onNodeCreated: function(event, treeId, treeNode) {
                        if (treeNode.id === 'UT00000000000000') {
                            _self._unitModalTree.expandNode(treeNode, true, false, true, true);
                        }
                    },
                    beforeClick: function(treeId, treeNode, clickFlag) {
                        var selectedUnitId = $('#selectedUnitId');
                        if (clickFlag === 0) {
                            // cancel select.
                            selectBtn.prop('disabled', true);
                            selectedUnitId.val('');
                            return false;
                        } else {
                            selectedUnitId.val(treeNode.id);
                            selectBtn.prop('disabled', false);
                        }
                    }
                }
            });
        }
    };
    _self = $.mr.status;
});