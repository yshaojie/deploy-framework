/**=========================================================
 * Module: user.service.js
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('app.user')
        .service('UserService', UserService);
    UserService.$inject = ['$http','$location'];
    function UserService($http,$location) {
        this.login = login;

        function login(account, password) {
            $http
                .post("/users/login",{"account":account,"password":password})
                .success(function (data) {
                    if(data.code == 1000000){
                        $location.path("/#/app/question");
                    }else {
                        alert(data.message);
                    }
                });
        }


        this.logout = logout;

        function logout(onReady, onError) {
            onError = onError || function() {
                    //alert('Logout Fail!');
                };
            $http
                .post("/users/logout")
                .success(function (data) {
                    if(data.code == 1000000){
                        $location.path("/#/page/login");
                    }else {
                        //location.reload("/#/page/login");
                    }
                })
                .error(onError);
        }
    }
})();
