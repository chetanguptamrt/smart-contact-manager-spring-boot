function deleteContactById(contactId, totalContact){
	$.ajax({
		url: "/user/deleteContact/"+contactId,
		method: "POST",
		success: function(data){
			if(data==="done"){
				alert("Your contact successfully deleted.");
			} else {
				alert("Something went wrong!");
			}
			if(totalContact%10==1) {
				location.href="/user/viewContact/0"; 
			} else {
				location.reload();		
			}	
		},
		error: function(){
			alert("Something went wrong, Please try again later.");
		}
	});
}