function mInit() {
	document.getElementById("Log_phone").focus();
	if (document.getElementById("msg").value != null) {
		alert(document.getElementById("msg").value);
	}

}

function passwordFind(password) {
	//아이디 찾기인지 비밀번호 찾기인지 카테고리히든값에 저장
	document.getElementById("user_cate").value = password;
	//page 카테고리 지정
	document.getElementById("page").value = 'pageSms';
	//폰번호값을 히든값에 저장
	document.getElementById("user_phone").value = document.getElementById("Log_phone").value;
	//아이디 값 을 히든값에 저장
	document.getElementById("user_id").value = document.getElementById("Log_Id").value;
	document.forms[0].submit();
}
function controlSend(page) {
	document.getElementById("page").value = page;
	document.getElementsByTagName("form")[0].submit();
}