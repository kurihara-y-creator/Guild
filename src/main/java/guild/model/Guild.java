package guild.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Guild
{
  public static void main(String[] args)
  {
    try (Scanner sc = new Scanner(System.in))
    {
      while (true)
      {
        try
        {
          System.out.println("ここは冒険者ギルドです。");
          System.out.println("1.一覧\r2.登録\r3.変更\r4.削除\r9.終了");
          System.out.print("何をしますか？>");
          int select = selectInt(sc);//selectInt=String→int
          switch (select)
          {
            case 1:
              view();
              break;
            case 2:
              add(sc);
              break;
            case 3:
              change(sc);
              break;
            case 4:
              delete(sc);
              break;
            case 9:
              System.out.println("ご利用ありがとうございました。");
              return;
            default:
              throw new IllegalArgumentException();
          }
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
          System.out.println("もう一度入力しなおしてください。");
        }
      }
    }
  }

  public static void view() throws SQLException
  {
    List<GuildMember> members = GuildMemberORM.getAll();
    if (members.isEmpty())
    {
      System.out.println("冒険者はまだ登録されていません。\r");
      return;
    }
    System.out.println("[冒険者一覧]");
    for (GuildMember gm : members)
    {
      System.out.println(gm.getId() + "." + gm);
    }
  }

  /*public static void view(List<GuildMember> members)
  {
    if (members.isEmpty())
    {
      System.out.println("冒険者はまだ登録されていません。\r");
      return;
    }
    System.out.println("[冒険者一覧]");
    for (int i = 0; i < members.size(); i++)
    {
      System.out.println((i + 1) + "." + members.get(i));
    }
  }*/
  public static void add(Scanner sc) throws SQLException
  {
    String name = null;
    Race race = null;
    Gender gender = null;
    Job job = null;

    while (true)
    {
      try
      {
        if (name == null)
        {
          System.out.println("新しく登録する冒険者の名前を入力してください。>");
          String inputName = sc.nextLine();
          if (inputName.isBlank())
          {
            throw new IllegalArgumentException();
          }
          name = inputName;
        }

        if (job == null)
        {
          System.out.println(Job.enumView());
          System.out.println("新しく登録する冒険者の職業を入力してください。>");
          job = selectJob(sc);
        }

        if (gender == null)
        {
          System.out.println(Gender.enumView());
          System.out.println("新しく登録する冒険者の性別を入力してください。>");
          gender = selectGender(sc);
        }

        if (race == null)
        {
          System.out.println(Race.enumView());
          System.out.println("新しく登録する冒険者の種族を入力してください。>");
          race = selectRace(sc);
        }

        System.out.println("登録する冒険者は\n" + name + "(" + race + "/" + gender + "/" + job + ")\nでよろしいでしょうか？>");

        if (answer(sc))
        {
          GuildMemberORM.add(new GuildMember(null, name, job, gender, race));
          System.out.println("登録しました。");
        }
        else
        {
          System.out.println("キャンセルしました。");
        }
        break;
      }
      catch (Exception ex)
      {
        System.out.println("不正な文字が入力されました。もう一度入力してください。\r");
      }
    }
  }

  /*public static void add(Scanner sc, List<GuildMember> members)
  {
    String name = null;
    Race race = null;
    Gender gender = null;
    Job job = null;
  
    while (true)
    {
      try
      {
        if (name == null)
        {
          System.out.println("新しく登録する冒険者の名前を入力してください。>");
          var inputName = sc.nextLine();
          if (inputName.isBlank() || inputName == null)
            throw new IllegalArgumentException("入力が空欄です。入力しなおしてください。");
          name = inputName;
        }
  
        if (job == null)
        {
          System.out.println(Job.enumView());
          System.out.println("新しく登録する冒険者の職業を入力してください。>");
          job = Job.getByJob(sc.nextLine());
        }
        if (gender == null)
        {
          System.out.println(Gender.enumView());
          System.out.println("新しく登録する冒険者の性別を入力してください。＞");
          gender = Gender.getByGender(sc.nextLine());
        }
  
        if (race == null)
        {
          System.out.println(Race.enumView());
          System.out.print("新しく登録する冒険者の種族を入力してください。>");
          race = Race.getByRace(sc.nextLine());
        }
  
        System.out.println("登録する冒険者は\n" + name + "(" + race + "/" + gender + "/" + job + ")\nでよろしいでしょうか？>");
  
        if (answer(sc))
        {
          members.add(new GuildMember(name, job, gender, race));
          System.out.println("登録しました。\r");
          break;
        }
        else
        {
          System.out.println("キャンセルしました。");
          break;
        }
      }
      catch (Exception ex)
      {
        System.out.println("不正な文字が入力されました。もう一度入力してください。\r");
      }
    }
  }*/
  public static void change(Scanner sc) throws SQLException
  {
    while (true)
    {
      try
      {
        List<GuildMember> members = GuildMemberORM.getAll();
        if (members.isEmpty())
        {
          System.out.println("冒険者はまだ登録されていません。\n");
          return;
        }
        view();
        System.out.println("変更する冒険者のIDを入力してください。");
        int id = selectInt(sc);
        GuildMember gm = GuildMemberORM.get(id);
        System.out.println("1. 名前");
        System.out.println("2. 職業");
        System.out.println(gm.getName() + "の何を変更しますか？>");

        int c = selectInt(sc);
        switch (c)
        {
          case 1:
          {
            System.out.println("新しい名前を入力してください。>");
            String newName = sc.nextLine();
            if (newName.isBlank())
            {
              throw new IllegalArgumentException();
            }
            gm.setName(newName);
            System.out.println(newName + "に変更しました。");
            break;
          }
          case 2:
          {
            System.out.println(Job.enumView());
            System.out.println(gm.getName() + "の職業を選択してください。>");
            Job newJob = selectJob(sc);
            gm.setJob(newJob);
            System.out.println("(" + gm.getName() + ")は" + newJob + "に転職しました。");
            break;
          }
          default:
            throw new IllegalArgumentException();
        }
        GuildMemberORM.modify(gm);
        break;
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        System.out.println("不正な文字が入力されました");
      }
    }
  }

  /*public static void change(Scanner sc, List<GuildMember> members)
  {
    while (true)
    {
      if (members.isEmpty())
      {
        System.out.println("冒険者はまだ登録されていません。\n");
        break;
      }
      try
      {
        view(members);
        System.out.println("変更する冒険者の番号を入力してください。");
  
        var i = selectInt(sc);
  
        if (i < 1 || i > members.size())
          throw new IllegalArgumentException();
        GuildMember gm = members.get(i - 1);
  
        System.out.println("1. 名前");
        System.out.println("2. 職業");
        System.out.println(gm.getName() + "の何を変更しますか？>");
        var c = selectInt(sc);
        switch (c)
        {
          case 1:
          {
            System.out.println("新しい名前を入力してください。>");
            var newName = sc.nextLine();
            if (newName == null || newName.isBlank())
              throw new IllegalArgumentException();
  
            gm.setName(newName);
            System.out.println(newName + "に変更しました。");
          }
          case 2:
          {
            System.out.println(Job.enumView());
            System.out.println(gm.getName() + "の職業を選択してください。>");
            Job newJob = Job.getByJob(sc.nextLine());
            gm.setJob(newJob);
            System.out.println("(" + gm.getName() + ")は" + newJob + "に転職しました。");
            break;
          }
  
          default:
            throw new Exception();
        }
        break;
      }
      catch (Exception ex)
      {
        System.out.println("不正な文字が入力されました。最初から入力しなおしてください。\n");
      }
    }
  }*/
  public static void delete(Scanner sc) throws SQLException
  {
    while (true)
    {
      try
      {
        view();
        System.out.println("削除する冒険者のIDを入力してください。>");
        int id = selectInt(sc);
        GuildMember gm = GuildMemberORM.get(id);
        System.out.println(gm.getName() + "を削除してもよろしいですか？>");

        if (answer(sc))
        {
          GuildMemberORM.remove(id);
          System.out.println(gm.getName() + "を削除しました。\r");
        }
        else
        {
          System.out.println("キャンセルしました。");
        }
        break;
      }
      catch (Exception ex)
      {
        System.out.println("不正な文字が入力されました。最初から入力しなおしてください。\r");
      }
    }
  }

  /*public static void delete(Scanner sc, List<GuildMember> members)
  {
    while (true)
    {
      if (members.isEmpty())
      {
        System.out.println("冒険者はまだ登録されていません。\r");
        return;
      }
      try
      {
        view(members);
        System.out.println("削除する冒険者の番号を入力してください。>");
        var i = selectInt(sc);
        if (i < 1 || i > members.size())
          throw new IllegalArgumentException();
        GuildMember gm = members.get(i - 1);
  
        System.out.println(gm.getName() + "を削除してもよろしいですか？＞");
        if (answer(sc))
        {
          members.remove(i - 1);
          System.out.println(gm.getName() + "を削除しました。\r");
        }
        else
        {
          System.out.println("キャンセルしました。");
        }
        break;
      }
      catch (Exception ex)
      {
        System.out.println("不正な文字が入力されました。最初から入力しなおしてください。\r");
      }
    }
  }*/
  private static Job selectJob(Scanner sc)
  {
    if (sc.hasNextInt())
    {
      int num = sc.nextInt();
      sc.nextLine();
      return Job.codeOf(num);
    }
    else
    {
      String s = sc.nextLine();
      return Job.getByJob(s);
    }
  }

  private static Race selectRace(Scanner sc)
  {
    if (sc.hasNextInt())
    {
      int num = sc.nextInt();
      sc.nextLine();
      return Race.codeOf(num);
    }
    else
    {
      String s = sc.nextLine();
      return Race.getByRace(s);
    }
  }

  private static Gender selectGender(Scanner sc)
  {
    if (sc.hasNextInt())
    {
      int num = sc.nextInt();
      sc.nextLine();
      return Gender.codeOf(num);
    }
    else
    {
      String s = sc.nextLine();
      return Gender.getByGender(s);
    }
  }
  /*private static Gender selectGender(Scanner sc)
  {
    String s = sc.nextLine();
    if (s.matches("\\d+"))
    {
      return Gender.codeOf(Integer.parseInt(s));
    }
    else
    {
      return Gender.getByGender(s);
    }
  }*/

  private static int selectInt(Scanner sc)
  {
    return Integer.parseInt(sc.nextLine());//String→int
  }

  public static boolean answer(Scanner sc)
  {
    while (true)
    {
      var yn = sc.nextLine();
      switch (yn)
      {
        case "y", "ｙ", "Y", "Ｙ", "yes", "Yes", "YES", "はい":
        {
          return true;
        }
        case "n", "ｎ", "N", "Ｎ", "no", "No", "NO", "いいえ":
        {
          return false;
        }
        default:
          System.out.println("不正な文字が入力されました。もう一度入力してください。");
      }
    }
  }
}
