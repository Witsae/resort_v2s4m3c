<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html> 
<html lang="ko"> 
<head> 
<meta charset="UTF-8"> 
<meta name="viewport" content="user-scalable=yes, initial-scale=1.0, maximum-scale=3.0, width=device-width" /> 
<title>Resort world</title>
 
<link href="../css/style.css" rel="Stylesheet" type="text/css">
 
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap-theme.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<script type="text/javascript">
  $(function() { // 자동 실행
    // id가 'btn_send'인 태그를 찾아 'click' 이벤트 처리자(핸들러)로 send 함수를 등록
    // $('#btn_checkID').on('click', checkID);  
    // document.getElementById('btn_checkID').addEventListener('click', checkID); 동일
    // $('#btn_DaumPostcode').on('click', DaumPostcode); // 다음 우편 번호
    // $('#btn_close').on('click',setFocus);   // Dialog 창을 닫은 후 포커스 이동
    // $('#btn_send').on('click', send);
    count_by_contentsno();
  });

  // jQuery ajax 요청
  function count_by_contentsno() {
    // $('#btn_close').attr("data-focus", "이동할 태그 선언");
    // var frm = $('#frm'); // id가 frm인 태그 검색
    // var id = $('#id', frm).val(); // frm 폼에서 id가 'id'인 태그 검색
    var params = 'contentsno=' + ${param.contentsno}; 
    var msg = '';

    // alert('params : ' + params);

      $.ajax({
        url: '../attachfile/count_by_contentsno.do', // spring execute
        type: 'get',  // post
        cache: false, // 응답 결과 임시 저장 취소
        async: true,  // true: 비동기 통신
        dataType: 'json', // 응답 형식: json, html, xml...
        data: params,      // 데이터
        success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
          //alert(rdata.cnt);
          var msg = "";
          
          if (rdata.cnt > 0) {
            msg = "현재 " + rdata.cnt + "건의 첨부 파일이 있습니다. <br><br>";
            msg += "<button type='button' onclick='delete_by_contentsno()' class='btn btn-danger'>첨부 파일 삭제 </button>";
          }
          $('#panel1').html(msg);              // 다이얼로그 출력
          $('#panel1').attr("class", "msg_warning");
        },
        // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
        error: function(request, status, error) { // callback 함수
          var msg = 'ERROR\n';
          msg += 'request.status: '+request.status + '\n';
          msg += 'message: '+error;
          console.log(msg);
        }
      });
      
      // 처리중 출력
       var gif = '';
      gif +="<div style='margin: 0px auto;'>";
      gif +="  <img src='./images/ani04.gif' style='width: 10%;'>";
      gif +="</div>";
      
      $('#panel1').html(gif);
      $('#panel1').show(); // 출력
      
  }

  

  function setFocus() {    // focus 이동
    var tag = $('#btn_close').attr('data-focus');   // 포커스를 적용할 태그 id 가져오기
    $('#' +  tag).focus();    // 포커스 지정
  }

  function send() {   // 회원가입처리
    if ($('#passwd').val() != $('#passwd2').val()) {
      msg = '입력된 패스워드가 일치하지 않습니다.<br>';
      msg += "패스워드를 다시 입력해주세요.<br>"; 
      
      $('#modal_content').attr('class', 'alert alert-danger'); // CSS 변경
      $('#modal_title').html('패스워드 일치 여부  확인'); // 제목 
      $('#modal_content').html(msg);  // 내용
      $('#modal_panel').modal();         // 다이얼로그 출력

      $('#btn_close').attr('data-focus', 'passwd');
      
      return false; // submit 중지
    }

    $('#frm').submit();
  }

  //jQuery ajax 요청
  function delete_by_contentsno() {
    // $('#btn_close').attr("data-focus", "이동할 태그 선언");
    // var frm = $('#frm'); // id가 frm인 태그 검색
    // var id = $('#id', frm).val(); // frm 폼에서 id가 'id'인 태그 검색
    var params = 'contentsno=' + ${param.contentsno}; 
    var msg = '';

    // alert('params : ' + params);

      $.ajax({
        url: '../attachfile/delete_by_contentsno.do', // spring execute
        type: 'post',  // post
        cache: false, // 응답 결과 임시 저장 취소
        async: true,  // true: 비동기 통신
        dataType: 'json', // 응답 형식: json, html, xml...
        data: params,      // 데이터
        success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
          //alert(rdata.cnt);
          var msg = "";
          
          if (rdata.cnt > 0) {
            msg = rdata.cnt + "건의 첨부 파일을 삭제했습니다";
          }
          $('#panel1').html(msg);              // 다이얼로그 출력
          $('#panel1').attr("class", "msg_warning");
        },
        // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
        error: function(request, status, error) { // callback 함수
          var msg = 'ERROR\n';
          msg += 'request.status: '+request.status + '\n';
          msg += 'message: '+error;
          console.log(msg);
        }
      });
      
      // 처리중 출력
       var gif = '';
      gif +="<div style='margin: 0px auto;'>";
      gif +="  <img src='./images/ani04.gif' style='width: 10%;'>";
      gif +="</div>";
      
      $('#panel1').html(gif);
      $('#panel1').show(); // 출력
      
  }

  

  function setFocus() {    // focus 이동
    var tag = $('#btn_close').attr('data-focus');   // 포커스를 적용할 태그 id 가져오기
    $('#' +  tag).focus();    // 포커스 지정
  }

  function send() {   // 회원가입처리
    if ($('#passwd').val() != $('#passwd2').val()) {
      msg = '입력된 패스워드가 일치하지 않습니다.<br>';
      msg += "패스워드를 다시 입력해주세요.<br>"; 
      
      $('#modal_content').attr('class', 'alert alert-danger'); // CSS 변경
      $('#modal_title').html('패스워드 일치 여부  확인'); // 제목 
      $('#modal_content').html(msg);  // 내용
      $('#modal_panel').modal();         // 다이얼로그 출력

      $('#btn_close').attr('data-focus', 'passwd');
      
      return false; // submit 중지
    }

    $('#frm').submit();
  }
</script>

</head> 
 
<body>
<jsp:include page="/menu/top.jsp" flush='false' />
  <DIV class='title_line'>
    카테고리 이름
  </DIV>

  <ASIDE class="aside_left">
    <A href=''>카테고리 그룹</A> > 
    <A href=''>카테고리</A> > 수정
  </ASIDE>
  <ASIDE class="aside_right">
    <A href=''>목록</A>
    <!-- <span class='menu_divide' >│</span> --> 
  </ASIDE> 
  
  <div class='menu_line'></div>
 
  <FORM name='frm' method='POST' action='./delete.do'>
      <input type='hidden' name='contentsno' value='${param.contentsno}'>
      <input type="hidden" name="cateno" value="${param.cateno }">
      
      <div id='panel1' style="width: 60%; text-align: center; margin: 20px auto;"></div>
            
      <div class="form-group">   
        <div class="col-md-12" style='text-align: center; margin: 30px;'>
          삭제 되는글: ${contentsVO.title }<br><br>
          삭제하시겠습니까? 삭제하시면 복구 할 수 없습니다.<br><br>
          
        <div class="form-group">   
        <div class="col-md-12">
          <input type='password' class="form-control" name='passwd'  value='' placeholder="패스워드" style='width: 20%; margin: 10px auto;'>
        </div>
      </div>
          
          <button type = "submit" class="btn btn-info">삭제 진행</button>
          <button type = "button" onclick = "history.back()" class="btn btn-info">취소</button>
        </div>
      </div>   
  </FORM>
 
<jsp:include page="/menu/bottom.jsp" flush='false' />
</body>
 
</html>