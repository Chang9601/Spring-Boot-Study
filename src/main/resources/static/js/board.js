let index = {
	init: function() {
		$("#btn-save").on("click", () => {
			this.save();
		});
	},

	save: function() {
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};
		
		console.log(JSON.stringify(data));

		$.ajax({
			type: "POST",
			url: "/api/board",
			contentType: "application/json; charset=UTF-8", 
			data: JSON.stringify(data),
			dataType: "json"
		}).done(function(resp) { // 응답 결과
			alert("글쓰기 완료");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	}
}

index.init();