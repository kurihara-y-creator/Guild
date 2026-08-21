package guild.controller;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import guild.model.GuildMember;
import guild.model.GuildMemberORM;

@WebServlet("/guildmembers")
@MultipartConfig
public class GuildMembers extends HttpServlet
{
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    resp.setCharacterEncoding("utf-8");
    resp.setContentType("application/json");
    var out = resp.getWriter();
    try
    {
      var guildmembers = GuildMemberORM.getAll();
      String json = """
          {
          "status": "success",
          "data" : [
          """;
      var aa = new CopyOnWriteArrayList<String>();
      for (var a : guildmembers)
      {
        aa.add(a.getJson());
      }
      json += String.join(",", aa);
      json += """
          ]
          }
          """;
      out.println(json);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      out.println(String.format("""
          { "status":"failure","reason":"%s"}
          """, ex.getMessage().replaceAll("\"", "")));
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("utf-8");
    resp.setContentType("application/json");

    var out = resp.getWriter();

    try
    {
      var name = req.getParameter("name");
      var racecode = req.getParameter("race");
      var gendercode = req.getParameter("gender");
      var jobcode = req.getParameter("job");

      GuildMember member = new GuildMember(null, name, jobcode, gendercode, racecode);

      GuildMemberORM.add(member);

      out.println("""
          {
            "status":"success"
          }
          """);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      out.println(String.format("""
          {
            "status":"failure",
            "reason":"%s"
          }
          """, ex.getMessage().replace("\"", "")));
    }
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException
  {
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    var out = resp.getWriter();
    try
    {
      var id = req.getParameter("id");
      GuildMemberORM.remove(Integer.parseInt(id));
      out.println("""
          {
            "status":"success"
          }
          """);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      out.println("""
          {
            "status":"failure"
          }
          """);
    }
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException
  {
    resp.setCharacterEncoding("UTF-8");
    req.setCharacterEncoding("UTF-8");
    resp.setContentType("application/json");
    var out = resp.getWriter();
    try
    {
      var id = req.getParameter("id");
      var name = req.getParameter("name");
      var racecode = req.getParameter("race");
      var gendercode = req.getParameter("gender");
      var jobcode = req.getParameter("job");

      GuildMember member = new GuildMember(id, name, jobcode, gendercode, racecode);
      GuildMemberORM.modify(member);

      out.println("""
          {
              "status":"success"
          }
          """);

    }
    catch (Exception e)
    {

      e.printStackTrace();

      out.println("""
          {
              "status":"failure"
          }
          """);
    }
  }
}
