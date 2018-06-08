/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;
import cls.clsManage;
import cls.clsQuery;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class reportServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String module = new String(request.getParameter("module").getBytes(), "TIS-620");
        if (module.equals("getapprovelog")) {
            getapprovelog(request, response);
        }else if (module.equals("getcountrycount")) {
            getcountrycount(request, response);
        }
        
        
    }
    
    protected void getapprovelog(HttpServletRequest request, HttpServletResponse response) throws IOException {

       PrintWriter out = response.getWriter();
       try {
           
             HttpSession session = request.getSession();
            String appcode = (String) session.getAttribute("sessionCode");
            String empid = request.getParameter("xempcode");
            String xfrom = request.getParameter("xfrom");
            String xto = request.getParameter("xto");
            
            clsQuery cQuery = new clsQuery();
            String sapp = "" ;
            sapp = cQuery.getapprovelog(appcode,empid,xfrom,xto);
            out.print(sapp);
        } catch (Exception ex) {
            out.print("");
            ex.printStackTrace(response.getWriter());
        }

    }
    
    protected void getcountrycount(HttpServletRequest request, HttpServletResponse response) throws IOException {

       PrintWriter out = response.getWriter();
       try {
            
            String xcntry = request.getParameter("xcntry");
            String xfrom = request.getParameter("xfrom");
            String xto = request.getParameter("xto");
            
            clsQuery cQuery = new clsQuery();
            String sapp = "" ;
            sapp = cQuery.getcountrycount(xcntry,xfrom,xto);
            out.print(sapp);
        } catch (Exception ex) {
            out.print("");
            ex.printStackTrace(response.getWriter());
        }

    }
    
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
