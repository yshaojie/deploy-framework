(function() {
    'use strict';

    angular
        .module('app.user')
        .controller('UserLoginController', UserLoginController);

    UserLoginController.$inject = ['UserService'];
    function UserLoginController(UserService) {
        var vm = this;

        active();
        
        function active() {
            vm.login = function () {
                UserService.login(vm.account.email,vm.account.password);
            }
        }

    }


})();
