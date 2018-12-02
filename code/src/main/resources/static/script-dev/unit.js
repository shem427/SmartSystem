$(function() {
    var self;
    $.mr.unit = {
        deptTree: null,
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
            self.deptTree = $.mr.tree.create({
                selector: '#deptTree',
                url: 'unit/subUnit',
                checkEnabled: false,
                editEnabled: false,
                selectedMulti: false,
                callback: {
                    onNodeCreated: function(event, treeId, treeNode) {
                        if (treeNode.id === 1) {
                            self.deptTree.expandNode(treeNode, true, false, true, true);
                        }
                    },
                    beforeClick: function(treeId, treeNode, clickFlag) {
                        var selectedDeptId = $('#selectedDeptId');
                        var selectedDeptName = $('#selectedDeptName');
                        var selectedDeptRemark = $('#selectedDeptRemark');
                        var selectedDeptManagers = $('#selectedDeptManagers');
                        if (clickFlag === 0) {
                            // cancel select.
                            selectedDeptId.val('');
                            selectedDeptName.val('');
                            selectedDeptRemark.text('');
                            selectedDeptManagers.empty();
                            return false;
                        }
                        selectedDeptId.val(treeNode.id);
                        selectedDeptName.val(treeNode.name);
                        selectedDeptRemark.text(treeNode.deptRemark);
                        self._getDeptManagers(treeNode.id, selectedDeptManagers);

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
            var addDeptBtn = $('#addDept');
            var editDeptBtn = $('#editDept');
            var deleteDeptBtn = $('#deleteDept');

            addDeptBtn.click(function() {
                var selectedNodes = $.mr.tree.getSelectedNode(self.deptTree);
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
                    $.mr.messageBox.alert($.mr.resource.DEPT_NO_SELECTION);
                }
            });
            editDeptBtn.click(function() {
                var selectedNodes = $.mr.tree.getSelectedNode(self.deptTree);
                var selectedNode;
                var parentNode;
                var data;
                if (selectedNodes && selectedNodes.length > 0) {
                    selectedNode = selectedNodes[0];
                    parentNode = $.mr.tree.getNodeByTId(self.deptTree, selectedNode.parentTId);
                    data = {
                        deptId: selectedNode.id,
                        deptName: selectedNode.name,
                        deptRemark: selectedNode.deptRemark
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
                    $.mr.messageBox.alert($.mr.resource.DEPT_NO_SELECTION);
                }
            });
            deleteDeptBtn.click(function() {
                var selectedNodes = $.mr.tree.getSelectedNode(self.deptTree);
                var msg;
                if (selectedNodes && selectedNodes.length > 0) {
                    msg = $.mr.resource.DEPT_DELETE_CONFIRM + selectedNodes[0].name;
                    $.mr.messageBox.confirm(msg, $.mr.resource.CONFIRM, {
                        yes: function() {
                            $.mr.ajax({
                                url: 'unit/deleteUnit',
                                type: 'post',
                                dataType: 'json',
                                data: {
                                    deptId: selectedNodes[0].id
                                },
                                success: function() {
                                    var parentNode = $.mr.tree.getNodeByTId(self.deptTree, selectedNodes[0].parentTId);
                                    $.mr.tree.refreshNode(self.deptTree, parentNode);
                                    self._clearDetail();
                                }
                            });
                        }
                    });
                } else {
                    $.mr.messageBox.alert($.mr.resource.DEPT_NO_SELECTION);
                }
            });
        },
        /**
         * 从DB中获取组织的管理者，设置到页面的List组件中
         * @param deptId 组织ID
         * @param selectedDeptManagers 页面显示管理者的List组件对象
         * @private
         */
        _getDeptManagers: function(deptId, selectedDeptManagers) {
            $.mr.ajax({
                url: 'unit/getManagers',
                type: 'get',
                dataType: 'json',
                data: {deptId: deptId},
                success: function(data) {
                    var option;
                    var user;
                    selectedDeptManagers.empty();
                    if (!data || data.length === 0) {
                        return;
                    }
                    for (var i = 0; i < data.length; i++) {
                        user = data[i];
                        option = $('<option></option>');
                        option.val(user.userId);
                        option.text(user.name + "(" + user.policeNumber + ")");
                        option.appendTo(selectedDeptManagers);
                    }
                }
            });
        },
        _clearDetail: function() {
            $('#selectedDeptId').val('');
            $('#selectedDeptName').val('');
            $('#selectedDeptRemark').val('');
            $('#selectedDeptManagers').empty();
        }
        // ------------------------------------- 组织管理页面 结束-------------------------------------------------
    };
    self = $.mr.unit;
});