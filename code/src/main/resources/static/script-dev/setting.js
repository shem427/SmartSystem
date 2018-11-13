$(function() {
    $.mr.setting = {
        changePassword: {
            init: function() {
                $('a.menuItem').removeClass('active');
                $('#changePasswordLink').addClass('active');
                $('#savePasswordBtn').click($.mr.setting.changePassword.savePassword);
            },
            savePassword: function() {
                var oldPassword = $('#oldPassword').val();
                var newPassword = $('#newPassword').val();
                var newPasswordConfirm = $('#newPasswordConfirm').val();

                if (newPassword !== newPasswordConfirm) {
                    // TODO: message;
                    return;
                }
                $.mr.ajax({
                    url: 'setting/savePassword',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        oldPassword: oldPassword,
                        newPassword: newPassword
                    },
                    success: function(data) {
                        // TODO: message box.
                    }
                });
            }
        },
        updateProfile: {}
    };
});