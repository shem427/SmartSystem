$(function() {
    $.mr.user = {
        /**
         * display user data in table.
         * @param data user data.
         * @private
         */
        _displayUser: function(data) {
            var $userTableBody = $('#userTable').find('tbody');
            $userTableBody.empty();
            if (!data || data.length === 0) {
                // no users data.
                $userTableBody.append('<tr><td colspan="3">没有用户数据</td></tr>');
                return;
            }
            $.each(data, function(indx, item) {
                var noHtml = '<td>' + (indx + 1) + '</td>';
                var userIdHtml = '<td><a href="#" class="userIdLink">' + item.userId + '</a></td>';
                var userNameHtml = '<td>' + item.name + '</td>';
                var trHtml = '<tr>' + noHtml + userIdHtml + userNameHtml + '</tr>';
                $userTableBody.append(trHtml);
            });

            $('a.userIdLink').click(function(e) {
                e.preventDefault();
                var userId = $(this).text();
                $.mr.user.showUserModal(userId);
            });
        },
        /**
         * get user data from server.
         */
        getUserData: function() {
            $.mr.ajax({
                url: contextPath + 'user/getUsers',
                type: 'get',
                dataType: 'json',
                success: function(data) {
                    $.mr.user._displayUser(data);
                }
            });
        },
        showUserModal: function(userId) {
            var url = contextPath + 'user/userModal';
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
            $("a.menuItem").removeClass('active');
            $('#userLink').addClass('active');
            $.mr.user.getUserData();

            $('#createUserBtn').click(function() {
                $.mr.user.showUserModal();
            });
        }
    };
});