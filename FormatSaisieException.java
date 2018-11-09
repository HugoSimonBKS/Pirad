class FormatSaisieException extends Exception{
  public FormatSaisieException(String formatValide){
    errMsg(formatValide);
  }
  public String errMsg(String formatValide){
    return ("format invalide, bon format :" + formatValide);
  }
}
