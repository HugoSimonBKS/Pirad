import  javax.swing.*;
import  java.awt.*;
import  java.awt.event.*;
import  java.util.regex.*;
import  java.util.*;
import  java.text.SimpleDateFormat;

/**
* 	Cette classe va gerer l'affichage ainsi que la conversion du texte saisi en heure de type Date
*   @Author Hugo SIMON
*   @Version 1.6
*/

public class Pirad extends JFrame implements ActionListener {
	
	/**
	 * 	indique le format de saisie de l'heure, ici l'heure doit etre au format ** h **
	 */
	
  public Pattern p = Pattern.compile("^[0-9]{1,2}\\ h\\ [0-9]{0,2}$");
  
  /**
   * 	zone de saisie de l'heure de depart
   */
  
  public JTextField saisie = new JTextField(5);
  
  /**
   * 	zone de saisie de la duree
   */
  
  public JTextField format = new JTextField(5);
  
  /**
   * 	zone d'affichage du resultat
   */
  
  public JTextField resultat = new JTextField(50);
  
  /**
   * 	panel qui affiche le cercle
   */
  
  public Panneau panne = new Panneau();
  
  /**
   * 	bouton qui va demarer le calcul du temps restant en pi radiant, celui ci etant actualise chaque seconde
   */
  
  JButton envoi = new JButton("calculer");
  
  /**
   *	bouton d'arret 
   */
  
  JButton stop = new JButton("stopper");
  
  /**
   * 	Thread qui va permettre de pouvoir gerer le fait que le programme tourne ou soit a l'arret
   */
  
  public Thr tred;
  
  /**
   * 	Constructeur initialisant la JFrame en lui mettant un titre, ajoutant le pannel de dessin, 
   * 	methode d'arret lorsqu'on appuye sur la croix ainsi que les dimensions de la JFrame
   */
  
  public Pirad(){
    super("Calcul Pirad");
    this.setContentPane(panne);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    init();
    this.setBounds(0, 0, 1000, 600);
    this.setVisible(true);
  }
  
  /**
   * 	methode qui va creer le panel de saisie, et y mettre le focus 
   */
  
  public void init(){
    this.setFocusable(true);
    this.requestFocus();
    this.add(convertisseur());
  }

  /**
   *	methode appellee chaque seconde une fois le programme lance, elle effectue les conversions de string vers date au format minute et heure,
   *	puis effectue un affichage en pi radiant tout en demandant au panel de dessin d'afficher le cercle correspondant
   *	@see Reader, Panneau
   */
  
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
      
      //on check si le pattern est bon et si l'utilisateur n'est pas un tard
      if(Reader.isHour(horaireDepart)){
    	
        int heure = Integer.parseInt(horaireDepart[0]);
        int minutes = Integer.parseInt(horaireDepart[2]);
        minutes += (heure*60);
        String[] tpsrest = Reader.read(format.getText(), this.p, "\\ ");
        
        //conversion de la saisie en SimpleDateFormat pour les calculs
        SimpleDateFormat minutage = new SimpleDateFormat ("mm");
        SimpleDateFormat Horaire = new SimpleDateFormat ("HH");
        
        //recupere l'heure en ms
        Date currentTime_1 = new Date(System.currentTimeMillis());
        Date currentTime_2 = new Date(System.currentTimeMillis());
        
        //cast en int pour les calculs
        int tempsrestant = Integer.parseInt(minutage.format(currentTime_1));
        int heurerestante = Integer.parseInt(Horaire.format(currentTime_2))*60;
        tempsrestant += heurerestante;
        
        //pareil mais pour la duree
        if(Reader.isHour(tpsrest)){
          int heureDuree = Integer.parseInt(tpsrest[0]);
          int minutesDuree = Integer.parseInt(tpsrest[2]);
          minutesDuree += (heureDuree*60);
          tempsrestant -= minutes;
          int tempsfinal = minutesDuree - tempsrestant;
          
          //conversion du temps en angle pour l'afficher 
          double angle = ((double)tempsfinal*2/(double)minutesDuree)*Math.PI;
          int engleu = 360 - (int)Math.toDegrees(angle);
          for(int i = 0; i < engleu; i++){
            this.panne.dessineLine(getGraphics(), i);
          }
          
          //conversion du temps en pi radiant pour l'affichage
          if(tempsfinal < minutesDuree){
            tempsfinal *= 2;
            for(int i = minutesDuree; i > 1; i--){
              if(tempsfinal % i == 0 && minutesDuree % i == 0){
                tempsfinal /= i;
                minutesDuree /= i;
              }
            }
          }
          
          //update l'affichage
          this.resultat.setText(tempsfinal + "/" + minutesDuree + "π radiant");
          this.resultat.update(this.resultat.getGraphics());
        }
      }
    }catch(FormatSaisieException fse){
      this.resultat.setText(fse.errMsg(this.p.toString()));
    }
  }

  /**
   * 	panel contenant les zones de saisies et les boutons
   * 	@see GridBagConstraints	pour voir comment ca marche
   * 	@return	le panel une fois que tout a ete place
   */
  
  public JPanel convertisseur(){
      JPanel pano = new JPanel();
      stop.addActionListener(this);
      envoi.addActionListener(this);
      
      //empeche qu'un retard essaye de fuck up la sortie
      this.resultat.setFocusable(false);
      
      //le GridBagLayout permet d'avoir une grille relative entre les elements
      pano.setLayout(new GridBagLayout());
      GridBagConstraints c = new GridBagConstraints();
      c.fill = GridBagConstraints.HORIZONTAL;
      c.gridx = 0;
      c.gridy = 1;
      pano.add(saisie, c);
      c.gridx = 1;
      pano.add(format, c);
      c.gridx = 3;
      pano.add(resultat, c);
      c.gridx = 2;
      pano.add(envoi, c);
      c.gridy = 2;
      pano.add(stop, c);
      c.gridy = 0;
      c.gridx = 0;
      pano.add(new JLabel("Heure de depart    "), c);
      c.gridx = 1;
      pano.add(new JLabel("duree"), c);
      return pano;
    }

  	/**
  	 * 	Methode affichant le cercle vide servant de repere pour le cercle representant l'avancement entre l'heure de depart et l'heure d'arrivee
  	 * 	@param g parametre necessaire pour toute methode relative a paintComponent
  	 */
  
    protected void paintComponent(Graphics g){
      g.drawOval(110,110,50,50);
    }
    
    /**
     *	Listener sur les boutons qui permet de lancer/relancer ou stopper le programme en debutant/arretant un thread
     *	@param e evenement qui est capture a chaque clic de bouton 
     */
    
    public void actionPerformed(ActionEvent e)
    {
    	System.out.println("format de seconde erreur");
    	if(e.getSource().equals(stop))
    		tred.arraiter();
    	else {
    		tred = new Thr();
    		tred.start();
    	}
    }
    
    /**
     * 	Sous classe gerant le thread
     * 	@author BONGROPAYCAYSAMEWE
     */
    
    public class Thr extends Thread {     
    	
    	/**
    	 * 	booleen permettant d'indiquer au programme si il doit tourner ou s'arreter
    	 */
    	
    	boolean running = true;
    	
    	/**
    	 *  Methode appellee par le bouton stopper pour arreter l'execution du thread
    	 */
    	
        public void arraiter() {
        	this.running = false;
        }
        
        /**
         * 	methode qui va chaque seconde appeller la methode actualiser tant que running = true
         */
        
        public void run()
        {
        	 long sec = System.nanoTime()/1000000000;
             System.out.println(sec);
             
             while(running){
               try{
            	 //	une fois qu'il a tourne met le thread en pause pendant 1 sec, necessaire sinon le programme bouffe trop de ram
                 Thread.sleep(1);
               }
               catch(InterruptedException ie){
                 System.out.println(ie.getMessage());
               }
               // malgres le sleep il faut verifier qu'il est bien synchro on recupere donc le temps et verifie qu'il s'est ecoule une sec
               if(System.nanoTime()/1000000000>sec){
                 actualiser();
                 sec = System.nanoTime()/1000000000;
               }
               //si le nombre de secondes est = a 0 il y a une erreur donc on sort
               else if(sec == 0)
                 break;
             }
             
        }   
    }
}
