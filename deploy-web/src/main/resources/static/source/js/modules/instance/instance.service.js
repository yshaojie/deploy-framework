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
        this.queryServerInstances = queryServerInstances;
        this.deployServers = deployServers;
        this.startServers = startServers;
        this.restartServers = restartServers;
        this.startServers = startServers;
        this.deleteServers = deleteServers;


         function queryAllInstanceGroup(onSuccess) {
            $http
                .get("/instance_group/list")
                .success(function (data) {
                    onSuccess(data);
                });
        }

        /**
         * 根据实例组id查询实例列表
         * @param groupId
         */
        function queryServerInstances(groupId,onSuccess) {
            $http
                .get("/instance/list",{
                    params:{
                        groupId:groupId
                    }
                })
                .success(function (data) {
                    onSuccess(data);
                });
        }

        function deployServers(ids,onSuccess) {
            $http
                .put("/instance/deploy",{},{
                    params:{
                        serverInstanceIds:ids
                    }
                })
                .success(function (data) {
                    onSuccess(data);
                });
        }

        function startServers(ids,onSuccess) {
            $http
                .put("/instance/deploy",{},{
                    params:{
                        serverInstanceIds:ids
                    }
                })
                .success(function (data) {
                    onSuccess(data);
                });
        }

        function restartServers(ids,onSuccess) {
            $http
                .get("/instance/list",{
                    params:{
                        groupId:groupId
                    }
                })
                .success(function (data) {
                    onSuccess(data);
                });
        }

        function startServers(ids,onSuccess) {
            $http
                .get("/instance/list",{
                    params:{
                        groupId:groupId
                    }
                })
                .success(function (data) {
                    onSuccess(data);
                });
        }

        function deleteServers(ids,onSuccess) {
            $http
                .get("/instance/list",{
                    params:{
                        groupId:groupId
                    }
                })
                .success(function (data) {
                    onSuccess(data);
                });
        }
    }

})();
