let index = {
	init: function() {
		$("#btn-save").on("click", () => { // function() {}, () => {} this 바인딩
			this.save();
		});
	},
	
	save: function() {
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};
		
		// ajax 호출 시 default가 비동기 호출
		// ajax 통신을 이용해서 3개의 데이터 JSON으로 변경하며 INSERT 요청
		// ajax 통신 성공 후 서버가 JSON을 반환하면 자동으로 자바스크립트 객체로 변환
		$.ajax({ 			
			type: "POST",
			url: "/blog/api/user",
			contentType: "application/json; charset=UTF-8", // 요청 데이터의 MIME 타입
			data: JSON.stringify(data), // 자바스크립트 객체 -> JSON 문자열, HTTP body
			dataType: "json" // 응답 데이터의 타입, 기본적으로 모든 것은 문자열, JSON이면 자바스크립트 객체로 변경
		}).done(function(resp){ // 응답 결과
			alert("회원가입 완료");
			//console.log(resp);
			location.href = "/blog";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	}
}

index.init();