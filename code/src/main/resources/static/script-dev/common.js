$(function() {
    // csrf
    var header = $("meta[name='_csrf_header']").attr("content");
    var token =$("meta[name='_csrf']").attr("content");
    // ajax global setting.
    $.ajaxSetup({
        beforeSend : function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        error: function() {
            console("error");
        }
    });

    $.mr = {
        contextPath: contextPath,
        messageBox: {

        },
        ajax: function(options) {
            var ajaxOptions = {
                url: options.url,
                traditional : true,
                type: options.type || "get",
                data: options.data || {},
                dataType: options.dataType || "json",
                async: options.async === false ? false : true,
                cache: false,
                complete: function (data) {
                    if ($.isFunction(options.complete)) {
                        options.complete(data);
                    }
                },
                success: function (data) {
                    if (options.dataType === "html") {
                        try {
                            data = $.parseJSON(data);
                        } catch (ex) {
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
            $.ajax(ajaxOptions);
        }
    };
});