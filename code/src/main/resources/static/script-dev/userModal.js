$(function() {
    $.mr.user.modal = {
        unshwoUserModel: function() {
            $('#userModal').modal('hide');
            $('#userModal').remove();
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
                        mailAddress: $('#eMail').val(),
                        isCreate: $('#isCreate').val()
                    },
                    success: function(data) {
                        $.mr.user.modal.unshwoUserModel();
                        $.mr.user.getUserData();
                    }
                });
            });

            $('#closeUserBtn').click(function() {
                $.mr.user.modal.unshwoUserModel();
            });
        }
    };
});