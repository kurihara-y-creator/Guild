package guild.model;

public class GuildMemberRule
{
  private GuildMemberRule()
  {

  }

  public static void validate(Race race, Job job, Gender gender)
  {
    if (race == Race.DWARF && job == Job.DRAGOON && gender == Gender.FEMALE)
    {
      throw new IllegalArgumentException("おんな ドワーフは りゅうきし には なれないぞ");
    }
  }
}
