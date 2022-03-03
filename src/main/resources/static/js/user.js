let index = {
	init: function() {
		$("#btn-save").on("click", () => {
			this.save();
		});
	},
	
	save: function() {
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		}
		
		$.ajax({ // ajax 통신을 이용해서 3개의 데이터 JSON으로 변경하며 INSERT 요청			
		}).done({
			
		}).fail({
			
		});
		
	}
}

index.init();