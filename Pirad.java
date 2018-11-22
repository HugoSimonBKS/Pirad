import  javax.swing.*;
import  java.awt.*;
import  java.awt.event.*;
import  java.util.regex.*;
import  java.util.*;
import  java.text.SimpleDateFormat;

/**
*   @Author Hugo SIMON
*   @Version 1.4
*/

public class Pirad extends JFrame implements ActionListener {
  public Pattern p = Pattern.compile("^[0-9]{1,2}\\ h\\ [0-9]{0,2}$");
  public JTextField saisie = new JTextField(5);
  public JTextField format = new JTextField(5);
  public JTextField resultat = new JTextField(50);
  public Panneau panne = new Panneau();
  public Pirad(){
    super("Calcul Pirad");
    this.setContentPane(panne);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    init();
    this.setBounds(0, 0, 1000, 600);
    this.setVisible(true);
  }
  public void init(){
    this.setFocusable(true);
    this.requestFocus();
    this.add(convertisseur());
  }

  public void actualiser(){
    try{
      /*  Memo des variables : horaireDepart tableau de string contenant l'heure de depart entree par l'user
      *   heure = heure saisie castee en int, minutes = minutes saisie castee en Integer
      *   tpsRest = tableau de string contenant la duree du cours en heure entree par l'user
      *   minutage = format minutes horaire = format heure, ils servent a formater les deux variables currentTime qui recuperent l'heure en ms
      *   tempsrestant = variable calculant le temps restant en minutes, heurerestante = variable calculant le nombre d'heure restantes
      *   heureDuree = duree du cours totale en int minutesDuree = duree totale du cours en minute
      *   tempsfinal = temps restant reel en prenant en compte la duree du cours
      *   angle = temps radian engleu = temps en toDegrees
      */
      String[] horaireDepart = Reader.read(saisie.getText(), this.p, "\\ ");

      if(Reader.isHour(horaireDepart)){
        int heure = Integer.parseInt(horaireDepart[0]);
        int minutes = Integer.parseInt(horaireDepart[2]);
        minutes += (heure*60);
        String[] tpsrest = Reader.read(format.getText(), this.p, "\\ ");
        SimpleDateFormat minutage = new SimpleDateFormat ("mm");
        SimpleDateFormat Horaire = new SimpleDateFormat ("HH");
        Date currentTime_1 = new Date(System.currentTimeMillis());
        Date currentTime_2 = new Date(System.currentTimeMillis());
        int tempsrestant = Integer.parseInt(minutage.format(currentTime_1));
        int heurerestante = Integer.parseInt(horaire.format(currentTime_2))*60;
        tempsrestant += heurerestante;

        if(Reader.isHour(tpsrest)){
          int heureDuree = Integer.parseInt(tpsrest[0]);
          int minutesDuree = Integer.parseInt(tpsrest[2]);
          minutesDuree += (heureDuree*60);
          tempsrestant -= minutes;
          int tempsfinal = minutesDuree - tempsrestant;
          System.out.println("heure : " + tempsrestant + "minutes : " + minutes +  "minutes d (je sais plus ca fait quoi) : " + minutesd);
          double angle = ((double)tempsfinal*2/(double)minutesDuree)*Math.PI;
          int engleu = 360 - (int)Math.toDegrees(angle);
          for(int i = 0; i < engleu; i++){
            this.panne.dessineLine(getGraphics(), i);
            this.panne.update(this.panne.getGraphics());
            this.panne.repaint();
          }
          if(tempsfinal < minutesDuree){
            tempsfinal *= 2;
            for(int i = minutesDuree; i > 1; i--){
              if(tempsfinal % i == 0 && minutesDuree % i == 0){
                tempsfinal /= i;
                minutesDuree /= i;
              }
            }
          }
          this.resultat.setText(tempsfinal + "/" + minutesDuree + "π radiant");
          this.resultat.update(this.resultat.getGraphics());
          this.panne.update(this.panne.getGraphics());
          this.panne.repaint();
          this.resultat.repaint();
        }
      }
    }catch(FormatSaisieException fse){
      this.resultat.setText(fse.errMsg(this.p.toString()));
    }
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
      pano.add(format, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 3;
      pano.add(resultat, c);
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 2;
      pano.add(envoi, c);
      return pano;
    }

    public void run(){
      long sec = System.nanoTime()/1000000000;
      System.out.println(sec);
      while(true){
        try{
          Thread.sleep(1);
        }
        catch(InterruptedException ie){
          System.out.println(ie.getMessage());
        }
        if(System.nanoTime()/1000000000>sec){
          this.actualiser();
          sec = System.nanoTime()/1000000000;
        }
        else if(sec == 0)
          break;
      }
      System.out.println("format de seconde erreur");
    }

    protected void paintComponent(Graphics g){
      g.drawOval(110,110,50,50);
    }

    public void actionPerformed(ActionEvent e)
    {
      this.run();
      //  a opti parce que wallah c'est de la merde
    }

}
