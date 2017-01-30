(function() {
    'use strict';

    angular
        .module('meetmeApp')
        .controller('ApplicationDialogController', ApplicationDialogController);

    ApplicationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Application', 'User'];

    function ApplicationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Application, User) {
        var vm = this;

        vm.application = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        //vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.application.id !== null) {
                Application.update(vm.application, onSaveSuccess, onSaveError);
            } else {
                Application.save(vm.application, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('meetmeApp:applicationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
