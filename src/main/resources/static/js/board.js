let index = {
	init: function() {
		$("#btn-save").on("click", () => {
			this.save();
		});
		
		$("#btn-delete").on("click", () => {
			this.deleteById();
		});		
		
		$("#btn-update").on("click", () => {
			this.update();
		});		
	},
	

	save: function() {
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};

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
	},
	
	deleteById: function() {
		let id = $("#id").text();
		
		$.ajax({
			type: "DELETE",
			url: `/api/board/${id}` ,
			dataType: "json"
		}).done(function(resp) { // 응답 결과
			alert("글삭제 완료");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},
	
	update: function() {
		let id = $("#id").val();
		
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};
		
		$.ajax({
			type: "PUT",
			url: `/api/board/${id}`,
			contentType: "application/json; charset=UTF-8", 
			data: JSON.stringify(data),
			dataType: "json"
		}).done(function(resp) { // 응답 결과
			alert("글수정 완료");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},	
}

index.init();