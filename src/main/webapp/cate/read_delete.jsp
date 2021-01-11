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
 
  
</script>
 
</head> 
 
<body>
<jsp:include page="/menu/top.jsp" />
 
  <DIV class='title_line'>카테고리 > ${cateVO.name } 삭제</DIV>
 
  <DIV id='panel_delete' style='padding: 10px 0px 10px 0px; background-color: #F9F9F9; width: 100%; text-align: center;'>
    <div class="msg_warning">카테고리를 삭제하면 복구 할 수 없습니다.</div>
    <FORM name='frm_update' id='frm_update' method='POST' action='./delete.do'>
      <input type='hidden' name='cateno' id='cateno' value='${cateVO.cateno }'>
      <input type='hidden' name='categrpno' id='categrpno' value='${cateVO.categrpno}'>
      
      <label>카테고리 번호</label> : ${cateVO.categrpno } &nbsp;
      <label>그룹 이름</label> : ${cateVO.name } &nbsp;
      <label>순서</label> : ${cateVO.seqno } &nbsp;
      <label>출력 형식</label> : ${cateVO.visible } &nbsp;
      <label>등록글수</label> : ${cateVO.cnt } &nbsp;
       
      <button type="submit" id='submit'>삭제</button>
      <button type="button" onclick="location.href='./list.do'">취소</button>
    </FORM>
  </DIV>
 
  
<TABLE class='table table-striped'>
  <colgroup>
    <col style='width: 5%;'/>
    <col style='width: 5%;'/>
    <col style='width: 40%;'/>
    <col style='width: 20%;'/>
    <col style='width: 10%;'/>
    <col style='width: 20%;'/>
  </colgroup>
 
  <thead>  
  <TR>
    <TH class="th_bs">순서</TH>
    <TH class="th_bs">그룹</TH>
    <TH class="th_bs">카테고리</TH>
    <TH class="th_bs">등록일</TH>
    <TH class="th_bs">출력</TH>
    <TH class="th_bs">기타</TH>
  </TR>
  </thead>
  
  <tbody>
  <c:forEach var="cateVO" items= "${list}">
    <c:set var="cateno" value="${cateVO.cateno}" />
    <TR>
      <TD class="td_bs">${cateVO.seqno }</TD>
      <TD class="td_bs">${cateVO.categrpno }</TD>
      <TD class="td_bs_left">${cateVO.name }</TD>
      <TD class="td_bs">${cateVO.rdate.substring(0, 10) }</TD>
      <TD class="td_bs">
        <c:choose>
          <c:when test="${cateVO.visible == 'Y'}">
            <A href="./update_visible.do?cateno=${cateno }&visible=${cateVO.visible}&categrpno=${categrpno}"><IMG src="./images/open.png"></A>
          </c:when>
          <c:when test="${cateVO.visible == 'N'}">
            <A href="./update_visible.do?cateno=${cateno }&visible=${cateVO.visible}"><IMG src="./images/close.png"></A>
          </c:when>
        </c:choose>
      </TD>   
      

      <TD class="td_bs">
        <A href="./read_update.do?cateno=${cateno }" title="수정"><span class="glyphicon glyphicon-pencil"></span></A>
        <A href="./read_delete.do?cateno=${cateno }" title="삭제"><span class="glyphicon glyphicon-trash"></span></A>
        <A href="./update_seqno_up.do?cateno=${cateno }" title="우선순위 상향"><span class="glyphicon glyphicon-arrow-up"></span></A>
        <A href="./update_seqno_down.do?cateno=${cateno }" title="우선순위 하향"><span class="glyphicon glyphicon-arrow-down"></span></A>         
      </TD>   
    </TR>
  </c:forEach>
 
  </tbody>
  
 
</TABLE>
 
 
<jsp:include page="/menu/bottom.jsp" />
</body>
 
</html>