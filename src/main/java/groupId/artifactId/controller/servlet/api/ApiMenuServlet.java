package groupId.artifactId.controller.servlet.api;

import groupId.artifactId.exceptions.IncorrectEncodingException;
import groupId.artifactId.exceptions.IncorrectServletInputStreamException;
import groupId.artifactId.exceptions.IncorrectServletWriterException;
import groupId.artifactId.service.MenuService;
import groupId.artifactId.service.api.IMenuService;
import groupId.artifactId.utils.JsonConverter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

//CRUD controller
//IMenuRow
@WebServlet(name = "MenuForm", urlPatterns = "/api/menu")
public class ApiMenuServlet extends HttpServlet {
    private final IMenuService menuService = MenuService.getInstance();

    //Read POSITION
    //1) Read list
    //2) Read item need id param
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        try {
            if (id != null) {
                if (menuService.isIdValid(Long.valueOf(id))) {
                    resp.getWriter().write(JsonConverter.fromMenuToJson(menuService.get(Long.valueOf(id))));
                } else {
                    resp.setStatus(400);
                    throw new IllegalArgumentException("Error code 400. Menu id is not exist");
                }

            } else {
                resp.getWriter().write(JsonConverter.fromMenuListToJson(menuService.get()));
            }
        } catch (IOException e) {
            resp.setStatus(500);
            throw new IncorrectServletWriterException("Incorrect servlet state during response writer method", e);
        }
        resp.setStatus(200);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            menuService.add(JsonConverter.fromJsonToMenu(req.getInputStream()));
        } catch (UnsupportedEncodingException e) {
            resp.setStatus(500);
            throw new IncorrectEncodingException("Failed to set character encoding UTF-8", e);
        } catch (IOException e) {
            resp.setStatus(500);
            throw new IncorrectServletInputStreamException("Impossible to get input stream from request", e);
        }
        resp.setStatus(201);
    }
}
//to add new Menu in Storage
//[
//   {
//           "price":20.0,
//           "pizzaInfo":{
//           "name":"ITALIANO PIZZA",
//           "description":"Mozzarella cheese, basilica, ham",
//           "size":32
//           }
//           }
//           ]