$(document).ready(function(){
    $("#forgotForm").on("submit",function(event){
        event.preventDefault();
        var f = $(this).serialize();
        $.ajax({
            url: "/forgotOTP",
            data: f,
            method: "post",
            success: function(data){
                if(data.trim()==="noUser") {
                    $("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("No user found!");
                } else if(data.trim()==="done") {
                    $("#status").show();
                    $("#status").removeClass("text-danger");
                    $("#status").addClass("text-success");
                    $("#status").html("OTP send on email");
                    $("#oemail").val($("#email").val().trim());
                    $("#email").prop('disabled', true);
                    $("#send-otp-btn").prop('disabled', true);
                } else {
                    $("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Something went wrong!");
                }
            },
            error: function(){
                $("#status").show();
                $("#status").removeClass("text-success");
                $("#status").addClass("text-danger");
                $("#status").html("Something went wrong!");
            }
        });
    });
    $("#otpConfirm").on("submit",function(event){
        event.preventDefault();
        var f = $(this).serialize();
        $.ajax({
            url: "/forgotPasswordConfirm",
            data: f,
            method: "post",
            success: function(data){
                if(data.trim()==="invalid") {
                    $("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("OTP not matched!");
                } else if(data.trim()==="done") {
                    location.href = "/signin";
                } else {
                    $("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Something went wrong!");
                }
            },
            error: function(){
                $("#status").show();
                $("#status").removeClass("text-success");
                $("#status").addClass("text-danger");
                $("#status").html("Something went wrong!");
            }
        });
    });
});