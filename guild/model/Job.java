package guild.model;

public enum Job
{
  WARRIOR(1, "せんし"), DRAGOON(2, "りゅうきし"), THIEF(3, "とうぞく"), PRIEST(4, "そうりょ"), MAGE(5, "まじゅつし"), SAMURAI(6, "さむらい"),
  NINJA(7, "にんじゃ"), BARD(8, "ぎんゆうしじん");

  public static Job codeOf(int num)//数値→Enum
  {
    for (Job job : values())
    {
      if (job.code == num)
      {
        return job;
      }
    }
    throw new IllegalArgumentException();
  }

  private String name;//表示
  private Integer code;//DB

  private Job(Integer code, String name)
  {
    this.code = code;
    this.name = name;
  }

  public String toString()
  {
    return this.name;
  }

  public Integer getCode()
  {
    return code;
  }

  public static Job getByJob(String text)
  {
    for (Job job : values())
    {
      if (job.name.equals(text))
      {
        return job;
      }
    }
    throw new IllegalArgumentException("不明な職業名: " + text);
  }

  public static String enumView()
  {
    StringBuilder sb = new StringBuilder();
    for (Job job : values())
    {
      sb.append(job.code).append(".").append(job.name).append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    return "\r[" + sb + "]";
  }
}
