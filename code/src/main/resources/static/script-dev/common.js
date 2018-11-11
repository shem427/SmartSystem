$(function() {
    // ajax global setting for csrf.
    $.ajaxSetup({
        beforeSend: function(xhr) {
            var header = $("meta[name='_csrf_header']").attr("content");
            var token = $("meta[name='_csrf']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        error: function(xhr, status, error) {
            console.log(error);
            if (xhr.status === 401) {
                location.reload(true);
            }
        }
    });

    $.mr = {
        contextPath: contextPath,
        storageCache: {},
        modal: {
            create: function(options) {
                var ajaxOptions = {
                    url: $.mr.contextPath + options.url,
                    traditional: true,
                    type: options.type || "get",
                    data: options.data || {},
                    dataType: options.dataType || "html",
                    async: options.async !== false,
                    cache: false,
                    success: function(data) {
                        var dialog = $(data);
                        // create dialog
                        $("body").append(dialog);
                        dialog.modal({
                            keyboard: false,
                            backdrop: 'static'
                        });
                        dialog.on('hidden.bs.modal', function() {
                            dialog.empty().remove();
                        });
                        dialog.on('show.bs.modal', function() {
                            if (options.afterDisplaying) {
                                options.afterDisplaying(dialog);
                            }
                        });
                        dialog.modal('show');
                    }
                };
                $.ajax(ajaxOptions);
            },
            destroy: function(options) {
                var dialog = $(options.selector);
                dialog.modal('hide');
            }
        },
        messageBox: {
            msgFormat: function(msg, args) {
                return msg.replace(/\{(\d+)\}/g, function() {
                    var val = args[arguments[1]];
                    return (!val) ? arguments[0] : val;
                });
            },
            alert: function(message, title, callback) {
                var newTitle = title || $.mr.resource.ALERT;
                $.mr.messageBox._message("alert", message, newTitle, callback);
            },
            error: function(message, title, callback) {
                var newTitle = title || $.mr.resource.ERROR;
                $.mr.messageBox._message("error", message, newTitle, callback);
            },
            confirm: function(message, title, callback) {
                var newTitle = title || $.mr.resource.CONFIRM;
                $.mr.messageBox._message("confirm", message, newTitle, callback);
            },
            info: function(message, title, callback) {
                var newTitle = title || $.mr.resource.INFO;
                $.mr.messageBox._message("info", message, newTitle, callback);
            },
            _message: function(type, message, title, callback) {
                var dialog = $("#poaMsgModal");
                var modalType = type.toLowerCase();
                var dialogHtml;
                var dialogHeader;
                var dialogContent;
                var dialogBody;
                var dialogFooter;

                if (dialog.length > 0) {
                    dialog.empty().remove();
                }

                dialogHtml = "<div class='modal fade' id='poaMsgModal' tabindex='-1' role='dialog'"
                    + " aria-labelledby='poaMsgModalLabel' aria-hidden='true'>"
                    + "    <div class='modal-dialog'>"
                    + "        <div class='modal-content'>"
                    + "            <div class='modal-header'>"
                    + "                <h4 class='modal-title' id='poaMsgModalLabel'></h4>"
                    + "            </div>"
                    + "            <div class='modal-body'></div>"
                    + "            <div class='modal-footer'></div>"
                    + "        </div>"
                    + "    </div>"
                    + "</div>";
                dialog = $(dialogHtml);
                // create dialog
                $("body").append(dialog);

                dialogHeader = dialog.find('h4');
                dialogBody = dialog.find(".modal-body");
                dialogContent = dialog.find(".modal-content");
                dialogFooter = dialog.find(".modal-footer");

                // config dialog
                dialogBody.html(message);
                if (modalType === 'alert') {
                    dialogHeader.html('<i class="fa fa-warning fa-fw"></i>' + title);
                    dialogContent.addClass('modal-alert');
                    dialogFooter.append('<button type="button" class="btn btn-sm btn-primary">'
                        + $.mr.resource.OK + '</button>');
                    dialogFooter.children('button').click(function() {
                        if (callback) {
                            callback();
                        }
                        dialog.modal('hide');
                    });
                } else if (modalType === 'error') {
                    dialogHeader.html('<i class="fa fa-window-close-o fa-fw"></i>' + title);
                    dialogContent.addClass('modal-error');
                    dialogFooter.append('<button type="button" class="btn btn-sm btn-primary">'
                        + $.mr.resource.OK + '</button>');
                    dialogFooter.children('button').click(function() {
                        if (callback) {
                            callback();
                        }
                        dialog.modal('hide');
                    });
                } else if (modalType === 'confirm') {
                    dialogHeader.html('<i class="fa fa-question-circle-o fa-fw"></i>' + title);
                    dialogContent.addClass('modal-confirm');
                    dialogFooter.append('<button type="button" class="btn btn-sm btn-default">'
                        + $.mr.resource.NO + '</button>');
                    dialogFooter.append('<button type="button" class="btn btn-sm btn-primary">'
                        + $.mr.resource.YES + '</button>');
                    dialogFooter.children('button:eq(0)').click(function() {
                        if (callback && callback.no) {
                            callback.no();
                        }
                        dialog.modal('hide');
                    });
                    dialogFooter.children('button:eq(1)').click(function() {
                        if (callback && callback.yes) {
                            callback.yes();
                        }
                        dialog.modal('hide');
                    });
                } else if (modalType === 'info') {
                    dialogHeader.html('<i class="fa fa-info-circle fa-fw"></i>' + title);
                    dialogContent.addClass('modal-info');
                    dialogFooter.append('<button type="button" class="btn btn-sm btn-primary">'
                        + $.mr.resource.OK + '</button>');
                    dialogFooter.children('button').click(function() {
                        if (callback) {
                            callback();
                        }
                        dialog.modal('hide');
                    });
                }
                dialog.modal({
                    keyboard: false,
                    backdrop: 'static'
                });
                dialog.on('hidden.bs.modal', function() {
                    dialog.empty().remove();
                });
                dialog.on('shown.bs.modal', function() {
                    dialog.find('button.btn-primary').focus();
                });
                dialog.modal('show');
            }
        },
        ajax: function(options) {
            var ajaxOptions = {
                url: $.mr.contextPath + options.url,
                traditional: true,
                type: options.type || "get",
                data: options.data || {},
                dataType: options.dataType || "json",
                async: options.async !== false,
                cache: false,
                complete: function(data) {
                    if ($.isFunction(options.complete)) {
                        options.complete(data);
                    }
                },
                success: function(retData) {
                    var data = retData;
                    if (!data) {
                        return;
                    }
                    if (options.dataType === "html") {
                        try {
                            data = $.parseJSON(retData);
                        } catch (ex) {
                            // ignore.
                        }
                    }
                    if (data.status === 'ERROR') {
                        $.mr.messageBox.error(data.message);
                        return;
                    } else if (data.status === 'WARNING') {
                        $.mr.messageBox.alert(data.message);
                        return;
                    }
                    if ($.isFunction(options.success)) {
                        options.success(data);
                    }
                }
            };
            if (options.contentType) {
                ajaxOptions.contentType = options.contentType;
            }
            $.ajax(ajaxOptions);
        },
        ajaxFileUpload: function(options, importData) {
            var ajaxOptions = {
                url: $.mr.contextPath + options.url,
                data: importData,
                secureuri: false,
                fileElementId: options.fileId,
                dataType: options.dataType || "json",
                success: options.success,
                complete: function(data) {
                    if ($.isFunction(options.complete)) {
                        options.complete(data);
                    }
                }
            };
            $.ajaxFileUpload(ajaxOptions);
        },
        storage: {
            get: function(name) {
                var cache = $.mr.storageCache[name];
                var storage = window.localStorage;
                if (cache) {
                    return cache;
                }
                if (storage) {
                    cache = storage[name];
                    if (cache) {
                        $.mr.storageCache[name] = cache;
                    }
                    return cache;
                } else {
                    return null;
                }
            },
            set: function(name, value) {
                var storage = window.localStorage;
                $.mr.storageCache[name] = value;
                if (storage) {
                    storage[name] = value;
                }
            }
        },
        tree: {
            create: function(options, data) {
                var zNodes;
                var $tree = $(options.selector);
                var setting = {
                    check: {
                        enable: options.checkEnabled !== false
                    },
                    edit: {
                        enable: options.editEnabled !== false
                    },
                    view: {
                        selectedMulti: options.selectedMulti !== false
                    },
                    data: {
                        simpleData: {
                            enable: true,
                            idKey: 'id',
                            pIdKey: 'pId',
                            rootPId: 0
                        }
                    },
                    async: {
                        url: $.mr.contextPath + options.url,
                        autoParam: ['id'],
                        type: options.type || 'get',
                        enable: options.url,
                        dataType: 'json',
                        dataFilter: options.dataFilter || null
                    },
                    callback: options.callback
                };
                if (data) {
                    zNodes = data;
                } else {
                    zNodes = [];
                }
                return $.fn.zTree.init($tree, setting, zNodes);
            },
            getSelectedNode: function(zTree) {
                return zTree.getSelectedNodes();
            },
            getNodeByTId: function(zTree, id) {
                return zTree.getNodeByTId(id);
            },
            getNodeByParam: function(zTree, key, value, parentNode) {
                return zTree.getNodeByParam(key, value, parentNode);
            },
            refreshNode: function(zTree, node, callback) {
                zTree.reAsyncChildNodes(node, 'refresh', false, callback);
            },
            selectNode: function(zTree, node) {
                zTree.selectNode(node, false, false);
            }
        },
        table: {
            create: function(options) {
                var $table = $(options.selector);
                $table.bootstrapTable({
                    url: $.mr.contextPath + options.url,
                    method: options.method || 'get',
                    toolbar: options.toolbar || '#toolbar',
                    striped: true,
                    cache: options.cache === true,
                    pagination: options.pagination !== false,
                    sortable: options.sortable !== false,
                    sortName: options.sortName,
                    sortOrder: 'asc',
                    queryParams: options.queryParams || function(params) {
                        return {
                            limit: params.limit,
                            offset: params.offset,
                            sortOrder: params.order,
                            sortField: params.sort
                        };
                    },
                    ajaxOptions: options.ajaxOptions,
                    sidePagination: 'server',
                    pageNumber: 1,
                    pageSize: options.pageSize || 10,
                    pageList: options.pageList || [10, 25, 50, 100],
                    search: options.search === true,
                    contentType: 'application/json',
                    dataType: 'json',
                    strictSearch: true,
                    clickToSelect: false,
                    uniqueId: options.uniqueId,
                    showToggle: false,
                    cardView: false,
                    detailView: false,
                    columns: options.columns,
                    undefinedText: '',
                    rowStyle: options.rowStyle,
                    showColumns: options.showColumns === true,
                    showRefresh: options.showRefresh === true,
                    onCheck: options.onCheck,
                    onCheckAll: options.onCheckAll,
                    onUncheck: options.onUncheck,
                    onUncheckAll: options.onUncheckAll,
                    onLoadSuccess: options.onLoadSuccess,
                    onLoadError: options.onLoadError
                });
            },
            refresh: function(options) {
                var $table = $(options.selector);
                $table.bootstrapTable('refresh', options.params);
            },
            check: function(selector, indx) {
                var $table = $(selector);
                $table.bootstrapTable('check', indx);
            },
            getSelections: function(selector) {
                return $(selector).bootstrapTable('getSelections');
            }
        }
    };
});