package Utilities;

public class VoiceCall
{
  public VoiceCall() {}
  
  public static final String ACCOUNT_SID = GenericKeywords.getConfigProperty("ACCOUNT_SID");
  public static final String AUTH_TOKEN = GenericKeywords.getConfigProperty("AUTH_TOKEN");
  public static String toMobNo = GenericKeywords.getConfigProperty("ToMobileNo");
  public static String fromMobNo = GenericKeywords.getConfigProperty("FromMobileNo");
  
  public static void voiceCall()  
  {
  }
}
