$(function() {
    var _self;
    $.mr.user.modal = {
        unshowUserModel: function() {
            $.mr.modal.destroy({
                selector: '#userModal'
            })
        },
        init: function() {
            $('#saveUserBtn').click(function() {
                var selectedRoles = $('#roles').find('option:selected');
                var roleArray = [];
                selectedRoles.each(function(idx, item) {
                    roleArray.push($(item).val());
                });
                $.mr.ajax({
                    url: 'user/saveUser',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        userId: $('#userID').val(),
                        name: $('#userName').val(),
                        password: $('#userPassword').val(),
                        mailAddress: $('#eMail').val(),
                        phoneNumber: $('#phoneNumber').val(),
                        userRoles: roleArray.join(','),
                        isCreate: $('#isCreate').val()
                    },
                    success: function(data) {
                        var isSelf = $('#isSelf').val();
                        if (isSelf === 'true') {
                            $('#secUserName').text($('#userName').val());
                        }
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
    _self = $.mr.user.modal;
});