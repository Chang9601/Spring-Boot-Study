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
		
		$("#btn-reply-save").on("click", () => {
			this.replySave();
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
	
	replySave: function() {
		let data = {
			userId: $("#userId").val(),
			boardId: $("#boardId").val(),
			content: $("#reply-content").val()
		};
		let boardId = $("#boardId").val();
		
		$.ajax({
			type: "POST",
			url: `/api/board/${data.boardId}/reply`,
			contentType: "application/json; charset=UTF-8", 
			data: JSON.stringify(data),
			dataType: "json"
		}).done(function(resp) { // 응답 결과
			alert("댓글쓰기 완료");
			location.href = `/board/${data.boardId}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},	
	
	replyDelete: function(boardId, replyId) {
		$.ajax({
			type: "DELETE",
			url: `/api/board/${boardId}/reply/${replyId}`,
			dataType: "json"
		}).done(function(resp) { // 응답 결과
			alert("댓글삭제 완료");
			location.href = `/board/${boardId}`;
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},	
}

index.init();