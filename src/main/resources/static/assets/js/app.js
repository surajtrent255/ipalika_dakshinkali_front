$(function() {
    var LalitpurApp = function() {
        $(document).ajaxSend(function(event, jqxhr, settings) {
            jqxhr.setRequestHeader('X-CSRF-TOKEN', $('meta[name="csrf-token"]').attr('content'));
        });
        if ($.support.pjax) {
            $.pjax.defaults.timeout = 9000;
            $.pjax.defaults.dataType = 'html';
            $(document).pjax('a:not(.no-pjax)', '#pjax-container');
        }
        this.numeric($(".numeric"));
    };
    LalitpurApp.prototype.numeric = function(e){
        return e.each(function(){
            $(this).keydown(function(e){
                var key = e.charCode || e.keyCode || 0;
                return (
                    key == 8 || 
                    key == 9 ||
                    key == 13 ||
                    key == 46 ||
                    key == 110 ||
                    key == 190 ||
                    (key >= 35 && key <= 40) ||
                    (key >= 48 && key <= 57) ||
                    (key > 96 && key <= 105));
            });
        });
    },
    LalitpurApp.prototype.notification = function(t, m){
        Swal.fire({
          type: t,
          title: t,
          text: m,
        });
    },
    LalitpurApp.prototype.submitForm = function(e) {
        var _this = this;
        window.buttonVal = $("input[type='submit']").val();
        $("input[type='submit']").prop("disabled", true).val("loading...");
        $.ajax({
            type: "POST",
            dataType: "json",
            url: e.u,
            data: e.d,
            enctype: 'multipart/form-data',
            success: function(res) {
                if (res.status) {
                    _this.notification('success', res.message);
                    if (res.reload) {
                        $.pjax({
                            url: res.reload,
                            container: '#pjax-container',
                            dataType: 'html'
                        })
                    }
                } else {
                    _this.notification('warning', res.message);
                }
                $("input[type='submit']").prop("disabled", false).val(window.buttonVal);
            },
            error: function(res) {
                if (res.status === 422) {
                    $("label").removeClass("text-danger").attr("title", "");
                    $(".validation-holder").remove();
                    $errors = res.responseJSON.errors;
                    $.each($errors, function(key, value) {
                        var e = '';
                        $.each(value, function(i, v) {
                            if ($("[name='" + key + "']").length || $("."+key).length) {
                                e += v + "<br>";
                            } else {
                                _this.notification('error', value[0]);
                            }
                        });
                        if ($("[name='" + key + "']").length && $("[name='" + key + "']").closest('.col-md-4.col-lg-3').find("label").length) {
                            $("[name='" + key + "']").closest('.col-md-4.col-lg-3').find("label").addClass("text-danger").append('<span class="validation-holder">&nbsp;<i class="fa fa-question-circle text-danger" title="' + e + '" data-toggle="tooltip" tooltip-placement="auto"></i><span>');
                        }
                        else if ($("[name='" + key + "']").length && $("[name='" + key + "']").closest('.col-md-4.col-lg-4').find("label").length) {
                            $("[name='" + key + "']").closest('.col-md-4.col-lg-4').find("label").addClass("text-danger").append('<span class="validation-holder">&nbsp;<i class="fa fa-question-circle text-danger" title="' + e + '" data-toggle="tooltip" tooltip-placement="auto"></i><span>');
                        }
                        else if ($("."+key).length && $("."+key).closest('.col-md-4.col-lg-4').find("label").length) {
                            $("."+key).closest('.col-md-4.col-lg-4').find("label").addClass("text-danger").append('<span class="validation-holder">&nbsp;<i class="fa fa-question-circle text-danger" title="' + e + '" data-toggle="tooltip" tooltip-placement="auto"></i><span>');
                        }
                        else if ($("."+key).length && $("."+key).closest('.col-md-4.col-lg-3').find("label").length) {
                            $("."+key).closest('.col-md-4.col-lg-3').find("label").addClass("text-danger").append('<span class="validation-holder">&nbsp;<i class="fa fa-question-circle text-danger" title="' + e + '" data-toggle="tooltip" tooltip-placement="auto"></i><span>');
                        }
                    });
                    $('[data-toggle="tooltip"]').tooltip({
                        html: true
                    });
                }
                $("input[type='submit']").prop("disabled", false).val(window.buttonVal);
            },
            cache: false,
            contentType: false,
            processData: false
        });
    },
    LalitpurApp.prototype.delete = function(e) {
        var _this = this;
        Swal.fire({
            title: 'Delete',
            text: "Are you sure you want to delete selected household?  This process cannot be undone once in process",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.value) {
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: e.u,
                    success: function(res) {
                        if (res.status) {
                            _this.notification('success', res.message);
                        } else {
                            _this.notification('warning', res.message);
                        }
                        window.grid
                         .search('')
                         .columns().search('')
                         .draw();
                    },
                    complete: function(xhr, textStatus) {
                        if (xhr.status == 500) {
                            _this.notification('error', "Cannot process the action. Data have relation with others.");
                        } else if (xhr.status == 403) {
                            _this.notification('error', "You don't have the proper credentials to access this action.");
                        }
                    }
                });
            }
        })
    },
    LalitpurApp.prototype.expired = function(e) {
        var _this = this;
        Swal.fire({
            title: e.t,
            text: e.m,
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, do it!'
        }).then((result) => {
            if (result.value) {
                $.ajax({
                    type: "DELETE",
                    dataType: "json",
                    url: e.u,
                    success: function(res) {
                        if (res.status) {
                            _this.notification('success', res.message);
                        } else {
                            _this.notification('warning', res.message);
                        }
                        window.grid
                         .search('')
                         .columns().search('')
                         .draw();
                    },
                    complete: function(xhr, textStatus) {
                        if (xhr.status == 500) {
                            _this.notification('error', "Cannot process the action. Data have relation with others.");
                        } else if (xhr.status == 403) {
                            _this.notification('error', "You don't have the proper credentials to access this action.");
                        }
                    }
                });
            }
        })
    },
    $.LalitpurApp = new LalitpurApp, $.LalitpurApp.Constructor = LalitpurApp
});
$(function() {
    var app = $.LalitpurApp;
	$(document).on('submit', 'form[data-ajax]', function(e) {
		app.submitForm({
            d: new FormData(this),
            u: $(this).data("action"),
        });
	})
    $(document).off("click", ".delete").on("click", ".delete", function (e) {
        app.delete({u: $(this).data('action')});
    });
    $(document).off("click", ".expired").on("click", ".expired", function (e) {
        app.expired({u: $(this).data('action'), t: $(this).data('title'), m: $(this).data('message')});
    });
});
google.load("elements", "1", {
    packages: "transliteration"
});
function onLoad() {
    var options = {
        sourceLanguage: 'en',
        destinationLanguage: ['ne'],
        shortcutKey: 'ctrl+g',
        transliterationEnabled: true
    };
    var control =new google.elements.transliteration.TransliterationControl(options);
    var uniElement = $(".unicode input[type='text'], #grid_filter input");
    control.makeTransliteratable(uniElement);
    // control.showControl('translControl');
}
$(window).load(function(){
    onLoad();
});
$(document).on('pjax:complete', function() {
    onLoad();
})