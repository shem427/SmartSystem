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
                    $.mr.messageBox.alert($.mr.resource.USER_NO_SELETION);
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
                    $.mr.messageBox.alert($.mr.resource.USER_NO_SELETION);
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
        }
    };
    _self = $.mr.user;
});