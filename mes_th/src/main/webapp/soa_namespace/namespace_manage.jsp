<%@ page language="java" import="java.sql.*"
	contentType="text/html;charset=gb2312"%>
<jsp:directive.page import="java.util.List" />
<jsp:directive.page import="java.util.ArrayList" />
<jsp:directive.page import="com.qm.mes.beans.NameSpace" />
<jsp:useBean id="Conn" scope="page" class="com.qm.th.helpers.Conn_MES" />
<%@page import="com.qm.mes.framework.dao.*"%>
<%@ page import="com.qm.mes.framework.DataBaseType"%>
<%@taglib uri="http://www.faw-qm.com.cn/mes" prefix="mes"%>
<%@page import="com.qm.mes.ra.util.*"%>

<%
	response.setHeader("progma","no-cache");
	response.setHeader("Cache-Control","no-cache");
    
    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
	String eid = null;
	eid=request.getParameter("eid");
	ArrayList<String> colist = new ArrayList<String>(2);
	int PageCount=0; //总页数
    try
    {
		//获取连接
	   	conn=Conn.getConn();
		stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
		int PageSize; //一页显示的记录数
		int RowCount; //记录总数
		int intPage; //待显示页码
		String strPage;
		int i=0;
		PageSize=10;
		//获得待显示的某一页数
		strPage=request.getParameter("page");
		if(strPage==null|| strPage.equals(""))
		{    intPage=1;
		}
		else
		{    intPage=Integer.parseInt(strPage);
		    if(intPage<1) intPage =1;
		} 
				
		String info="";
	    info=request.getParameter("info");
	    if(info==null)
			info="";			
		//查询用
	    String sql="";
		IDAO_Core dao = DAOFactory_Core.getInstance(DataBaseType.getDataBaseType(conn));
		if(info==null||info.equals("")){
			sql=dao.getSQL_QueryNameSpaceForProcessStatement();
		}
		else{				
			//选择要查询的字段			
			colist.add("NNAMESPACEID".toLowerCase());
			colist.add("CNAMESPACE".toLowerCase());					
			info=info.trim();			  
			sql=dao.getSQL_QueryNameSpaceForProcessStatement();
			//模糊查询		
		   LinkQuery link=new LinkQuery();		    
		   StringBuffer wh= link.linkquery(info,colist,sql,conn);
		   sql = "select * from (" + sql +") a where " + wh.toString(); 
			// sql = dao.getStateByName(info);		     
		}			   
		sql=sql+" order by nnamespaceid";
		List<NameSpace> list=new ArrayList<NameSpace>();
	    rs=stmt.executeQuery(sql);
		rs.last();
		//获得总记录数
		RowCount=rs.getRow();
		
		//总页数
		PageCount = (RowCount+PageSize-1) / PageSize;
		if(intPage>PageCount) intPage = PageCount;
		if(PageCount>0)
		{
		
		    //将记录指针定位到下一显示位置
		    rs.absolute((intPage-1)*PageSize + 1);
		    i = 0;
		    while(i<PageSize && !rs.isAfterLast())
		    {
				NameSpace ns=new NameSpace();
				ns.setId(rs.getInt(1));
				ns.setNamespace(rs.getString(2));
				ns.setDesc(rs.getString(3));
				list.add(ns);
			i++;
	   		rs.next();
	      }
	}
	%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="../cssfile/style.css">
		<meta http-equiv="Content-Language" content="zh-cn" />
		<style>
.head2{	height:22px;color:ffff00;
}
</style>
	</head>
	<body background="../images/background.jpg">
		<!-- 引用通用脚本 -->
		<script type="text/javascript"
			src="../JarResource/META-INF/tag/taglib_common.js"></script>
		<div align="center">
			<strong>命名空间管理</strong>
		</div>
		<br>
		<div align="center">
			<mes:table reSourceURL="../JarResource/" id="table1">
				<mes:thead>

					<mes:tr>
						<td width="83">
							命名空间号
						</td>
						<td width="240">
							命名空间名称
						</td>
						<td width="160">
							描述
						</td>
						<td width="60">
							更新
						</td>
						<td width="60">
							删除
						</td>
					</mes:tr>
				</mes:thead>
				<mes:tbody iterator="<%=list.iterator() %>"
					type="mes.beans.NameSpace" varName="name">
					<mes:tr id="tr${name.id}">
						<mes:td>${name.id}</mes:td>
						<mes:td>${name.namespace}</mes:td>
						<mes:td>${name.desc}</mes:td>
						<mes:td>
							<a	href='namespace_update.jsp?id=${name.id}&intPage=<%=intPage %>&info=<%=info%>'>更新</a>
						</mes:td>
						<mes:td>
							<a	href='namespace_deleting.jsp?id=${name.id}&intPage=<%=intPage %>&info=<%=info%>'>删除</a>
						</mes:td>
					</mes:tr>
				</mes:tbody>
				<mes:tfoot>
					<mes:tr>
						<mes:td colspan="100%" align="center">

							<form name="form" style="margin: 0px" method="post"
								onSubmit="return checkinput(this)" action="namespace_manage.jsp">
								<input type="hidden" value="<%=info%>" name="info">

								共
								<%=RowCount%>
								条 第
								<%=intPage%>
								页 共
								<%=PageCount%>
								页
								<%if(PageCount>1){
		 out.println("跳转到第");
		 out.print("<input size=\"1\"  name=\"page\" value=");%>
								<%=intPage%>
								<%out.println(">");
		 out.println("页");
		 }%>
								<%if(intPage>1){    
			out.println("<a href=\"namespace_manage.jsp?page=1&info="+info+"\">第一页</a>");	
			out.println("<a href=\"namespace_manage.jsp?page="+(intPage-1)+"&info="+info+"\">上一页</a>");		
			}%>
								<%if(intPage<PageCount){
			out.println("<a href=\"namespace_manage.jsp?page="+(intPage+1)+"&info="+info+"\">下一页</a>");
			out.println("<a href=\"namespace_manage.jsp?page="+(PageCount)+"&info="+info+"\">最后一页</a>");
			}%>

								<a href='namespace_add.jsp?intPage=<%=intPage %>&info=<%=info%>'>【添加新记录】</a>
							</form>
						</mes:td>
					</mes:tr>
				</mes:tfoot>
			</mes:table>
			<form name="form1" method="post" style="margin: 0px"
				action="namespace_manage.jsp">
				<table class="tbnoborder">
					<tr>
						<td class="tbnoborder">
							关键字查询：&nbsp;&nbsp;
						</td>
						<td class="tdnoborder">
							<input type=text value="<%=info%>" name="info" id="info"
								class="box1" size=10 onMouseOver="this.className='box3'"
								onFocus="this.className='box3'"
								onMouseOut="this.className='box1'"
								onBlur="this.className='box1'">
						</td>
						<td class="tdnoborder">
							<mes:button id="s1" reSourceURL="../JarResource/" submit="true"
								value="查询" />
						</td>
					</tr>
				</table>
			</form>			
			<div style="color:ff0000;font-size:9pt">
				参数为空的时候查询所有信息，查询条件以"," 分开。
			</div>
		</div>

	</body>

	<%
		//释放资源
	
		}catch(Exception e)
		{		
			e.printStackTrace();
		}finally {			
			if(stmt!=null)stmt.close();
			if(conn!=null)conn .close();
			}
		
	%>
	<script type="text/javascript">
function checkinput(thisform){
        var re =  /^[0-9]+$/;
		var i=0;
		var qm;
		var mm = document.getElementsByName("method");
		for(i=0;i<mm.length;i++)
			if(mm[i].checked==true)
				qm = mm[i].value;
				
		if((qm=="ById")&&(isNaN(thisform.info.value)==true)){
			alert('代码只允许输入数字！');
			thisform.info.value="";
			thisform.info.focus();
			return false;
		}
		if(<%=PageCount%>>1)
		if(!re.test(document.getElementsByName("page")[0].value ))
     {  alert("跳转页数应由正整数组成!");
	    document.getElementsByName("page")[0].value="";
	    return false;
    }	
		 return true;	    
	}
	function selecttr(){
		var eid = '<%=eid==null?"-1":eid%>';		
		if(eid!=-1){
			document.getElementById('tr'+eid).click();
		}		
	}selecttr();
</script>
</html>
