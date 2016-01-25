<%@ page language="java" import="java.sql.*" contentType="text/html;charset=gb2312"%>
<jsp:useBean id="Conn" scope="page" class="common.Conn"/>
<%@page import="java.util.*" %>
<%@page import="mes.framework.*" %>

<%
	/*
	 * 业务描述：添加适配器信息，校验适配器是否重复配置由相应服务完成
	 */
	 
	//获取请求参数
	String serviceid = request.getParameter("serviceid");
	String paraname = request.getParameter("paraname");
	String setparaname = request.getParameter("setparaname");
	String setparatype = request.getParameter("setparatype");
	String paratype = request.getParameter("paratype");
	String paradesc = request.getParameter("paradesc");
    String intpage="";
	intpage=request.getParameter("intpage");
	String eid = null;
	eid=request.getParameter("eid");
	String info = request.getParameter("info");
		info = info==null?"":info;
	
	if(serviceid==null||serviceid.trim().equals("")||paraname==null||paraname.trim().equals("")||setparaname==null||setparaname==""||setparatype==null||setparatype.trim().equals("")||paratype==null||paratype.trim().equals("")||paradesc==null||paradesc.trim().equals(""))
	{
%>
<script>
	alert("参数为空");window.location.href='servicePara_manage.jsp?page=<%=intpage%>&eid=<%=eid%>&info=<%=info%>';
</script>
<%
		return;
	}
	
	Connection con=null;
	ExecuteResult er=null;
	ServiceException se=null;
//	String questiondesc="";
	
	List list=null;
	try
	{
		//获取资源
		con=Conn.getConn();
		
		IServiceBus bus = ServiceBusFactory.getInstance();
		IMessage message = MessageFactory.createInstance();
		//设置用户参数
		message.setUserParameter("serviceid", serviceid);
		message.setUserParameter("paratype",paratype);
		message.setUserParameter("setparatype", setparatype);
		message.setUserParameter("paraname", paraname);
		message.setUserParameter("setparaname",setparaname);
        message.setUserParameter("paradesc", paradesc);
		message.setOtherParameter("con", con);
		
		//调用soa中"更新服务参数"的流程 流程号为12
		//todo
		er=bus.doProcess("12", message);
		
		//释放资源
		if(con!=null)con.close();
		
		//对执行结果的处理
		if(er==ExecuteResult.sucess)
		{
%>
<script>
	alert("操作成功！");window.location.href='servicePara_manage.jsp?page=<%=intpage%>&eid=<%=eid%>&info=<%=info%>';
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
 	alert("操作失败！原因不明！请与管理员联系！");window.location.href='servicePara_manage.jsp?page=<%=intpage%>&eid=<%=eid%>&info=<%=info%>';;
 </script>
<%
			}
			else
			{
				se=(ServiceException)list.get(list.size()-1);
%>
<script>
 	alert("操作失败！<%=se.getDescr()%>");window.location.href='servicePara_manage.jsp?page=<%=intpage%>&eid=<%=eid%>&info=<%=info%>';
</script>
<%
			}
		}
		
	}
	catch(Exception e)
	{	
		//释放资源
		if(con!=null)con.close();
		
%>
<script>  
	alert("发生异常！<%=e.toString()%>");window.location.href='servicePara_manage.jsp?page=<%=intpage%>&eid=<%=eid%>&info=<%=info%>';
</script>	
<%   
	throw e;
	}
%>
		