$(function() {
    $.mr.resource = {
        ALERT: '警告',
        ERROR: '错误',
        CONFIRM: '确认',
        INFO: '信息',

        OK: '确定',
        CANCEL: '取消',

        YES: '是',
        NO: '否',

        LOGOUT_CONFIRM: '请确认是否注销？',
        LOGIN_ERROR: '用户登陆发生错误，请确认用户名与密码。如果再次发生错误，请联系管理员。',

        USER_NO_SELECTION: '请选择人员。',
        USER_DELETE_CONFIRM: '确定要删除人员？<br/>&nbsp;&nbsp;&nbsp;&nbsp;删除件数=',
        USER_DELETE_SUCCESS: '删除成功。删除件数=',
        USER_EDIT_MULTI_SELECT: '编辑时，请只选择一名人员。',
        USER_ID_LENGTH: '请输入6位用户ID。',
        USER_ID_VALID: '用户ID只能输入半角字母字符和数字。',
        USER_RESET_PASSWORD_CONFIRM: '确定重置密码为默认密码？<br/>&nbsp;&nbsp;&nbsp;&nbsp;重置件数=',

        NEW_PASSWORD_NOT_MATCH: '两次输入的新密码不一致，请重新输入。',
        OLD_NEW_PASSWORD_SAME: '新密码与旧密码相同，请输入与旧密码不同的新密码。',
        PASSWORD_LENGTH_NOT_VALID: '密码长度不正确，不能小于6位或者超过32位。',
        CHANGE_PASSWORD_SUCCESS: '密码修改成功。',

        UPDATE_PROFILE_SUCCESS: '账号信息更新成功。',
        UPDATE_THRESHOLD_SUCCESS: '阈值信息更新成功',

        VALIDATION_THRESHOLD_NOMAL_WARN: '警告辐射阈值必须小于正常辐射阈值',
        VALIDATION_MSG_NOT_EMPTY: '不能为空！',
        VALIDATION_MSG_NUMERIC: '只能输入数字！',
        VALIDATION_MSG_MAIL_ADDRESS: '邮件地址格式不正确！',
        VALIDATION_MSG_DIGIT: '只能输入数字！',

        VALIDATION_MSG_DATE_FROM_TO: '结束日期必须大于开始日期',

        UNIT_ONLY_ONE_SELECTION: '请只选择一件组织节点。',
        UNIT_NO_SELECTION: '请选择组织节点。',
        UNIT_DELETE_CONFIRM: '确定要删除组织？<br/>&nbsp;&nbsp;&nbsp;&nbsp;组织名：',
        UNIT_NOT_ADD_UNDER_LEAF: '叶节点组织无法添加子组织。',
        MANAGER_NO_SELECTION: '选择人员为空，请选择人员，然后点击保存。',
        UNIT_IMPORT_SUCCESS: '组织信息导入结束。',
        UNIT_IMPORT_WARNING: '组织信息导入结束。其中有以下错误：\r\n',

        SENSOR_NO_SELECTION: '请选择传感器',
        SENSOR_EDIT_MULTI_SELECT: '编辑时，请只选择一件传感器。',
        SENSOR_DELETE_CONFIRM: '确定要删除传感器？<br/>&nbsp;&nbsp;&nbsp;&nbsp;删除件数=',
        SENSOR_DELETE_SUCCESS: '删除成功。删除件数=',
        UNIT_ICON_TYPE: {
            '0': 'dashboard',
            '1': 'plane',
            '2': 'train',
            '3': 'car',
            '4': 'bicycle',
            '51': 'hospital-o',
            '52': 'building-o',
            '61': 'medkit',
            '62': 'users',
            '71': 'stethoscope',
            '72': 'users',
            '81': 'stethoscope',
            '82': 'users',
            '9': 'lightbulb-o'
        },
        STATUS_NORMAL: '合格',
        STATUS_WARNING: '延长照射时间',
        STATUS_ERROR: '不合格',

        ACTIVE_SENSOR: '活跃传感器',
        INACTIVE_SENSOR: '非活跃传感器',

        ROLE_ADMIN: '管理员',
        ROLE_USER: '用户'
    };
});
