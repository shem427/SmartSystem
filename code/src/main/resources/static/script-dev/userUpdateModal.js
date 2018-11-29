$(function() {
    $.mr.user.modal = {
        unshowUserModel: function() {
            $.mr.modal.destroy({
                selector: '#userModal'
            })
        },
        init: function() {
            $('#saveUserBtn').click(function() {
                $.mr.ajax({
                    url: 'user/saveUser',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        userId: $('#userID').val(),
                        name: $('#userName').val(),
                        password: $('#userPassword').val(),
                        mailAddress: $('#eMail').val(),
                        isCreate: $('#isCreate').val()
                    },
                    success: function(data) {
                        $.mr.user.modal.unshowUserModel();
                        $.mr.table.refresh({
                            selector: '#userTable',
                            params: {
                                silent: true
                            }
                        });
                    }
                });
            });

            $('#closeUserBtn').click(function() {
                $.mr.user.modal.unshowUserModel();
            });
        }
    };
});