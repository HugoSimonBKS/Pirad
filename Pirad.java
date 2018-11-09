 import  javax.swing.*;
import  java.awt.*;
import  java.awt.event.*;
import  java.util.regex.*;
import  java.util.*;
import  java.text.SimpleDateFormat;
public class Pirad extends JFrame implements ActionListener {
  public Pattern p = Pattern.compile("^[0-9]{1,2}\\ h\\ [0-9]{0,2}$");
  public JTextField saisie = new JTextField(5);
  public JTextField format = new JTextField(5);
  public JTextField resultat = new JTextField(50);
  public Pirad(){
    super("Calcul Pirad");
    init();
    this.setBounds(0, 0, 1000, 600);
    this.setVisible(true);
  }
  public void init(){
    this.setFocusable(true);
    this.requestFocus();
    this.add(convertisseur());
  }
  public JPanel convertisseur(){
      JPanel pano = new JPanel();
      JButton envoi = new JButton("calculer");
      envoi.addActionListener(this);
      this.resultat.setFocusable(false);
      pano.setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 0;
      pano.add(saisie, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 1;
      c.gridy = 0;
      pano.add(format, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 3;
      c.gridy = 0;
      pano.add(resultat, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 2;
      c.gridy = 0;
      pano.add(envoi, c);
      return pano;
    }



    public void actionPerformed(ActionEvent e)
    {
      
      //  a opti parce que wallah c'est de la merde

      try{
        String[] horaire = Reader.read(saisie.getText(), this.p, "\\ ");

        if(Reader.isHour(horaire)){
          int heure = Integer.parseInt(horaire[0]);
          String h = horaire[1];
          int minutes = Integer.parseInt(horaire[2]);
          minutes += (heure*60);
          String[] tpsrest = Reader.read(format.getText(), this.p, "\\ ");
          SimpleDateFormat minutage = new SimpleDateFormat ("mm");
          SimpleDateFormat horaire2 = new SimpleDateFormat ("HH");
          Date currentTime_1 = new Date(System.currentTimeMillis());
          Date currentTime_2 = new Date(System.currentTimeMillis());
          int tempsrestant = Integer.parseInt(minutage.format(currentTime_1));
          int heurerestante = Integer.parseInt(horaire2.format(currentTime_2))*60;
          tempsrestant += heurerestante;

          if(Reader.isHour(tpsrest)){
            int heured = Integer.parseInt(tpsrest[0]);
            String hd = horaire[1];
            int minutesd = Integer.parseInt(tpsrest[2]);
            minutesd += (heured*60);
            tempsrestant -= minutes;
            int tempsfinal = minutesd - tempsrestant;
            System.out.println("heure : " + tempsrestant + "minutes : " + minutes +  "minutes d (je sais plus ca fait quoi) : " + minutesd);
            if(tempsfinal < minutesd){
              tempsfinal *= 2;
              for(int i = minutesd; i > 1; i--){
                if(tempsfinal % i == 0 && minutesd % i == 0){
                  tempsfinal /= i;
                  minutesd /= i;
                }
              }
            }
            this.resultat.setText(tempsfinal + "/" + minutesd + "π radiant");
          }
        }
      }catch(FormatSaisieException fse){
        this.resultat.setText(fse.errMsg(this.p.toString()));
      }
    }

}
