<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*,java.util.regex.Pattern" errorPage="" %>
<%@page import="java.util.*" %> 
<%@page import="com.qm.mes.framework.*" %>  
<jsp:useBean id="Conn" scope="page" class="com.qm.th.helper.Conn_MES"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>�ޱ����ĵ�</title>
</head>
<body>
<%
	   String userid=(String)session.getAttribute("userid");	
	%>
	<% 
	//��ȡ�������
	String element_name = request.getParameter("element_name");
	String codelength = request.getParameter("codelength");
	String description = request.getParameter("description");
	userid = userid==null?"-1":userid;   //��½�û�id
	if(element_name==null||element_name.trim().equals("")||userid==null||userid.trim().equals("")||codelength==null||codelength.trim().equals(""))
	{
%>
<script>
<!-- 
alert("����Ϊ��");window.location.href='materialidentify_update.jsp';
 -->
</script>
<%
		return;
	}	
	String a[]=new String[element_name.length()];
	int sum=0;
	for(int i=0;i<element_name.length();i++){
		int j = i+1;
		a[i]=element_name.substring(i,j );
		boolean hanzi=Pattern.compile("[\u4e00-\u9fa5]").matcher(a[i]).find();
		boolean shuzi = Pattern.compile("[a-zA-Z_0-9]").matcher(a[i]).find();
		if(true==hanzi){
			sum = sum+2;
		}else if(true==shuzi){
			sum = sum+1;
		}
	}
		if(sum>40)
	{
%>
<script>
<!-- 
alert("����ֵ���40���ַ�,ÿ����ռ2���ַ�");window.location.href='materialidentify_view.jsp';
 -->
</script>
<%
		return;
	}
	Connection con=null;
	ExecuteResult er=null;
	ServiceException se=null;
	
	List list=null;
	try
	{
		//��ȡ��Դ
		con=Conn.getConn();
		
		IServiceBus bus = ServiceBusFactory.getInstance();
		IMessage message = MessageFactory.createInstance();
%>
<%
		//�����û�����
		message.setUserParameter("element_name", element_name);
		message.setUserParameter("codelength", codelength);
		message.setUserParameter("description",description);
		message.setUserParameter("userid",userid);
		message.setOtherParameter("con", con);
		//����soa�е����� ��ʱ��Ϊ1 
		//todo "51"�����̶��������"�޸����ϱ�ʶ"
		er=bus.doProcess("51", message);
%>
<%
		//�ͷ���Դ
		if(con!=null)con.close();
		
		//��ִ�н���Ĵ���
		if(er==ExecuteResult.sucess)
		{
%>
<script>
<!-- 
alert("�����ɹ���");window.location.href='materialidentify_view.jsp';
 -->
</script>
<%
		}
		else
		{
			list=message.getServiceException();
			if(list==null||list.size()==0)
			{
%>
<script>
<!-- 
alert("����ʧ�ܣ�ԭ�������������Ա��ϵ��");window.location.href='materialidentify_view.jsp';
 -->
</script>
<%
			}
			else
			{
				se=(ServiceException)list.get(list.size()-1);
%>
<script>
<!-- 
alert('����ʧ�ܣ�<%=se.getDescr().replaceAll("\n","")%>');window.location.href='materialidentify_view.jsp';
 -->
</script>
<%
			}
		}
		
	}
	catch(Exception e)
	{	
		//�ͷ���Դ
		if(con!=null)con.close();
		
%>
<script>
<!--  
alert("�����쳣��<%=e.toString().replaceAll("\n","")%>");window.location.href='materialidentify_view.jsp';
-->
</script>	
<%
	throw e;
	}
%>
</body>
</html>