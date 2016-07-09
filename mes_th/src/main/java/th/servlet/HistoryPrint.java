package th.servlet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qm.mes.th.assembly.entities.RequestParam;
import com.qm.mes.th.assembly.historyprint.AssemblyHistoryPrintFacade;

/**
 * @author Administrator
 */
public class HistoryPrint extends HttpServlet {
    /**
     * 重复打印逻辑函数处理
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 设置响应头
        response.setContentType("application/octet-stream");

        // 从客户端接收到的请求参数
        RequestParam requestParam = new RequestParam();
        requestParam.setRequestDate(request.getParameter("rq"));// 请求日期
        requestParam.setChassisNumber(request.getParameter("ch"));// 底盘号
        requestParam.setGroupId(request.getParameter("groupid"));// 打印组号
        requestParam.setJs(request.getParameter("js"));// 架子号

        // 数据写出到客户端
        write2Client(response, new AssemblyHistoryPrintFacade().assemblyPrint(requestParam));
    }
    
    /**
     * 数据写出到客户端
     * 
     * @param response
     * @param obj
     */
    private void write2Client(HttpServletResponse response, Object obj) {
        // 设置响应头
        response.setContentType("application/octet-stream");
        
        OutputStream streamOutPut = null;
        ObjectOutputStream objectOutput = null;

        try {
            streamOutPut = response.getOutputStream();
            objectOutput = new ObjectOutputStream(streamOutPut);

            objectOutput.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectOutput != null) {
                try {
                    objectOutput.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    objectOutput = null;
                }
            }
            if (streamOutPut != null) {
                try {
                    streamOutPut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    streamOutPut = null;
                }
            }
        }
    }
}
