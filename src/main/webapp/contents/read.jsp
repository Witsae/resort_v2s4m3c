<%@ page contentType="text/html; charset=UTF-8"%>
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
</head>

<body>
<c:set var="cateno" value="${cateVO.cateno}" />
<c:set var="contentsno" value="${contentsVO.contentsno }" />

<jsp:include page="/menu/top.jsp" flush='false' />
  <DIV class='title_line'>
    ${cateVO.name }
  </DIV>
  
  <ASIDE style='float: left;'>
    ${categrpVO.name } > ${cateVO.name }
  </ASIDE>
  <ASIDE style='float: right;'>
    <A href='../attachfile/create.do?contentsno=${contentsno }&cateno=${cateno }'>파일 등록</A>
    <span class='menu_divide' > | </span>
    <A href="javascript:location.reload();">새로고침</A>
    <span class='menu_divide' > | </span> 
    <A href='./list.do?cateno=${cateno }'>목록</A>
    <span class='menu_divide' > | </span> 
    <A href='./update.do?cateno=${cateno }&contentsno=${contentsno}'>수정</A>
    <span class='menu_divide' > | </span> 
    <A href='./delete.do?cateno=${cateno }&contentsno=${contentsno}'>삭제</A>
    
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
            <%-- 대표 이미지 출력 --%>
            <c:set var="file1" value="${contentsVO.file1.toLowerCase() }" />
            <c:set var="thumb1" value="${contentsVO.thumb1.toLowerCase() }" />
            <c:if test="${thumb1.endsWith('jpg') || thumb1.endsWith('png') || thumb1.endsWith('gif')}">
              <A href="javascript:panel_img('./storage/main_images/', '${file1 }')"><IMG src='./storage/main_images/${thumb1 }' style='margin-top: 2px; width: 120px; height: 80px;'></A>
            </c:if>
            
            <%-- 첨부파일(attachfile) 이미지 출력 --%>
            <c:forEach var="attachfileVO" items="${attachfile_list }">
              <c:set var="thumb" value="${attachfileVO.thumb.toLowerCase() }" />
              
              <c:choose>
                <c:when test="${thumb.endsWith('jpg') || thumb.endsWith('png') || thumb.endsWith('gif')}">
                  <A href="javascript:panel_img('../attachfile/storage/', '${attachfileVO.fname }')"><IMG src='../attachfile/storage/${thumb }' style='margin-top: 2px;'></A>
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
          <li class="li_none">
            <DIV style='text-decoration: none;'>
              검색어(키워드): ${contentsVO.word } IP: ${contentsVO.ip }
            </DIV>
          </li>
        </ul>
      </fieldset>
  </FORM>

<jsp:include page="/menu/bottom.jsp" flush='false' />
</body>

</html>