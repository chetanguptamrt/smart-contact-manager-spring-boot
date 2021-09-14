const searchContact=()=>{
	let query = $("#search").val();
	if(query == ""){
		$(".search-result").hide();
	} else {
		
		let url = "http://localhost:8080/search/"+query;
		fetch(url).then((response) => {
			return response.json();
		}).then((data) => {
			let text = '<div class="list-group">';
			data.forEach((contact) => {
				text += '<a class="list-group-item list-group-item-action" href="/user/showContact/'+contact.cid+'">'+ contact.name +'</a>';
			});
			text += '</div>';
			$(".search-result").html(text);
		});		
		$(".search-result").show();
		
	}
}