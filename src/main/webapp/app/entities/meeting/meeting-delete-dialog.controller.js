(function() {
    'use strict';

    angular
        .module('meetmeApp')
        .controller('MeetingDeleteController',MeetingDeleteController);

    MeetingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Meeting'];

    function MeetingDeleteController($uibModalInstance, entity, Meeting) {
        var vm = this;

        vm.meeting = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Meeting.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
