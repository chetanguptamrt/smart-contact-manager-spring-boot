$(document).ready(()=>{
	$("#changePassword").on("submit", function(event){
        event.preventDefault();
        var f = $(this).serialize();
		$.ajax({
			url: "/user/changePassword",
			method: "POST",
			data: f,
			success: function(data){
				if(data==="done"){
					$("#status").show();
                    $("#status").removeClass("text-danger");
                    $("#status").addClass("text-success");
                    $("#status").html("Password successfully changed");
				} else if(data==="notMatch"){
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Old password not matched")
				} else {
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Something went wrong.")
				}
			},
			error: function(){
				alert("Something went wrong, Please try again later.");
			}
		});
	});
	$("#changePhoto").on("submit", function(event){
		event.preventDefault();
        var form = $("#changePhoto")[0];
        var formData = new FormData(form);
		$.ajax({
			url: "/user/processProfilePhoto",
			method: "post",
			data: formData,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
			success: function(data){
				console.log(data);
				if(data==="done") {
					$("#pstatus").show();
                    $("#pstatus").removeClass("text-danger");
                    $("#pstatus").addClass("text-success");
                    $("#pstatus").html("Profile Update successfully.");
				} else if (data==="fileError") {
					$("#pstatus").show();
                    $("#pstatus").removeClass("text-success");
                    $("#pstatus").addClass("text-danger");
                    $("#pstatus").html("Image doesn't upload, try again later.");
				} else if (data==="invalidFile") {
					$("#pstatus").show();
                    $("#pstatus").removeClass("text-success");
                    $("#pstatus").addClass("text-danger");
                    $("#pstatus").html("Image format invald, Please provie only jpg/jpeg file.");
				} else{
					$("#pstatus").show();
                    $("#pstatus").removeClass("text-success");
                    $("#pstatus").addClass("text-danger");
                    $("#pstatus").html("Something went wrong.");
				}
			},
			error: function(){
				alert("Something went wrong! Please try again later");
			}
		});
	});
});

function deleteAccount() {
    if(confirm("Do you want to delete your account?")){
        $.ajax({
           url: "/user/deleteAccount",
           method: "post",
           success: function(data){
               if(data.trim()==="done"){
                   location.href = "/signin";
               } else{
                   alert("Something went wrong!");
               }
           },
           error: function(){
                alert("Something went wrong!");
           }
        });
    }
}