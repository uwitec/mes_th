package mes.util;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.*;
import mes.ra.dao.*;
import mes.ra.bean.*;
import jxl.*;
import jxl.write.Label;
import jxl.write.WritableSheet;
import java.util.*;
import mes.ra.factory.*;
import java.text.*;
public class InstructionExport {
	
	public static synchronized void createExcelFile(ResultSet rs,
			OutputStream os,Connection con) throws Exception {
		//Method 2：将WritableWorkbook直接写入到输出流
	jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(os);
	//创建Excel工作表
	WritableSheet sheet = wwb.createSheet("生产指令", 0);
	 HashMap<Integer, String> m = new   HashMap<Integer, String>(); 
	 HashMap<Integer, String> m1 = new   HashMap<Integer, String>();
		DAO_ProduceUnit dao_produceunit =new DAO_ProduceUnitForOracle() ;
	    String producesql=dao_produceunit.getAllProduceUnit();
	   Statement stat1=con.createStatement();
	    
	 ResultSet Unit = stat1.executeQuery(producesql);
	   //ProduceUnitFactory unit=new ProduceUnitFactory();
	   // ResultSet Unit= unit.getAllProduceUnit(con);
	    while(Unit.next()){
	  
	    m.put(Unit.getInt("int_id"),(String)Unit.getString("str_name"));
	    }
	    if(stat1!=null){
	    	stat1.close();
	    }
	    StateFactory state=new StateFactory();
		   //ResultSet stat= state.getAllState2(conn);
		   List<State> liststate = new ArrayList<State>();
		 liststate=state.getAllState(con);
		 Iterator<State> iterstate=liststate.iterator();
		    while(iterstate.hasNext()){
		         State stat=new State();
		              stat=(State)iterstate.next();
		       m1.put(stat.getId(), stat.getStateName());
		    }
		 
	    ResultSetMetaData metaData = rs.getMetaData();
		int cols = metaData.getColumnCount(), row = 0;
            cols=16;
		if (true) {
			
			sheet.addCell(new Label(0, row, "指令序号"));
			sheet.addCell(new Label(1, row, "生产单元"));
			sheet.addCell(new Label(2, row, "生产日期"));
			sheet.addCell(new Label(3, row, "版本号"));
			sheet.addCell(new Label(4, row, "指令顺序号"));
			sheet.addCell(new Label(5, row, "计划日期"));
			sheet.addCell(new Label(6, row, "计划顺序号"));
			sheet.addCell(new Label(7, row, "产品类别标识"));
			sheet.addCell(new Label(8, row, "产品名称"));
			sheet.addCell(new Label(9, row, "产品标识"));
			sheet.addCell(new Label(10, row, "班次"));
			sheet.addCell(new Label(11, row, "班组"));
			sheet.addCell(new Label(12, row, "预计开始时间"));
			sheet.addCell(new Label(13, row, "预计结束时间"));
			sheet.addCell(new Label(14, row, "指令数量"));
			sheet.addCell(new Label(15, row, "指令状态"));
			/**for (int i = 1; i <= cols; i++) {
				sheet.addCell(new Label(i - 1, row, metaData.getColumnName(i)));
			}
			
			*/
			row++;
		}
       boolean f=true;
		while (rs.next()) {
			for (int i = 1; i <= cols; i++) {
				f=true;
				if(i==2){
				int t=rs.getInt(i);
				String name=m.get(t);
				sheet.addCell(new Label(i - 1, row, name));
				f=false;
				}
			    if(i==3){
			    	
			    	sheet.addCell(new Label(i - 1, row, (new SimpleDateFormat("yyyy-MM-dd"))
			    			.format(rs.getDate(i))));
			    	f=false;
			    }
                if(i==6){
			    	
			    	sheet.addCell(new Label(i - 1, row, (new SimpleDateFormat("yyyy-MM-dd"))
			    			.format(rs.getDate(i))));
			    	f=false;
			    }
				if(i==16){
					int t1= rs.getInt(i);
					String name1=m1.get(t1);
				sheet.addCell(new Label(i - 1, row, name1));
				f=false;
				}
				if(f){
				sheet.addCell(new Label(i - 1, row, rs.getString(i)));
				
				}
			}
			row++;
		}
		
	   
	if (rs != null)
		rs.close();
	wwb.write();
	wwb.close();
}

   
}
