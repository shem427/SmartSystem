$(function() {
    var _self;
    $.mr.index = {
        init: function() {
            // menu link.
            var $updateProfileLink = $('#updateProfileLink');
            var $updateProfileSettingLink = $('#updateProfileSettingLink');
            var $changePasswordLink = $('#changePasswordLink');
            var $changePasswordSettingLink = $('#changePasswordSettingLink');
            var $updateThresholdSettingLink = $('#updateThresholdSettingLink');
            var $logoutLink = $('#logoutLink');

            var $dashboardLink = $('#dashboardLink');
            var $userLink = $('#userLink');
            var $unitLink = $('#unitLink');
            var $sensorLink = $('#sensorLink');
            var $unKnownsensorLink = $('#$unKnownsensorLink');
            var $warnLink = $('#warnLink');
            var $hospitalStatusLink = $('#hospitalStatusLink');
            var $statusSearchLink = $('#statusSearchLink');

            var menuItems = $('.menuItem');

            var changePassword = function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $changePasswordSettingLink.addClass('active');
                _self.getWrapperPage('setting/changePassword');
            };
            var updateProfile = function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $updateProfileSettingLink.addClass('active');
                _self.getWrapperPage('setting/updateProfile');
            };

            // dashboard link
            $hospitalStatusLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $hospitalStatusLink.addClass('active');
                _self.getWrapperPage('dashboard/index');
            });

            // user link
            $userLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $userLink.addClass('active');
                _self.getWrapperPage('user/index');
            });

            // unit link
            $unitLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $unitLink.addClass('active');
                _self.getWrapperPage('unit/index');
            });

            // sensor link
            $sensorLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $sensorLink.addClass('active');
                _self.getWrapperPage('sensor/index');
            });

            // unknown sensor link
            $unKnownsensorLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $unKnownsensorLink.addClass('active');
                _self.getWrapperPage('sensorUnknown/index');
            });

            // update profile
            $updateProfileLink.click(updateProfile);
            $updateProfileSettingLink.click(updateProfile);

            // change password
            $changePasswordLink.click(changePassword);
            $changePasswordSettingLink.click(changePassword);

            $updateThresholdSettingLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $updateThresholdSettingLink.addClass('active');
                _self.getWrapperPage('setting/threshold');
            });

            $logoutLink.click(function(e) {
                e.preventDefault();
                $.mr.messageBox.confirm($.mr.resource.LOGOUT_CONFIRM, '', {
                    yes: function() {
                        $('#logoutForm').submit();
                    }
                });
            });

            $warnLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $warnLink.addClass('active');
                _self.getWrapperPage('warn/index');
            });

            $dashboardLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $dashboardLink.addClass('active');
                _self.getWrapperPage('status/hospitalIndex');
            });

            $statusSearchLink.click(function(e) {
                e.preventDefault();
                menuItems.removeClass('active');
                $statusSearchLink.addClass('active');
                _self.getWrapperPage('status/statusIndex');
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
                    history.pushState(data, null, location.href);
                }
            });
        }
    };

    _self = $.mr.index;
});
