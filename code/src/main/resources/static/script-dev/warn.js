$(function() {
    var _self = $.mr.warn = {
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
                    field: 'unitPath',
                    title: '关联组织'
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
                    var selectedUnit = $('#selectedUnit').val();
                    return {
                        limit: params.limit,
                        offset: params.offset,
                        begin: begin,
                        end: end,
                        unitPath: selectedUnit
                    };
                }
            });
            _self._initUnitChain();
        },
        _initUnitChain: function(callback) {
            var currentUnitId = $('#currentUnitId').val();
            $.mr.ajax({
                url: 'unit/getUnitChain',
                type: 'get',
                dataType: 'json',
                data: { unitId: currentUnitId },
                success: function (data) {
                    $('#currentUnitIdChain').val(data.idChain);
                    $('#selectedUnit').val(data.fullPath);
                    _self._initUnitMegaMenu();
                    if (callback) {
                        callback();
                    }
                }
            });
        },
        _initUnitMegaMenu: function() {
            var unitSelectA = $('#unitSelectA');
            var unitSelectUl = $('#unitSelectUl');
            var unitSelectCancelBtn = $('#unitSelectCancelBtn');
            var unitSelectOKBtn = $('#unitSelectOKBtn');

            unitSelectA.unbind('click').click(function(e) {
                var currentUnitId = $('#currentUnitId').val();
                var unitIdArray = $('#currentUnitIdChain').val().split(",");
                var unitNameArray = $('#selectedUnit').val().split('/');
                var dropDownHeader = $('li.dropdown-header > ul');
                var li;
                unitNameArray[0] = '全系统';
                e.preventDefault();
                if (!unitSelectUl.is(':hidden')) {
                    return;
                }
                unitSelectUl.show();

                dropDownHeader.empty();
                for (var i = 0; i < unitIdArray.length; i++) {
                    if (i === unitIdArray.length - 1) {
                        li = $('<li class="selected"></li>').prop('id', unitIdArray[i]).text(unitNameArray[i]);
                    } else {
                        li = $('<li></li>').prop('id', unitIdArray[i]).html('<a href="#">' + unitNameArray[i] + '</a>');
                    }
                    dropDownHeader.append(li);
                }

                _self._initMegaHeadEvt();

                // show submenu in mega.
                _self._initMegaItemEvt(currentUnitId)
            });
            unitSelectCancelBtn.unbind('click').click(function() {
                unitSelectUl.hide();
            });
            unitSelectOKBtn.unbind('click').click(function() {
                var selectedUnitId = $('li.dropdown-header > ul > li:last').prop('id');
                $('#currentUnitId').val(selectedUnitId);
                unitSelectUl.hide();

                _self._initUnitChain(function() {
                    $.mr.table.refresh({
                        selector: '#warnTable',
                        params: {
                            silent: true
                        }
                    });
                });
            });
        },
        _initMegaItemEvt: function(unitId) {
            $.mr.ajax({
                url: 'unit/subUnitByUser',
                type: 'get',
                data: { id: unitId },
                success: function(units) {
                    var unitSelectUl = $('#unitSelectUl');
                    var lastDivider = unitSelectUl.find('li.divider:last');
                    unitSelectUl.find('li.dropdown-item').remove();
                    $.each(units, function(idx, unit) {
                        var li;
                        if (unit.isParent) {
                            li = '<li class="dropdown-item" id="' + unit.id + '"><a href="#">' + unit.name + '</a></li>';
                        } else {
                            li = '<li class="dropdown-item dropdown-content" id="' + unit.id + '">' + unit.name + '</li>';
                        }
                        lastDivider.before(li);
                    });
                    _self._initMegaMenuEvt();
                }
            });
        },
        _initMegaHeadEvt: function() {
            $('.dropdown-header > ul > li > a').unbind('click').click(function(e) {
                var unitId = $(this).parent().prop('id');
                var unitName = $(this).text();
                e.preventDefault();
                $(this).parent().addClass('selected').nextAll().remove();
                $(this).parent().empty().text(unitName);

                _self._initMegaItemEvt(unitId);
            });
        },
        _initMegaMenuEvt: function() {
            $('li.dropdown-item > a').unbind('click').click(function(e) {
                var unitId = $(this).parent().prop('id');
                var unitName = $(this).text();
                var dropDownHeader = $('li.dropdown-header > ul');
                var lastLi = dropDownHeader.find('li:last');
                var lastUnitName = lastLi.text();
                e.preventDefault();
                lastLi.removeClass('selected').text('').html('<a href="#">' + lastUnitName + '</a>');
                dropDownHeader.append($('<li class="selected"></li>').prop('id', unitId).text(unitName));

                _self._initMegaHeadEvt();

                // show submenu in mega.
                _self._initMegaItemEvt(unitId)
            });
        }
    };
});
