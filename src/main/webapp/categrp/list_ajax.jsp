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
  $(function(){
    $('#btn_send').on('click', send)
  });

  function send() {
    // $('#btn_close').attr("data-focus", "이동할 태그 선언");
    // var frm = $('#frm'); // id가 frm인 태그 검색
    // $('#frm').attr('action', './create_ajax.do');
    // var id = $('#id', frm).val(); // frm 폼에서 id가 'id'인 태그 검색
    // var params = 'contentsno=' + ${param.contentsno}; 
    var params = $('#frm').serialize();   // 직렬화, 폼의 데이터들을 키와 값의 구조로 조합

    var msg = '';

      $.ajax({
        url: './create_ajax.do', // spring execute
        type: 'post',  // post
        cache: false, // 응답 결과 임시 저장 취소
        async: true,  // true: 비동기 통신
        dataType: 'json', // 응답 형식: json, html, xml...
        data: params,      // 데이터
        success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
          if (rdata.cnt > 0) {
            msg = "「" + $('#name').val() + "」" + " 카테고리 그룹을 등록했습니다. ";
            msg += " <button type='button' onclick='location.reload(true)'>확인</button> ";
          } else {
            msg = "「" + $('#name').val() + "」" + " 카테고리 그룹 등록을 실패했습니다. ";
          }
          $('#panel1').html(msg);   // 다이얼로그 출력
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

  function update_form(categrpno) {
    // $('#btn_close').attr("data-focus", "이동할 태그 선언");
    // var frm = $('#frm'); // id가 frm인 태그 검색
    // var id = $('#id', frm).val(); // frm 폼에서 id가 'id'인 태그 검색
    // $('#frm').attr('action', './read_ajax.do');
    $('#btn_send').off("click");
    $('#btn_send').on('click', update_proc);
    var params = 'categrpno=' + categrpno; 
    // var params = $('#frm_create').serialize();   // 직렬화, 폼의 데이터들을 키와 값의 구조로 조합

    var msg = '';

      $.ajax({
        url: './read_ajax.do', // spring execute
        type: 'get',  // post
        cache: false, // 응답 결과 임시 저장 취소
        async: true,  // true: 비동기 통신
        dataType: 'json', // 응답 형식: json, html, xml...
        data: params,      // 데이터
        success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
          var frm = $('#frm');
          $('#categrpno', frm).val(rdata.categrpno);
          $('#name', frm).val(rdata.name);
          $('#seqno', frm).val(rdata.seqno);
          $('#visible', frm).val(rdata.visible);

          $('#btn_send').html("수정");
          $('#panel1').hide(); // 출력
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

  function cancel() {
    $('#categrpno', frm).val('');
    $('#name', frm).val('');
    $('#seqno', frm).val('');
    $('#visible', frm).val('Y');
    $('#btn_send').off("click");
    $('#btn_send').on('click', send);
    $('#btn_send').html('등록');
    $('#panel1').hide();
  }

  function update_proc() {
    var params = $('#frm').serialize();   // 직렬화, 폼의 데이터들을 키와 값의 구조로 조합

    var msg = '';

      $.ajax({
        url: './update_ajax.do', // spring execute
        type: 'post',  // post
        cache: false, // 응답 결과 임시 저장 취소
        async: true,  // true: 비동기 통신
        dataType: 'json', // 응답 형식: json, html, xml...
        data: params,      // 데이터
        success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
          if (rdata.cnt > 0) {
            msg = "「" + $('#name').val() + "」" + " 카테고리 그룹을 수정했습니다. ";
            msg += " <button type='button' onclick='location.reload(true)'>확인</button> ";
          } else {
            msg = "「" + $('#name').val() + "」" + " 카테고리 그룹 수정을 실패했습니다. ";
          }
          $('#panel1').html(msg);   // 다이얼로그 출력
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

  function delete_form(categrpno) {
    // $('#btn_close').attr("data-focus", "이동할 태그 선언");
    // var frm = $('#frm'); // id가 frm인 태그 검색
    // var id = $('#id', frm).val(); // frm 폼에서 id가 'id'인 태그 검색
    // $('#frm').attr('action', './read_ajax.do');
    // $('#btn_send').off("click");
    // $('#btn_send').on('click', delete_proc);
    var params = 'categrpno=' + categrpno; 
    // var params = $('#frm_create').serialize();   // 직렬화, 폼의 데이터들을 키와 값의 구조로 조합

    var msg = '';

      $.ajax({
        url: './read_ajax.do', // spring execute
        type: 'get',  // post
        cache: false, // 응답 결과 임시 저장 취소
        async: true,  // true: 비동기 통신
        dataType: 'json', // 응답 형식: json, html, xml...
        data: params,      // 데이터
        success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
          if (rdata.name.length > 0) {
            msg = "「" + rdata.name + "」" + " 카테고리 그룹을 삭제하시겠습니까?  <br>";
            msg += "카테고리 그룹을 삭제하면 복구할 수 없습니다. <br>"
            msg += " <button type='button' onclick=\"delete_proc(" + categrpno +", " + "'" + rdata.name + "'" + ")\">삭제 진행</button> ";
            msg += " <button type='button' onclick='cancel()'>취소</button> ";
          } else {
            msg = "「" + rdata.name + "」" + " 카테고리 그룹 정보가 없습니다. ";
            msg += " <button type='button' onclick='location.reload(true)'>확인</button> ";
          }
          $('#panel1').html(msg);   // 다이얼로그 출력
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

  function delete_proc(categrpno, name) {
    var params = 'categrpno=' + categrpno;
    //var params = $('#frm').serialize();   // 직렬화, 폼의 데이터들을 키와 값의 구조로 조합

    var msg = '';

      $.ajax({
        url: './delete_ajax.do', // spring execute
        type: 'post',  // post
        cache: false, // 응답 결과 임시 저장 취소
        async: true,  // true: 비동기 통신
        dataType: 'json', // 응답 형식: json, html, xml...
        data: params,      // 데이터
        success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
          if (rdata.cnt > 0) {
            msg = "「" + name + "」" + " 카테고리 그룹을 삭제했습니다. ";
            msg += " <button type='button' onclick='location.reload(true)'>확인</button> ";
          } else {
            msg = "「" + name + "」" + " 카테고리 그룹 삭제를 실패했습니다. ";
          }
          $('#panel1').html(msg);   // 다이얼로그 출력
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
 
  
</script>
 
</head> 
 
<body>
<jsp:include page="/menu/top.jsp" />
 
  <DIV class='title_line'>카테고리 그룹</DIV>
 
  <DIV id='panel_create' style='padding: 10px 0px 10px 0px; background-color: #F9F9F9; width: 100%; text-align: center;'>
    <FORM name='frm' id='frm' method='POST' action=''>
      <!-- <input type='hidden' name='lang' id='lang' value='en'> --> <!-- ko, en -->
      <input type='hidden' name='categrpno' id='categrpno' value='1'>
      <label>그룹 이름</label>
      <input type='text' name='name' id='name' value='' required="required" style='width: 25%;'>
 
      <label>순서</label>
      <input type='number' name='seqno' id='seqno' value='1' required="required" 
                min='1' max='1000' step='1' style='width: 5%;'>
  
      <label>형식</label>
      <select name='visible' id='visible'>
        <option value='Y' selected="selected">Y</option>
        <option value='N'>N</option>
      </select>
       
      <button type="button" id='btn_send'>등록</button>
      <button type="button" onclick="cancel();">취소</button>
    </FORM>
  </DIV>
  <div id='panel1' style="width: 40%; text-align: center; margin: 10px auto; display: none;"></div>
 
  
<TABLE class='table table-striped'>
  <colgroup>
    <col style='width: 10%;'/>
    <col style='width: 35%;'/>
    <col style='width: 20%;'/>
    <col style='width: 10%;'/>
    <col style='width: 20%;'/>
  </colgroup>
 
  <thead>  
  <TR>
    <TH class="th_bs">순서</TH>
    <TH class="th_bs">대분류명</TH>
    <TH class="th_bs">등록일</TH>
    <TH class="th_bs">출력</TH>
    <TH class="th_bs">기타</TH>
  </TR>
  </thead>
  
  <tbody>
  <c:forEach var="categrpVO" items= "${list}">
    <c:set var="categrpno" value="${categrpVO.categrpno}" />
    <TR>
      <TD class="td_bs">${categrpno }</TD>
      <TD class="td_bs_left"><A href="../cate/list.do?categrpno=${categrpno }">${categrpVO.name }</A></TD>
      <TD class="td_bs">${categrpVO.rdate.substring(0, 10) }</TD>
      <TD class="td_bs">
        <c:choose>
          <c:when test="${categrpVO.visible == 'Y'}">
            <A href="./update_visible.do?categrpno=${categrpno }&visible=${categrpVO.visible}"><IMG src="./images/open.png"></A>
          </c:when>
          <c:when test="${categrpVO.visible == 'N'}">
            <A href="./update_visible.do?categrpno=${categrpno }&visible=${categrpVO.visible}"><IMG src="./images/close.png"></A>
          </c:when>
        </c:choose>
      </TD>
      
      <TD class="td_bs">
        <A href="javascript:update_form(${categrpno })" title="수정"><span class="glyphicon glyphicon-pencil"></span></A>
        <A href="javascript:delete_form(${categrpno })" title="삭제"><span class="glyphicon glyphicon-trash"></span></A>
        <A href="./update_seqno_up.do?categrpno=${categrpno }" title="우선순위 상향"><span class="glyphicon glyphicon-arrow-up"></span></A>
        <A href="./update_seqno_down.do?categrpno=${categrpno }" title="우선순위 하향"><span class="glyphicon glyphicon-arrow-down"></span></A>         
      </TD>   
    </TR>
  </c:forEach>
 
  </tbody>
 
</TABLE>
 
 
<jsp:include page="/menu/bottom.jsp" />
</body>
 
</html> 