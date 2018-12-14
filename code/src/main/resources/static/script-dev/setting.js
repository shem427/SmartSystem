$(function() {
    var _self;
    $.mr.setting = {
        changePassword: {
            init: function() {
                var form = $('#changePasswordForm');
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
                        oldPassword: {
                            validators: {
                                notEmpty: {
                                    message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                                },
                                stringLength: {
                                    message: $.mr.resource.PASSWORD_LENGTH_NOT_VALID,
                                    min: 6,
                                    max: 32
                                }
                            }
                        },
                        newPassword: {
                            validators: {
                                notEmpty: {
                                    message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                                },
                                different: {
                                    field: 'oldPassword',
                                    message: $.mr.resource.OLD_NEW_PASSWORD_SAME
                                },
                                stringLength: {
                                    message: $.mr.resource.PASSWORD_LENGTH_NOT_VALID,
                                    min: 6,
                                    max: 32
                                }
                            }
                        },
                        newPasswordConfirm: {
                            validators: {
                                notEmpty: {
                                    message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                                },
                                identical: {
                                    field: 'newPassword',
                                    message: $.mr.resource.NEW_PASSWORD_NOT_MATCH
                                },
                                stringLength: {
                                    message: $.mr.resource.PASSWORD_LENGTH_NOT_VALID,
                                    min: 6,
                                    max: 32
                                }
                            }
                        }
                    }
                });
                $('#savePasswordBtn').click(_self.changePassword._changePassword);
            },
            _changePassword: function() {
                var oldPassword = $('#oldPassword');
                var newPassword = $('#newPassword');
                var newPasswordConfirm = $('#newPasswordConfirm');

                if (_self.changePassword._validatePassword()) {
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
            _validatePassword: function() {
                var form = $("#changePasswordForm");
                form.bootstrapValidator('validate');
                if (!form.data('bootstrapValidator').isValid()) {
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
                        },
                        mail: {
                            validators: {
                                notEmpty: {
                                    message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                                },
                                emailAddress: {
                                    message: $.mr.resource.VALIDATION_MSG_MAIL_ADDRESS
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
        },
        thresholdSetting: {
            init: function() {
                var form = $("#thresholdSettingForm");
                $('#btnReset').click(function() {
                    form.trigger("reset");
                    form.data("bootstrapValidator").resetForm();
                });
                // 保存按钮事件
                $('#btnUpdateThreshold').click(function() {
                    var data = _self.thresholdSetting._validate();
                    if (!data) {
                        return;
                    }
                    $.mr.ajax({
                        url: 'setting/updateThreshold',
                        type: 'post',
                        dataType: 'json',
                        data: data,
                        success: function() {
                            $.mr.messageBox.info($.mr.resource.UPDATE_THRESHOLD_SUCCESS);
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
                        normalThreshold: {
                            validators: {
                                numeric: {
                                    message: $.mr.resource.VALIDATION_MSG_NUMERIC
                                },
                                notEmpty: {
                                    message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                                }
                            }
                        },
                        warningThreshold: {
                            validators: {
                                numeric: {
                                    message: $.mr.resource.VALIDATION_MSG_NUMERIC
                                },
                                notEmpty: {
                                    message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                                },
                                callback: {
                                    message: $.mr.resource.VALIDATION_THRESHOLD_NOMAL_WARN,
                                    callback: function(value) {
                                        var normal = $('#normalThreshold').val();
                                        return normal > value;
                                    }
                                }
                            }
                        }
                    }
                });
            },
            _validate: function() {
                var form = $("#thresholdSettingForm");
                form.data("bootstrapValidator").resetForm();
                form.bootstrapValidator('validate');
                if (form.data('bootstrapValidator').isValid()) {
                    return {
                        dataId: 'RADIATION',
                        norml: $('#normalThreshold').val(),
                        warn: $('#warningThreshold').val()
                    };
                }
                return null;
            }
        }
    };
    _self = $.mr.setting;
});