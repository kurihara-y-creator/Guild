package guild.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MySqlClient
{
  // 0. 前準備
  private static final String DB_PATH;
  private static final String DB_USER;
  private static final String DB_PASS;

  static
  {
    DB_PATH = "jdbc:mysql://localhost:3306/sample1" + "?useUnicode=true" + "&characterEncoding=UTF-8";
    DB_USER = "root";
    DB_PASS = "atipas";
    try
    {
      Class.forName("com.mysql.cj.jdbc.Driver");
    }
    catch (ClassNotFoundException ex)
    {
      ex.printStackTrace();
    }
  }

  public static List<Object[]> select(String sql, Object... params) throws SQLException//データを取得する
  {

    try (Connection cn = DriverManager.getConnection(DB_PATH, DB_USER, DB_PASS); var st = cn.prepareStatement(sql);)//stから先にクローズ
    {
      // 2. クエリ
      int n = 1;
      for (var p : params)
      {
        st.setObject(n++, p);
      }

      // 3. 結果取得
      var rs = st.executeQuery();
      var md = rs.getMetaData();
      int columnCount = md.getColumnCount();

      var list = new CopyOnWriteArrayList<Object[]>();
      while (rs.next())
      {
        Object[] oa = new Object[columnCount];
        for (int i = 1; i <= columnCount; i++)
        {
          oa[i - 1] = rs.getObject(i);
        }
        list.add(oa);
      }
      return list;
    }
  }

  public static int execute(String sql, Object... params) throws SQLException//データを変更する
  {
    try (Connection cn = DriverManager.getConnection(DB_PATH, DB_USER, DB_PASS); var st = cn.prepareStatement(sql);)//prepare→Connectionの順
    {
      int n = 1;
      for (var p : params)
      {
        st.setObject(n++, p);
      }
      return st.executeUpdate();
    }
  }
}