$(function() {
    var _self;
    $.mr.index = {
        init: function() {
            // menu link.
            var $updateProfileLink = $('#updateProfileLink');
            var $updateProfileSettingLink = $('#updateProfileSettingLink');
            var $changePasswordLink = $('#changePasswordLink');
            var $changePasswordSettingLink = $('#changePasswordSettingLink');
            var $logoutLink = $('#logoutLink');

            var $dashboardLink = $('#dashboardLink');
            var $sensorDetail1Link = $('#sensorDetail1Link');
            var $sensorDetail2Link = $('#sensorDetail2Link');
            var $sensorDetail3Link = $('#sensorDetail3Link');
            var $sensorDetail4Link = $('#sensorDetail4Link');
            var $sensorDetail5Link = $('#sensorDetail5Link');
            var $userLink = $('#userLink');

            var menuItems = $('.menuItem');

            var changePassword = function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $changePasswordSettingLink.addClass('active');
                _self.getWrapperPage('setting/changePassword');
            };

            // dashboard link
            $dashboardLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $dashboardLink.addClass('active');
                _self.getWrapperPage('dashboard/index');
            });

            // user link
            $userLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $userLink.addClass('active');
                _self.getWrapperPage('user/index');
            });

            // change password
            $changePasswordLink.click(changePassword);
            $changePasswordSettingLink.click(changePassword);

            $logoutLink.click(function(e) {
                e.preventDefault();
                $.mr.messageBox.confirm($.mr.resource.LOG_OUT_CONFIRM, '', {
                    yes: function() {
                        $('#logoutForm').submit();
                    }
                });
            });

            // click dashboard link.
            $dashboardLink.trigger('click');
        },
        /**
         * get page wrapper contents.
         * @param url - url.
         */
        getWrapperPage: function(url) {
            var $pageWrapper = $('#page-wrapper');
            $.mr.ajax({
                url: url,
                type: 'get',
                dataType: 'html',
                success: function(data) {
                    $pageWrapper.empty().append(data);
                }
            });
        }
    };

    _self = $.mr.index;
});