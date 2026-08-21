package guild.model;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuildMemberORM
{
  public static GuildMember get(Integer id) throws SQLException
  {
    String sql = """
        SELECT
        *
        FROM
         guild_member
        WHERE
         id = ?
        """;
    var list = MySqlClient.select(sql, id);
    return new GuildMember(list.get(0));
  }

  public static List<GuildMember> getAll() throws SQLException
  {
    String sql = """
        SELECT
        *
        FROM
         guild_member
        """;
    var list = MySqlClient.select(sql);
    var member = new CopyOnWriteArrayList<GuildMember>();//DB→保持するリスト
    for (Object[] oa : list)
    {
      GuildMember gm = new GuildMember(oa);
      member.add(gm);
    }
    return member;
  }

  public static void add(GuildMember gm) throws SQLException
  {
    String sql = """
        INSERT INTO guild_member
         (name,job,gender,race)
        VALUES
         (?,?,?,?)
        """;
    int n = MySqlClient.execute(sql, gm.getName(), gm.getJob().getCode(), gm.getGender().getCode(),
        gm.getRace().getCode());

    if (n != 1)//1件だけ許可
    {
      throw new IllegalArgumentException();
    }
  }

  public static void remove(Integer id) throws SQLException
  {
    String sql = """
        DELETE FROM
         guild_member
        WHERE
         id = ?
        """;
    int n = MySqlClient.execute(sql, id);
    if (n != 1)
    {
      throw new IllegalArgumentException();
    }
  }

  public static void modify(GuildMember gm) throws SQLException
  {
    String sql = """
        UPDATE
         guild_member
        SET
         name = ?,
         job = ?,
         gender = ?,
         race = ?
        WHERE
         id = ?
        """;
    int n = MySqlClient.execute(sql, gm.getName(), gm.getJob().getCode(), gm.getGender().getCode(),
        gm.getRace().getCode(), gm.getId());
    if (n != 1)
    {
      throw new IllegalArgumentException();
    }
  }

}