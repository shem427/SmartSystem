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

        // TABLE
        // TABLE_NO_SELECTED: '请在表格中选择数据',
        // TABLE_ONLY_ONE_SELECT: '请在表格中只选择一条数据',
        // TABLE_DATA_NUMER: '数据处理成功，处理件数='

        USER_NO_SELETION: '请选择人员。',
        USER_DELETE_CONFIRM: '确定要删除人员？<br/>&nbsp;&nbsp;&nbsp;&nbsp;删除件数=',
        USER_DELETE_SUCCESS: '删除成功。删除件数=',
        USER_EDIT_MULTI_SELECT: '编辑时，请只选择一名人员',
        USER_ID_LENGTH: '请输入6位用户ID',
        USER_ID_VALID: '用户ID只能输入半角字母字符和数字',

        NEW_PASSWORD_NOT_MATCH: '两次输入的新密码不一致，请重新输入',
        OLD_NEW_PASSWORD_SAME: '新密码与旧密码相同，请输入与旧密码不同的新密码',
        PASSWORD_LENGTH_NOT_VALID: '密码长度不正确，不能小于6位或者超过32位',
        CHANGE_PASSWORD_SUCCESS: '密码修改成功',

        UPDATE_PROFILE_SUCCESS: '账号信息更新成功',

        VALIDATION_MSG_NOT_EMPTY: '不能为空！',
        VALIDATION_MSG_NUMERIC: '只能输入数字！',
        VALIDATION_MSG_MAIL_ADDRESS: '邮件地址格式不正确！',
        VALIDATION_MSG_DIGIT: '只能输入数字！',

        DEPT_NO_SELECTION: '请选择组织节点。',
        DEPT_DELETE_CONFIRM: '确定要删除组织？<br/>&nbsp;&nbsp;&nbsp;&nbsp;组织名：',
        USER_NO_SELECTION: '选择人员为空，请选择人员，然后点击保存。'
    };
});