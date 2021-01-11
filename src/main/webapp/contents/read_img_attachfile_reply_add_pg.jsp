<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 글 + 이미지 조회하는 경우 참고  --%>

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
  // 첨부 이미지 출력, dir: ../attachfile/storage/, ./storage/main_images/
  function panel_img(dir, file) {
    var tag = "";
    tag   = "<A href=\"javascript: $('#attachfile_panel').hide();\">"; // 이미지 감추기
    tag += "  <IMG src='" + dir + file + "' style='width: 100%;'>";
    tag += "</A>";

    $('#attachfile_panel').html(tag);  // 문자열을 태그로 적용
    $('#attachfile_panel').show();      // 패널 출력
  }

</script>

<script type="text/javascript">
  $(function() { // 자동 실행
    $('#btn_delete_youtube').on('click', youtube_delete_send); 
    // $('#btn_delete_mp3').on('click', mp3_delete_send);
    // $('#btn_delete_mp4').on('click', mp4_delete_send);
    
    // -------------------- 댓글 관련 시작 --------------------
    var frm_reply = $('#frm_reply');
    $('#content', frm_reply).on('click', check_login);  // 댓글 작성시 로그인 여부 확인
    $('#btn_create', frm_reply).on('click', reply_create);  // 댓글 작성시 로그인 여부 확인

    list_by_contentsno_join(); // 댓글 목록
    // list_by_contentsno_join_add(); // 댓글 페이징 지원 목록, 동시 접속시 페이징 문제 있음.

    // $("#btn_add").on("click", list_by_contentsno_join_add); // 더보기 버튼 이벤트 등록, 페이징 문제 있음.
    $("#btn_add").on("click", list_by_contentsno_join_add_pg); // 더보기 버튼 이벤트 등록
    // -------------------- 댓글 관련 종료 --------------------
    
  });

  function youtube_delete_modal() {
    $('#modal_panel_youtube').modal();              // 다이얼로그 출력
  }
  
  // jQuery ajax 요청
  function youtube_delete_send () {
    // alert('삭제 진행');
    // return;
    
    var frm = $('#frm_youtube');  // Youtube 삭제 폼
    var contentsno = $('#contentsno', frm).val();
    var passwd = $('#passwd', frm).val();
    
    var params = 'contentsno=' + contentsno + '&passwd=' + passwd;
    var msg = '';
  
    $.ajax({
      url: './passwd.do',
      type: 'get',  // post
      cache: false, // 응답 결과 임시 저장 취소
      async: true,  // true: 비동기 통신
      dataType: 'json', // 응답 형식: json, html, xml...
      data: params,      // 데이터
      success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
        // alert(rdata);
        var msg = "";
          
        if (rdata.cnt > 0) { // 패스워드 일치
          frm.submit();
        } else {  // 패스워드 불일치
          msg = "패스워드가 일치하지 않습니다.";
          
          $('#modal_content').attr('class', 'alert alert-danger'); // Bootstrap CSS 변경
          $('#modal_title').html('패스워드 에러'); // 제목 
          $('#modal_content').html(msg);        // 내용
          $('#modal_panel').modal();              // 다이얼로그 출력
        }
      },
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
        var msg = 'ERROR<br><br>';
        msg += '<strong>request.status</strong><br>'+request.status + '<hr>';
        msg += '<strong>error</strong><br>'+error + '<hr>';
        console.log(msg);
      }
    });
  }

 
  // 댓글 작성시 로그인 여부 확인
  function check_login() {
    var frm_reply = $('#frm_reply');
    if ($('#memberno', frm_reply).val().length == 0 ) {
      $('#modal_title').html('댓글 등록'); // 제목 
      $('#modal_content').html("로그인해야 등록 할 수 있습니다."); // 내용
      $('#modal_panel').modal();            // 다이얼로그 출력
      return false;  // 실행 종료
    }
  }

  // 댓글 등록
  function reply_create() {
    var frm_reply = $('#frm_reply');
    
    if (check_login() !=false) { // 로그인 한 경우만 처리
      var params = frm_reply.serialize();
      // alert(params);
      // return;

      // 자바스크립트: 영숫자, 한글, 공백, 특수문자: 글자수 1로 인식
      // 오라클: 한글 1자가 3바이트임으로 300자로 제한
      // alert('내용 길이: ' + $('#content', frm_reply).val().length);
      // return;
      
      if ($('#content', frm_reply).val().length > 300) {
        $('#modal_title').html('댓글 등록'); // 제목 
        $('#modal_content').html("댓글 내용은 300자이상 입력 할 수 없습니다."); // 내용
        $('#modal_panel').modal();           // 다이얼로그 출력
        return;  // 실행 종료
      }

      $.ajax({
        url: "../reply/create.do", // action 대상 주소
        type: "post",          // get, post
        cache: false,          // 브러우저의 캐시영역 사용안함.
        async: true,           // true: 비동기
        dataType: "json",   // 응답 형식: json, xml, html...
        data: params,        // 서버로 전달하는 데이터
        success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
          // alert(rdata);
          var msg = ""; // 메시지 출력
          var tag = ""; // 글목록 생성 태그
          
          if (rdata.cnt > 0) {
            $('#modal_content').attr('class', 'alert alert-success'); // CSS 변경
            msg = "댓글을 등록했습니다.";
            $('#content', frm_reply).val('');
            $('#passwd', frm_reply).val('');

            // list_by_contentsno_join(); // 댓글 목록을 새로 읽어옴
            
            $('#reply_list').html(''); // 댓글 목록 패널 초기화, val(''): 안됨
            $("#reply_list").attr("data-replypage", 1);  // 댓글이 새로 등록됨으로 1로 초기화
            
            // list_by_contentsno_join_add(); // 페이징 댓글, 페이징 문제 있음.
            // alert('댓글 목록 읽기 시작');
            global_rdata = new Array(); // 댓글을 새로 등록했음으로 배열 초기화
            list_by_contentsno_join(); // 페이징 댓글
          } else {
            $('#modal_content').attr('class', 'alert alert-danger'); // CSS 변경
            msg = "댓글 등록에 실패했습니다.";
          }
          
          $('#modal_title').html('댓글 등록'); // 제목 
          $('#modal_content').html(msg);     // 내용
          $('#modal_panel').modal();           // 다이얼로그 출력
        },
        // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
        error: function(request, status, error) { // callback 함수
          var msg = 'ERROR request.status: '+request.status + '/ ' + error;
          console.log(msg); // Chrome에 출력
        }
      });
    }
  }

  // --------------------- 댓글 출력 관련 시작 --------------------- 
  var global_rdata = new Array(); // 내용 저장
  var global_rdata_cnt = 0;         // 현재 출력한 댓글의 수, 1 page = 10개 레코드, 11개인 경우 중간 종료

  // Reply class 선언
  // {"memberno":3,"rdate":"2020-12-17 15:16:16.0","passwd":"18","replyno":32,"id":"user1","content":"18","contentsno":53}
  function Reply(memberno, rdate, replyno, id, content, contentsno) {
    this.memberno = memberno;
    this.rdate = rdate;
    this.replyno = replyno;
    this.id = id;
    this.content = content;
    this.contentsno = contentsno;
  }
  
  // contentsno별 소속된 댓글 전체 목록
  function list_by_contentsno_join() {
    var params = 'contentsno=' + ${contentsVO.contentsno };

    $.ajax({
      url: "../reply/list_by_contentsno_join.do", // action 대상 주소
      type: "get",           // get, post
      cache: false,          // 브러우저의 캐시영역 사용안함.
      async: true,           // true: 비동기
      dataType: "json",   // 응답 형식: json, xml, html...
      data: params,        // 서버로 전달하는 데이터
      success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
        // alert(rdata);
        var msg = '';
        
        $('#reply_list').html(''); // 패널 초기화, val(''): 안됨
        
        for (i=0; i < rdata.list.length; i++) {
          var row = rdata.list[i];
          // alert(row.replyno);
          var reply = new Reply(row.memberno, row.rdate, row.replyno, row.id, row.content, row.contentsno); // 객체 생성
          global_rdata.push(reply); // 배열에 저장
        }

        // alert('페이징된 댓글 목록 읽기 시작');
        list_by_contentsno_join_add_pg() // javascript paging
      },
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
        var msg = 'ERROR request.status: '+request.status + '/ ' + error;
        console.log(msg);
      }
    });
    
  }

  // contentsno 별 소속된 댓글 목록
  function list_by_contentsno_join_add_pg() {
    var replypage = parseInt($("#reply_list").attr("data-replypage")); // 1, 2, 3...
    var end = replypage * 2;     // 2, 4, 6...
    var start = end - 2;             // 0, 2, 4..., 배열 index는 0부터 시작 
    var msg="";

    for (var i=start; i < end; i++) {
      global_rdata_cnt = global_rdata_cnt + 1; // DIV 태그에 글 출력 카운트
      if (global_rdata.length < global_rdata_cnt ) { // 1 page = 10개 레코드, 11개인 경우 중간 종료
        break;
      }

      var row = global_rdata[i]; // 배열에서 글 1건 산출
          
      msg += "<DIV id='"+row.replyno+"' style='border-bottom: solid 1px #EEEEEE; margin-bottom: 10px;'>";
      msg += "<span style='font-weight: bold;'>" + row.id + "</span>";
      msg += "  " + row.rdate;
      if ('${sessionScope.memberno}' == row.memberno) { // 글쓴이 일치여부 확인, 본인의 글만 삭제 가능함 ★
        msg += " <A href='javascript:reply_delete("+row.replyno+")'><IMG src='./images/delete.png'></A>";
      }
      msg += "  " + "<br>";
      msg += row.content;
      msg += "</DIV>";
    }
    // alert(msg);
    $('#reply_list').append(msg);
    $("#reply_list").attr("data-replypage", replypage+1);  // 개발자정의 속성 쓰기
    
  }

  
  // 댓글 삭제 레이어 출력
  function reply_delete(replyno) {
    // alert('replyno: ' + replyno);
    var frm_reply_delete = $('#frm_reply_delete');
    $('#replyno', frm_reply_delete).val(replyno); // 삭제할 댓글 번호 저장
    $('#modal_panel_delete').modal();             // 삭제폼 다이얼로그 출력
  }

  // 댓글 삭제 처리
  function reply_delete_proc(replyno) {
    // alert('replyno: ' + replyno);
    var params = $('#frm_reply_delete').serialize();
    $.ajax({
      url: "../reply/delete.do", // action 대상 주소
      type: "post",           // get, post
      cache: false,          // 브러우저의 캐시영역 사용안함.
      async: true,           // true: 비동기
      dataType: "json",   // 응답 형식: json, xml, html...
      data: params,        // 서버로 전달하는 데이터
      success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
        // alert(rdata);
        var msg = "";
        
        if (rdata.passwd_cnt ==1) { // 패스워드 일치
          if (rdata.delete_cnt == 1) { // 삭제 성공

            $('#btn_frm_reply_delete_close').trigger("click"); // 삭제폼 닫기, click 발생 ★ 
            
            $('#' + replyno).remove(); // 태그 삭제 ★
              
            return; // 함수 실행 종료
          } else {  // 삭제 실패
            msg = "패스 워드는 일치하나 댓글 삭제에 실패했습니다. <br>";
            msg += " 다시한번 시도해주세요."
          }
        } else { // 패스워드 일치하지 않음.
          // alert('패스워드 불일치');
          // return;
          
          msg = "패스워드가 일치하지 않습니다.";
          $('#modal_panel_delete_msg').html(msg);

          $('#passwd', '#frm_reply_delete').focus();
          
        }
      },
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
        var msg = 'ERROR request.status: '+request.status + '/ ' + error;
        console.log(msg);
      }
    });
  }

  /*
  //contentsno 별 소속된 댓글 목록 + 더보기 버튼 + 페이지 문제 있음
  function list_by_contentsno_join_add() {
    var replypage = parseInt($("#reply_list").attr("data-replypage")); // 현재 페이지 
    var params = 'contentsno=' + ${contentsVO.contentsno } + "&replypage="+replypage;

    $.ajax({
      url: "../reply/list_by_contentsno_join_add.do", // action 대상 주소
      type: "get",           // get, post
      cache: false,          // 브러우저의 캐시영역 사용안함.
      async: true,           // true: 비동기
      dataType: "json",   // 응답 형식: json, xml, html...
      data: params,        // 서버로 전달하는 데이터
      success: function(rdata) { // 서버로부터 성공적으로 응답이 온경우
        $("#reply_list").attr("data-replypage", replypage+1);  // 개발자 정의 속성 쓰기, 페이지수 증가
        // alert(rdata);
        var msg = '';
        
        // $('#reply_list').html(''); // 패널 초기화, val(''): 안됨
        
        for (i=0; i < rdata.list.length; i++) {
          var row = rdata.list[i];
          
          msg += "<DIV id='"+row.replyno+"' style='border-bottom: solid 1px #EEEEEE; margin-bottom: 10px;'>";
          msg += "<span style='font-weight: bold;'>" + row.id + "</span>";
          msg += "  " + row.rdate;
          if ('${sessionScope.memberno}' == row.memberno) { // 글쓴이 일치여부 확인
            msg += " <A href='javascript:reply_delete("+row.replyno+")'><IMG src='./images/delete.png'></A>";
          }
          msg += "  " + "<br>";
          msg += row.content;
          msg += "</DIV>";
        }
        // alert(msg);
        $('#reply_list').append(msg);  //  reply_list 태그의 body의 마지막 부분에 태그 추가 
      },
      // Ajax 통신 에러, 응답 코드가 200이 아닌경우, dataType이 다른경우 
      error: function(request, status, error) { // callback 함수
        var msg = 'ERROR request.status: '+request.status + '/ ' + error;
        console.log(msg);
      }
    });
    
  }
  */
  
  // --------------------- 댓글 출력 관련 종료 ---------------------

</script>
</head>

<body>
<c:set var="cateno" value="${cateVO.cateno}" />
<c:set var="contentsno" value="${contentsVO.contentsno }" />

<jsp:include page="/menu/top.jsp" flush='false' />
  <!-- Modal Youtube 삭제 요청 시작 -->
  <div class="modal fade" id="modal_panel_youtube" role="dialog">
    <div class="modal-dialog">
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">×</button>
          <h4 class="modal-title">Youtube 삭제</h4><!-- 제목 -->
        </div>
        <div class="modal-body">
          <p>
            <form name='frm_youtube' id='frm_youtube' action='./youtube_delete.do'
                      method='POST'>
              <input type='hidden' name='cateno' id='cateno' value='${cateno }'>                      
              <input type='hidden' name='contentsno' id='contentsno' value='${contentsno }'>
              삭제된 Youtube 영상 관련 정보는 복구 할 수 없습니다.<br>
              <input type='password' class="form-control" name='passwd' id='passwd' value='' placeholder="패스워드" style='width: 20%;'>
            </form>
          </p>  <!-- 내용 -->
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
          <button type="button" id="btn_delete_youtube" class="btn btn-danger" data-dismiss="modal">삭제</button>
        </div>
      </div>
    </div>
  </div> <!-- Modal Youtube 삭제 요청  종료 -->

  <!-- Modal 알림창 시작 -->
  <div class="modal fade" id="modal_panel" role="dialog">
    <div class="modal-dialog">
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">×</button>
          <h4 class="modal-title" id='modal_title'></h4><!-- 제목 -->
        </div>
        <div class="modal-body">
          <p id='modal_content'></p>  <!-- 내용 -->
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
        </div>
      </div>
    </div>
  </div> <!-- Modal 알림창 종료 -->
  
  <!-- -------------------- 댓글 삭제폼 시작 -------------------- -->
  <div class="modal fade" id="modal_panel_delete" role="dialog">
    <div class="modal-dialog">
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">×</button>
          <h4 class="modal-title">댓글 삭제</h4><!-- 제목 -->
        </div>
        <div class="modal-body">
          <form name='frm_reply_delete' id='frm_reply_delete'>
            <input type='hidden' name='replyno' id='replyno' value=''>
            
            <label>패스워드</label>
            <input type='password' name='passwd' id='passwd' class='form-control'>
            <DIV id='modal_panel_delete_msg' style='color: #AA0000; font-size: 1.1em;'></DIV>
          </form>
        </div>
        <div class="modal-footer">
          <button type='button' class='btn btn-danger' 
                       onclick="reply_delete_proc(frm_reply_delete.replyno.value); frm_reply_delete.passwd.value='';">삭제</button>

          <button type="button" class="btn btn-default" data-dismiss="modal" 
                       id='btn_frm_reply_delete_close'>Close</button>
        </div>
      </div>
    </div>
  </div>
  <!-- -------------------- 댓글 삭제폼 종료 -------------------- -->
    
  <DIV class="title_line">
    ${contentsVO.title}
  </DIV>
  <ASIDE class="aside_left">
    <A href="../categrp/list.do">카테고리 그룹</A> > 
    <A href="../cate/list.do?categrpno=${categrpVO.categrpno }">${categrpVO.name }</A> > 
    <A href="../contents/list.do?cateno=${contentsVO.cateno }&word=${param.word }&nowPage=${param.nowPage}">${cateVO.name}</A>  
  </ASIDE>
  <ASIDE class="aside_right">
    <A href="javascript:location.reload();">새로고침</A>
    <span class='menu_divide' > | </span> 
    <A href="./create.do">글 등록</A>
    <span class='menu_divide' > | </span>
    <A href='./reply.do?contentsno=${contentsno }&cateno=${cateno }'>답변</A>
    <span class='menu_divide' > | </span>
    <A href='./list.do?cateno=${cateno }&word=${param.word }&nowPage=${param.nowPage}'>목록</A>

    <%-- 글을 등록한 회원만 메뉴 출력 --%>
    <c:if test="${sessionScope.memberno == contentsVO.memberno or sessionScope.id_admin ne null }">
      <%-- 글을 등록한 회원임. ${sessionScope.memberno} / ${contentsVO.memberno } --%>
      <c:choose>
        <c:when test="${contentsVO.file1.trim().length() > 0 }">
          <span class='menu_divide' > | </span> 
          <A href='./img_update.do?cateno=${cateno }&contentsno=${contentsno}&nowPage=${param.nowPage}'>메인 이미지 변경/삭제</A>     
        </c:when>
        <c:otherwise>
          <span class='menu_divide' > | </span> 
          <A href='./img_create.do?cateno=${cateno }&contentsno=${contentsno}&nowPage=${param.nowPage}'>메인 이미지 등록</A>     
        </c:otherwise>
      </c:choose>    
      <span class='menu_divide' > | </span>
      <A href='../attachfile/create.do?contentsno=${contentsno }&cateno=${cateno }'>첨부 파일 등록</A>
      <span class='menu_divide' > | </span>
      <A href='../attachfile/list_by_contentsno.do?contentsno=${contentsno }&cateno=${cateno }'>첨부 파일 삭제</A>
  
      <span class='menu_divide' > | </span> 
      <A href='./update.do?cateno=${cateno }&contentsno=${contentsno}&nowPage=${param.nowPage}'>수정</A>
      
      <c:choose>
        <c:when test="${contentsVO.map.trim().length() > 0 }">
          <span class='menu_divide' > | </span> 
          <A href='./map_delete.do?cateno=${cateno }&contentsno=${contentsno}'>지도 삭제</A>     
        </c:when>
        <c:otherwise>
          <span class='menu_divide' > | </span> 
          <A href='./map_create.do?cateno=${cateno }&contentsno=${contentsno}'>지도 등록</A>     
        </c:otherwise>
      </c:choose> 
       
      <c:choose>
        <c:when test="${contentsVO.youtube.trim().length() > 0 }">
          <span class='menu_divide' > | </span> 
          <A href='javascript:youtube_delete_modal()'>Youtube 삭제</A>     
        </c:when>
        <c:otherwise>
          <span class='menu_divide' > | </span> 
          <A href='./youtube_create.do?cateno=${cateno }&contentsno=${contentsno}'>Youtube 등록</A>     
        </c:otherwise>
      </c:choose>    
  
      <c:choose>
        <c:when test="${contentsVO.mp3.trim().length() > 0 }">
          <span class='menu_divide' > | </span> 
          <A href='./mp3_delete.do?cateno=${cateno }&contentsno=${contentsno}'>MP3 삭제</A>     
        </c:when>
        <c:otherwise>
          <span class='menu_divide' > | </span> 
          <A href='./mp3_create.do?cateno=${cateno }&contentsno=${contentsno}'>MP3 등록</A>     
        </c:otherwise>
      </c:choose>
  
      <c:choose>
        <c:when test="${contentsVO.mp4.trim().length() > 0 }">
          <span class='menu_divide' > | </span> 
          <A href='./mp4_delete.do?cateno=${cateno }&contentsno=${contentsno}'>MP4 삭제</A>     
        </c:when>
        <c:otherwise>
          <span class='menu_divide' > | </span> 
          <A href='./mp4_create.do?cateno=${cateno }&contentsno=${contentsno}'>MP4 등록</A>     
        </c:otherwise>
      </c:choose>
              
      <span class='menu_divide' > | </span> 
      <A href='./delete.do?cateno=${cateno }&contentsno=${contentsno}&nowPage=${param.nowPage}'>삭제</A>
    </c:if>
  </ASIDE> 
  
  <div class='menu_line'></div>

  <FORM name='frm' method="get" action='./update.do'>
      <input type="hidden" name="contentsno" value="${contentsno}">
      <fieldset class="fieldset">
        <ul>
          <li class="li_none" style='border-bottom: solid 1px #AAAAAA;'>
            <span>${contentsVO.title}</span>
            (<span>${contentsVO.recom}</span>)
            <span>${contentsVO.rdate.substring(0, 16)}</span>
          </li>
          
          <%-- ********** 첨부 파일 이미지 목록 출력 시작 ********** --%>
          <li class="li_none">
            <DIV id='attachfile_panel' style="width: 70%; margin: 0px auto;"></DIV> <!-- 원본 이미지 출력 -->
          </li>
          <li class="li_none" style='text-align: center;' >
            <c:set var="file1" value="${contentsVO.file1.toLowerCase() }" />
            <c:if test="${file1.endsWith('jpg') || file1.endsWith('png') || file1.endsWith('gif')}">
              <A href="javascript:panel_img('./storage/main_images/', '${contentsVO.file1 }')"><IMG src='./storage/main_images/${contentsVO.thumb1 }' style='margin-top: 2px; width: 120px; height: 80px;'></A>
            </c:if>
            
            <c:forEach var="attachfileVO" items="${attachfile_list }">
              <c:set var="thumb" value="${attachfileVO.thumb.toLowerCase() }" />
              
              <c:choose>
                <c:when test="${thumb.endsWith('jpg') || thumb.endsWith('png') || thumb.endsWith('gif')}">
                  <A href="javascript:panel_img('../attachfile/storage/', '${attachfileVO.fname }')"><IMG src='../attachfile/storage/${attachfileVO.thumb }' style='margin-top: 2px; width: 120px; height: 80px;'></A>
                </c:when>
              </c:choose>
            </c:forEach>
          </li>
          <%-- ********** 첨부 파일 이미지 목록 출력 종료 ********** --%>
                    
          <li class="li_none">
            <DIV>${contentsVO.content }</DIV>
          </li>
          <li class="li_none">
            ${contentsVO.web }
          </li>

          <c:if test="${contentsVO.map.trim().length() > 0 }">
            <li class='li_none' style='clear: both;'>
              <DIV style='width:640px; height: 380px; margin: 0px auto;'>
                ${contentsVO.map }
              </DIV>
            </li>
          </c:if>
          
          <c:if test="${contentsVO.youtube.trim().length() > 0 }">
            <li class='li_none' style='clear: both;'>
              <DIV style='width:900px; margin: 0px auto; text-align: center;'>
                ${contentsVO.youtube}
              </DIV>
            </li>
          </c:if>     

          <c:if test="${contentsVO.mp4.trim().length() > 0 }">
            <li class='li_none' style='clear: both;'>
              <DIV style='width:900px; margin: 0px auto; text-align: center;'>
                <VIDEO controls src='./storage/mp4/${contentsVO.mp4}'
                            style='width: 70%; background-color: #EEEEEE;'></VIDEO>
              </DIV>
            </li>
          </c:if>   
          
          <li class="li_none">
            <DIV style='text-decoration: none;'>
              검색어(키워드): ${contentsVO.word } IP: ${contentsVO.ip }
              
              <c:if test="${contentsVO.mp3.trim().length() > 0 }">
                <AUDIO controls autoplay="autoplay">
                  <source src="./storage/mp3/${contentsVO.mp3 }" type="audio/mp3">
                </AUDIO>
              </c:if>  
          
            </DIV>
          </li>   
          
          <li class="li_none">
            <DIV>
              <c:if test="${contentsVO.file1.trim().length() > 0 }">
                대표이미지: <A href='${pageContext.request.contextPath}/download2?dir=/contents/storage/main_images&filename=${contentsVO.file1}&downname=${contentsVO.file1}'>${contentsVO.file1}</A> (${contentsVO.size1_label})  
              </c:if>
            </DIV>
            <DIV>
              <c:forEach var="attachfileVO" items="${attachfile_list }">
                <c:set var="fname" value="${attachfileVO.fname.toLowerCase() }" />
                <A href='${pageContext.request.contextPath}/download2?dir=/attachfile/storage&filename=${attachfileVO.fupname}&downname=${attachfileVO.fname}'>${attachfileVO.fname}</A>(${attachfileVO.flabel})               
              </c:forEach>
            </DIV>  
          </li>                    
        </ul>
      </fieldset>
  </FORM>

  <!-- ---------- 댓글 영역 시작 ---------- -->
  <DIV style='width: 80%; margin: 0px auto;'>
      <HR>
      <FORM name='frm_reply' id='frm_reply'> <%-- 댓글 등록 폼 --%>
          <input type='hidden' name='contentsno' id='contentsno' value='${contentsno}'>
          <input type='hidden' name='memberno' id='memberno' value='${sessionScope.memberno}'>
          
          <textarea name='content' id='content' style='width: 100%; height: 60px;' placeholder="댓글 작성, 로그인해야 등록 할 수 있습니다."></textarea>
          <input type='password' name='passwd' id='passwd' placeholder="비밀번호">
          <button type='button' id='btn_create'>등록</button>
      </FORM>
      <HR>
      <DIV id='reply_list' data-replypage='1'>  <%-- 댓글 목록 --%>
      
      </DIV>
      <DIV id='reply_list_btn' style='border: solid 1px #EEEEEE; margin: 0px auto; width: 100%; background-color: #EEFFFF;'>
          <button id='btn_add' style='width: 100%;'>더보기 ▽</button>
      </DIV>  
    
  </DIV>
  
  <!-- ---------- 댓글 영역 종료----------  -->
  
<jsp:include page="/menu/bottom.jsp" flush='false' />
</body>

</html>