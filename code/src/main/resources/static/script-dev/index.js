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