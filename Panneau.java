import java.awt.Graphics;
import javax.swing.JPanel;

public class Panneau extends JPanel {
  protected void paintComponent(Graphics g){
    g.drawOval(this.getWidth()/2-100,this.getHeight()/2-100,200,200);
  }
  public void dessineLine(Graphics g, int angle){
    g.drawLine(this.getWidth()/2+3, this.getHeight()/2+28, this.getWidth()/2+3+(int)(Math.sin(Math.toRadians(angle))*100), this.getHeight()/2+28+(int)(Math.cos(Math.toRadians(angle))*100));
  }
}
