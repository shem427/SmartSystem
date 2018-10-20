$(function() {
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

            var changePassword = function(e) {
                e.preventDefault();
                $.mr.index.getWrapperPage(contextPath + 'setting/changePassword');
            };

            // dashboard link
            $dashboardLink.click(function(e) {
                e.preventDefault();
                $.mr.index.getWrapperPage(contextPath + 'dashboard/index');
            });

            // user link
            $userLink.click(function(e) {
                e.preventDefault();
                $.mr.index.getWrapperPage(contextPath + 'user/index');
            });

            // change password
            $changePasswordLink.click(changePassword);
            $changePasswordSettingLink.click(changePassword);

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


});