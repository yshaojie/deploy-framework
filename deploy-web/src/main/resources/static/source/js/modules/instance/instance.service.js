/**=========================================================
 * Module: project.service.js
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('app.instance')
        .service('InstanceService', InstanceService);
    InstanceService.$inject = ['$http','$location'];

    function InstanceService($http) {
        this.queryAllInstanceGroup = queryAllInstanceGroup;


        queryAllInstanceGroup = function (onSuccess) {
            $http
                .get("/instance_group/list")
                .success(function (data) {
                    onSuccess(data);
                });
        }
    }

})();
