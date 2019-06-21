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
                    title: '操作',
                    formatter: function(value, row) {
                        return '<div style="width:100%;text-align:center;"><button class="btn btn-xs btn-default btnRegistSensor" id="' + row.radiationModelId + '">登录</button></div>';
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
                var radiationModelId = $(this).prop('id');
                $.mr.modal.create({
                    url: 'sensorUnknown/sensorModal',
                    data: {
                        radiationModelId: radiationModelId
                    }
                });
            });
        },
        init: function() {
            _self._initSensorTable();
        },
        initModal: function() {
            _self._validateSensorModal();

            $('#saveSensor').click(function() {
                var radiationModelId = $('#radiationModelId').val();
                var sensorName = $('#sensorName').val();
                var sensorModel = $('#sensorModel').val();
                var sensorSn = $('#sensorSn').val();
                var sensorRemark = $('#sensorRemark').val();
                var unitId = $('#unitIdModal').val();

                var form = $('#sensorForm');

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
                    isCreate: true
                };

                $.mr.ajax({
                    url: 'sensor/saveSensor',
                    type: 'post',
                    dataType: 'json',
                    data: data,
                    success: function(data) {
                        _self._unshowUserModal();
                        _self._moveDataToRadition(radiationModelId);
                        $.mr.table.refresh({
                            selector: '#sensorUnknownTable',
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
        _moveDataToRadition: function(radiationModelId) {
            debugger;
            $.mr.ajax({
                url: 'sensorUnknown/moveData',
                type: 'post',
                dataType: 'json',
                data: { radiationModelId: radiationModelId }
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
        _unshowUserModal: function() {
            $.mr.modal.destroy({
                selector: '#sensorModal'
            })
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
    };
    _self = $.mr.sensorUnknown;
});
