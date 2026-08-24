package guild.model;

public class GuildMember
{
  private Integer id;
  private String name;
  private Job job;
  private Gender gender;
  private Race race;

  public GuildMember(Integer id, String name, Job job, Gender gender, Race race)
  {
    this.id = id;
    this.name = name;
    this.job = job;
    this.gender = gender;
    this.race = race;
  }

  //二個目作る
  public GuildMember(String id, String name, String job, String gender, String race)
  {
    this(id == null ? null : Integer.parseInt(id), //
        name, //
        Job.codeOf(Integer.parseInt(job)), //
        Gender.codeOf(Integer.parseInt(gender)), //
        Race.codeOf(Integer.parseInt(race)));
  }

  public GuildMember(Object[] oa)
  {
    this((Integer) oa[0], //
        (String) oa[1], //
        Job.codeOf((Integer) oa[2]), //
        Gender.codeOf((Integer) oa[3]), //
        Race.codeOf((Integer) oa[4]));
  }

  public Integer getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Job getJob()
  {
    return job;
  }

  public void setJob(Job job)
  {
    this.job = job;
  }

  public Gender getGender()
  {
    return gender;
  }

  public void setGender(Gender gender)
  {
    this.gender = gender;
  }

  public Race getRace()
  {
    return race;
  }

  public void setRace(Race race)
  {
    this.race = race;
  }

  public String getJson()
  {
    return String.format("""
        {"id":%d,"name":"%s","race":"%s","gender":"%s","job":"%s"}
        """, id, name, race, gender, job);
  }

  public String toString()
  {
    return "名前:" + name + "/ 種族:" + race + "/ 性別:" + gender + "/ 職業:" + job;

  }

}
