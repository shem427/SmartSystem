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
                        if (treeNode.id === 'UT00000000000000') {
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
                        if (treeNode.isParent === false) {
                            self._getUnitManagers(treeNode.id, selectedUnitManagers);
                            selectedUnitManagers.parent().show();
                        } else {
                            selectedUnitManagers.parent().hide();
                        }

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
            var downloadTemplateBtn = $('#downloadTemplate');
            var importUnitBtn = $('#importUnit');

            addUnitBtn.click(function() {
                var selectedNodes = $.mr.tree.getSelectedNode(self.unitTree);
                var selectedNode;
                if (selectedNodes && selectedNodes.length > 0) {
                    selectedNode = selectedNodes[0];
                    if (!selectedNode.isParent) {
                        $.mr.messageBox.alert($.mr.resource.UNIT_NOT_ADD_UNDER_LEAF);
                        return;
                    }
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
                        unitRemark: selectedNode.unitRemark,
                        isParent: selectedNode.isParent
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
            downloadTemplateBtn.click(function() {
                $('#downloadForm').submit();
            });
            importUnitBtn.click(function() {
                $.mr.modal.create({
                    url: 'unit/import'
                });
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
                        option.text(user.name + "(" + user.userId + ")");
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
        },
        // ------------------------------------- 组织管理页面 结束-------------------------------------------------

        // ------------------------------------- 组织添加/编辑Modal 开始-------------------------------------------
        /**
         * 组织添加/编辑的Modal对话框的初始化，以及各个控件的事件定义
         */
        initModal: function() {
            var leaf = $('#unitLeaf');
            var unitId = $('#unitId').val();
            var managersDiv = $('#managersDiv');
            var initManagerArea = function() {
                var isLeaf = leaf.is(':checked');
                if (isLeaf) {
                    managersDiv.show();
                } else {
                    managersDiv.hide();
                }
            };
            if (!unitId) {
                initManagerArea();
            }
            leaf.change(initManagerArea);
            // 保存按钮事件
            $('#saveUnit').click(function() {
                var unitId = $('#unitId').val();
                var unitName = $('#unitName').val();
                var unitRemark = $('#unitRemark').val();
                var parentId = $('#parentId').val();
                var isEdit = false;
                var data = {
                    name: unitName,
                    unitRemark: unitRemark
                };
                if (unitId) {
                    data.id = unitId;
                    isEdit = true;
                } else {
                    data.pId = parentId;
                    data.isParent = $('#unitLeaf').is(':checked') === false;
                }
                data.managerIdList = self._setUnitManagers(false);
                $.mr.ajax({
                    url: 'unit/saveUnit',
                    type: 'post',
                    dataType: 'json',
                    data: data,
                    success: function() {
                        var selectedNodes = $.mr.tree.getSelectedNode(self.unitTree);
                        var parentNode;
                        var tobeSelectNode;
                        $.mr.modal.destroy({
                            selector: '#unitModal'
                        });
                        if (isEdit) {
                            parentNode = $.mr.tree.getNodeByTId(self.unitTree, selectedNodes[0].parentTId);
                            $.mr.tree.refreshNode(self.unitTree, parentNode, function() {
                                tobeSelectNode = $.mr.tree.getNodeByParam(self.unitTree, 'id',
                                    unitId, parentNode);
                                if (tobeSelectNode) {
                                    $('#' + tobeSelectNode.tId + ' > a').trigger('click');
                                }
                            });
                        } else {
                            $.mr.tree.refreshNode(self.unitTree, selectedNodes[0], function() {
                                tobeSelectNode = $.mr.tree.getNodeByParam(self.unitTree, 'id',
                                    unitId, selectedNodes[0]);
                                if (tobeSelectNode) {
                                    $('#' + tobeSelectNode.tId + ' > a').trigger('click');
                                }
                            });
                        }
                    }
                });
            });
            // 管理者list控件。
            var unitManagers = $('#unitManagers');
            // 添加管理者事件
            $('#addManager').click(function() {
                var managers = self._setUnitManagers(true);
                // 弹出用户选择对话框
                $.mr.modal.create({
                    url: 'user/userSelectModal',
                    afterDisplaying: function(dialog) {
                        // 初始化已选择人员列表
                        var selectedUsers = $('#selectedUsers', dialog);
                        var selectUsersBtn = $('#selectUsers', dialog);
                        selectedUsers.empty();
                        $.each(managers, function(indx, item) {
                            var option = $('<option></option>');
                            option.val(item.userId);
                            option.text(item.name);
                            option.appendTo(selectedUsers);
                        });

                        // 设置保存按钮事件
                        selectUsersBtn.click(function() {
                            var option;
                            var userOption;
                            var selectedUserOptions = selectedUsers.find('option');
                            unitManagers.empty();
                            if (!selectedUserOptions || selectedUserOptions.length === 0) {
                                $.mr.messageBox.alert($.mr.resource.MANAGER_NO_SELECTION);
                                return;
                            }
                            for (var i = 0; i < selectedUserOptions.length; i++) {
                                userOption = $(selectedUserOptions[i]);
                                option = $('<option></option>');
                                option.val(userOption.val());
                                option.text(userOption.text());
                                option.appendTo(unitManagers);
                            }

                            $.mr.modal.destroy({
                                selector: '#userSelectModal'
                            });
                        });
                    }
                });
            });
            // 删除管理者事件
            $('#deleteManager').click(function() {
                var selected = unitManagers.find('option:selected');
                selected.remove();
            });
        },
        /**
         * 获取页面设置的组织管理者
         * @returns {Array} 页面设置的组织管理者
         * @private
         */
        _setUnitManagers: function(includeName) {
            var selectedUnitManagers = $('#unitManagers > option');
            var userList = [];
            selectedUnitManagers.each(function(indx, item) {
                if (includeName) {
                    userList.push({
                        userId: $(item).val(),
                        name: $(item).text()
                    });
                } else {
                    userList.push($(item).val());
                }
            });
            return userList;
        },
        // ------------------------------------- 组织添加/编辑Modal 结束--------------------------------------------

        // --------------------------------------- 组织导入Modal 开始-----------------------------------------------
        initImportModal: function() {
            $('#uploadFile').change(function() {
                var path = $(this).val();
                $(this).parent().next().text(path.replace(/^.*[\\\/]/, ''));
            });
            $('#btnImportUnit').click(function() {
                var type = "file";
                var formData = new FormData();
                formData.append(type, $("#uploadFile")[0].files[0]);
                /*$.mr.ajax({
                    type: "post",
                    url: 'unit/importUnit',
                    data: formData,
                    processData: false,
                    contentType: false,
                    mimeType: "multipart/form-data",
                    success: function (data) {
                        $.mr.modal.destroy({
                            selector: '#unitImportModal'
                        });
                        $.mr.messageBox.info($.mr.resource.UNIT_IMPORT_SUCCESS, null, function() {
                            var rootNode = $.mr.tree.getNodeByTId(self.unitTree, 0);
                            $.mr.tree.refreshNode(self.unitTree, rootNode);
                            self._clearDetail();
                        });
                    }
                });*/
                $.ajax({
                    type: 'POST',
                    url: $.mr.contextPath + 'unit/importUnit',
                    data: formData,
                    contentType: false,
                    processData: false,
                    dataType: "json",
                    mimeType: "multipart/form-data",
                    success: function (data) {
                        $.mr.modal.destroy({
                            selector: '#unitImportModal'
                        });
                        $.mr.messageBox.info($.mr.resource.UNIT_IMPORT_SUCCESS, null, function() {
                            var rootNode = $.mr.tree.getNodeByTId(self.unitTree, 0);
                            $.mr.tree.refreshNode(self.unitTree, rootNode);
                            self._clearDetail();
                        });
                    }
                });
            });
        }
        // --------------------------------------- 组织导入Modal 结束-----------------------------------------------
    };
    self = $.mr.unit;
});
