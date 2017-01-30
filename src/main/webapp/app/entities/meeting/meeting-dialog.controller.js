(function() {
    'use strict';

    angular
        .module('meetmeApp')
        .controller('MeetingDialogController', MeetingDialogController);

    MeetingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Meeting', 'Application', 'User'];

    function MeetingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Meeting, Application, User) {
        var vm = this;

        vm.meeting = entity;
        vm.clear = clear;
        vm.save = save;
        vm.applications = Application.query();
        //vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.meeting.id !== null) {
                Meeting.update(vm.meeting, onSaveSuccess, onSaveError);
            } else {
                Meeting.save(vm.meeting, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('meetmeApp:meetingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
