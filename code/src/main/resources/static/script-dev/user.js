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
            var url = 'user/userModal';
            if (userId) {
                url += ('?userId=' + userId);
            }
            $.mr.ajax({
                url: url,
                type: 'get',
                dataType: 'html',
                success: function(html) {
                    $('body').append(html);
                    $('#userModal').modal({show: true});
                }
            });
        },
        init: function() {
            _self._initUserTable();
            _self._initSearchEvt('#userTable');

            $('#createUserBtn').click(function() {
                _self.showUserModal();
            });
        }
    };
    _self = $.mr.user;
});