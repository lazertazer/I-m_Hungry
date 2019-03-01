package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *	TODO put description
 */
@WebServlet("/UserLists")
public class ListManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ListManagementServlet() {
        super();
    }

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
