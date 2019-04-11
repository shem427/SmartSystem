$(function() {
    var _self;
    $.mr.sensor = {
        // ------------------------------------- 传感器页面 开始-------------------------------------------------
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
                    field: 'radiationModelId',
                    title: '紫外模块ID'
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
            $('#exportSensorBtn').click(function() {
                $('#downloadForm').submit();
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
        },
        // ------------------------------------- 传感器页面 结束-------------------------------------------------

        // ------------------------------------- 传感器添加/编辑Modal 开始---------------------------------------
        _unshowUserModal: function() {
            $.mr.modal.destroy({
                selector: '#sensorModal'
            })
        },
        initModal: function() {
            _self._validateSensorModal();

            $('#saveSensor').click(function() {
                var sensorId = $('#sensorId').val();
                var radiationModelId = $('#radiationModelId').val();
                var sensorName = $('#sensorName').val();
                var sensorModel = $('#sensorModel').val();
                var sensorSn = $('#sensorSn').val();
                var sensorRemark = $('#sensorRemark').val();
                var unitId = $('#unitIdModal').val();

                var form = $('#sensorForm');
                var isCreate = false;
                if (!sensorId) {
                    isCreate = true;
                }

                form.bootstrapValidator('validate');
                if (!form.data('bootstrapValidator').isValid()) {
                    return;
                }

                var data = {
                    sensorName: sensorName,
                    radiationModelId: radiationModelId,
                    sensorModel: sensorModel,
                    sensorSn: sensorSn,
                    sensorRemark: sensorRemark,
                    unitId: unitId,
                    isCreate: isCreate
                };
                if (sensorId) {
                    data.sensorId = sensorId;
                }

                $.mr.ajax({
                    url: 'sensor/saveSensor',
                    type: 'post',
                    dataType: 'json',
                    data: data,
                    success: function(data) {
                        _self._unshowUserModal();
                        $.mr.table.refresh({
                            selector: '#sensorTable',
                            params: {
                                silent: true
                            }
                        });
                    }
                });
            });

            $('#selectSensor').click(function() {
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
                                        $('#unitIdModal').val(selectedUnitId);
                                        $('#unitFullPathModal').val(data.value);
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
        },
        _validateSensorModal: function() {
            var form = $('#sensorForm');
            form.bootstrapValidator({
                message: 'value is not valid',
                live: 'disabled',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    sensorName: {
                        validators: {
                            notEmpty: {
                                message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                            }
                        }
                    },
                    sensorModel: {
                        validators: {
                            notEmpty: {
                                message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                            }
                        }
                    },
                    sensorSn: {
                        validators: {
                            notEmpty: {
                                message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                            }
                        }
                    },
                    unitFullPathModal: {
                        validators: {
                            notEmpty: {
                                message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                            }
                        }
                    }
                }
            });
        }
        // ------------------------------------- 传感器添加/编辑Modal 结束---------------------------------------
    };
    _self = $.mr.sensor;
});