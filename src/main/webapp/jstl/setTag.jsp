<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 
<!DOCTYPE html> 
<html lang="ko"> 
<head> 
<meta charset="UTF-8"> 
<title>/webapp/jstl/setTag.jsp</title> 
<style type="text/css">
  * { font-size: 32px; }
</style>
</head> 
 
<body>
<DIV class='container'>
<DIV class='content'>
   <!-- JSTL -->
   <c:set var="img" value="<IMG src='./images/bu6.png'>" />

   
   ${img } JAVA<BR>
   ${img } JSP<BR>
   ${img } SPRING<BR>
   ${img } MyBATIS<BR>
   ${img } R<BR>
   ${img } Python<BR>
   ${img } Crawling<BR>
   ${img } Analysis<BR>
   ${img } Machine Learning<BR> 
   
   <br>
   <!-- EL을 값으로 할당 -->
   <c:set var="root" value="${pageContext.request.contextPath }" />
   절대 경로: ${root }

   <br><br>
   
   <!-- Scriptlet을 값으로 할당 -->

   
</DIV> <!-- content END -->
</DIV> <!-- container END -->
</body>
 
</html>