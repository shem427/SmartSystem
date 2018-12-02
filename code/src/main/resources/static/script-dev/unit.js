$(function() {
    var self;
    $.mr.unit = {
        unitTree: null,
        // ------------------------------------- 组织管理页面 开始-------------------------------------------------
        /**
         * 组织页面的初始化
         */
        init: function() {
            // init Tree
            self._initTree();
            // init button event.
            self._initButtonEvt();
        },
        /**
         * 组织树的初始化
         * @private
         */
        _initTree: function() {
            self.unitTree = $.mr.tree.create({
                selector: '#unitTree',
                url: 'unit/subUnit',
                checkEnabled: false,
                editEnabled: false,
                selectedMulti: false,
                callback: {
                    onNodeCreated: function(event, treeId, treeNode) {
                        if (treeNode.id === 1) {
                            self.unitTree.expandNode(treeNode, true, false, true, true);
                        }
                    },
                    beforeClick: function(treeId, treeNode, clickFlag) {
                        var selectedUnitId = $('#selectedUnitId');
                        var selectedUnitName = $('#selectedUnitName');
                        var selectedUnitRemark = $('#selectedUnitRemark');
                        var selectedUnitManagers = $('#selectedUnitManagers');
                        if (clickFlag === 0) {
                            // cancel select.
                            selectedUnitId.val('');
                            selectedUnitName.val('');
                            selectedUnitRemark.text('');
                            selectedUnitManagers.empty();
                            return false;
                        }
                        selectedUnitId.val(treeNode.id);
                        selectedUnitName.val(treeNode.name);
                        selectedUnitRemark.text(treeNode.unitRemark);
                        self._getUnitManagers(treeNode.id, selectedUnitManagers);

                        return true;
                    }
                }
            });
        },
        /**
         * 设置树的各个按钮的动作事件
         * @private
         */
        _initButtonEvt: function() {
            var addUnitBtn = $('#addUnit');
            var editUnitBtn = $('#editUnit');
            var deleteUnitBtn = $('#deleteUnit');

            addUnitBtn.click(function() {
                var selectedNodes = $.mr.tree.getSelectedNode(self.unitTree);
                var selectedNode;
                if (selectedNodes && selectedNodes.length > 0) {
                    selectedNode = selectedNodes[0];
                    $.mr.modal.create({
                        url: 'unit/addPage',
                        data: {
                            parentId: selectedNode.id,
                            parentName: selectedNode.name
                        }
                    });
                } else {
                    $.mr.messageBox.alert($.mr.resource.UNIT_NO_SELECTION);
                }
            });
            editUnitBtn.click(function() {
                var selectedNodes = $.mr.tree.getSelectedNode(self.unitTree);
                var selectedNode;
                var parentNode;
                var data;
                if (selectedNodes && selectedNodes.length > 0) {
                    selectedNode = selectedNodes[0];
                    parentNode = $.mr.tree.getNodeByTId(self.unitTree, selectedNode.parentTId);
                    data = {
                        unitId: selectedNode.id,
                        unitName: selectedNode.name,
                        unitRemark: selectedNode.unitRemark
                    };
                    if (parentNode) {
                        data.parentId = parentNode.id;
                        data.parentName = parentNode.name;
                    } else {
                        data.parentId = 0;
                        data.parentName = '';
                    }
                    $.mr.modal.create({
                        url: 'unit/editPage',
                        data: data
                    });
                } else {
                    $.mr.messageBox.alert($.mr.resource.UNIT_NO_SELECTION);
                }
            });
            deleteUnitBtn.click(function() {
                var selectedNodes = $.mr.tree.getSelectedNode(self.unitTree);
                var msg;
                if (selectedNodes && selectedNodes.length > 0) {
                    msg = $.mr.resource.UNIT_DELETE_CONFIRM + selectedNodes[0].name;
                    $.mr.messageBox.confirm(msg, $.mr.resource.CONFIRM, {
                        yes: function() {
                            $.mr.ajax({
                                url: 'unit/deleteUnit',
                                type: 'post',
                                dataType: 'json',
                                data: {
                                    unitId: selectedNodes[0].id
                                },
                                success: function() {
                                    var parentNode = $.mr.tree.getNodeByTId(self.unitTree, selectedNodes[0].parentTId);
                                    $.mr.tree.refreshNode(self.unitTree, parentNode);
                                    self._clearDetail();
                                }
                            });
                        }
                    });
                } else {
                    $.mr.messageBox.alert($.mr.resource.UNIT_NO_SELECTION);
                }
            });
        },
        /**
         * 从DB中获取组织的管理者，设置到页面的List组件中
         * @param unitId 组织ID
         * @param selectedUnitManagers 页面显示管理者的List组件对象
         * @private
         */
        _getUnitManagers: function(unitId, selectedUnitManagers) {
            $.mr.ajax({
                url: 'unit/getManagers',
                type: 'get',
                dataType: 'json',
                data: {unitId: unitId},
                success: function(data) {
                    var option;
                    var user;
                    selectedUnitManagers.empty();
                    if (!data || data.length === 0) {
                        return;
                    }
                    for (var i = 0; i < data.length; i++) {
                        user = data[i];
                        option = $('<option></option>');
                        option.val(user.userId);
                        option.text(user.name + "(" + user.policeNumber + ")");
                        option.appendTo(selectedUnitManagers);
                    }
                }
            });
        },
        _clearDetail: function() {
            $('#selectedUnitId').val('');
            $('#selectedUnitName').val('');
            $('#selectedUnitRemark').val('');
            $('#selectedUnitManagers').empty();
        }
        // ------------------------------------- 组织管理页面 结束-------------------------------------------------
    };
    self = $.mr.unit;
});