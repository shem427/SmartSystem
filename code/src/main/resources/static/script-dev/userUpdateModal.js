$(function() {
    var _self;
    $.mr.user.modal = {
        unshowUserModel: function() {
            $.mr.modal.destroy({
                selector: '#userModal'
            })
        },
        init: function() {
            var form = $('#userModalForm');
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
                    userID: {
                        validators: {
                            regexp: {
                                regexp: /^[0-9a-zA-Z]+$/i,
                                message: $.mr.resource.USER_ID_VALID
                            },
                            callback: {
                                message: $.mr.resource.USER_ID_LENGTH,
                                callback: function(value) {
                                    return value.length === 6;
                                }
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
                    userPassword: {
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
                    phoneNumber: {
                        validators: {
                            notEmpty: {
                                message: $.mr.resource.VALIDATION_MSG_NOT_EMPTY
                            },
                            digits: {
                                message: $.mr.resource.VALIDATION_MSG_DIGIT
                            }
                        }
                    },
                    mailAddress: {
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
            $('#saveUserBtn').click(function() {
                var selectedRoles = $('#roles').find('option:selected');
                var roleArray = [];
                var form = $('#userModalForm');
                form.bootstrapValidator('validate');
                if (!form.data('bootstrapValidator').isValid()) {
                    return;
                }
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