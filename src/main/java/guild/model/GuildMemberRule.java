package guild.model;

public class GuildMemberRule
{
  private GuildMemberRule()
  {

  }

  public static void validate(Race race, Job job)
  {
    if (race == Race.DWARF && job == Job.DRAGOON)
    {
      throw new IllegalArgumentException("ドワーフは りゅうきし には なれないぞ");
    }
  }
}
