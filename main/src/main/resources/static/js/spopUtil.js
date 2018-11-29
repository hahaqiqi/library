function spopSucess(msg) {
    spop({
        template: '<h4 style="background-color: yellow" class="spop-title"></h4>' + msg ,
        group: 'submit-status',
        style: 'success',
        autoclose:3000
    });
}

function spopBatchHint(msg) {
    spop({
        template: '<h4 class="spop-title"></h4>' + msg ,
        group: 'submit-status',
    });
}

function spopFail(msg,msgError) {
    spop({
        template:  '<h4 class="spop-title">'+msg+'</h4>'+msgError,
        group: 'submit-status',
        style: 'error',
    });
}

function spopTs(msg) {
    spop({
        template: '<h4 style="background-color: yellow" class="spop-title"></h4>' + msg ,
        group: 'submit-status',
        autoclose:3000
    });
}