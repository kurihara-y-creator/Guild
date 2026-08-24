package guild.model;

public enum Gender
{
  MALE(1, "おとこ"), FEMALE(2, "おんな");

  public static Gender codeOf(int num)//数値→Enum
  {
    for (Gender gender : values())
    {
      if (gender.code == num)
      {
        return gender;
      }
    }
    throw new IllegalArgumentException();
  }

  private Integer code;
  private String name;

  private Gender(Integer code, String name)
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

  public static Gender getByGender(String text)
  {
    for (Gender gender : values())
    {
      if (gender.name.equals(text))
      {
        return gender;
      }
    }
    throw new IllegalArgumentException("不明な性別:" + text);
  }

  public static String enumView()
  {
    StringBuilder sb = new StringBuilder();
    for (Gender gender : values())
    {
      sb.append(gender.code).append(".").append(gender.name).append(",");
    }
    sb.deleteCharAt(sb.length() - 1);
    return "\r[" + sb + "]";
  }
}
