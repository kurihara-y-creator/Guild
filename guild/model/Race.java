package guild.model;

public enum Race
{
  HUMAN(1, "にんげん"), ELF(2, "エルフ"), DWARF(3, "ドワーフ"), GNOME(4, "ノーム"), HOBBIT(5, "ホビット");

  public static Race codeOf(int num)//数値→Enum
  {
    for (Race race : values())
    {
      if (race.code == num)
      {
        return race;
      }
    }
    throw new IllegalArgumentException();
  }

  private String name;
  private Integer code;

  private Race(Integer code, String name)
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

  public static Race getByRace(String text)
  {
    for (Race race : values())
    {
      if (race.name.equals(text))
      {
        return race;
      }
    }
    throw new IllegalArgumentException("不明な種族名: " + text);
  }

  public static String enumView()
  {
    StringBuilder sb = new StringBuilder();
    for (Race race : values())
    {
      sb.append(race.code).append(".").append(race.name).append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    return "\r[" + sb + "]";
  }
}
