$(document).ready(function() {
	$("#addContactForm").on("submit", function(event){
		event.preventDefault();
        var form = $("#addContactForm")[0];
        var formData = new FormData(form);
		$.ajax({
			url: "/user/processContact",
			method: "post",
			data: formData,
			enctype: 'multipart/form-data',
		    processData: false,
		    contentType: false,
			success: function(data){
				console.log(data);
				if(data==="done") {
					$("#status").show();
                    $("#status").removeClass("text-danger");
                    $("#status").addClass("text-success");
                    $("#status").html("Contact add successfully.");
                    $("#name").val("");
                    $("#nickName").val("");
                    $("#phone").val("");
                    $("#email").val("");
                    $("#occupation").val("");
                    $("#image").val("");
                    $("#description").val("");
				} else if (data==="fileError") {
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Image doesn't upload, try again later.");
				} else if (data==="invalidFile") {
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Image format invald, Please provie only jpg/jpeg file.");
				} else if (data==="noName") {
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Please provide contact name.");
				} else{
					$("#status").show();
                    $("#status").removeClass("text-success");
                    $("#status").addClass("text-danger");
                    $("#status").html("Something went wrong.");
				}
			},
			error: function(){
				alert("Something went wrong! Please try again later");
			}
		});
	});
});