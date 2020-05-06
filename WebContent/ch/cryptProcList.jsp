<%@page import="ch.update.UpdateDBBean"%>
<%@page import="ch.update.UpdateDataBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
List<UpdateDataBean> memberList=null;
UpdateDBBean dbPro=UpdateDBBean.getInstance();
memberList=dbPro.getMember();

%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jsp!</title>
</head>
<body>
<table>
	<tr>
		<td>아이디</td>
		<td>비밀번호</td>
		
	</tr>
	<%
	for(int i=0;i<memberList.size();i++){
		UpdateDataBean member=(UpdateDataBean)memberList.get(i);
		String id=member.getId();
		String passwd=member.getPasswd();
	%>
	<tr>
		<td><%=id %></td>
		<td><%=passwd %></td>	
	</tr>
	<%
	}
	%>

</table>


</body>
</html>