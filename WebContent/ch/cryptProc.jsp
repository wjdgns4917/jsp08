<%@page import="ch.update.UpdateDBBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("utf-8"); %>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jsp!</title>
</head>
<body>
<h2>cryptProc.jsp</h2>
<h3>암호화전</h3>
<jsp:include page="cryptProcList.jsp" />
<%
UpdateDBBean dbPro=UpdateDBBean.getInstance();
dbPro.updateMember();//암호화처리 메소드

%>


<hr />
<h3>암호화후</h3>
<jsp:include page="cryptProcList.jsp" />
</body>
</html>