(function() {
    'use strict';

    angular
        .module('app.instance')
        .controller('InstanceController', ['InstanceService','$scope','ngDialog',
            function InstanceController(InstanceService,$scope,ngDialog) {
            var vm = this;

            active();

            function active() {
                InstanceService.queryAllInstanceGroup(function (data) {
                    $scope.instanceGroup = data;
                });
            }

            vm.group_click = function (index) {
                $scope.select_group = $scope.instanceGroup[index];
                InstanceService.queryServerInstances($scope.select_group.id,function (data) {
                    vm.serverInstances = data;
                    vm.instance_status={};
                    angular.forEach(vm.serverInstances, function(serverInstance, key){
                        vm.instance_status[serverInstance.id] = false;
                    });

                })
            }

            vm.batchDeploy = function () {
                var ids = [];
                angular.forEach(vm.instance_status, function(selected, serverInstanceId){
                    if(selected){
                        ids.push(serverInstanceId);
                    }
                });

                InstanceService.deployServers(ids,function (data) {
                    alert(angular.toJson(data))
                    ngDialog.open({ template: 'firstDialogId',
                        data:data,
                        className: 'ngdialog-theme-default' });
                });

            }

            vm.addInstance = function () {
                InstanceService.addInstance($scope.select_group.id,$scope.add_instance_ip,function (data) {
                    $scope.add_instance_ip = undefined;
                    alert(data.message)
                });
            }
        }]);
})();
