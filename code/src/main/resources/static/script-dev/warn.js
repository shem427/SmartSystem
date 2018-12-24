$(function() {
    $.mr.warn = {
        init: function() {
            // init data.
            var btnDate = $('.btn-date');
            var dateControls = $('#beginDate, #endDate');
            var form = $('#warnSearchForm');

            dateControls.datetimepicker({
                format: 'yyyy-mm-dd',
                language: 'zh-CN',
                todayHighlight: 1,
                minView: "month",
                autoclose: 1
            });
            btnDate.click(function() {
                var $this = $(this);
                var dateControl = $this.parent().prev();
                dateControl.datetimepicker('show');
            });
            form.bootstrapValidator({
                message: 'value is not valid',
                live: 'disabled',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    endDate: {
                        validators: {
                            callback: {
                                message: $.mr.resource.VALIDATION_MSG_DATE_FROM_TO,
                                callback: function(value) {
                                    var beginDate = $('#beginDate').val();
                                    var endDate = value;
                                    return beginDate <= endDate;
                                }
                            }
                        }
                    }
                }
            });


            $('#warnSearch').click(function() {
                var form = $("#warnSearchForm");
                form.data("bootstrapValidator").resetForm();
                form.bootstrapValidator('validate');
                if (!form.data('bootstrapValidator').isValid()) {
                    return false;
                }
                $.mr.table.refresh({
                    selector: '#warnTable',
                    params: {
                        silent: true
                    }
                });
            });

            $.mr.table.create({
                selector: '#warnTable',
                url: 'warn/getWarns',
                sortable: false,
                pageSize: 5,
                pageList: [5, 10, 20, 50],
                columns: [{
                    field: 'unitName',
                    title: '组织名称'
                }, {
                    field: 'unitStatus',
                    title: '状态',
                    formatter: function(value, row, index) {
                        if (value === 1) {
                            return $.mr.resource.STATUS_WARNING;
                        } else if (value === 2) {
                            return $.mr.resource.STATUS_ERROR;
                        } else {
                            return '';
                        }
                    }
                }, {
                    field: 'notifyTimeDisplay',
                    title: '通知时间'
                }],
                queryParams: function(params) {
                    var begin = $('#beginDate').val();
                    var end = $('#endDate').val();
                    return {
                        limit: params.limit,
                        offset: params.offset,
                        begin: begin,
                        end: end
                    };
                }
            });
        }
    };
});