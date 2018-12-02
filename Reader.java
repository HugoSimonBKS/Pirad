import java.util.regex.*;

/**
 * 	Classe qui permet de lire des entrees texte de JFrame et tester si il s'agit de dates etc
 * 	@author Hugo Simon
 *	@version 1.1
 */

public final class Reader{
	
	/**
	 * 	methode qui divise une chaine selon le pattern fourni
	 * @param text	chaine a lire
	 * @param charsplit	charactere determinant comment split
	 * @return	le tableau de String
	 */
  public static String[] read(String text, String charsplit){
      String[] valeurs = text.split(charsplit);
      return valeurs;
  }


  public static String[] read(String text, Pattern p, String charsplit) throws FormatSaisieException{
    Matcher m = p.matcher(text);
    if (!m.matches()){
      throw new FormatSaisieException(p.toString());
    }
    else{
        String[] valeurs = text.split(charsplit);
        return valeurs;
    }
  }

  public static boolean isHour(String[] hour){
    int heure = Integer.parseInt(hour[0]);
    String h = hour[1];
    int minutes = Integer.parseInt(hour[2]);
    return(minutes >= 0 && minutes < 60 && heure >= 0 && heure < 25);
  }

  public static boolean isDate(String[] date){
    int jour = Integer.parseInt(date[0]);
    int mois = Integer.parseInt(date[1]);
    int annee = Integer.parseInt(date[2]);
    return(jour >= 0 && isDayValid(jour, mois, annee) && mois <= 12);
  }

  private static boolean isDayValid(int day, int month, int year){
    if(day <= 28){
      return true;
    }
    else if((month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) && (day <= 31)){
      return true;
    }
    else if((month == 4 || month == 6 || month == 9 || month == 11)&&(day <= 30)){
      return true;
    }
    else if((year % 4 == 0) && (month == 2) && (day <= 29)){
      return true;
    }
    else
      return false;
  }
}
