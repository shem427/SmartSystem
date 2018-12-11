$(function() {
    var _self;
    $.mr.user = {
        _initUserTable: function() {
            $.mr.table.create({
                selector: '#userTable',
                url: 'user/getUsers',
                sortName: 'USER_ID',
                pageSize: 5,
                pageList: [5, 10, 20, 50],
                columns: [{
                    checkbox: true
                }, {
                    field: 'userId',
                    title: '用户ID'
                }, {
                    field: 'name',
                    title: '姓名'
                }, {
                    field: 'phoneNumber',
                    title: '电话号码'
                }, {
                    field: 'mailAddress',
                    title: '邮件'
                }],
                queryParams: function(params) {
                    var userIdLike = $('#userIdLike').val();
                    var nameLike = $('#userNameLike').val();
                    return {
                        limit: params.limit,
                        offset: params.offset,
                        sortOrder: params.order,
                        sortField: params.sort,
                        userIdLike: userIdLike,
                        nameLike: nameLike
                    };
                }
            });
        },
        _initSearchEvt: function(tableSelector) {
            $('#userSearch').click(function() {
                $.mr.table.refresh({
                    selector: tableSelector,
                    params: {
                        silent: true
                    }
                });
            });
        },
        showUserModal: function(userId) {
            var data;
            if (userId) {
                data = {userId: userId};
            }
            $.mr.modal.create({
                url: 'user/userModal',
                data: data
            });
        },
        init: function() {
            _self._initUserTable();
            _self._initSearchEvt('#userTable');

            $('#createUserBtn').click(function() {
                _self.showUserModal();
            });
            $('#updateUserBtn').click(function() {
                var selections = $.mr.table.getSelections('#userTable');
                var userId;
                if (selections.length === 0) {
                    $.mr.messageBox.alert($.mr.resource.USER_NO_SELECTION);
                } else if (selections.length > 1) {
                    $.mr.messageBox.alert($.mr.resource.USER_EDIT_MULTI_SELECT);
                } else {
                    userId = selections[0].userId;
                    if (userId) {
                        _self.showUserModal(userId);
                    }
                }
            });
            $('#deleteUserBtn').click(function() {
                var selections = $.mr.table.getSelections('#userTable');
                var userIdArray = [];
                if (selections.length === 0) {
                    $.mr.messageBox.alert($.mr.resource.USER_NO_SELECTION);
                    return;
                }
                $.each(selections, function(idx, item) {
                    userIdArray.push(item.userId);
                });
                $.mr.messageBox.confirm($.mr.resource.USER_DELETE_CONFIRM + userIdArray.length, '', {
                    yes: function() {
                        $.mr.ajax({
                            url: 'user/deleteUsers',
                            type: 'post',
                            data: JSON.stringify(userIdArray),
                            contentType: 'application/json',
                            success: function (data) {
                                $.mr.table.refresh({
                                    selector: '#userTable',
                                    params: {
                                        silent: true
                                    }
                                });
                                $.mr.messageBox.info($.mr.resource.USER_DELETE_SUCCESS + data.number);
                            }
                        });
                    }
                });
            });
            $('#resetPassword').click(function() {
                var selections = $.mr.table.getSelections('#userTable');
                var userIdArray = [];
                if (selections.length === 0) {
                    $.mr.messageBox.alert($.mr.resource.USER_NO_SELECTION);
                    return;
                }
                $.each(selections, function(idx, item) {
                    userIdArray.push(item.userId);
                });
                $.mr.messageBox.confirm($.mr.resource.USER_RESET_PASSWORD_CONFIRM + userIdArray.length, '', {
                    yes: function() {
                        $.mr.ajax({
                            url: 'user/deleteUsers',
                            type: 'post',
                            data: JSON.stringify(userIdArray),
                            contentType: 'application/json',
                            success: function (data) {
                                $.mr.table.refresh({
                                    selector: '#userTable',
                                    params: {
                                        silent: true
                                    }
                                });
                                $.mr.messageBox.info($.mr.resource.USER_DELETE_SUCCESS + data.number);
                            }
                        });
                    }
                });
            });
        },

        // ------------------------------------- 人员选择Modal 开始-----------------------------------------------
        initModalUsers: function() {
            _self._initModalUserTable();
            _self._initSearchEvt('#modalUsersTable');
        },
        _initModalUserTable: function() {
            $.mr.table.create({
                selector: '#modalUsersTable',
                url: 'user/getUsers',
                sortName: 'USER_ID',
                pageSize: 5,
                pageList: [5, 10, 20, 50],
                columns: [{
                    checkbox: true
                }, {
                    field: 'userId',
                    title: '人员ID'
                }, {
                    field: 'name',
                    title: '姓名'
                }],
                queryParams: function(params) {
                    var userIdLike = $('#userId').val();
                    var nameLike = $('#userName').val();
                    return {
                        limit: params.limit,
                        offset: params.offset,
                        sortOrder: params.order,
                        sortField: params.sort,
                        userIdLike: userIdLike,
                        nameLike: nameLike
                    };
                },
                onCheck: _self._tableCheck,
                onCheckAll: function(rows) {
                    if (rows && $.isArray(rows)) {
                        $.each(rows, function(indx, row) {
                            _self._tableCheck(row);
                        });
                    }
                },
                onUncheck: _self._tableUncheck,
                onUncheckAll: function(rows) {
                    if (rows && $.isArray(rows)) {
                        $.each(rows, function(indx, row) {
                            _self._tableUncheck(row);
                        });
                    }
                },
                onLoadSuccess: function(data) {
                    var selectedUserIds;
                    if (data.rows.length === 0) {
                        return;
                    }
                    selectedUserIds = _self._getSelectedUsers();
                    $.each(data.rows, function(indx, item) {
                        var userId = item.userId;
                        if ($.inArray(userId.toString(), selectedUserIds) >= 0) {
                            $.mr.table.check('#modalUsersTable', indx);
                        }
                    });
                }
            });
        },
        _tableCheck: function(row) {
            var selectedUsersList = $('#selectedUsers');
            var option;
            var selectedUserIds = _self._getSelectedUsers();
            if ($.inArray(row.userId.toString(), selectedUserIds) >= 0) {
                return;
            }
            option = $('<option></option>');
            option.val(row.userId);
            option.text(row.name + '(' + row.userId + ')');
            option.appendTo(selectedUsersList);
        },
        _tableUncheck: function(row) {
            var selectedUsersList = $('#selectedUsers');
            var options = selectedUsersList.find('option');
            options.each(function(indx, item) {
                var opt = $(item);
                if (opt.val() === row.userId.toString()) {
                    opt.remove();
                }
            });
        },
        _getSelectedUsers: function() {
            var selectedUserIds = [];
            var selectedUserOptions = $('#selectedUsers > option');
            var userOption;
            if (!selectedUserOptions || selectedUserOptions.length === 0) {
                return selectedUserIds;
            }
            for (var i = 0; i < selectedUserOptions.length; i++) {
                userOption = $(selectedUserOptions[i]);
                selectedUserIds.push(userOption.val());
            }
            return selectedUserIds;
        }
        // ------------------------------------- 人员选择Modal 结束-----------------------------------------------
    };
    _self = $.mr.user;
});