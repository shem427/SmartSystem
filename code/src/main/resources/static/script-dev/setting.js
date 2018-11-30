$(function() {
    var _self;
    $.mr.setting = {
        changePassword: {
            init: function() {
                $('#savePasswordBtn').click(_self.changePassword._changePassword);
            },
            _changePassword: function() {
                var oldPassword = $('#oldPassword');
                var newPassword = $('#newPassword');
                var newPasswordConfirm = $('#newPasswordConfirm');

                if (_self.changePassword._validatePassword(oldPassword, newPassword, newPasswordConfirm)) {
                    $.mr.ajax({
                        url: 'setting/changePassword',
                        type: 'post',
                        dataType: 'json',
                        data: {
                            oldPassword: oldPassword.val(),
                            newPassword: newPassword.val()
                        },
                        success: function() {
                            oldPassword.val('');
                            newPassword.val('');
                            newPasswordConfirm.val('');
                            $.mr.messageBox.info($.mr.resource.CHANGE_PASSWORD_SUCCESS);
                        }
                    });
                }
            },
            _validatePassword: function(oldPassword, newPassword, newPasswordConfirm) {
                if (newPassword.val() !== newPasswordConfirm.val()) {
                    // 新密码与新密码确认必须一致。
                    $.mr.messageBox.alert($.mr.resource.NEW_PASSWORD_NOT_MATCH, '', function() {
                        oldPassword.val('');
                        newPassword.val('');
                        newPasswordConfirm.val('');
                    });
                    return false;
                } else if (oldPassword.val() === newPassword.val()) {
                    // 新密码与旧密码必须不一致。
                    $.mr.messageBox.alert($.mr.resource.OLD_NEW_PASSWORD_SAME, '', function() {
                        oldPassword.val('');
                        newPassword.val('');
                        newPasswordConfirm.val('');
                    });
                    return false;
                }
                return true;
            }
        },
        updateProfile: {
            init: function() {
                var form = $("#updateProfileForm");
                $('#btnReset').click(function() {
                    form.trigger("reset");
                    form.data("bootstrapValidator").resetForm();
                });
                // 保存按钮事件
                $('#btnUpdateProfile').click(function() {
                    var data = _self.updateProfile._validateProfile();
                    if (!data) {
                        return;
                    }
                    $.mr.ajax({
                        url: 'setting/updateProfile',
                        type: 'post',
                        dataType: 'json',
                        data: data,
                        success: function() {
                            $('#secUserName').text(data.name);
                            $.mr.messageBox.info($.mr.resource.UPDATE_PROFILE_SUCCESS);
                        }
                    });
                });
                // Validation
                form.bootstrapValidator({
                    message: 'value is not valid',
                    live: 'disabled',
                    feedbackIcons: {
                        valid: 'glyphicon glyphicon-ok',
                        invalid: 'glyphicon glyphicon-remove',
                        validating: 'glyphicon glyphicon-refresh'
                    },
                    fields: {
                        policeNumber: {
                            validators: {
                                notEmpty: {
                                    message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                                }
                            }
                        },
                        userName: {
                            validators: {
                                notEmpty: {
                                    message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                                }
                            }
                        },
                        phone: {
                            validators: {
                                numeric: {
                                    message: $.mr.resource.VALIDATION_MSG_NUMERIC
                                },
                                notEmpty: {
                                    message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                                }
                            }
                        }
                    }
                });
            },
            _validateProfile: function() {
                var form = $("#updateProfileForm");
                form.bootstrapValidator('validate');
                if (form.data('bootstrapValidator').isValid()) {
                    return {
                        userId: $('#userId').val(),
                        name: $('#userName').val(),
                        mailAddress: $('#mail').val(),
                        phoneNumber: $('#phone').val()
                    };
                }
                return null;
            }
        }
    };
    _self = $.mr.setting;
});